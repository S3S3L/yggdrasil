package org.s3s3l.yggdrasil.utils.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;

import org.s3s3l.yggdrasil.bean.FileType;
import org.s3s3l.yggdrasil.bean.KeyValuePair;
import org.s3s3l.yggdrasil.bean.exception.ResourceNotFoundException;
import org.s3s3l.yggdrasil.bean.exception.ResourceProcessException;
import org.s3s3l.yggdrasil.utils.collection.CollectionUtils;
import org.s3s3l.yggdrasil.utils.common.StringUtils;
import org.s3s3l.yggdrasil.utils.common.Utils;
import org.s3s3l.yggdrasil.utils.security.SecurityUtils;
import org.s3s3l.yggdrasil.utils.verify.Verify;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.VerifyException;
import com.google.common.io.Resources;

import lombok.AllArgsConstructor;

/**
 * 
 * <p>
 * 文件操作工具类
 * </p>
 * ClassName: FileUtils <br>
 * date: Jan 17, 2019 9:54:27 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public abstract class FileUtils {
    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

    public static final String CLASS_PATH_PRIFIX = "classpath:";
    public static final String FILE_PATH_PRIFIX = "file:";

    /**
     * 
     * 分割文件
     * 
     * @param srcPath
     *            源文件路径，支持classpath中的文件
     * @param destPath
     *            目标文件，如/tmp/test.txt，最终会生成/tmp/test.txt.00,/tmp/test.txt.01
     *            ...等多个文件
     * @param sizePerSplit
     *            每个分片的大小，单位B。会向上浮动最多maxLineSize。不能为负数。
     * @param maxLineSize
     *            查找行尾的最大长度。会从每个分片的默认位置向后查找最近的行尾作为最终分割位置。不能为负数。
     * @param lineSeparator
     *            行尾符号
     * @param executor
     *            执行线程池。每个分片的切割工作会作为一个独立的任务并发进行。会同时打开多次源文件（和并发数相同）。为空时，所有任务串行的在当前线程中执行。
     * @return 目标文件地址
     * @throws IOException
     *             遇到IO错误的时候会抛出
     * @throws VerifyException
     *             在入参不合法的情况下抛出
     * @since JDK 1.8
     */
    public static Map<String, Path> split(final String srcPath,
            final String destPath,
            final long sizePerSplit,
            final long maxLineSize,
            final char lineSeparator,
            final ExecutorService executor) throws IOException {
        logger.debug(
                "Starting file split. srcPath: {}, destPath: {}, sizePerSplit: {}, maxLineSize: {}, lineSeparator: {}",
                srcPath, destPath, sizePerSplit, maxLineSize, lineSeparator);

        Verify.hasText(srcPath);
        Verify.hasText(destPath);
        Verify.largerThan(sizePerSplit, 0, "sizePerSplit must be more than zero");
        Verify.largerThan(maxLineSize, 0);

        File srcFile = getFirstExistFile(srcPath);
        Verify.notNull(srcFile, "source file not exists");

        Map<String, Path> partFiles = new HashMap<>();
        final long sourceSize = Files.size(srcFile.toPath());

        logger.debug("Source file size: {}", sourceSize);

        createMissingDirectories(new File(destPath));

        if (sizePerSplit >= sourceSize) {
            KeyValuePair<String, Path> target = partFilePath(destPath, 0);
            Files.copy(Paths.get(srcPath), target.getValue(), StandardCopyOption.REPLACE_EXISTING);
            partFiles.put(target.getKey(), target.getValue());
            return partFiles;
        }

        List<Callable<KeyValuePair<String, Path>>> tasks = new LinkedList<>();

        List<FilePart> fileParts = partFile(sizePerSplit, maxLineSize, lineSeparator, srcFile, sourceSize);

        for (FilePart filePart : fileParts) {

            tasks.add(() -> {
                KeyValuePair<String, Path> dest = partFilePath(destPath, filePart.part);
                try (RandomAccessFile src = new RandomAccessFile(srcFile, "r"); FileChannel fc = src.getChannel()) {
                    writePartToFile(filePart.endOfPart - filePart.startOfPart, filePart.startOfPart, fc,
                            dest.getValue());
                }
                return dest;
            });
        }

        executeCallsStream(tasks, executor).forEach(dest -> partFiles.put(dest.getKey(), dest.getValue()));
        return partFiles;
    }

    /**
     * 
     * 生成分片文件名
     * 
     * @param destPath
     *            目标文件路径
     * @param part
     *            分片号
     * @return 分片文件名
     * @since JDK 1.8
     */
    private static KeyValuePair<String, Path> partFilePath(final String destPath, final int part) {
        String suffix = ".".concat(SecurityUtils.baseStringFixed(part, 16, 2, '0'));
        String shardName = new File(destPath).getName()
                .concat(suffix);
        return new KeyValuePair<>(shardName, Paths.get(destPath.concat(suffix)));
    }

    /**
     * 
     * 从文件中查找下一个指定字符的位置
     * 
     * @param begin
     *            开始位置
     * @param end
     *            结束位置
     * @param file
     *            文件
     * @param seekChar
     *            需要查找的字符
     * @return 下一个指定字符的位置，如果没找到则返回-1
     * @throws IOException
     *             遇到IO错误的时候会抛出
     * @since JDK 1.8
     */
    private static long findNext(long begin, long end, RandomAccessFile file, char seekChar) throws IOException {
        file.seek(begin);
        while (file.getFilePointer() < end) {
            if (file.readByte() == seekChar) {
                return file.getFilePointer();
            }
        }

        if (file.getFilePointer() == file.length()) {
            return file.getFilePointer();
        }

        return -1;
    }

    /**
     * 
     * 将源文件的一部分写入到目标文件
     * 
     * @param byteSize
     *            写入块的大小
     * @param position
     *            开始位置
     * @param sourceChannel
     *            源文件
     * @param dest
     *            目标文件路径
     * @throws IOException
     *             遇到IO错误的时候会抛出
     * @since JDK 1.8
     */
    private static void writePartToFile(long byteSize, long position, FileChannel sourceChannel, Path dest)
            throws IOException {
        try (RandomAccessFile toFile = new RandomAccessFile(dest.toFile(), "rw");
                FileChannel toChannel = toFile.getChannel()) {
            sourceChannel.position(position);
            toChannel.transferFrom(sourceChannel, 0, byteSize);
        }
    }

    /**
     * 
     * 输入流到文件
     * 
     * @param file
     *            目标文件
     * @param input
     *            输入流
     * @throws IOException
     *             {@link IOException}
     * @since JDK 1.8
     */
    public static void saveToFile(File file, InputStream input) throws IOException {
        saveToFile(file, input, false);
    }

    /**
     * 
     * 输入流到文件
     * 
     * @param file
     *            目标文件
     * @param input
     *            输入流
     * @param overwrite
     *            是否覆盖
     * @throws IOException
     *             {@link IOException}
     * @since JDK 1.8
     */
    public static void saveToFile(File file, InputStream input, boolean overwrite) throws IOException {
        Verify.notNull(file);
        Verify.notNull(input);
        createMissingDirectories(file);
        if (overwrite && file.exists()) {
            Files.delete(file.toPath());
        }
        Files.copy(input, file.toPath());
    }

    /**
     * 
     * 创建不存在的父目录
     * 
     * @param file
     *            文件
     * @since JDK 1.8
     */
    public static void createMissingDirectories(File file) {
        Verify.notNull(file);
        File dir = file.getParentFile();
        if (dir != null && !dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * 
     * 获取文件拓展名
     * 
     * @param file
     *            文件
     * @return 拓展名（不带'.'）
     * @since JDK 1.8
     */
    public static String getExtension(File file) {
        Verify.notNull(file);
        int index = file.getName()
                .lastIndexOf('.');
        if (index < 0 || file.getName()
                .endsWith(".")) {
            return StringUtils.EMPTY_STRING;
        }
        return file.getName()
                .substring(index + 1);
    }

    /**
     * 
     * 获取文件类型
     * 
     * @param file
     *            文件
     * @return 文件类型
     * @since JDK 1.8
     */
    public static FileType getFileType(File file) {
        return FileType.parse(getExtension(file));
    }

    /**
     * 
     * 拆分文件内容到字符串列表
     * 
     * @param filepath
     *            文件路径
     * @param splitor
     *            分割字符串
     * @param charset
     *            字符集
     * @return 字符串列表
     * @throws IOException
     *             {@link IOException}
     * @since JDK 1.8
     */
    public static List<String> splitFile(String filepath, String splitor, Charset charset) throws IOException {
        return splitFile(new File(filepath), splitor, charset);
    }

    /**
     * 
     * 拆分文件内容到字符串列表
     * 
     * @param filepath
     *            文件路径
     * @param splitor
     *            分割字符串
     * @param charset
     *            字符集
     * @return 字符串列表
     * @throws IOException
     *             {@link IOException}
     * @since JDK 1.8
     */
    public static List<String> splitFile(File file, String splitor, Charset charset) throws IOException {
        List<String> files = new ArrayList<>();
        StringBuilder buf = new StringBuilder();
        try (Stream<String> fileStream = Files.lines(file.toPath(), charset)) {
            for (String line : fileStream.collect(Collectors.toList())) {
                if (line.trim()
                        .equals(splitor)) {
                    files.add(buf.toString());
                    buf = new StringBuilder();
                }
                buf.append(line)
                        .append(StringUtils.NEW_LINE_STRING);
            }
        }
        files.add(buf.toString());
        return files;
    }

    /**
     * 
     * 读取整个文件内容
     * 
     * @param filepath
     *            路径
     * @param charset
     *            字符集
     * @return 文件的文本内容
     * @throws IOException
     *             {@link IOException}
     * @since JDK 1.8
     */
    public static String readToEnd(String filepath, Charset charset) throws IOException {
        StringBuilder buf = new StringBuilder();
        try (Stream<String> fileStream = Files.lines(Paths.get(filepath), charset)) {
            fileStream.forEach(line -> buf.append(line)
                    .append(StringUtils.NEW_LINE_STRING));
        }
        return buf.toString();
    }

    public static String readToEnd(InputStream is) throws IOException {
        String line;
        StringBuilder buf = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            while ((line = reader.readLine()) != null) {
                buf.append(line)
                        .append(System.getProperty("line.separator"));
            }
        }

        return buf.toString();
    }

    public static List<String> ls(InputStream is) throws IOException {
        String line;
        List<String> files = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            while ((line = reader.readLine()) != null) {
                files.add(line);
            }
        }

        return files;
    }

    public static List<String> ls(String path) throws IOException {
        return ls(getFirstExistResource(path));
    }

    public static List<String> ls(InputStream is, Predicate<String> predicate) throws IOException {
        return ls(is).stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    public static List<String> ls(String path, Predicate<String> predicate) throws IOException {
        return ls(getFirstExistResource(path), predicate);
    }

    /**
     * 
     * 相对路径转化为绝对路径
     * 
     * @param path
     *            相对路径
     * @return 绝对路径
     * @since JDK 1.8
     */
    public static Path mapLocalFullPath(String path) {
        String pathStr = FileUtils.class.getResource("/")
                .getPath()
                .concat(path);
        String os = System.getProperty("os.name")
                .toLowerCase();
        if ((os.indexOf("win") >= 0)) {
            pathStr = pathStr.substring(1);
        }
        return Paths.get(pathStr);
    }

    /**
     * 
     * 获取资源文件的绝对路径
     * 
     * @param path
     *            相对路径
     * @return 绝对路径
     * @since JDK 1.8
     */
    public static String mapResource(String path) {
        return Resources.getResource(path)
                .getPath();
    }

    /**
     * 
     * 获取gzip文件的reader
     * 
     * @param path
     *            文件路径
     * @param encoding
     *            编码
     * @return {@link BufferedReader}
     * @throws IOException
     *             {@link IOException}
     * @since JDK 1.8
     */
    public static BufferedReader readGZIPFile(String path, String encoding) throws IOException {
        InputStream fileStream = new FileInputStream(path);
        InputStream gzipStream = new GZIPInputStream(fileStream);
        Reader decoder = new InputStreamReader(gzipStream, encoding);
        return new BufferedReader(decoder);
    }

    /**
     * 
     * Get full path from relative path or class path.
     * 
     * @param path
     *            Path string with prifix {@value #CLASS_PATH_PRIFIX} for
     *            classpath or {@value #FILE_PATH_PRIFIX} for relative path or
     *            non prifix for both (will first check relative path. if not
     *            exist, then return classpath).
     * @return full path string.
     * @since JDK 1.8
     */
    public static String getFullFilePath(String path) {
        Verify.hasText(path);
        if (path.startsWith(CLASS_PATH_PRIFIX)) {
            return mapLocalFullPath(path.replaceFirst(CLASS_PATH_PRIFIX, StringUtils.EMPTY_STRING)).toString();
        } else if (path.startsWith(FILE_PATH_PRIFIX)) {
            return new File(path.replaceFirst(FILE_PATH_PRIFIX, StringUtils.EMPTY_STRING)).getAbsolutePath();
        } else {
            File classpath = mapLocalFullPath(path).toFile();
            File filepath = new File(path);
            if (filepath.exists()) {
                return filepath.getAbsolutePath();
            } else {
                return classpath.getAbsolutePath();
            }
        }
    }

    /**
     * 
     * 获取首个存在的文件
     * 
     * @param paths
     *            文件路径列表
     * @return 首个存在的文件
     * @since JDK 1.8
     */
    public static File getFirstExistFile(String... paths) {
        for (String path : paths) {
            File file = new File(getFullFilePath(path));
            if (file.exists()) {
                return file;
            }
        }

        return null;
    }

    public static InputStream getFirstExistResource(String... paths) {
        File file = getFirstExistFile(paths);
        if (file != null && file.exists()) {
            try {
                return new FileInputStream(file);
            } catch (FileNotFoundException e) {
                throw new ResourceNotFoundException(e);
            }
        } else {
            InputStream is = null;
            for (String path : paths) {
                is = FileUtils.class.getResourceAsStream(path);
                if (is != null) {
                    return is;
                }
            }
        }

        throw new ResourceNotFoundException("resource not found.");
    }

    public static String getFirstExistResourcePath(String... paths) {
        File file = getFirstExistFile(paths);
        if (file != null && file.exists()) {
            return file.getAbsolutePath();
        } else {
            InputStream is = null;
            for (String path : paths) {
                is = FileUtils.class.getResourceAsStream(path);
                if (is != null) {
                    return path;
                }
            }
        }

        throw new ResourceNotFoundException("resource not found.");
    }

    public static void delete(File file) {
        if (file == null || !file.exists()) {
            return;
        }

        File[] listFiles = file.listFiles();
        if (file.isDirectory() && listFiles != null && listFiles.length > 0) {
            for (File subFile : listFiles) {
                delete(subFile);
            }
        }

        try {
            Files.deleteIfExists(file.toPath());
        } catch (IOException e) {
            throw new ResourceProcessException(e);
        }
    }

    /**
     * 
     * 文件内容按行去重
     * 
     * @param in
     *            输入文件
     * @param out
     *            输出文件
     * @param workspace
     *            工作目录
     * @param shardNum
     *            分片数量
     * @param maxShardSize
     *            分片文件最大大小。若分片大于该大小会自动执行二次分片
     * @param maxLineSize
     *            单行最大字符数
     * @param executor
     *            执行线程池
     * @throws IOException
     * @since JDK 1.8
     */
    public static void deduplication(File in,
            File out,
            String workspace,
            final int shardNum,
            final long maxShardSize,
            final long maxLineSize,
            final int batchSize,
            ExecutorService executor) {
        if (!in.exists()) {
            throw new ResourceNotFoundException("Input file not found.");
        }

        if (out.exists() && out.isDirectory()) {
            throw new VerifyException("Output file is a directory.");
        }

        int actualShardNum = Utils.powerOfTwoFor(shardNum);

        logger.debug("Provided shardNum is '{}'. Actually shardNum is set to '{}'", shardNum, actualShardNum);

        if (workspace.endsWith(File.separator)) {
            workspace = workspace.substring(0, workspace.length() - 1);
        }

        String taskWorkspace = String.join(File.separator, workspace, StringUtils.getUUIDNoLine());
        File ws = new File(taskWorkspace);

        if (ws.exists() && !ws.isDirectory()) {
            throw new VerifyException("Workspace is not a directory.");
        }

        if (!ws.exists()) {
            ws.mkdirs();
        }
        List<FilePart> fileParts;
        try {
            final long sourceSize = Files.size(in.toPath());

            fileParts = partFile(maxShardSize, maxLineSize, '\n', in, sourceSize);
        } catch (IOException e) {
            throw new ResourceProcessException(e);
        }

        List<Callable<Set<File>>> shardJobs = new LinkedList<>();
        for (FilePart filePart : fileParts) {
            shardJobs.add(() -> hashShard(in, filePart.startOfPart, filePart.endOfPart, taskWorkspace, actualShardNum,
                    batchSize));
        }

        Set<File> shardFiles = executeCallsStream(shardJobs, executor).flatMap(Set::stream)
                .collect(Collectors.toSet());

        try (OutputStream os = new FileOutputStream(out)) {
            List<Callable<Boolean>> calls = new LinkedList<>();
            for (File shardFile : shardFiles) {
                calls.add(() -> deduplication(maxShardSize, maxLineSize, os, shardFile));
            }
            if (executeCallsStream(calls, executor).anyMatch(r -> !r)) {
                logger.warn("Deduplication task not all successed.");
            }

        } catch (IOException e) {
            throw new ResourceProcessException(e);
        } finally {
            delete(ws);
        }
    }

    private static boolean deduplication(long maxShardSize, long maxLineSize, OutputStream os, File shardFile)
            throws IOException {
        long shardSize = Files.size(shardFile.toPath());
        if (shardSize > maxShardSize) {
            Map<String, Path> shardSplits = split(shardFile.getAbsolutePath(),
                    Paths.get(shardFile.getParent(), shardFile.getName()
                            .concat("_split"))
                            .toString(),
                    maxShardSize, maxLineSize, '\n', null);
            File mergeFile = new File(shardFile.getAbsolutePath()
                    .concat("_merge"));
            try (OutputStream shardOs = new FileOutputStream(mergeFile)) {
                for (Path shardSplit : shardSplits.values()) {
                    deduplication(shardSplit.toFile(), shardOs);
                }
            }
            return deduplication(mergeFile, os);
        } else {
            return deduplication(shardFile, os);
        }
    }

    private static boolean deduplication(File src, OutputStream out) {
        try {
            Set<String> contentSet = new HashSet<>();
            try (Stream<String> lines = Files.lines(src.toPath())) {
                lines.forEach(contentSet::add);
            }
            for (String line : contentSet) {
                out.write(line.concat(System.lineSeparator())
                        .getBytes(StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            logger.error("Fail to deduplication", e);
            return false;
        }

        return true;
    }

    private static Set<File> hashShard(final File in,
            final long startPointer,
            final long endPointer,
            final String workspace,
            final int shardNum,
            final int batchSize) {

        Set<File> shardFiles = new HashSet<>();
        OutputStream[] shardStreams = new OutputStream[shardNum];
        Map<Integer, List<String>> contentMap = new HashMap<>(shardNum);
        for (int i = 0; i < shardNum; i++) {
            try {
                contentMap.put(i, new LinkedList<>());
                File shardFile = new File(String.join("/", workspace, String.valueOf(i)));
                shardFiles.add(shardFile);
                createMissingDirectories(shardFile);
                shardStreams[i] = new FileOutputStream(shardFile, true);
            } catch (IOException e) {
                throw new ResourceProcessException(e);
            }
        }
        try (RandomAccessFile inFile = new RandomAccessFile(in, "r")) {
            logger.debug("Reading from {} to {}", startPointer, endPointer);
            inFile.seek(startPointer);
            String line;
            while (inFile.getFilePointer() < endPointer && (line = inFile.readLine()) != null) {
                if (StringUtils.isEmpty(line)) {
                    continue;
                }
                int shard = (shardNum - 1) & Utils.hash(line);
                List<String> lines = contentMap.get(shard);
                lines.add(line);
                if (lines.size() > batchSize) {
                    OutputStream os = shardStreams[shard];
                    os.write(String.join(System.lineSeparator(), lines)
                            .concat(System.lineSeparator())
                            .getBytes(StandardCharsets.UTF_8));
                    lines.clear();
                }
            }

            for (int i = 0; i < shardNum; i++) {
                OutputStream os = shardStreams[i];
                List<String> lines = contentMap.get(i);
                if (os != null && !lines.isEmpty()) {
                    os.write(String.join(System.lineSeparator(), lines)
                            .concat(System.lineSeparator())
                            .getBytes(StandardCharsets.UTF_8));
                }
                if (os != null) {
                    os.flush();
                    os.close();
                }
            }
        } catch (IOException e) {
            throw new ResourceProcessException(e);
        }

        return shardFiles;
    }

    private static <T> Stream<T> executeCallsStream(List<Callable<T>> calls, ExecutorService executor) {
        if (CollectionUtils.isEmpty(calls)) {
            return Stream.empty();
        }
        if (executor == null) {
            return calls.parallelStream()
                    .map(call -> {
                        try {
                            return call.call();
                        } catch (Exception e) {
                            logger.error(e.getMessage(), e);
                            throw new ResourceProcessException(e);
                        }
                    });
        } else {
            try {
                return executor.invokeAll(calls)
                        .stream()
                        .map(future -> {
                            try {
                                return future.get();
                            } catch (InterruptedException | ExecutionException e) {
                                logger.error(e.getMessage(), e);
                                Thread.currentThread()
                                        .interrupt();
                            }
                            return null;
                        })
                        .filter(Objects::nonNull);
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
                Thread.currentThread()
                        .interrupt();
            }
        }

        return Stream.empty();
    }

    private static List<FilePart> partFile(final long sizePerSplit,
            final long maxLineSize,
            final char lineSeparator,
            final File srcFile,
            final long sourceSize) throws IOException {
        final AtomicInteger partCounter = new AtomicInteger();
        List<FilePart> fileParts = new LinkedList<>();
        try (RandomAccessFile sourceFile = new RandomAccessFile(srcFile, "r")) {
            long pointer = 0;
            while (pointer < sourceSize) {
                long startOfPart = pointer;
                pointer += sizePerSplit;
                long next = Math.min(
                        findNext(pointer, Math.min(pointer + maxLineSize, sourceSize), sourceFile, lineSeparator) - 1,
                        sourceSize);
                long endOfPart = next <= 0 ? sourceSize : next;

                int part = partCounter.getAndIncrement();

                logger.debug("part:{}, next: {}, startOfPart: {}, endOfPart: {}", part, next, startOfPart, endOfPart);
                pointer = endOfPart + 1;
                fileParts.add(new FilePart(part, startOfPart, endOfPart));
            }
        }
        return fileParts;
    }

    @AllArgsConstructor
    private static class FilePart {
        private int part;
        private long startOfPart;
        private long endOfPart;
    }
}

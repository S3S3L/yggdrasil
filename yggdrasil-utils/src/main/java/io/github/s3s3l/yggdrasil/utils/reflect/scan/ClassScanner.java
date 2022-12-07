package io.github.s3s3l.yggdrasil.utils.reflect.scan;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

import io.github.s3s3l.yggdrasil.bean.FileType;
import io.github.s3s3l.yggdrasil.utils.common.StringUtils;
import io.github.s3s3l.yggdrasil.utils.file.FileUtils;
import io.github.s3s3l.yggdrasil.utils.reflect.exception.ReflectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClassScanner implements Scanner {
    private static final Logger logger = LoggerFactory.getLogger(ClassScanner.class);

    @Override
    public Set<Class<?>> scan(String... packages) {
        ClassLoader cl = Thread.currentThread()
                .getContextClassLoader();
        return Arrays.stream(packages)
                .map(pkg -> scan(cl, pkg))
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }

    /**
     * 扫描指定包下的所有类
     * 
     * @param cl
     *            ClassLoader
     * @param pkg
     *            包名
     * @return
     */
    private Set<Class<?>> scan(ClassLoader cl, String pkg) {
        String jarPath = pkg.replace('.', File.separatorChar);
        Set<Class<?>> clzSet = new HashSet<>();
        try {
            Enumeration<URL> resources = cl.getResources(jarPath);
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                if (resource.getProtocol()
                        .equals("jar")) {
                    JarURLConnection jarConn = (JarURLConnection) resource.openConnection();
                    try (JarFile jarFile = jarConn.getJarFile()) {
                        clzSet.addAll(findAllClassInJar(cl, jarFile, jarPath));
                    }
                } else if (resource.getProtocol()
                        .equals("file")) {
                    clzSet.addAll(findAllClassInFolder(cl, new File(resource.toURI()), pkg));
                }
            }
        } catch (IOException | URISyntaxException e) {
            throw new ReflectException(e);
        }

        return clzSet;
    }

    /**
     * 查找指定目录下的所有class
     * 
     * @param cl
     *            ClassLoader
     * @param folder
     *            目录
     * @param pkg
     *            基础包名
     * @return
     */
    private Set<Class<?>> findAllClassInFolder(ClassLoader cl, File folder, String pkg) {
        Set<Class<?>> clzSet = new HashSet<>();
        if (!folder.isDirectory()) {
            return clzSet;
        }

        for (File file : Optional.ofNullable(folder.listFiles())
                .orElse(new File[] {})) {
            if (isClassFile(file)) {
                String className = getClassName(file.getName());
                if (StringUtils.isEmpty(className)) {
                    continue;
                }

                try {
                    clzSet.add(cl.loadClass(String.join(".", pkg, className)));
                } catch (ClassNotFoundException e) {
                    logger.warn("fail to load class", e);
                }
            } else if (file.isDirectory()) {
                clzSet.addAll(findAllClassInFolder(cl, file, String.join(".", pkg, file.getName())));
            }
        }

        return clzSet;
    }

    /**
     * 校验文件是否为class文件
     * 
     * @param file
     *            文件对象
     * @return true: 文件是class文件;false: 文件不是class文件
     */
    private boolean isClassFile(File file) {
        if (file == null || !file.exists() || !file.isFile()) {
            return false;
        }

        return FileUtils.getFileType(file) == FileType.CLASS;
    }

    /**
     * 从class文件名中获取类名
     * 
     * @param fileName
     *            class文件名
     * @return 如果文件不是已.class结尾，则返回空字符串；否则，返回解析后的类名
     */
    private String getClassName(String fileName) {
        if (StringUtils.isEmpty(fileName) || !fileName.endsWith(".class")) {
            return "";
        }

        return fileName.substring(0, fileName.length() - 6);
    }

    /**
     * 
     * 查找jar包中指定路径下所有的class
     * 
     * @param cl
     *            ClassLoader
     * @param jarFile
     *            jar包文件
     * @param path
     *            目录路径
     * @return 指定路径下的所有类的集合
     */
    private Set<Class<?>> findAllClassInJar(ClassLoader cl, JarFile jarFile, String path) {
        Set<Class<?>> clzSet = new HashSet<>();
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            if (entry.getName()
                    .startsWith(path)
                    && entry.getName()
                            .endsWith(".class")) {
                try {
                    clzSet.add(cl.loadClass(entry.getName()
                            .replace(".class", "")
                            .replace(File.separatorChar, '.')));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return clzSet;
    }

}

package io.github.s3s3l.yggdrasil.game.core.snapshot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文件快照存储器
 * 将快照序列化后存储到文件系统中
 */
public class FileSnapshotStore implements SnapshotStore {

    private final Path storagePath;
    private final SnapshotSerializer serializer;

    /**
     * 创建文件快照存储器
     *
     * @param storageDir 存储目录
     * @param serializer 序列化器
     */
    public FileSnapshotStore(String storageDir, SnapshotSerializer serializer) {
        this.storagePath = Paths.get(storageDir);
        this.serializer = serializer;

        // 确保存储目录存在
        try {
            Files.createDirectories(this.storagePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create storage directory", e);
        }
    }

    private Path getSnapshotPath(long id) {
        return storagePath.resolve(id + ".snapshot.json");
    }

    @Override
    public void save(Snapshot snapshot) {
        Path path = getSnapshotPath(snapshot.getId());
        try {
            String json = serializer.serialize(snapshot);
            Files.writeString(path, json);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save snapshot: " + snapshot.getId(), e);
        }
    }

    @Override
    public Snapshot get(long id) {
        Path path = getSnapshotPath(id);
        if (!Files.exists(path)) {
            return null;
        }

        try {
            String json = Files.readString(path);
            return serializer.deserialize(json, ObjectSnapshot.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load snapshot: " + id, e);
        }
    }

    @Override
    public boolean delete(long id) {
        Path path = getSnapshotPath(id);
        try {
            return Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete snapshot: " + id, e);
        }
    }

    @Override
    public boolean exists(long id) {
        Path path = getSnapshotPath(id);
        return Files.exists(path);
    }

    @Override
    public Collection<Snapshot> getAll() {
        List<Snapshot> snapshots = new ArrayList<>();
        try (var stream = Files.list(storagePath)) {
            stream.filter(p -> p.getFileName().toString().endsWith(".snapshot.json"))
                    .forEach(path -> {
                        String idStr = path.getFileName().toString().replace(".snapshot.json", "");
                        try {
                            long id = Long.parseLong(idStr);
                            Snapshot snapshot = get(id);
                            if (snapshot != null) {
                                snapshots.add(snapshot);
                            }
                        } catch (NumberFormatException e) {
                            // 忽略无效的文件名
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException("Failed to list snapshots", e);
        }
        return snapshots;
    }

    @Override
    public void clear() {
        try (var stream = Files.list(storagePath)) {
            stream.filter(p -> p.getFileName().toString().endsWith(".snapshot.json"))
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            throw new RuntimeException("Failed to delete snapshot file: " + path, e);
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException("Failed to clear snapshots", e);
        }
    }

    @Override
    public int count() {
        try (var stream = Files.list(storagePath)) {
            return (int) stream.filter(p -> p.getFileName().toString().endsWith(".snapshot.json")).count();
        } catch (IOException e) {
            throw new RuntimeException("Failed to count snapshots", e);
        }
    }
}

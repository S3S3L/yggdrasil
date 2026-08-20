package io.github.s3s3l.yggdrasil.game.core.telemetry;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.Objects;

final class JsonLinesTelemetrySink implements TelemetrySink {

    private final Path output;
    private BufferedWriter writer;

    JsonLinesTelemetrySink(Path output) {
        this.output = Objects.requireNonNull(output, "output");
    }

    @Override
    public void accept(TelemetrySnapshot snapshot) throws IOException {
        if (writer == null) {
            writer = Files.newBufferedWriter(output, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.APPEND);
        }
        try {
            writer.write(TelemetryJson.toJson(snapshot, Instant.now().toString()));
            writer.newLine();
            writer.flush();
        } catch (IOException failure) {
            closeWriter();
            throw failure;
        }
    }

    @Override
    public void close() throws IOException {
        closeWriter();
    }

    private void closeWriter() throws IOException {
        if (writer != null) {
            try {
                writer.close();
            } finally {
                writer = null;
            }
        }
    }
}

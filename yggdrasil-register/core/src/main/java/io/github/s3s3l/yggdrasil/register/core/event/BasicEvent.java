package io.github.s3s3l.yggdrasil.register.core.event;

import java.util.function.Supplier;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BasicEvent implements Event<BasicEventType, byte[]> {
    private BasicEventType eventType;
    private Supplier<byte[]> data;
    private Supplier<byte[]> oldData;

    @Override
    public BasicEventType eventType() {
        return eventType;
    }

    @Override
    public byte[] data() {
        return data.get();
    }

    @Override
    public byte[] oldData() {
        return oldData.get();
    }
}

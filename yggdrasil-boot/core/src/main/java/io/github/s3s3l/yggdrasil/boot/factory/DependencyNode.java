package io.github.s3s3l.yggdrasil.boot.factory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class DependencyNode<T> {
    private T value;
    private DependencyNode<T> pre;
    private DependencyNode<T> next;
}

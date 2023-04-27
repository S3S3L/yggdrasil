package io.github.s3s3l.yggdrasil.boot.factory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.github.s3s3l.yggdrasil.boot.exception.CircularDependenciesException;

public class DependencyChain<T> {

    private static final String LINK = " -> ";

    private DependencyNode<T> first;
    private DependencyNode<T> last;

    private Map<T, DependencyNode<T>> index = new ConcurrentHashMap<>();

    public DependencyChain<T> append(T value) {

        index.compute(value, (v, node) -> {
            if (node != null) {
                DependencyNode<T> current = node;
                StringBuilder sb = new StringBuilder(current.getValue()
                        .toString());
                while ((current = current.getNext()) != null) {
                    sb.append(LINK);
                    sb.append(current.getValue()
                            .toString());
                }
                sb.append(LINK);
                sb.append(value.toString());
                throw new CircularDependenciesException(sb.toString());
            }

            node = DependencyNode.<T> builder()
                    .value(value)
                    .build();

            if (first == null) {
                first = node;
            }

            if (last != null) {
                node.setPre(last);
                last.setNext(node);
            }
            last = node;

            return node;
        });

        return this;
    }

    public T pop() {
        if (last == null) {
            return null;
        }

        T value = last.getValue();

        DependencyNode<T> pre = last.getPre();
        if (pre == null) {
            first = null;
            last = null;
            index.clear();
        } else {
            pre.setNext(null);
            last = pre;
            index.remove(value);
        }

        return value;
    }
}

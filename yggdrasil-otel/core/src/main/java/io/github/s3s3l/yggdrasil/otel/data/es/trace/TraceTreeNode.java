package io.github.s3s3l.yggdrasil.otel.data.es.trace;

import java.util.SortedSet;
import java.util.TreeSet;

import io.github.s3s3l.yggdrasil.otel.data.es.DataPiece;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TraceTreeNode implements Comparable<TraceTreeNode> {
    @Builder.Default
    private boolean root = false;
    private DataPiece data;
    @Builder.Default
    private SortedSet<TraceTreeNode> children = new TreeSet<>();

    @Override
    public int compareTo(TraceTreeNode o) {
        if (o == null) {
            return 1;
        }

        return this.data.getTimestamp()
                .compareTo(o.data.getTimestamp());
    }
    
    public void addChild(TraceTreeNode child) {
        if (child == null) {
            return;
        }

        children.add(child);
    }
}

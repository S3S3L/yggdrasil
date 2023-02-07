package io.github.s3s3l.yggdrasil.register.core.node;

import java.util.Map;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Node {
    private String name;
    private String host;
    private int port;
    private String group;
    private Set<String> tags;
    private Map<String, String> meta;
    private NodeType nodeType;
}

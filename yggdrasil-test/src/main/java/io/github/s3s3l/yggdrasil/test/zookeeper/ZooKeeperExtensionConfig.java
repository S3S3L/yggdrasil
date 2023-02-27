package io.github.s3s3l.yggdrasil.test.zookeeper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ZooKeeperExtensionConfig {
    private String tarLocation;
}

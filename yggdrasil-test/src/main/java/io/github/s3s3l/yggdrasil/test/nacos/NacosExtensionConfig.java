package io.github.s3s3l.yggdrasil.test.nacos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class NacosExtensionConfig {
    @Builder.Default
    private int port = 8848;
    @Builder.Default
    private String startedRegex = ".*Nacos started successfully in stand alone mode.*";
}

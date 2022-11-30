package org.s3s3l.yggdrasil.starter.datasource.config;

import org.s3s3l.yggdrasil.orm.exec.CreateConfig;

import lombok.Data;

@Data
public class AutoCreateConfig extends CreateConfig {
    private boolean enable = false;
}

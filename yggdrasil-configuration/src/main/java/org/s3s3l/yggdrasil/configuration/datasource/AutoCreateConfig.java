package org.s3s3l.yggdrasil.configuration.datasource;

import lombok.Data;

@Data
public class AutoCreateConfig {
    private boolean enable = false;
    private boolean force = false;
}

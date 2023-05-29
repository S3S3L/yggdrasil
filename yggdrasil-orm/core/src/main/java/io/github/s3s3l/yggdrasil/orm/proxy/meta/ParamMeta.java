package io.github.s3s3l.yggdrasil.orm.proxy.meta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ParamMeta {
    private String name;
    private int index;
}

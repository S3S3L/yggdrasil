package io.github.s3s3l.yggdrasil.utils.reflect;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PlaceHolderMeta {
    private String fieldName;
    @Builder.Default
    private boolean array = false;
    private int index;
}

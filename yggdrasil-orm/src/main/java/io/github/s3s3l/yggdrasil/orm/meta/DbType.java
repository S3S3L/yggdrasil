package io.github.s3s3l.yggdrasil.orm.meta;

import java.util.List;
import java.util.Objects;

import io.github.s3s3l.yggdrasil.utils.collection.CollectionUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class DbType {
    private String type;
    private List<String> args;
    @Builder.Default
    private boolean primary = false;
    @Builder.Default
    private boolean notNull = false;
    @Builder.Default
    private boolean def = false;
    @Builder.Default
    private String defValue = "";

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof DbType)) {
            return false;
        }

        DbType o = (DbType) obj;

        return Objects.equals(this.type, o.type) && Objects.equals(this.primary, o.primary)
                && Objects.equals(this.notNull, o.notNull) && Objects.equals(this.def, o.def)
                && Objects.equals(this.defValue, o.defValue) && CollectionUtils.equals(this.args, o.args);
    }
}

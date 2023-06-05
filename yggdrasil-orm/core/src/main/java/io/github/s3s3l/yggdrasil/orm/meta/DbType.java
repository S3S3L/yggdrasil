package io.github.s3s3l.yggdrasil.orm.meta;

import java.sql.JDBCType;
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
    private JDBCType type;
    private String typeName;
    private List<String> args;
    @Builder.Default
    private boolean primary = false;
    @Builder.Default
    private boolean notNull = false;
    @Builder.Default
    private boolean def = false;
    @Builder.Default
    private String defValue = "";
    @Builder.Default
    private boolean array = false;

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof DbType)) {
            return false;
        }

        DbType o = (DbType) obj;

        return this.type.equals(o.type)
                // && Objects.equals(this.primary, o.primary) // 主键不支持变更，所以不参与比较
                && Objects.equals(this.notNull, o.notNull) && Objects.equals(this.def, o.def)
                && Objects.equals(this.defValue, o.defValue) && CollectionUtils.equals(this.args, o.args);
    }
}

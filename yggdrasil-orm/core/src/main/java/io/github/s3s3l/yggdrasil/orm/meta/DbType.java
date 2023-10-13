package io.github.s3s3l.yggdrasil.orm.meta;

import java.sql.JDBCType;
import java.util.List;

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
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((args == null) ? 0 : args.hashCode());
        result = prime * result + (notNull ? 1231 : 1237);
        result = prime * result + (def ? 1231 : 1237);
        result = prime * result + ((defValue == null) ? 0 : defValue.hashCode());
        result = prime * result + (array ? 1231 : 1237);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DbType other = (DbType) obj;
        if (type != other.type)
            return false;
        if (args == null) {
            if (other.args != null)
                return false;
        } else if (!args.equals(other.args))
            return false;
        if (notNull != other.notNull)
            return false;
        if (def != other.def)
            return false;
        if (defValue == null) {
            if (other.defValue != null)
                return false;
        } else if (!defValue.equals(other.defValue))
            return false;
        if (array != other.array)
            return false;
        return true;
    }
}

package org.s3s3l.yggdrasil.orm.bind;

import java.lang.reflect.Field;

import org.s3s3l.yggdrasil.orm.handler.TypeHandler;
import org.s3s3l.yggdrasil.orm.validator.Validator;
import org.s3s3l.yggdrasil.utils.common.StringUtils;
import org.s3s3l.yggdrasil.utils.reflect.ReflectionBean;
import org.s3s3l.yggdrasil.utils.verify.Verify;

/**
 * 
 * <p>
 * </p>
 * ClassName: ColumnStruct <br>
 * date: Sep 20, 2019 11:28:19 AM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class ColumnStruct implements DataBindNode {

    private String name;
    private String alias;
    private String tableAlias;
    private Field field;
    private Validator validator;
    private TypeHandler typeHandler;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getTableAlias() {
        return tableAlias;
    }

    public void setTableAlias(String tableAlias) {
        this.tableAlias = tableAlias;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public Validator getValidator() {
        return validator;
    }

    public void setValidator(Validator validator) {
        this.validator = validator;
    }

    public TypeHandler getTypeHandler() {
        return typeHandler;
    }

    public void setTypeHandler(TypeHandler typeHandler) {
        this.typeHandler = typeHandler;
    }

    @Override
    public SqlStruct toSqlStruct(ReflectionBean bean) {
        Verify.notNull(bean);

        SqlStruct struct = new SqlStruct();

        if (StringUtils.isEmpty(this.name) || this.field == null || this.validator == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder(" ");

        if (!StringUtils.isEmpty(this.tableAlias)) {
            sb.append(this.tableAlias)
                    .append(".");
        }

        sb.append(this.name);

        sb.append(String.format(" AS %s", StringUtils.isEmpty(this.alias) ? this.name : this.alias));

        struct.setSql(sb.toString());

        return struct;
    }
}

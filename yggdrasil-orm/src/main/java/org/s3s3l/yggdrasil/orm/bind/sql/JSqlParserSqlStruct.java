package org.s3s3l.yggdrasil.orm.bind.sql;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.sf.jsqlparser.expression.Expression;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JSqlParserSqlStruct implements SqlStruct {
    private Expression expression;
    @Builder.Default
    private final List<Object> params = new LinkedList<>();

    public JSqlParserSqlStruct addParam(Object... param) {
        this.params.addAll(Arrays.asList(param));
        return this;
    }

    public JSqlParserSqlStruct addParams(List<Object> params) {
        this.params.addAll(params);
        return this;
    }

    @Override
    public String getSql() {
        return expression == null ? "" : expression.toString();
    }

}

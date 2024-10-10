package io.github.s3s3l.yggdrasil.orm.bind.express.jsqlparser.postgresql;

import java.util.List;
import java.util.stream.Collectors;

import io.github.s3s3l.yggdrasil.utils.collection.CollectionUtils;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.expression.ValueListExpression;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;

public class ArrayValue extends ValueListExpression {

    public ArrayValue(Expression... expressions) {
        this.setExpressionList(new ExpressionList(expressions));
    }

    public ArrayValue(List<Expression> expressions) {
        this.setExpressionList(new ExpressionList(expressions));
    }

    @Override
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    @Override
    public String toString() {

        List<Expression> expressions = getExpressionList().getExpressions();
        if (CollectionUtils.isEmpty(expressions)) {
            return "ARRAY[]::text[]";
        }
        return String.format("ARRAY[%s]", String.join(",", expressions
                .stream()
                .map(Object::toString)
                .collect(Collectors.toList())));
    }

}

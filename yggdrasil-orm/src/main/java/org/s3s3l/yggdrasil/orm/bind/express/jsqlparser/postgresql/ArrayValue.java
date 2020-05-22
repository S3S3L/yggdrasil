package org.s3s3l.yggdrasil.orm.bind.express.jsqlparser.postgresql;

import java.util.List;
import java.util.stream.Collectors;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.expression.ValueListExpression;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;

public class ArrayValue extends ValueListExpression implements Expression {

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

        return String.format("ARRAY[%s]", String.join(",", getExpressionList().getExpressions()
                .stream()
                .map(Object::toString)
                .collect(Collectors.toList())));
    }

}

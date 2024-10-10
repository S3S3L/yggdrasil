package io.github.s3s3l.yggdrasil.orm.bind.value;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import io.github.s3s3l.yggdrasil.orm.bind.DataBindNode;
import io.github.s3s3l.yggdrasil.orm.bind.sql.DefaultSqlStruct;
import io.github.s3s3l.yggdrasil.orm.bind.sql.SqlStruct;
import io.github.s3s3l.yggdrasil.orm.handler.TypeHandler;
import io.github.s3s3l.yggdrasil.orm.meta.ColumnMeta;
import io.github.s3s3l.yggdrasil.utils.reflect.ReflectionBean;
import io.github.s3s3l.yggdrasil.utils.verify.Verify;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * 
 * <p>
 * </p>
 * ClassName: ValuesStruct <br>
 * date: Sep 20, 2019 11:29:55 AM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
@AllArgsConstructor
@NoArgsConstructor
public class ValuesStruct implements DataBindNode {

    private List<ColumnMeta> nodes = new ArrayList<>();

    public void addNode(ColumnMeta node) {
        this.nodes.add(node);
    }

    public void addNodes(Collection<ColumnMeta> nodes) {
        this.nodes.addAll(nodes);
    }

    @Override
    public SqlStruct toSqlStruct(ReflectionBean bean) {
        return toSqlStruct(bean, false);
    }

    public DefaultSqlStruct toSqlStruct(ReflectionBean bean, boolean first) {
        Verify.notNull(bean);

        // TODO 是否要过滤有效字段
        // List<ColumnStruct> validatedNodes = this.nodes.stream()
        // .filter(r -> r.getValidator()
        // .isValid(bean.getFieldValue(r.getField()
        // .getName())))
        // .collect(Collectors.toList());
        List<ColumnMeta> validatedNodes = this.nodes;

        if (validatedNodes.isEmpty()) {
            return null;
        }

        DefaultSqlStruct struct = new DefaultSqlStruct();

        String values = String.join(", ", validatedNodes.stream()
                .map(r -> {
                    Class<?> fieldType = r.getField().getType();
                    Object fieldValue = bean.getFieldValue(r.getField().getName());
                    TypeHandler typeHandler = r.getTypeHandler();

                    if (fieldType.isArray()) {
                        StringBuilder sb = new StringBuilder("ARRAY[");
                        List<String> paramPlaceHolders = new LinkedList<>();
                        if (r.getValidator()
                                .isValid(fieldValue)) {
                            Arrays.stream(((Object[]) fieldValue))
                                    .forEach(obj -> {
                                        struct.addParam(typeHandler
                                                .toJDBCType(obj));
                                        paramPlaceHolders.add("?");
                                    });
                        }
                        sb.append(String.join(",", paramPlaceHolders)).append("]");
                        if (paramPlaceHolders.isEmpty()) {
                            Class<?> componentType = fieldType.getComponentType();
                            if (int.class.isAssignableFrom(componentType)) {
                                sb.append("::int[]");
                            } else if (double.class.isAssignableFrom(componentType)) {
                                sb.append("::float[]");
                            } else {
                                sb.append("::text[]");
                            }
                        }
                        return sb.toString();
                    } else if (Collection.class.isAssignableFrom(fieldType)) {
                        StringBuilder sb = new StringBuilder("ARRAY[");
                        List<String> paramPlaceHolders = new LinkedList<>();
                        if (r.getValidator()
                                .isValid(fieldValue)) {
                            ((Collection<?>) fieldValue)
                                    .forEach(obj -> {
                                        struct.addParam(typeHandler
                                                .toJDBCType(obj));
                                        paramPlaceHolders.add("?");
                                    });
                        }
                        sb.append(String.join(",", paramPlaceHolders)).append("]");
                        return sb.toString();
                    } else {
                        struct.addParam(typeHandler
                                .toJDBCType(fieldValue));
                        return "?";
                    }
                })
                .collect(Collectors.toList()));

        if (first) {
            String fields = String.join(", ", validatedNodes.stream()
                    .map(ColumnMeta::getName)
                    .collect(Collectors.toList()));
            struct.setSql(String.format(" (%s) VALUES (%s)", fields, values));
        } else {
            struct.setSql(String.format(", (%s)", values));
        }

        return struct;
    }
}

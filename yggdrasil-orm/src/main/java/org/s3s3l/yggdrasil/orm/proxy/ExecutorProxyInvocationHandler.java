package org.s3s3l.yggdrasil.orm.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.s3s3l.yggdrasil.orm.ds.DatasourceHolder;
import org.s3s3l.yggdrasil.orm.exception.ProxyExecutingException;
import org.s3s3l.yggdrasil.orm.exception.SqlExecutingException;
import org.s3s3l.yggdrasil.orm.handler.StatementHelper;
import org.s3s3l.yggdrasil.orm.proxy.meta.ParamMeta;
import org.s3s3l.yggdrasil.orm.proxy.meta.ProxyMeta;
import org.s3s3l.yggdrasil.orm.proxy.meta.ProxyMethodMeta;
import org.s3s3l.yggdrasil.utils.collection.CollectionUtils;
import org.s3s3l.yggdrasil.utils.common.FreeMarkerHelper;
import org.s3s3l.yggdrasil.utils.reflect.PlaceHolderMeta;
import org.s3s3l.yggdrasil.utils.reflect.ReflectionUtils;

import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuperBuilder
@AllArgsConstructor
public class ExecutorProxyInvocationHandler implements InvocationHandler {
    private final Pattern placeHolderRegex = Pattern.compile("#[a-zA-Z\\d\\.\\[\\]]+#");

    private final ProxyMeta proxyMeta;
    private final FreeMarkerHelper freeMarkerHelper;
    private final DatasourceHolder datasourceHolder;
    private final StatementHelper statementHelper;

    @Override
    public Object invoke(Object target, Method method, Object[] args) throws Throwable {
        ProxyMethodMeta methodMeta = proxyMeta.getMethod(method);
        if (methodMeta == null) {
            throw new ProxyExecutingException("Proxy method meta not found. " + method.getName());
        }

        Map<String, Object> paramMap = new HashMap<>();
        int index = 0;
        for (Parameter param : method.getParameters()) {
            paramMap.put(param.getName(), args[index++]);
        }

        if (!CollectionUtils.isEmpty(methodMeta.getParams())) {
            for (ParamMeta paramMeta : methodMeta.getParams()) {
                paramMap.put(paramMeta.getName(), args[paramMeta.getIndex()]);
            }
        }

        String sqlTemplate = freeMarkerHelper.format(
                String.join("#", method.getDeclaringClass().getName(), method.getName()),
                methodMeta.getSql(), paramMap);
        Matcher matcher = placeHolderRegex.matcher(sqlTemplate);

        List<Object> params = new LinkedList<>();

        while (matcher.find()) {
            params.add(resolvePlaceHolder(matcher.group(), paramMap));
        }

        String finalSql = matcher.replaceAll("?");

        log.debug("Execute sql: \n{}", finalSql);
        return datasourceHolder.useConn(conn -> {
            try (PreparedStatement statement = conn.prepareStatement(finalSql)) {

                for (int i = 0; i < params.size(); i++) {
                    Object param = params.get(i);
                    int paramIndex = i + 1;
                    statement.setObject(paramIndex, param);
                    log.debug("Param{}: {}", paramIndex, param);
                }

                ResultSet rs = statement.executeQuery();

                return mapResult(rs, method.getReturnType(), method.getGenericReturnType());
            } catch (InvocationTargetException | NoSuchMethodException e) {
                throw new SqlExecutingException(e);
            }
        });
    }

    private Object resolvePlaceHolder(String placeHolder, Map<String, Object> paramMap) {
        String ph = placeHolder.substring(1, placeHolder.length() - 1);
        List<PlaceHolderMeta> resolvedPlaceHolder = ReflectionUtils.resolvePlaceHolder(ph);
        Object res = null;
        for (int i = 0; i < resolvedPlaceHolder.size(); i++) {
            if (i == 0) {
                res = ReflectionUtils.getObject(resolvedPlaceHolder.get(i), paramMap);
            } else {
                res = ReflectionUtils.getObject(resolvedPlaceHolder.get(i), res);
            }
        }
        return res;
    }

    private Object mapResult(ResultSet rs, Class<?> returnType,
            Type genericReturnType)
            throws SQLException, InvocationTargetException, NoSuchMethodException, SecurityException {
        if (returnType.isPrimitive()) {
            if (!rs.next()) {
                throw new SqlExecutingException("There is not any result. But required one.");
            }
            return rs.getObject(1);
        }

        if (Collection.class.isAssignableFrom(returnType)) {
            if (List.class.isAssignableFrom(returnType) && genericReturnType instanceof ParameterizedType) {
                return statementHelper
                        .mapResultTo(ReflectionUtils.getGenericClass((ParameterizedType) genericReturnType, 0), rs);
            }

            throw new SqlExecutingException("Return type not supported. " + returnType.getName());
        }

        if (returnType.isArray()) {
            return statementHelper
                    .mapResultTo(ReflectionUtils.getGenericClass((ParameterizedType) genericReturnType, 0), rs)
                    .toArray();
        }

        List<?> res = statementHelper
                .mapResultTo(returnType, rs);

        if (res.size() > 1) {
            throw new SqlExecutingException("Multi records in result. But return type is one. " + returnType.getName());
        }

        return CollectionUtils.getFirst(res);
    }

}

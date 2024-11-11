package io.github.s3s3l.yggdrasil.utils.verify;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import io.github.s3s3l.yggdrasil.bean.exception.VerifyException;
import io.github.s3s3l.yggdrasil.bean.verify.Examine;
import io.github.s3s3l.yggdrasil.bean.verify.Examines;
import io.github.s3s3l.yggdrasil.utils.collection.CollectionUtils;
import io.github.s3s3l.yggdrasil.utils.common.StringUtils;
import io.github.s3s3l.yggdrasil.utils.reflect.PropertyDescriptorReflectionBean;
import io.github.s3s3l.yggdrasil.utils.reflect.ReflectionBean;
import io.github.s3s3l.yggdrasil.utils.reflect.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * </p>
 * ClassName:CommonVerifier <br>
 * Date: Apr 7, 2017 4:59:24 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
@Slf4j
public class CommonVerifier implements Verifier {

    @Override
    public void tryVerify(Object param, Class<?> type) {
        if (param == null) {
            throw new VerifyException("Param is null.");
        }
        doVerify(param, type, "");
    }

    @Override
    public <T> void verify(T param, Class<T> type) {
        verify(param, type, "");
    }

    @Override
    public <T> void verify(T param, Class<T> type, String scope) {
        if (param == null) {
            throw new VerifyException("Param is null.");
        }
        doVerify(param, type, scope);
    }

    private void doVerify(Object param, Class<?> type, String scope) {
        if (!param.getClass()
                .isAssignableFrom(type)) {
            throw new VerifyException(
                    String.format("The object need to be verified is not assignable from the given type [%s]. [%s]",
                            type.getName(), param.getClass()
                                    .getName()));
        }

        ReflectionBean ref = new PropertyDescriptorReflectionBean(param);
        for (Field field : ReflectionUtils.getFields(type)) {
            Object value = ref.getFieldValue(field.getName());
            if (field.isAnnotationPresent(Examine.class)) {
                Examine examine = field.getAnnotation(Examine.class);

                if (ignoreNull(examine, value)) {
                    continue;
                }

                doVerify(examine, field, type, value, scope);
            } else if (field.isAnnotationPresent(Examines.class)) {
                Examine[] examines = field.getAnnotation(Examines.class)
                        .value();
                for (Examine examine : Arrays.stream(examines)
                        .filter(r -> !r.withinTheCollection())
                        .collect(Collectors.toList())) {
                    if (ignoreNull(examine, value)) {
                        continue;
                    }
                    doVerify(examine, field, type, value, scope);
                }
                for (Examine examine : Arrays.stream(examines)
                        .filter(r -> r.withinTheCollection())
                        .collect(Collectors.toList())) {
                    if (ignoreNull(examine, value)) {
                        continue;
                    }
                    doVerify(examine, field, type, value, scope);
                }
            }
        }
    }

    private void doVerify(Examine examine, Field field, Class<?> type, Object value, String scope) {
        if (!StringUtils.isEmpty(examine.scope()) && StringUtils.isEmpty(scope)) {
            return;
        }
        if (examine.withinTheCollection()) {
            verifyCollections(examine, field, value, scope);
        } else {
            verifyObject(examine, field, type, value, scope);
        }
    }

    private void verifyCollections(Examine examine, Field field, Object value, String scope) {
        Class<?> fieldType = field.getType();
        if (value == null) {
            return;
        }
        if (!Collection.class.isAssignableFrom(fieldType)) {
            throw new VerifyException("You can not set 'withinTheCollection' [true], via a non 'Collection' field.");
        }
        for (Object item : (Collection<?>) value) {
            verifyObject(examine, field, item.getClass(), item, scope);
        }
    }

    private void verifyObject(Examine examine, Field field, Class<?> type, Object value, String scope) {
        if (!(StringUtils.isEmpty(scope) || examine.scope()
                .equalsIgnoreCase(scope)
                || CollectionUtils.getFirstOptional(Arrays.asList(examine.scopes()), r -> r.equalsIgnoreCase(scope))
                        .isPresent())) {
            return;

        }

        Class<?> fieldType = field.getType();
        switch (examine.value()) {
            case NULL:
                Verify.isNull(value, examine.msg());
                break;
            case NOT_NULL:
                Verify.notNull(value, examine.msg());
                break;
            case EMPTY:
                Verify.empty(value, examine.msg());
                break;
            case NOT_EMPTY:
                Verify.notEmpty(value, examine.msg());
                break;
            case EXAMINED:
                if (fieldType.isPrimitive()) {
                    log.warn("Expectation [EXAMINED] only effect on 'non primitive' type.");
                    break;
                }
                doVerify(value, value.getClass(), scope);
                break;
            case LENGTH_LIMIT:
                if (examine.length() <= 0) {
                    log.warn("Length limit not set. Skip. {}.{} [{}]", type.getName(), field.getName(), value);
                    break;
                }
                Verify.lenthLimit(value, examine.length(), examine.msg());
                break;
            case FIXED_LENGTH:
                if (examine.length() <= 0) {
                    log.warn("Length limit not set. Skip. {}.{} [{}]", type.getName(), field.getName(), value);
                    break;
                }
                Verify.fixedLength(value, examine.length(), examine.msg());
                break;
            case HAS_LENGTH:
                Verify.hasLength(value, examine.msg());
                break;
            case TYPE:
                Verify.typeCheck(value, examine.type(), examine.msg());
                break;
            case IS_INTERFACE:
                Verify.isInterface(fieldType, examine.msg());
                break;
            case MATCH:
                Verify.isMatch(value, examine.regex(), examine.msg());
                break;
            case LARGER_THAN:
                Verify.largerThan(value, examine.standard(), examine.msg());
                break;
            case LESS_THAN:
                Verify.lessThan(value, examine.standard(), examine.msg());
                break;
            case NOT_LESS_THAN:
                Verify.notLessThan(value, examine.standard(), examine.msg());
                break;
            case NOT_LARGER_THAN:
                Verify.notLargerThan(value, examine.standard(), examine.msg());
                break;
            default:
                break;
        }
    }

    private boolean ignoreNull(Examine examine, Object value) {
        return examine != null && examine.ignoreNullValue() && value == null;
    }

}

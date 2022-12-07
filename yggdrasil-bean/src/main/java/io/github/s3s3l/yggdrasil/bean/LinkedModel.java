package io.github.s3s3l.yggdrasil.bean;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.github.s3s3l.yggdrasil.bean.exception.ModelConvertException;

/**
 * <p>
 * </p>
 * ClassName:LinkedModel <br>
 * Date: Sep 1, 2016 10:40:44 AM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public interface LinkedModel<T> {

    public static <T> List<T> toManagementModel(List<? extends LinkedModel<T>> list) {
        return list.stream().map(LinkedModel::convertTo).collect(Collectors.toList());
    }

    public static <U extends LinkedModel<T>, T> List<U> fromManagementModel(List<T> list, Class<U> type) {
        List<U> result = new ArrayList<>();
        for (T v2Model : list) {
            U localModel;
            try {
                localModel = type.getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                throw new ModelConvertException(e);
            }
            localModel.convertFrom(v2Model);
            result.add(localModel);
        }
        return result;
    }

    T convertTo();

    void convertFrom(T model);
}

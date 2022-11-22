package org.s3s3l.yggdrasil.orm.validator;

import java.lang.reflect.InvocationTargetException;

import org.s3s3l.yggdrasil.bean.exception.BeanEstablishException;

/**
 * 
 * <p>
 * </p>
 * ClassName: DefaultValidatorFactory <br>
 * date: Sep 20, 2019 11:35:49 AM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class DefaultValidatorFactory implements ValidatorFactory {

    @Override
    public <T extends Validator> T getValidator(Class<T> validatorType) {
        try {
            return validatorType.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            throw new BeanEstablishException(e);
        }
    }

}

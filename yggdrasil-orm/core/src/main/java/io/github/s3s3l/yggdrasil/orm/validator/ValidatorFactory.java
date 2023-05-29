package io.github.s3s3l.yggdrasil.orm.validator;

/**
 * 
 * <p>
 * </p>
 * ClassName: ValidatorFactory <br>
 * date: Sep 20, 2019 11:36:05 AM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public interface ValidatorFactory {

    <T extends Validator> T getValidator(Class<T> validatorType);
}

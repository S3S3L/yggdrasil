package io.github.s3s3l.yggdrasil.orm.validator;

import io.github.s3s3l.yggdrasil.utils.common.StringUtils;

/**
 * 
 * <p>
 * </p>
 * ClassName: DefaultValidator <br>
 * date: Sep 20, 2019 11:35:42 AM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class DefaultValidator implements Validator {

    @Override
    public boolean isValid(Object param) {
        if (param instanceof String) {
            return !StringUtils.isEmpty(param.toString());
        } else {
            return param != null;
        }
    }

}

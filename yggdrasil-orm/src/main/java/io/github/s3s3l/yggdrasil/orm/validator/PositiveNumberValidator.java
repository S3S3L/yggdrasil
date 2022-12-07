package io.github.s3s3l.yggdrasil.orm.validator;

public class PositiveNumberValidator implements Validator {

    @Override
    public boolean isValid(Object param) {
        if (param instanceof Number) {
            return ((Number) param).doubleValue() > 0;
        } else {
            return false;
        }
    }
    
}

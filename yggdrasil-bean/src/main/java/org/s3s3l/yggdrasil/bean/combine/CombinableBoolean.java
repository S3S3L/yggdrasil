package org.s3s3l.yggdrasil.bean.combine;

/**
 * <p>
 * </p>
 * ClassName:CombinableBoolean <br>
 * Date: Mar 21, 2017 5:41:14 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class CombinableBoolean implements Combinable<Boolean> {
    private Boolean data;
    private BooleanCombineType type;

    public static enum BooleanCombineType {
        AND, OR
    }

    public CombinableBoolean(boolean data, BooleanCombineType type) {
        this.data = data;
        this.type = type;
    }

    @Override
    public Combinable<Boolean> combine(Boolean data) {
        switch (type) {
            case AND:
                this.data &= data;
                break;
            case OR:
                this.data |= data;
                break;
        }
        return this;
    }

    @Override
    public Combinable<Boolean> combine(Combinable<Boolean> combine) {
        combine(combine.get());
        return this;
    }

    @Override
    public Boolean get() {
        return data;
    }

    @Override
    public void set(Boolean data) {
        this.data = data;
    }

}

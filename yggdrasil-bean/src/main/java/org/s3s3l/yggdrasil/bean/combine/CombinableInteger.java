package org.s3s3l.yggdrasil.bean.combine;

/**
 * <p>
 * </p>
 * ClassName:CombinableInteger <br>
 * Date: Mar 21, 2017 5:47:06 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class CombinableInteger implements Combinable<Integer> {
    private Integer data;
    private IntegerCombineType type;

    public static enum IntegerCombineType {
        ADD, SUB
    }

    public CombinableInteger(int data, IntegerCombineType type) {
        this.data = data;
        this.type = type;
    }

    @Override
    public Combinable<Integer> combine(Integer data) {
        switch (type) {
            case ADD:
                this.data += data;
                break;
            case SUB:
                this.data -= data;
                break;
        }
        return this;
    }

    @Override
    public Integer get() {
        return data;
    }

    @Override
    public Combinable<Integer> combine(Combinable<Integer> combine) {
        combine(combine.get());
        return this;
    }

    @Override
    public void set(Integer data) {
        this.data = data;
    }

}

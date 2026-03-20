package io.github.s3s3l.yggdrasil.game.core.ui.binding;

import java.util.function.*;

/**
 * 绑定工具类
 * 提供创建绑定的静态方法
 */
public class Bindings {

    private Bindings() {
        // 防止实例化
    }

    /**
     * 创建字符串绑定
     */
    public static StringBinding createStringBinding(Supplier<String> computer, ObservableValue<?>... dependencies) {
        return new StringBinding(computer, dependencies);
    }

    /**
     * 创建整数绑定
     */
    public static IntegerBinding createIntegerBinding(Supplier<Integer> computer, ObservableValue<?>... dependencies) {
        return new IntegerBinding(computer, dependencies);
    }

    /**
     * 创建布尔绑定
     */
    public static BooleanBinding createBooleanBinding(Supplier<Boolean> computer, ObservableValue<?>... dependencies) {
        return new BooleanBinding(computer, dependencies);
    }

    /**
     * 创建 Double 绑定
     */
    public static DoubleBinding createDoubleBinding(Supplier<Double> computer, ObservableValue<?>... dependencies) {
        return new DoubleBinding(computer, dependencies);
    }

    /**
     * 创建 Float 绑定
     */
    public static FloatBinding createFloatBinding(Supplier<Float> computer, ObservableValue<?>... dependencies) {
        return new FloatBinding(computer, dependencies);
    }

    /**
     * 创建对象绑定
     */
    public static <T> ObjectBinding<T> createObjectBinding(Supplier<T> computer, ObservableValue<?>... dependencies) {
        return new ObjectBinding<T>(dependencies) {
            @Override
            protected T computeValue() {
                return computer.get();
            }
        };
    }

    /**
     * 绑定字符串格式化
     */
    public static StringBinding format(String format, ObservableValue<?>... args) {
        return createStringBinding(() -> String.format(format, extractValues(args)), args);
    }

    private static Object[] extractValues(ObservableValue<?>... args) {
        Object[] values = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            values[i] = args[i] != null ? args[i].getValue() : null;
        }
        return values;
    }

    /**
     * 连接多个字符串
     */
    public static StringBinding concatenate(ObservableValue<String>... strings) {
        return createStringBinding(() -> {
            StringBuilder sb = new StringBuilder();
            for (ObservableValue<String> s : strings) {
                if (s != null && s.getValue() != null) {
                    sb.append(s.getValue());
                }
            }
            return sb.toString();
        }, strings);
    }

    /**
     * 字符串相等比较
     */
    public static BooleanBinding equal(ObservableValue<String> a, ObservableValue<String> b) {
        return createBooleanBinding(() -> {
            if (a == null && b == null) return true;
            if (a == null || b == null) return false;
            String va = a.getValue();
            String vb = b.getValue();
            return va == null ? vb == null : va.equals(vb);
        }, a, b);
    }

    /**
     * 整数相等比较
     */
    public static BooleanBinding equalIntegers(ObservableValue<Integer> a, ObservableValue<Integer> b) {
        return createBooleanBinding(() -> {
            if (a == null && b == null) return true;
            if (a == null || b == null) return false;
            Integer va = a.getValue();
            Integer vb = b.getValue();
            return va == null ? vb == null : va.equals(vb);
        }, a, b);
    }

    /**
     * 整数比较 - 大于
     */
    public static BooleanBinding greaterThanIntegers(ObservableValue<Integer> a, ObservableValue<Integer> b) {
        return createBooleanBinding(() -> {
            if (a == null || b == null) return false;
            Integer va = a.getValue();
            Integer vb = b.getValue();
            return va != null && vb != null && va > vb;
        }, a, b);
    }

    /**
     * 整数比较 - 小于
     */
    public static BooleanBinding lessThanIntegers(ObservableValue<Integer> a, ObservableValue<Integer> b) {
        return createBooleanBinding(() -> {
            if (a == null || b == null) return false;
            Integer va = a.getValue();
            Integer vb = b.getValue();
            return va != null && vb != null && va < vb;
        }, a, b);
    }

    /**
     * 整数比较 - 大于等于
     */
    public static BooleanBinding greaterThanOrEqualIntegers(ObservableValue<Integer> a, ObservableValue<Integer> b) {
        return createBooleanBinding(() -> {
            if (a == null || b == null) return false;
            Integer va = a.getValue();
            Integer vb = b.getValue();
            return va != null && vb != null && va >= vb;
        }, a, b);
    }

    /**
     * 整数比较 - 小于等于
     */
    public static BooleanBinding lessThanOrEqualIntegers(ObservableValue<Integer> a, ObservableValue<Integer> b) {
        return createBooleanBinding(() -> {
            if (a == null || b == null) return false;
            Integer va = a.getValue();
            Integer vb = b.getValue();
            return va != null && vb != null && va <= vb;
        }, a, b);
    }

    /**
     * Double 比较 - 大于
     */
    public static BooleanBinding greaterThanDoubles(ObservableValue<Double> a, ObservableValue<Double> b) {
        return createBooleanBinding(() -> {
            if (a == null || b == null) return false;
            Double va = a.getValue();
            Double vb = b.getValue();
            return va != null && vb != null && va > vb;
        }, a, b);
    }

    /**
     * Double 比较 - 小于
     */
    public static BooleanBinding lessThanDoubles(ObservableValue<Double> a, ObservableValue<Double> b) {
        return createBooleanBinding(() -> {
            if (a == null || b == null) return false;
            Double va = a.getValue();
            Double vb = b.getValue();
            return va != null && vb != null && va < vb;
        }, a, b);
    }

    /**
     * 布尔与
     */
    public static BooleanBinding and(ObservableValue<Boolean> a, ObservableValue<Boolean> b) {
        return createBooleanBinding(() -> {
            Boolean va = a != null ? a.getValue() : false;
            Boolean vb = b != null ? b.getValue() : false;
            return (va != null && va) && (vb != null && vb);
        }, a, b);
    }

    /**
     * 布尔或
     */
    public static BooleanBinding or(ObservableValue<Boolean> a, ObservableValue<Boolean> b) {
        return createBooleanBinding(() -> {
            Boolean va = a != null ? a.getValue() : false;
            Boolean vb = b != null ? b.getValue() : false;
            return (va != null && va) || (vb != null && vb);
        }, a, b);
    }

    /**
     * 布尔非
     */
    public static BooleanBinding not(ObservableValue<Boolean> a) {
        return createBooleanBinding(() -> {
            Boolean va = a != null ? a.getValue() : false;
            return !(va != null && va);
        }, a);
    }

    /**
     * 整数加法
     */
    public static IntegerBinding addIntegers(ObservableValue<Integer> a, ObservableValue<Integer> b) {
        return createIntegerBinding(() -> {
            Integer va = a != null ? a.getValue() : 0;
            Integer vb = b != null ? b.getValue() : 0;
            return (va != null ? va : 0) + (vb != null ? vb : 0);
        }, a, b);
    }

    /**
     * 整数减法
     */
    public static IntegerBinding subtractIntegers(ObservableValue<Integer> a, ObservableValue<Integer> b) {
        return createIntegerBinding(() -> {
            Integer va = a != null ? a.getValue() : 0;
            Integer vb = b != null ? b.getValue() : 0;
            return (va != null ? va : 0) - (vb != null ? vb : 0);
        }, a, b);
    }

    /**
     * 整数乘法
     */
    public static IntegerBinding multiplyIntegers(ObservableValue<Integer> a, ObservableValue<Integer> b) {
        return createIntegerBinding(() -> {
            Integer va = a != null ? a.getValue() : 1;
            Integer vb = b != null ? b.getValue() : 1;
            return (va != null ? va : 1) * (vb != null ? vb : 1);
        }, a, b);
    }

    /**
     * 整数除法
     */
    public static IntegerBinding divideIntegers(ObservableValue<Integer> a, ObservableValue<Integer> b) {
        return createIntegerBinding(() -> {
            Integer va = a != null ? a.getValue() : 0;
            Integer vb = b != null ? b.getValue() : 1;
            if (vb == null || vb == 0) return 0;
            return (va != null ? va : 0) / vb;
        }, a, b);
    }

    /**
     * Double 加法
     */
    public static DoubleBinding addDoubles(ObservableValue<Double> a, ObservableValue<Double> b) {
        return createDoubleBinding(() -> {
            Double va = a != null ? a.getValue() : 0.0;
            Double vb = b != null ? b.getValue() : 0.0;
            return (va != null ? va : 0.0) + (vb != null ? vb : 0.0);
        }, a, b);
    }

    /**
     * Double 减法
     */
    public static DoubleBinding subtractDoubles(ObservableValue<Double> a, ObservableValue<Double> b) {
        return createDoubleBinding(() -> {
            Double va = a != null ? a.getValue() : 0.0;
            Double vb = b != null ? b.getValue() : 0.0;
            return (va != null ? va : 0.0) - (vb != null ? vb : 0.0);
        }, a, b);
    }

    /**
     * Double 乘法
     */
    public static DoubleBinding multiplyDoubles(ObservableValue<Double> a, ObservableValue<Double> b) {
        return createDoubleBinding(() -> {
            Double va = a != null ? a.getValue() : 1.0;
            Double vb = b != null ? b.getValue() : 1.0;
            return (va != null ? va : 1.0) * (vb != null ? vb : 1.0);
        }, a, b);
    }

    /**
     * Double 除法
     */
    public static DoubleBinding divideDoubles(ObservableValue<Double> a, ObservableValue<Double> b) {
        return createDoubleBinding(() -> {
            Double va = a != null ? a.getValue() : 0.0;
            Double vb = b != null ? b.getValue() : 1.0;
            if (vb == null || vb == 0.0) return 0.0;
            return (va != null ? va : 0.0) / vb;
        }, a, b);
    }

    /**
     * 当值为 null 时提供默认值
     */
    public static <T> ObjectBinding<T> whenNull(ObservableValue<T> source, T defaultValue) {
        return createObjectBinding(() -> {
            T val = source != null ? source.getValue() : null;
            return val != null ? val : defaultValue;
        }, source);
    }

    /**
     * 条件选择
     */
    public static <T> ObjectBinding<T> selectWhen(ObservableValue<Boolean> condition,
                                                   ObservableValue<T> whenTrue,
                                                   ObservableValue<T> whenFalse) {
        return createObjectBinding(() -> {
            Boolean cond = condition != null ? condition.getValue() : false;
            if (cond != null && cond) {
                return whenTrue != null ? whenTrue.getValue() : null;
            } else {
                return whenFalse != null ? whenFalse.getValue() : null;
            }
        }, condition, whenTrue, whenFalse);
    }
}

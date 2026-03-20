package io.github.s3s3l.yggdrasil.game.core.ui.binding;

/**
 * 值转换器接口
 *
 * @param <S> 源类型
 * @param <T> 目标类型
 */
public interface Converter<S, T> {

    /**
     * 转换源值到目标类型
     */
    T convert(S source);

    /**
     * 反向转换（用于双向绑定）
     */
    default S convertBack(T target) {
        throw new UnsupportedOperationException("Reverse conversion not supported");
    }

    /**
     * 空转换器实现（用于注解默认值）
     */
    class None implements Converter<Object, Object> {
        @Override
        public Object convert(Object source) {
            return source;
        }
    }
}

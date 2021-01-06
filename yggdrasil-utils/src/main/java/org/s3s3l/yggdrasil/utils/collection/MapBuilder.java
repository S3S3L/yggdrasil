package org.s3s3l.yggdrasil.utils.collection;

import java.util.Map;

/**
 * <p>
 * </p>
 * ClassName:MapBuilder <br>
 * Date: Oct 30, 2017 3:04:17 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class MapBuilder<T, U> {
    private Map<T, U> map;

    public MapBuilder(Map<T, U> map) {
        this.map = map;
    }

    public MapBuilder<T, U> put(T key, U value) {
        this.map.put(key, value);
        return this;
    }

    public Map<T, U> build() {
        return this.map;
    }
}

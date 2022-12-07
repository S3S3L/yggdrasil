package io.github.s3s3l.yggdrasil.http;

import io.github.s3s3l.yggdrasil.utils.common.StringUtils;

/**
 * <p>
 * </p>
 * ClassName:QueryStringKey <br>
 * Date: Nov 21, 2016 6:54:47 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class QueryStringKey {
    private String key;
    private int currentIndex = 0;

    public QueryStringKey(String key) {
        this.key = key;
    }

    public boolean hasNext() {
        String remainKey = getRemainKey();
        if (StringUtils.isEmpty(remainKey)) {
            return false;
        }
        remainKey = remainKey.replaceAll("\\[", "");
        remainKey = remainKey.replaceAll("\\]", "");
        return remainKey.length() > 0;
    }

    public boolean isArray() {
        return key.endsWith("[]");
    }

    public String nextkey() {
        if (StringUtils.isEmpty(key)) {
            return StringUtils.EMPTY_STRING;
        }

        String remainKey = getRemainKey();

        int endIndex = remainKey.indexOf('[');
        if (endIndex < 0) {
            endIndex = remainKey.length();
        }

        String nextKey = remainKey.substring(0, endIndex);
        currentIndex += endIndex;

        return nextKey.endsWith("]") ? nextKey.substring(0, nextKey.length() - 1) : nextKey;
    }

    public void reset() {
        this.currentIndex = 0;
    }

    private String getRemainKey() {

        String remainKey = key.substring(currentIndex);
        if (remainKey.startsWith("[")) {
            remainKey = remainKey.substring(1);
            currentIndex++;
        }

        return remainKey;
    }
}

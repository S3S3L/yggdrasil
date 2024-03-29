package io.github.s3s3l.yggdrasil.utils.stuctural.jackson;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.NamingBase;

/**
 * <p>
 * </p>
 * ClassName:HyphenToCamelNamingStrategy <br>
 * Date: Jan 18, 2019 8:44:18 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class HyphenToCamelNamingStrategy extends NamingBase {
    /**
     *
     */
    private static final long serialVersionUID = -4816517548506971756L;
    private static final char HYPHEN = '-';

    @Override
    public String translate(String propertyName) {
        if (StringUtils.isEmpty(propertyName)) {
            return propertyName;
        }
        StringBuilder sb = new StringBuilder();
        boolean findHyphen = false;
        char[] chs = propertyName.toCharArray();
        for (int i = 0; i < chs.length; i++) {
            char c = chs[i];
            if (c == HYPHEN) {
                findHyphen = true;
                continue;
            }
            if (findHyphen) {
                c = Character.toUpperCase(c);
                findHyphen = false;
            }
            sb.append(c);
        }
        return sb.toString();
    }
}

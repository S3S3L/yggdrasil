package io.github.s3s3l.yggdrasil.configuration.text;

import java.util.Map;
import java.util.Optional;

import io.github.s3s3l.yggdrasil.bean.exception.ConfigurationNotInitializedException;
import io.github.s3s3l.yggdrasil.bean.exception.TextNotFoundException;

import lombok.Data;

/**
 * <p>
 * </p> 
 * ClassName:TextConfiguration <br> 
 * Date:     Apr 25, 2019 9:18:42 PM <br>
 *  
 * @author   kehw_zwei 
 * @version  1.0.0
 * @since    JDK 1.8
 */
@Data
public class TextConfiguration {

    private Map<String, Map<String, String>> text;

    public String get(Enum<?> enumeration) {
        if (text == null) {
            throw new ConfigurationNotInitializedException("Please initialize configuration first.");
        }

        return Optional.ofNullable(Optional.ofNullable(text.get(enumeration.getClass()
                .getSimpleName()))
                .orElseThrow(() -> new TextNotFoundException(String.format("Text for %s not found.",
                        enumeration.getClass()
                                .getSimpleName())))
                .get(enumeration.name()))
                .orElseThrow(() -> new TextNotFoundException(
                        String.format("Text for %s.%s not found.", enumeration.getClass()
                                .getSimpleName(), enumeration.name())));
    }

}
  
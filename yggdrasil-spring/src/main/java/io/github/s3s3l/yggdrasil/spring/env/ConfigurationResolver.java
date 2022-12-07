
package io.github.s3s3l.yggdrasil.spring.env;

import org.springframework.core.env.Environment;

/**
 * <p>
 * </p>
 * ClassName:ConfigurationResolver <br>
 * Date: Jun 3, 2019 2:51:45 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public interface ConfigurationResolver<T> {

    T resolve(Environment environment);

    T resolve(String configuration);
}

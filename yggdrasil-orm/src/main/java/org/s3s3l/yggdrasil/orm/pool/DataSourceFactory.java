package org.s3s3l.yggdrasil.orm.pool;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

/**
 * 
 * <p>
 * </p>
 * ClassName: DataSourceFactory <br>
 * date: Sep 20, 2019 11:35:25 AM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public interface DataSourceFactory {

    DataSource getDataSource(Properties config);

    DataSource getDataSource(Map<String, Object> config);

    DataSource getDataSource(String profileKey, String profile, String... configFiles)
            throws IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException;
}

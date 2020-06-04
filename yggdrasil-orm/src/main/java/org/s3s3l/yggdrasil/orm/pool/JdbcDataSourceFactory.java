package org.s3s3l.yggdrasil.orm.pool;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.cfg.ConfigFeature;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.s3s3l.yggdrasil.orm.exception.DataSourceInitalizingException;
import org.s3s3l.yggdrasil.resource.prop.PropertyResourceLoader;
import org.s3s3l.yggdrasil.resource.yaml.YAMLResourceLoader;
import org.s3s3l.yggdrasil.utils.common.StringUtils;
import org.s3s3l.yggdrasil.utils.file.FileUtils;
import org.s3s3l.yggdrasil.utils.reflect.PropertyDescriptorReflectionBean;
import org.s3s3l.yggdrasil.utils.reflect.ReflectionBean;
import org.s3s3l.yggdrasil.utils.verify.Verify;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * <p>
 * </p>
 * ClassName: JdbcDataSourceFactory <br>
 * date: Sep 20, 2019 11:35:33 AM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class JdbcDataSourceFactory implements DataSourceFactory {
    private static final Logger logger = LoggerFactory.getLogger(JdbcDataSourceFactory.class);

    @Override
    public javax.sql.DataSource getDataSource(Properties config) {

        PoolProperties poolProp = new PoolProperties();
        ReflectionBean poolPropRef = new PropertyDescriptorReflectionBean(poolProp);
        DataSource datasource = new DataSource();
        try {
            poolPropRef.fill(config);
            datasource.setPoolProperties(poolProp);
        } catch (Exception e) {
            throw new DataSourceInitalizingException(e);
        }
        return datasource;
    }

    @Override
    public javax.sql.DataSource getDataSource(Map<String, Object> config) {

        PoolProperties poolProp = new PoolProperties();
        ReflectionBean poolPropRef = new PropertyDescriptorReflectionBean(poolProp);
        DataSource datasource = new DataSource();
        try {
            poolPropRef.fill(config);
            datasource.setPoolProperties(poolProp);
        } catch (Exception e) {
            throw new DataSourceInitalizingException(e);
        }
        return datasource;
    }

    @Override
    public javax.sql.DataSource getDataSource(String profileKey, String profile, String... configFiles) {
        Verify.notEmpty(configFiles);
        File configFile = FileUtils.getFirstExistFile(configFiles);

        if (null == configFile) {
            logger.error("No config file. '{}'", (Object) configFiles);
            throw new DataSourceInitalizingException("No config file.");
        }

        logger.error("Load datasource configuration {}", configFile.getAbsolutePath());

        String fileExtendsion = FileUtils.getExtension(configFile);
        if (YAMLResourceLoader.YAML_FILE_EXTENSIONS.contains(fileExtendsion)) {
            Map<ConfigFeature, Boolean> config = new HashMap<>();
            config.put(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            YAMLResourceLoader yaml;
            if (StringUtils.isEmpty(profileKey)) {
                yaml = YAMLResourceLoader.create(config);
            } else {
                yaml = YAMLResourceLoader.create(profileKey, config);
            }

            try {
                PoolProperties poolProp = yaml.loadConfiguration(PoolProperties.class, profile, configFile);
                DataSource datasource = new DataSource();
                datasource.setPoolProperties(poolProp);

                return datasource;
            } catch (InstantiationException | IllegalAccessException | IOException e) {
                throw new DataSourceInitalizingException(e);
            }

        } else if (PropertyResourceLoader.PROP_FILE_EXTENSIONS.contains(fileExtendsion)) {
            try {
                return getDataSource(
                        PropertyResourceLoader.getConfig(profileKey, profile, configFile.getAbsolutePath()));
            } catch (IOException e) {
                throw new DataSourceInitalizingException(e);
            }
        }

        throw new DataSourceInitalizingException("Config file type not support.");
    }

}

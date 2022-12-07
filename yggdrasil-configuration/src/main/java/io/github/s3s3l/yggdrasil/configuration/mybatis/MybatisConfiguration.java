package io.github.s3s3l.yggdrasil.configuration.mybatis;

import java.io.IOException;
import java.util.Map;

import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * <p>
 * </p>
 * ClassName:SqlSessionConfiguration <br>
 * Date: Aug 31, 2016 5:52:06 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
@ConditionalOnClass(SqlSessionFactoryBean.class)
@Configuration
@ConfigurationProperties(prefix = "mybatis")
public class MybatisConfiguration {
    private String config;
    private String mapperLocations;
    private String typeAliasesPackage;
    private boolean checkConfigLocation;
    private Map<String, String> mapperPackages;

    public SqlSessionFactoryBean getSqlSessionFacotyBean(DataSource datasource) throws IOException {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        PathMatchingResourcePatternResolver pathResolver = new PathMatchingResourcePatternResolver();
        sessionFactoryBean.setDataSource(datasource);
        sessionFactoryBean.setTypeAliasesPackage(this.getTypeAliasesPackage());
        sessionFactoryBean.setConfigLocation(pathResolver.getResource(this.getConfig()));
        sessionFactoryBean.setMapperLocations(pathResolver.getResources(this.getMapperLocations()));

        return sessionFactoryBean;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getMapperLocations() {
        return mapperLocations;
    }

    public void setMapperLocations(String mapperLocations) {
        this.mapperLocations = mapperLocations;
    }

    public String getTypeAliasesPackage() {
        return typeAliasesPackage;
    }

    public void setTypeAliasesPackage(String typeAliasesPackage) {
        this.typeAliasesPackage = typeAliasesPackage;
    }

    public boolean isCheckConfigLocation() {
        return checkConfigLocation;
    }

    public void setCheckConfigLocation(boolean checkConfigLocation) {
        this.checkConfigLocation = checkConfigLocation;
    }

    public Map<String, String> getMapperPackages() {
        return mapperPackages;
    }

    public void setMapperPackages(Map<String, String> mapperPackages) {
        this.mapperPackages = mapperPackages;
    }

}

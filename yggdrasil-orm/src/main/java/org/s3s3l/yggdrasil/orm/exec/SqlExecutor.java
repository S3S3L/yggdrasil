package org.s3s3l.yggdrasil.orm.exec;

import java.util.List;

import javax.sql.DataSource;

/**
 * 
 * <p>
 * </p>
 * ClassName: SqlExecutor <br>
 * date: Sep 20, 2019 11:31:44 AM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public interface SqlExecutor {

    void setDataSource(DataSource datasource);

    <T> int insert(List<T> model, Class<T> modelClass);

    <T> int delete(T model, Class<T> modelClass);

    <T> int update(T model, Class<T> modelClass);

    <T> List<T> select(T model, Class<T> modelClass);
}

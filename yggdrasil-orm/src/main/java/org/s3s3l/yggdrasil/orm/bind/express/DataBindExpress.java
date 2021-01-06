package org.s3s3l.yggdrasil.orm.bind.express;

import java.util.List;

import org.s3s3l.yggdrasil.orm.bind.sql.SqlStruct;

/**
 * 
 * <p>
 * </p>
 * ClassName: DataBindExpress <br>
 * date: Sep 20, 2019 11:27:35 AM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public interface DataBindExpress {
    DataBindExpress express(Class<?> modelType);

    String getAlias(String name);

    SqlStruct getInsert(List<?> model);

    SqlStruct getDelete(Object model);

    SqlStruct getUpdate(Object model);

    SqlStruct getSelect(Object model);
}

package io.github.s3s3l.yggdrasil.orm.bind.express;

import java.util.List;

import io.github.s3s3l.yggdrasil.orm.bind.sql.SqlStruct;

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

    SqlStruct getInsert(List<?> model);

    SqlStruct getDelete(Object condition);

    SqlStruct getUpdate(Object source, Object condition);

    SqlStruct getSelect(Object condition);

    SqlStruct getSelectCount(Object condition);

    SqlStruct getCreate(Class<?> tableType, boolean force);

    SqlStruct getDrop(Class<?> tableType);
}

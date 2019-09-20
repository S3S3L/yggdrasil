package org.s3s3l.yggdrasil.orm.bind;

import org.s3s3l.yggdrasil.utils.reflect.ReflectionBean;

/**
 * 
 * <p>
 * </p>
 * ClassName: DataBindNode <br>
 * date: Sep 20, 2019 11:29:10 AM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public interface DataBindNode {

    SqlStruct toSqlStruct(ReflectionBean bean);
}

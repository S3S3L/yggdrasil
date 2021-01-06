package org.s3s3l.yggdrasil.orm.bind.express;

import org.s3s3l.yggdrasil.orm.meta.MetaManager;

/**
 * 
 * <p>
 * </p>
 * ClassName: ExpressFactory <br>
 * date: Sep 20, 2019 11:28:09 AM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public interface ExpressFactory {

    <T> DataBindExpress getDataBindExpress(Class<T> modelType, MetaManager metaManager);
}

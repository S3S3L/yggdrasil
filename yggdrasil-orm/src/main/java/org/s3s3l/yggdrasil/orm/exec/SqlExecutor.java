package org.s3s3l.yggdrasil.orm.exec;

import java.util.List;

import org.s3s3l.yggdrasil.orm.ds.Transactable;
import org.s3s3l.yggdrasil.orm.pagin.ConditionForPagination;
import org.s3s3l.yggdrasil.orm.pagin.PaginResult;

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
public interface SqlExecutor extends Transactable {

    <S> int insert(List<S> sources);

    <C> int delete(C condition);

    <S, C> int update(S source, C condition);

    <C, R> List<R> select(C condition, Class<R> resultType);

    <C extends ConditionForPagination, R> PaginResult<List<R>> selectByPagin(C condition, Class<R> resultType);

    <C, R> R selectOne(C condition, Class<R> resultType);

    boolean create(Class<?> tableType, CreateConfig createConfig);

    boolean drop(Class<?> tableType);

    boolean execute(String sql);

    <P> P getProxy(Class<P> proxyInterfaceType);
}

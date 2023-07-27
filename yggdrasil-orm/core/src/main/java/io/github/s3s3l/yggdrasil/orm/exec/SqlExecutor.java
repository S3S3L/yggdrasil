package io.github.s3s3l.yggdrasil.orm.exec;

import java.util.List;

import io.github.s3s3l.yggdrasil.orm.ds.Transactable;
import io.github.s3s3l.yggdrasil.orm.pagin.ConditionForPagination;
import io.github.s3s3l.yggdrasil.orm.pagin.PaginResult;

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

    /**
     * 插入一条数据
     * 
     * @param <S>
     *            数据类型
     * @param source
     *            数据对象
     * @return 成功插入的数据条数
     */
    <S> int insert(S source);

    /**
     * 插入一组数据
     * 
     * @param <S>
     *            数据类型
     * @param sources
     *            数据对象
     * @return 成功插入的数据条数
     */
    <S> int insert(List<S> sources);

    /**
     * 删除数据
     * 
     * @param <C>
     *            条件类型
     * @param condition
     *            条件对象
     * @return 成功删除的数据条数
     */
    <C> int delete(C condition);

    /**
     * 更新数据
     * 
     * @param <S>
     *            数据类型
     * @param <C>
     *            条件类型
     * @param source
     *            新数据对象
     * @param condition
     *            条件对象
     * @return 成功更新的数据条数
     */
    <S, C> int update(S source, C condition);

    /**
     * 查询数据
     * 
     * @param <C>
     *            条件类型
     * @param <R>
     *            返回值类型
     * @param condition
     *            条件对象
     * @param resultType
     *            返回值类型的class对象
     * @return 一组返回值对象
     */
    <C, R> List<R> select(C condition, Class<R> resultType);

    /**
     * 查询数据（分页）
     * 
     * @param <C>
     *            条件类型
     * @param <R>
     *            返回值类型
     * @param condition
     *            条件对象
     * @param resultType
     *            返回值类型的class对象
     * @return 分页结果。data中包含当前页的一组返回值对象
     */
    <C extends ConditionForPagination, R> PaginResult<List<R>> selectByPagin(C condition, Class<R> resultType);

    /**
     * 查询一条记录
     * 
     * @param <C>
     *            条件类型
     * @param <R>
     *            返回值类型
     * @param condition
     *            条件对象
     * @param resultType
     *            返回值类型的class对象
     * @return 返回唯一的一条查询数据，如果查询条件命中多条，则会返回第一条数据
     */
    <C, R> R selectOne(C condition, Class<R> resultType);

    /**
     * 创建表
     * 
     * @param tableType
     *            表定义类型
     * @param createConfig
     *            建表配置
     * @return 是否创建成功
     */
    boolean create(Class<?> tableType, CreateConfig createConfig);

    /**
     * 删除表
     * 
     * @param tableType
     *            标定义类型
     * @return 是否删除成功
     */
    boolean drop(Class<?> tableType);

    /**
     * 执行sql
     * 
     * @param sql
     *            待执行的sql
     * @return 是否执行成功
     */
    boolean execute(String sql);

    /**
     * 获取代理对象
     * 
     * @param <P>
     *            需要被代理的接口类型
     * @param proxyInterfaceType
     *            需要被代理的接口类型的class对象
     * @return 对应接口类型的代理对象
     */
    <P> P getProxy(Class<P> proxyInterfaceType);
}

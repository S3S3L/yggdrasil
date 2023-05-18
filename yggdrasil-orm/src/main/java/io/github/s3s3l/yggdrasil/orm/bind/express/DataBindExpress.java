package io.github.s3s3l.yggdrasil.orm.bind.express;

import java.util.List;

import io.github.s3s3l.yggdrasil.orm.bind.sql.SqlStruct;
import io.github.s3s3l.yggdrasil.orm.enumerations.DatabaseType;
import io.github.s3s3l.yggdrasil.orm.meta.ColumnMeta;

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

    /**
     * @return 对应的SQL表达式构造器类型
     */
    ExpressBuilderType expressBuilderType();

    /**
     * @return 对应的数据库类型
     */
    DatabaseType databaseType();

    /**
     * 插入语句
     * 
     * @param model
     *            数据对象列表
     * @return Sql结构体
     */
    SqlStruct getInsert(List<?> model);

    /**
     * 删除语句
     * 
     * @param condition
     *            条件对象
     * @return Sql结构体
     */
    SqlStruct getDelete(Object condition);

    /**
     * 更新语句
     * 
     * @param source
     *            数据来源对象
     * @param condition
     *            条件对象
     * @return Sql结构体
     */
    SqlStruct getUpdate(Object source, Object condition);

    /**
     * 查询语句
     * 
     * @param condition
     *            查询条件
     * @return Sql结构体
     */
    SqlStruct getSelect(Object condition);

    /**
     * 查询数量
     * 
     * @param condition
     *            查询条件
     * @return Sql结构体
     */
    SqlStruct getSelectCount(Object condition);

    /**
     * 建表语句
     * 
     * @param tableType
     *            表定义对象类型
     * @param force
     *            是否直接创建。为true时，如果表已存在会抛出异常
     * @return Sql结构体
     */
    SqlStruct getCreate(Class<?> tableType, boolean force);

    /**
     * 删表语句
     * 
     * @param tableType
     *            表定义对象类型
     * @return Sql结构体
     */
    SqlStruct getDrop(Class<?> tableType);

    /**
     * 
     * 新增字段
     * 
     * @param columnMeta
     *            字段元数据
     * @return Sql结构体
     */
    SqlStruct getColumnAdd(ColumnMeta columnMeta);

    /**
     * 删除字段
     * 
     * @param columnMeta
     *            字段元数据
     * @return Sql结构体
     */
    SqlStruct getColumnDrop(ColumnMeta columnMeta);

    /**
     * 修改字段定义，不支持修改主键、唯一键约束、自增等
     * 
     * @param columnMeta
     *            字段元数据
     * @return Sql结构体
     */
    SqlStruct getColumnAlter(ColumnMeta columnMeta);
}

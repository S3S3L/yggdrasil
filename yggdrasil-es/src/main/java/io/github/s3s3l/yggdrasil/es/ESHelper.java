package io.github.s3s3l.yggdrasil.es;

import io.github.s3s3l.yggdrasil.es.query.Esdsl;
import io.github.s3s3l.yggdrasil.es.response.Hit;
import io.github.s3s3l.yggdrasil.es.response.Response;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * <p>
 * 通过Http调用DSL API访问ES
 * </p>
 * ClassName:ESHelper <br>
 * Date: Dec 29, 2018 7:53:35 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public interface ESHelper {

    /**
     * 
     * 查询ES
     * 
     * @param index
     *            目录
     * @param dsl
     *            DSL
     * @return 查询结果
     * @since JDK 1.8
     */
    JsonNode search(String index, Esdsl dsl);

    /**
     * 
     * 查询ES
     * 
     * @param index
     *            目录
     * @param dsl
     *            DSL
     * @param responseType
     *            返回类型（hits._source的数据结构）
     * @return 查询结果
     * @since JDK 1.8
     */
    <T> Response<T> search(String index, Esdsl dsl, TypeReference<Response<T>> responseType);

    /**
     * 
     * 查询ES
     * 
     * @param index
     *            目录
     * @param dsl
     *            DSL
     * @param paginCondition
     *            分页参数
     * @return 查询结果
     * @since JDK 1.8
     */
    // TODO
//    JsonNode search(String index, Esdsl dsl, ConditionForPagination paginCondition);

    /**
     * 
     * 查询ES
     * 
     * @param index
     *            目录
     * @param dsl
     *            DSL
     * @param responseType
     *            返回类型（hits._source的数据结构）
     * @param paginCondition
     *            分页参数
     * @return 查询结果
     * @since JDK 1.8
     */
    // TODO
//    <T> Response<T> search(String index,
//            Esdsl dsl,
//            TypeReference<Response<T>> responseType,
//            ConditionForPagination paginCondition);

    /**
     * 
     * 往指定路径中插入一个文档
     * 
     * @param index
     *            目录
     * @param type
     *            类型
     * @param id
     *            ID
     * @param doc
     *            文档对象
     * @return 是否成功
     * @since JDK 1.8
     */
    boolean put(String index, String type, String id, Object doc);

    /**
     * 
     * 更新指定文档
     * 
     * @param index
     *            目录
     * @param type
     *            类型
     * @param id
     *            ID
     * @param dsl
     *            DSL
     * @return 是否成功
     * @since JDK 1.8
     */
    boolean update(String index, String type, String id, Esdsl dsl);

    /**
     * 
     * 获取指定文档内容
     * 
     * @param index
     *            目录
     * @param type
     *            类型
     * @param id
     *            ID
     * @param responseType
     *            文档数据结构类型
     * @return 文档内容（hit._source）
     * @since JDK 1.8
     */
    <T> T get(String index, String type, String id, TypeReference<Hit<T>> responseType);

    /**
     * 
     * 校验某个文档是否存在
     * 
     * @param index
     *            目录
     * @param type
     *            类型
     * @param id
     *            ID
     * @return 是否存在
     * @since JDK 1.8
     */
    boolean exists(String index, String type, String id);
}

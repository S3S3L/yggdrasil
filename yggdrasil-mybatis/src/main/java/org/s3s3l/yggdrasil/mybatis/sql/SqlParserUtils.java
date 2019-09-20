package org.s3s3l.yggdrasil.mybatis.sql;

import java.io.StringReader;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import org.s3s3l.yggdrasil.mybatis.sql.enumerations.DatabaseType;
import org.s3s3l.yggdrasil.utils.collection.CollectionUtils;

import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.parser.ParseException;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;

/**
 * ClassName:SqlParserUtils <br>
 * Date: 2016年2月29日 下午1:37:29 <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class SqlParserUtils {

    public static String getCountingSqlBySqlParser(String baseSql) throws ParseException {
        CCJSqlParser parser = new CCJSqlParser(new StringReader(baseSql));
        PlainSelect select = parser.PlainSelect();
        select.setSelectItems(Arrays.asList(new SelectExpressionItem(new Column("COUNT(*)"))));
        select.setLimit(null);
        select.setOffset(null);
        select.setOrderByElements(null);
        return select.toString();
    }

    public static String getCountingSql(String baseSql) {
        int selectIndex = Math.max(baseSql.indexOf("SELECT"), baseSql.indexOf("select"));
        int fromIndex = Math.max(baseSql.indexOf("FROM"), baseSql.indexOf("from"));
        String countSql = baseSql.replace(baseSql.substring(selectIndex + 6, fromIndex), " COUNT(*) ");
        int offsetIndex = Math.max(countSql.indexOf("OFFSET"), countSql.indexOf("offset"));
        int limitIndex = Math.max(countSql.indexOf("LIMIT"), countSql.indexOf("limit"));
        int orderByIndex = Math.max(countSql.indexOf("ORDER BY"), countSql.indexOf("order by"));
        Set<Integer> sortedSet = new TreeSet<>();
        if (offsetIndex > 0) {
            sortedSet.add(offsetIndex);
        }
        if (limitIndex > 0) {
            sortedSet.add(limitIndex);
        }
        if (orderByIndex > 0) {
            sortedSet.add(orderByIndex);
        }

        if (!sortedSet.isEmpty()) {
            countSql = countSql.substring(0, CollectionUtils.getFirst(sortedSet));
        }

        return countSql;
    }

    public static String getPaginationSql(String baseSql, long offset, long rowCount, DatabaseType database) {

        return String.format("%s OFFSET %d LIMIT %d", baseSql, offset, rowCount);
    }
}

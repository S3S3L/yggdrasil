package org.s3s3l.yggdrasil.mybatis.sql.statement.select;

import org.s3s3l.yggdrasil.mybatis.sql.enumerations.DatabaseType;
import org.s3s3l.yggdrasil.utils.common.StringUtils;

import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.statement.select.Limit;

/**
 * ClassName:ExtLimit <br>
 * Date: 2016年2月29日 下午3:25:56 <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class ExtLimit extends Limit {

    private DatabaseType databaseType;

    public DatabaseType getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(DatabaseType databaseType) {
        this.databaseType = databaseType;
    }

    public ExtLimit() {

    }

    public ExtLimit(Limit limit, DatabaseType databaseType) {
        setRowCount(limit.getRowCount());
        setOffset(limit.getOffset());
        this.databaseType = databaseType;
    }

    @Override
    public String toString() {

        long rowCount = ((LongValue) getRowCount()).getValue();
        long offset = ((LongValue) getOffset()).getValue();

        StringBuilder retVal = new StringBuilder(StringUtils.EMPTY_STRING);
        switch (databaseType) {
            case MYSQL:
                retVal.append(" LIMIT ")
                        .append(offset)
                        .append(",")
                        .append(rowCount);
                break;
            case POSTGRESQL:
                retVal.append(" OFFSET ")
                        .append(offset)
                        .append(" LIMIT ")
                        .append(rowCount);
                break;
            case ORACLE:
            case SQLSERVER:
                retVal.append(String.format(" OFFSET %s ROWS FETCH NEXT %s ROWS ONLY", offset, rowCount));
                break;
            default:
                break;
        }
        return retVal.toString();
    }
}

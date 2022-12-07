package io.github.s3s3l.yggdrasil.mybatis.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

/**
 * ClassName:DataTimeTypeHandler <br>
 * Date: 2015年12月31日 下午4:53:02 <br>
 * 
 * @author kehw_zwei
 * @version 1.0
 * @since JDK 1.8
 */
public class DataTimeTypeHandler implements TypeHandler<LocalDateTime> {

    @Override
    public void setParameter(PreparedStatement ps, int i, LocalDateTime parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setTimestamp(i, Timestamp.valueOf(parameter));
    }

    @Override
    public LocalDateTime getResult(ResultSet rs, String columnName) throws SQLException {
        return rs.getTimestamp(columnName)
                .toLocalDateTime();
    }

    @Override
    public LocalDateTime getResult(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getTimestamp(columnIndex)
                .toLocalDateTime();
    }

    @Override
    public LocalDateTime getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return cs.getTimestamp(columnIndex)
                .toLocalDateTime();
    }

}

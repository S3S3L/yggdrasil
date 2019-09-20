package org.s3s3l.yggdrasil.mybatis.typehandler;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * <p>
 * </p>
 * ClassName:StringArrayTypeHandler <br>
 * Date: Dec 25, 2018 10:15:10 AM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class StringSetArrayTypeHandler extends BaseTypeHandler<Set<String>> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Set<String> parameter, JdbcType jdbcType)
            throws SQLException {
        super.setParameter(ps, i, parameter, jdbcType);
    }

    @Override
    public Set<String> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Array array = rs.getArray(columnName);
        return toStringList(array);
    }

    @Override
    public Set<String> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Array array = rs.getArray(columnIndex);
        return toStringList(array);
    }

    @Override
    public Set<String> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Array array = cs.getArray(columnIndex);
        return toStringList(array);
    }

    private Set<String> toStringList(Array array) throws SQLException {
        if (array == null) {
            return Collections.emptySet();
        }
        Object arr = array.getArray();
        if (arr instanceof String[]) {
            return Arrays.stream((String[]) array.getArray())
                    .collect(Collectors.toSet());
        }

        return Collections.emptySet();
    }

}

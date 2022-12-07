package io.github.s3s3l.yggdrasil.mybatis.typehandler;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * <p>
 * </p>
 * ClassName:ArrayTyepHandler <br>
 * Date: Nov 28, 2018 8:32:06 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class ShortArrayTypeHandler extends BaseTypeHandler<List<Short>> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<Short> parameter, JdbcType jdbcType)
            throws SQLException {
        super.setParameter(ps, i, parameter, jdbcType);
    }

    @Override
    public List<Short> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Array array = rs.getArray(columnName);
        return toShortList(array);
    }

    @Override
    public List<Short> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Array array = rs.getArray(columnIndex);
        return toShortList(array);
    }

    @Override
    public List<Short> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Array array = cs.getArray(columnIndex);
        return toShortList(array);
    }

    private List<Short> toShortList(Array array) throws SQLException {
        if (array == null) {
            return Collections.emptyList();
        }
        Object arr = array.getArray();
        if (arr instanceof Integer[]) {
            return Arrays.stream((Integer[]) array.getArray())
                    .map(Integer::shortValue)
                    .collect(Collectors.toList());
        } else if (arr instanceof Short[]) {
            return Arrays.stream((Short[]) array.getArray())
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

}

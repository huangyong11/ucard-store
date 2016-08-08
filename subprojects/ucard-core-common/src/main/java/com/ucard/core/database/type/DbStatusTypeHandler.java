package com.ucard.core.database.type;


import com.ucard.core.model.CodeEnum;
import com.ucard.core.utils.CodeEnumUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The base type handler for {@CodeEnum}.
 *
 * @author Wang Hui
 **/
public abstract class DbStatusTypeHandler<E extends Enum & CodeEnum> extends BaseTypeHandler<E> {
    private final Class<E> enumClass;

    public DbStatusTypeHandler(Class<E> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getCode());
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        if (rs.getObject(columnName) == null) {
            return null;
        }
        return CodeEnumUtils.parse(enumClass, rs.getInt(columnName));
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        if (rs.getObject(columnIndex) == null) {
            return null;
        }
        return CodeEnumUtils.parse(enumClass, rs.getInt(columnIndex));
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        if (cs.getObject(columnIndex) == null) {
            return null;
        }
        return CodeEnumUtils.parse(enumClass, cs.getInt(columnIndex));
    }
}

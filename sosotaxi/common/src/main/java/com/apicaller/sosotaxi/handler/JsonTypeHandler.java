package com.apicaller.sosotaxi.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * mySQL里json型字段到JSONObject类的映射。（数组也可以）
 * 用法一:
 * 入库：#{jsonDataField, typeHandler=com.lhz.test.JsonTypeHandler}
 * 出库：
 * <resultMap>
 * <result property="jsonDataField" column="json_data_field" javaType="com.lhz.MyClass" typeHandler="com.lhz.test.JsonTypeHandler"/>
 * </resultMap>
 * 或者：
 * 1）在mybatis-config.xml中指定handler:
 *      <typeHandlers>
 *              <typeHandler handler="JsonTypeHandler" javaType="MyClass"/>
 *      </typeHandlers>
 * 2)在MyClassMapper.xml里直接select/update/insert。
 *
 *
 * @author 骆荟州
 * @createTime 2020/7/13 12:33
 * @updateTime
 */

@MappedJdbcTypes(JdbcType.OTHER)
@MappedTypes({JSONArray.class, JSONObject.class})
public class JsonTypeHandler<T extends Object> extends BaseTypeHandler<T> {
    private static final ObjectMapper mapper = new ObjectMapper();
    private Class<T> clazz;

    public JsonTypeHandler(Class<T> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.clazz = clazz;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, this.toJson(parameter));
    }

    @Override
    public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return this.toObject(rs.getString(columnName), clazz);
    }

    @Override
    public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return this.toObject(rs.getString(columnIndex), clazz);
    }

    @Override
    public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return this.toObject(cs.getString(columnIndex), clazz);
    }

    private String toJson(T object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private T toObject(String content, Class<?> clazz) {
        if (content != null && !content.isEmpty()) {
            try {
                return (T) mapper.readValue(content, clazz);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            return null;
        }
    }
}


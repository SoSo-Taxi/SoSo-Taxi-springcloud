package com.apicaller.sosotaxi.handler;

import com.apicaller.sosotaxi.entity.GeoPoint;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.WKBReader;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * mySQL中point类型到自定义类型GeoPoint的映射。
 * @author 骆荟州
 * @CreateTime 2020/7/13 15:20
 * @UpdateTime
 */
@MappedTypes(value = {GeoPoint.class})
public class GeoPointTypeHandler extends BaseTypeHandler<GeoPoint> {

    private WKBReader _wkbReader;

    public GeoPointTypeHandler(int srid) {
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), srid);
        _wkbReader = new WKBReader(geometryFactory);
    }

    public GeoPointTypeHandler() {
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 0);
        _wkbReader = new WKBReader(geometryFactory);
    }

    //重写的这些方法决定了数据转换的方式。
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, GeoPoint geoPoint, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, geoPoint.toPointFormat());
    }

    @Override
    public GeoPoint getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return fromMysqlWkb(resultSet.getBytes(s));
    }

    @Override
    public GeoPoint getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return fromMysqlWkb(resultSet.getBytes(i));
    }

    @Override
    public GeoPoint getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return fromMysqlWkb(callableStatement.getBytes(i));
    }

    /**
     * bytes转GeoPoint对象
     */
    private GeoPoint fromMysqlWkb(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        try {
            byte[] geomBytes = ByteBuffer.allocate(bytes.length - 4).order(ByteOrder.LITTLE_ENDIAN)
                    .put(bytes, 4, bytes.length - 4).array();
            Geometry geometry = _wkbReader.read(geomBytes);
            Point point = (Point) geometry;
            return new GeoPoint(new Double(String.valueOf(point.getX())), new Double(String.valueOf(point.getY())));
        } catch (Exception e) {
        }
        return null;
    }

}


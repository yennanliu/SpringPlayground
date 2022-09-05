package com.wudimanong.efence.utils;

import com.alibaba.fastjson.JSON;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.postgis.LinearRing;
import org.postgis.PGgeometry;
import org.postgis.Point;
import org.postgis.Polygon;


/**
 * @author qiaojiang
 */
public class GeoJsonUtil {

    /**
     * GeoJson字符串数据转换为PostGis Polygon数据类型
     *
     * @param geoJson
     * @return
     */
    public static Polygon convertPointArrayJsonToPolygon(String geoJson) {
        //将GeoJson转换为Map对象
        Map<String, Object> mapJson = JSON.parseObject(geoJson, HashMap.class);
        //获取GeoJson中的坐标点列表数据
        String pointsJson = mapJson.get("coordinates").toString();
        List<List<List<BigDecimal>>> fencePoints = JSON.parseObject(pointsJson, ArrayList.class);
        List<Point> pointList = new ArrayList<>();
        for (List<List<BigDecimal>> lists : fencePoints) {
            for (List<BigDecimal> pointValue : lists) {
                Double x = pointValue.get(0).doubleValue();
                Double y = pointValue.get(1).doubleValue();
                Point point = new Point(x, y);
                pointList.add(point);
            }
        }
        Point[] points = new Point[pointList.size()];
        pointList.toArray(points);
        LinearRing linearRing = new LinearRing(points);
        Polygon polygon = new Polygon(new LinearRing[]{linearRing});
        polygon.setSrid(4326);
        return polygon;
    }

    /**
     * 将经纬度坐标转换为PostgreSQL Point点类型
     *
     * @param lng
     * @param lat
     * @return
     */
    public static String getPointByLngAndLat(double lng, double lat) {
        Point point = new Point(lng, lat);
        PGgeometry pGgeometry = new PGgeometry();
        pGgeometry.setGeometry(point);
        String wktPoint = pGgeometry.getValue();

        return wktPoint;
    }

    /**
     * int数组转换
     *
     * @param arr
     * @return
     */
    public static int NumberOf1(int[] arr) {
        int len = arr.length;
        int res = -1;
        if (len > 1) {
            res = arr[0];
            for (int i = 1; i < len; i++) {
                res = res ^ arr[i];
                System.out.println("-->" + res);
            }
        }
        return res;
    }
}

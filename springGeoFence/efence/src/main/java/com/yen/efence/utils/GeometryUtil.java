package com.yen.efence.utils;

// https://github.com/yennanliu/SpringPlayground/blob/main/courses/springBoot_springCloud_%E9%A0%82%E7%B4%9A%E9%96%8B%E7%99%BC_src_code/chapter04-efence/src/main/java/com/wudimanong/efence/utils/GeometryUtil.java

import com.github.davidmoten.geo.GeoHash;
import com.github.davidmoten.geo.LatLong;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GeometryUtil {

    public static List<String> getCoverGeoHashByPolygon(String geoJson, int precision) {
        List<String> result = null;
        /*if (StringUtils.isNotBlank(geoJson)) {
            try {
                Geometry geometry = (new GeometryJSON()).read(new StringReader(geoJson));
                result = getCoverGeoHashByGeometry(geometry, precision);
            } catch (Exception e) {
                //规范的
                JSONObject geoJsonObj = JSON.parseObject(geoJson);
                if (geoJsonObj != null && geoJsonObj.containsKey("crs")) {
                    geoJsonObj.remove("crs");
                    Geometry geometry = (new GeometryJSON()).read(new StringReader(geoJsonObj.toJSONString()));
                    result = getCoverGeoHashByGeometry(geometry, precision);
                    return result;
                }
                log.error("不太正确的GeoJson：{},{}", geoJson, e.getMessage());
            }
        }*/
        return result;
    }

    /**
     * 根据Geometry类型计算GeoHash
     *
     * @param geometry
     * @param precision
     * @return
     */
    public static List<String> getCoverGeoHashByGeometry(Geometry geometry, int precision) {
        List<String> result = new ArrayList<>();

        Envelope envelope = geometry.getEnvelopeInternal();

        double minLat = envelope.getMinY();
        double maxLat = envelope.getMaxY();
        double minLng = envelope.getMinX();
        double maxLng = envelope.getMaxX();

        double latSpan = getLatitudeSpanByPrecision(precision);
        double lngSpan = getLongitudeSpanByPrecision(precision);

        double latHalfSpan = latSpan / 2;
        double lngHalfSpan = lngSpan / 2;

        GeometryFactory geometryFactory = new GeometryFactory();
        String geoHash = getGeoHash(minLng, minLat, 8);
        double[] startPoint = getCentre(geoHash);
        maxLat = maxLat + latSpan;
        maxLng = maxLng + lngSpan;
        for (double lat = startPoint[1]; lat < maxLat; lat += latSpan) {
            for (double lng = startPoint[0]; lng < maxLng; lng += lngSpan) {
                //后面要考虑90,180 临界问题
                Envelope tempEnvelope = new Envelope(lng - lngHalfSpan, lng + lngHalfSpan, lat - latHalfSpan,
                        lat + latHalfSpan);
                if (geometry.intersects(geometryFactory.toGeometry(tempEnvelope))) {
                    result.add(getGeoHash(lng, lat, precision));
                }
            }
        }
        return result;
    }

    /**
     * 根据GeoHash 获得中心点经纬度
     *
     * @return [经度, 纬度]
     */
    public static double[] getCentre(String geohash) {
        LatLong latlon = GeoHash.decodeHash(geohash.trim());
        return new double[]{latlon.getLon(), latlon.getLat()};
    }

    /**
     * 根据经纬度获取GeoHash
     */
    public static String getGeoHash(double longitude, double latitude, int precision) {
        return GeoHash.encodeHash(latitude, longitude, precision);
    }

    /**
     * 获取指定GeoHash精度的经度跨度
     */
    public static double getLongitudeSpanByPrecision(int precision) {
        return GeoHash.widthDegrees(precision);
    }

    /**
     * 获取指定GeoHash精度的纬度跨度
     */
    public static double getLatitudeSpanByPrecision(int precision) {
        return GeoHash.heightDegrees(precision);
    }

}
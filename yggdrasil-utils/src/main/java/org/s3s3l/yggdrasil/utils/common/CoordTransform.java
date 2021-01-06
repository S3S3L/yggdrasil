package org.s3s3l.yggdrasil.utils.common;

/**
 * 坐标转换 一个提供了百度坐标（BD09）、国测局坐标（火星坐标，GCJ02）、和WGS84坐标系之间的转换的工具类
 * 参考https://github.com/wandergis/coordtransform 写的Java版本
 * 
 * @author Xinconan
 * @date 2016-03-18
 * @url https://github.com/xinconan/coordtransform
 */
public abstract class CoordTransform {
    private static final double XPI = 3.14159265358979324 * 3000.0 / 180.0;
    private static final double PI = 3.1415926535897932384626;
    private static final double A = 6378245.0;
    private static final double EE = 0.00669342162296594323;

    /**
     * 百度坐标系 (BD-09) 与 火星坐标系 (GCJ-02)的转换 即 百度 转 谷歌、高德
     * 
     * @param bdLon
     * @param bdLat
     * @return Double[lon,lat]
     */
    public static Double[] bd09ToGCJ02(Double bdLon, Double bdLat) {
        double x = bdLon - 0.0065;
        double y = bdLat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * XPI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * XPI);
        Double[] arr = new Double[2];
        arr[0] = z * Math.cos(theta);
        arr[1] = z * Math.sin(theta);
        return arr;
    }

    /**
     * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换 即谷歌、高德 转 百度
     * 
     * @param gcjLon
     * @param gcjLat
     * @return Double[lon,lat]
     */
    public static Double[] gcj02ToBD09(Double gcjLon, Double gcjLat) {
        double z = Math.sqrt(gcjLon * gcjLon + gcjLat * gcjLat) + 0.00002 * Math.sin(gcjLat * XPI);
        double theta = Math.atan2(gcjLat, gcjLon) + 0.000003 * Math.cos(gcjLon * XPI);
        Double[] arr = new Double[2];
        arr[0] = z * Math.cos(theta) + 0.0065;
        arr[1] = z * Math.sin(theta) + 0.006;
        return arr;
    }

    /**
     * WGS84转GCJ02
     * 
     * @param wgsLon
     * @param wgsLat
     * @return Double[lon,lat]
     */
    public static Double[] wgs84ToGCJ02(Double wgsLon, Double wgsLat) {
        if (outOfChina(wgsLon, wgsLat)) {
            return new Double[] { wgsLon, wgsLat };
        }
        double dlat = transformlat(wgsLon - 105.0, wgsLat - 35.0);
        double dlng = transformlng(wgsLon - 105.0, wgsLat - 35.0);
        double radlat = wgsLat / 180.0 * PI;
        double magic = Math.sin(radlat);
        magic = 1 - EE * magic * magic;
        double sqrtmagic = Math.sqrt(magic);
        dlat = (dlat * 180.0) / ((A * (1 - EE)) / (magic * sqrtmagic) * PI);
        dlng = (dlng * 180.0) / (A / sqrtmagic * Math.cos(radlat) * PI);
        Double[] arr = new Double[2];
        arr[0] = wgsLon + dlng;
        arr[1] = wgsLat + dlat;
        return arr;
    }

    /**
     * GCJ02转WGS84
     * 
     * @param gcjLon
     * @param gcjLat
     * @return Double[lon,lat]
     */
    public static Double[] gcj02ToWGS84(Double gcjLon, Double gcjLat) {
        if (outOfChina(gcjLon, gcjLat)) {
            return new Double[] { gcjLon, gcjLat };
        }
        double dlat = transformlat(gcjLon - 105.0, gcjLat - 35.0);
        double dlng = transformlng(gcjLon - 105.0, gcjLat - 35.0);
        double radlat = gcjLat / 180.0 * PI;
        double magic = Math.sin(radlat);
        magic = 1 - EE * magic * magic;
        double sqrtmagic = Math.sqrt(magic);
        dlat = (dlat * 180.0) / ((A * (1 - EE)) / (magic * sqrtmagic) * PI);
        dlng = (dlng * 180.0) / (A / sqrtmagic * Math.cos(radlat) * PI);
        double mglat = gcjLat + dlat;
        double mglng = gcjLon + dlng;
        return new Double[] { gcjLon * 2 - mglng, gcjLat * 2 - mglat };
    }

    private static Double transformlat(double lng, double lat) {
        double ret = -100.0 + 2.0 * lng + 3.0 * lat + 0.2 * lat * lat + 0.1 * lng * lat
                + 0.2 * Math.sqrt(Math.abs(lng));
        ret += (20.0 * Math.sin(6.0 * lng * PI) + 20.0 * Math.sin(2.0 * lng * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lat * PI) + 40.0 * Math.sin(lat / 3.0 * PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(lat / 12.0 * PI) + 320 * Math.sin(lat * PI / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    private static Double transformlng(double lng, double lat) {
        double ret = 300.0 + lng + 2.0 * lat + 0.1 * lng * lng + 0.1 * lng * lat + 0.1 * Math.sqrt(Math.abs(lng));
        ret += (20.0 * Math.sin(6.0 * lng * PI) + 20.0 * Math.sin(2.0 * lng * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lng * PI) + 40.0 * Math.sin(lng / 3.0 * PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(lng / 12.0 * PI) + 300.0 * Math.sin(lng / 30.0 * PI)) * 2.0 / 3.0;
        return ret;
    }

    /**
     * outOfChina
     * 
     * @描述: 判断是否在国内，不在国内则不做偏移
     * @param lng
     * @param lat
     * @return {boolean}
     */
    private static boolean outOfChina(Double lng, Double lat) {
        return (lng < 72.004 || lng > 137.8347) || (lat < 0.8293 || lat > 55.8271);
    }
}

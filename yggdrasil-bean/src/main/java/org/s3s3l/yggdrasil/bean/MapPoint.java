package org.s3s3l.yggdrasil.bean;

/**
 * <p>
 * </p>
 * ClassName:MapPoint <br>
 * Date: Mar 21, 2017 10:42:04 AM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class MapPoint implements Cacheable {

    /** 
     * @since JDK 1.8
     */  
    private static final long serialVersionUID = -1964199798024371136L;
    
    private double lat;
    private double lng;
    
    public MapPoint() {
        
    }
    
    public MapPoint(double lat,double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}

package org.s3s3l.yggdrasil.utils.json;  

import com.alibaba.fastjson.JSONObject;

/** 
 * ClassName:FastJsonUtils <br> 
 * Date:     2016年3月29日 上午10:34:48 <br> 
 * @author   kehw_zwei 
 * @version  1.0.0
 * @since    JDK 1.8
 */
public abstract class FastJsonUtils {

	public static JSONObject toJsonObject(Object obj){
		if(obj == null){
			return null;
		}
		return JSONObject.parseObject(JSONObject.toJSONString(obj));
	}
}
  
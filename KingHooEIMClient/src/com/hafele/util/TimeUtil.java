package com.hafele.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
* @author Dragon Wen E-mail:18475536452@163.com
* @version 创建时间：2017年11月9日 下午3:33:45
*时间工具类
*/
public class TimeUtil {

	private static String type = "yyyy/MM/dd HH:mm:ss";
	
	public static String getCurrentTime() {
		SimpleDateFormat sdf = new SimpleDateFormat(type);
		return sdf.format(new Date());
	}
	
}


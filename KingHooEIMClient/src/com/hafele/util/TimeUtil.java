package com.hafele.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
* @author Dragon Wen E-mail:18475536452@163.com
* @version ����ʱ�䣺2017��11��9�� ����3:33:45
*ʱ�乤����
*/
public class TimeUtil {

	private static String type = "yyyy/MM/dd HH:mm:ss";
	
	public static String getCurrentTime() {
		SimpleDateFormat sdf = new SimpleDateFormat(type);
		return sdf.format(new Date());
	}
	
}


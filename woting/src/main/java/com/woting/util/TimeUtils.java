package com.woting.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.util.Log;

/**
 * 时间工具类
 * 
 * @author辛龙
 */
@SuppressLint("SimpleDateFormat")
public class TimeUtils {
	
	public static String converTime(long time) {
		//今天已经度过的时间
		int todaytime = Integer.parseInt(getHour(System.currentTimeMillis()))*60*60
				+Integer.parseInt(getMinute(System.currentTimeMillis()))*60;

		long currentSeconds = System.currentTimeMillis() / 1000;// 当前系统时间
		long timeGap = currentSeconds - time / 1000;			// 与现在时间相差秒数
		String timeStr = null;
		if (timeGap > (2 * 24 * 60 * 60 + todaytime)) {			// 大于2天以上就返回标准时间
			timeStr = getDayTime(time) + " " + getMinTime(time);
		} else if (timeGap >( 24 * 60 * 60 + todaytime)) {		// 大于1天以上
			timeStr = "前天 " + getMinTime(time);
		} else if (timeGap > todaytime) {						// 大于0秒以上
			timeStr =  "昨天 " + getMinTime(time);
		} else {
			timeStr = "今天 " + getMinTime(time);
		}
		return timeStr;
	}

	public static String getChatTime(long time) {
		return getMinTime(time);
	}

//	public static String getPrefix(long time) {
//		long currentSeconds = System.currentTimeMillis();
//		long timeGap = currentSeconds - time;// 与现在时间差
//		String timeStr = null;
//		if (timeGap > 24 * 3 * 60 * 60 * 1000) {
//			timeStr = getDayTime(time) + " " + getMinTime(time);
//		} else if (timeGap > 24 * 2 * 60 * 60 * 1000) {
//			timeStr = "前天 " + getMinTime(time);
//		} else if (timeGap > 24 * 60 * 60 * 1000) {
//			timeStr = "昨天 " + getMinTime(time);
//		} else {
//			timeStr = "今天 " + getMinTime(time);
//		}
//		return timeStr;
//	}

	public static String getDayTime(long time) {
		SimpleDateFormat format = new SimpleDateFormat("MM-dd");
		return format.format(new Date(time));
	}
	
	public static String getHour(long time) {
		SimpleDateFormat format = new SimpleDateFormat("HH");
		return format.format(new Date(time));
	}
	
	public static String getMinute(long time) {
		SimpleDateFormat format = new SimpleDateFormat("mm");
		return format.format(new Date(time));
	}

	public static String getMinTime(long time) {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		return format.format(new Date(time));
	}
	
	public static int getTime(long time) {
		int h = Integer.parseInt(new SimpleDateFormat("HH").format(new Date(time)));
		int m = Integer.parseInt(new SimpleDateFormat("mm").format(new Date(time)));
		int s = Integer.parseInt(new SimpleDateFormat("ss").format(new Date(time)));
		int _time = h*60*60+m*60+s;
		Log.e("时分秒", h+"-"+m+"-"+s+"");
		return _time;
	}
	
	/**
	 * 获取当前时间
	 * @param time  "yyyy-MM-dd    HH:mm:ss "
	 * @return 
	 */
	public static  String getTime(String type,long time){
		SimpleDateFormat    formatter    =   new    SimpleDateFormat    (type);     
		Date    curDate    =   new    Date(time); 
		String    addtime    =    formatter.format(curDate); 
		return addtime;
	}
	
}

package cn.financial.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
public class TimeUtils {
	
	public final static String yyyy_MM_dd = "yyyy-MM-dd" ;
	
	public final static String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss" ;

	public final static String yyyy_MM_dd_HH_mm_ss_s = "yyyy-MM-dd HH:mm:ss.S" ;
	
	public final static String yyyy_MM_dd_HH_mm = "yyyy-MM-dd HH:mm" ;

	public final static String yyyyMMddHHmmssSSS = "yyyyMMddHHmmss:SSS" ;
	
	public final static Date getCurrentTime() {
		Date date = new Date() ; 
		return date ;
	}
	
	public final static int dayInterval(Date sdate,Date bdate){
		Calendar c1 = Calendar.getInstance();
		c1.setTime(sdate);
		c1.set(Calendar.HOUR_OF_DAY, 0);
		c1.set(Calendar.MINUTE, 0);
		c1.set(Calendar.SECOND, 0);
		c1.set(Calendar.MILLISECOND, 0);

		Calendar c2 = Calendar.getInstance();
		c2.setTime(bdate);
		c2.set(Calendar.HOUR_OF_DAY, 0);
		c2.set(Calendar.MINUTE, 0);
		c2.set(Calendar.SECOND, 0);
		c2.set(Calendar.MILLISECOND, 0);

		 long between_days=(c2.getTimeInMillis()-c1.getTimeInMillis())/(1000*3600*24);
		return Integer.parseInt(String.valueOf(between_days));
	}
	
	/** 
     * 计算两个日期之间相差的天数 
     * @param date1 
     * @param date2 
     * @return 
     */  
    public static int daysBetween(Date date1,Date date2)  
    {  
        Calendar cal = Calendar.getInstance();  
        cal.setTime(date1);  
        long time1 = cal.getTimeInMillis();               
        cal.setTime(date2);  
        long time2 = cal.getTimeInMillis();       
        long between_days=(time2-time1)/(1000*3600*24);  
           
       return Integer.parseInt(String.valueOf(between_days));         
    }
	
//	public final static int dayInterval(Date sdate,Date bdate){
//		Calendar c1 = Calendar.getInstance();
//		c1.setTime(sdate);
//		
//		Calendar c2 = Calendar.getInstance();
//		c2.setTime(bdate);
//		
//		System.out.println(TimeUtils.format(c1.getTime()));
//		System.out.println(TimeUtils.format(c2.getTime()));
//		
//		int y1 = c1.get(Calendar.YEAR);
//		int y2 = c2.get(Calendar.YEAR);
//		int d1 = y1 + c1.get(Calendar.DAY_OF_YEAR);
//		int d2 = y2 + c2.get(Calendar.DAY_OF_YEAR);
//		System.out.println(c1.get(Calendar.DAY_OF_YEAR));
//		System.out.println(c2.get(Calendar.DAY_OF_YEAR));
//		System.out.println(c1.getActualMaximum(Calendar.DAY_OF_YEAR));
//		
//		return (y2-y1)*c1.getActualMaximum(Calendar.DAY_OF_YEAR)+d2-d1;
//	}
	
	public final static int getCurrentHours() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.HOUR_OF_DAY);
	}
	
	public final static int getCurrentMinute() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.MINUTE);
	}
	
	public static long getCurrentTimestamp(){
		Calendar c = Calendar.getInstance();
		return c.getTimeInMillis();
	}
	
	/**
	 * 时间戳
	 * 
	 * @return
	 */
	public static String getTimestamp(Date date) {
		return new SimpleDateFormat("yyyyMMddHHmmss").format(date);
	}

	public static String getTimestamp() {
		return getTimestamp(new Date());
	}
	
	/**
	 * YYYYMMDD格式
	 * 
	 * @param date
	 * @return
	 */
	public static String shortFormatDate(Date date) {
		if (date == null) {
			return null;
		}
		return new SimpleDateFormat("yyyyMMdd").format(date);
	}
	
	public static Date randomDate(long start, long end) {
        try {
            long date = random(start, end);
            return new Date(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
	
	public static long random3Month(){
		long end = getCurrentTimestamp();
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -3);
		long start = c.getTimeInMillis();
		c.setTime(randomDate(start, end));
		System.out.println(format(c.getTime()));
		return c.getTimeInMillis();
	}
	
	private static long random(long begin, long end) {
        long rtnn = begin + (long) (Math.random() * (end - begin));
        if (rtnn == begin || rtnn == end) {
            return random(begin, end);
        }
        return rtnn;
    }
	
	public static String format(Date date, String format) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date) ;
	}
	
	public static String format(Date date){
		return format(date, yyyy_MM_dd) ;
	}
	
	public static Date string2Date(String dateValue, String dateFormat){
		Date date = null ;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			date = sdf.parse(dateValue) ;
		} catch (ParseException e) {
			e.printStackTrace() ;
			return null ;
		}
		return  date;
	}
	
	public static int getCurrentYear(){
		return getYear(getCurrentTime());
	}
	
	public static int getYear(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.YEAR);
	}
	
	public static int getDayOfMonth(int y,int m){
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, y);
		c.set(Calendar.MONTH, m);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.add(Calendar.DAY_OF_MONTH, -1);
		return c.get(Calendar.DAY_OF_MONTH);
	}
	
	public static Date string2Date(String dateValue) {
		return string2Date(dateValue, yyyy_MM_dd);
	}
	
	public static Date addYear(Date date,int value){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.YEAR, value);
		return c.getTime();
	}

	public static Date addDay(Date date,int value){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, value);
		return c.getTime();
	}
	
	public static void main(String[] args) {
//		Calendar cal = Calendar.getInstance();
//		cal.add(Calendar.DATE, -50);
//		cal.set(Calendar.HOUR_OF_DAY, 1);
//		cal.set(Calendar.MINUTE, 0);
//		
//		cal.set(Calendar.MONTH, Calendar.APRIL);
//		cal.set(Calendar.DATE, 19);
//		
//		Date dateFrom = cal.getTime();
//		cal.add(Calendar.DATE, 1);
//		Date dateTo = cal.getTime();
//		 System.out.println(dateTo);
//		 System.out.println(TimeUtils.format(dateFrom,TimeUtils.yyyy_MM_dd_HH_mm_ss));
//		 System.out.println(TimeUtils.format(dateTo,TimeUtils.yyyy_MM_dd_HH_mm_ss));
//		System.out.println(TimeUtils.random3Month());
		
		
//		Date date = TimeUtils.getCurrentTime();
//		System.out.println(TimeUtils.format(TimeUtils.addDay(date, 1), yyyy_MM_dd_HH_mm_ss));
		
//		String s = "13:30";
//		System.out.println(compareTime(s));
		
//		System.out.println(dayInterval(TimeUtils.getCurrentTime(),TimeUtils.string2Date("2017-01-20")));
		System.out.println(dayInterval(TimeUtils.getCurrentTime(),TimeUtils.string2Date("2017-01-20")));
	}
	
	/**
	 * 与当前时间做比较大小,当前时间大则返回正数,否则返回负数
	 * @param time 时间 格式 : HH:mm
	 * @return
	 */
	public static long compareTime(String time){
		Date d = TimeUtils.string2Date(time, "HH:mm");
		Calendar currentTime = Calendar.getInstance();
		currentTime.set(Calendar.YEAR, 1970);
		currentTime.set(Calendar.MONTH, 0);
		currentTime.set(Calendar.DAY_OF_MONTH, 1);
		return (currentTime.getTime().getTime()-d.getTime())/1000;
	}

}

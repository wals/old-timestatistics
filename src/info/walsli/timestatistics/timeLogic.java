package info.walsli.timestatistics;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.app.Activity;
import android.content.SharedPreferences;

public class timeLogic{
	SharedPreferences mySharedPreferences;
	SharedPreferences.Editor editor;
	DBHelper helper;
	
	public timeLogic()
	{
		mySharedPreferences =myApplication.getInstance().getSharedPreferences("info.walsli.timestatistics",Activity.MODE_MULTI_PROCESS);
		editor = mySharedPreferences.edit();
		helper = new DBHelper(myApplication.getInstance()); 
	}
	
	public void setBeginTime(String begintime)
	{
		editor.putString("begintime",begintime);
		editor.apply();
	}
	public void setScreenonFrequency(int i)
	{
		editor.putInt("screenon_frequency",i);
		editor.apply();
	}
	public int getScreenonFrequency()
	{
		return mySharedPreferences.getInt("screenon_frequency",1);
	}
	public long getTodaySeconds()
	{
		return mySharedPreferences.getLong("todayseconds",0);
	}
	
	public int getGapCount(Date endDate) {  
	    Calendar fromCalendar = Calendar.getInstance();     
	    fromCalendar.set(Calendar.YEAR, 2014);
	    fromCalendar.set(Calendar.MONTH, 6);
	    fromCalendar.set(Calendar.DATE,15);
	    fromCalendar.set(Calendar.HOUR_OF_DAY, 0);    
	    fromCalendar.set(Calendar.MINUTE, 0);    
	    fromCalendar.set(Calendar.SECOND, 0);    
	    fromCalendar.set(Calendar.MILLISECOND, 0);    
	    Calendar toCalendar = Calendar.getInstance();    
	    toCalendar.setTime(endDate);    
	    toCalendar.set(Calendar.HOUR_OF_DAY, 0);    
	    toCalendar.set(Calendar.MINUTE, 0);    
	    toCalendar.set(Calendar.SECOND, 0);    
	    toCalendar.set(Calendar.MILLISECOND, 0);    
	    return (int) ((toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24));  
	} 
	
	public boolean processTime(String end) 
	{
		String begin = mySharedPreferences.getString("begintime", "");
		if (!begin.equals("")) 
		{
			SimpleDateFormat dfs = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss");
			try
			{
				java.util.Date begins = dfs.parse(begin);
				java.util.Date ends = dfs.parse(end);
				if(((ends.getTime() - begins.getTime()) / 1000)>86400)
				{
					editor.putString("begintime", "");
					editor.commit();
					return false;
				}
				if((begins.getYear()<114)||(ends.getYear()<114))
				{
					editor.putString("begintime", "");
					editor.commit();
					return false;
				}
				if(ends.getTime() - begins.getTime()>=0)
				{
					if (begins.getDate() != ends.getDate()) 
					{
						helper.insert(getGapCount(begins), begins.getHours()*3600+begins.getMinutes()*60+begins.getSeconds(),86399);
						helper.insert(getGapCount(ends),0, ends.getHours()*3600+ends.getMinutes()*60+ends.getSeconds());
						helper.settlement(getGapCount(begins));
						long todayseconds = ends.getHours() * 3600 + ends.getMinutes()* 60 + ends.getSeconds();
						editor.putLong("todayseconds",todayseconds);
						long allseconds = mySharedPreferences.getLong("allseconds",0);
						allseconds+= (ends.getTime() - begins.getTime()) / 1000;
						editor.putLong("allseconds", allseconds);
						editor.putInt("date",ends.getDate());
						editor.putString("begintime", "");
						editor.putInt("screenon_frequency",1);
						editor.putBoolean("todayremind", false);
						editor.commit();
					}
					else 
					{
						helper.insert(getGapCount(ends), begins.getHours()*3600+begins.getMinutes()*60+begins.getSeconds(), ends.getHours()*3600+ends.getMinutes()*60+ends.getSeconds());
						int date=mySharedPreferences.getInt("date",0);
						long timepass=ends.getHours()*3600+ends.getMinutes()*60+ends.getSeconds()-begins.getHours()*3600-begins.getMinutes()*60-begins.getSeconds();
						if(date!=ends.getDate())
						{
							editor.putLong("todayseconds",timepass);
							editor.putInt("date",ends.getDate());
							editor.putInt("screenon_frequency",1);
							editor.putBoolean("todayremind", false);
							helper.settlement(getGapCount(begins)-1);
						}
						else
						{
							long todayseconds= mySharedPreferences.getLong("todayseconds",0);
							todayseconds+=timepass;
							editor.putLong("todayseconds",todayseconds);
						}
						long allseconds = mySharedPreferences.getLong("allseconds",0);
						allseconds +=timepass;
						editor.putLong("allseconds", allseconds);
						editor.putString("begintime", "");
						editor.commit();
						
					}
				} 
			}
			catch (ParseException e) 
			{
				e.printStackTrace();
			}
		}
		editor.putString("begintime", "");
		editor.commit();
		return true;
	}
	
}
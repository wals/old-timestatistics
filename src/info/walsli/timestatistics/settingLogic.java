package info.walsli.timestatistics;

import java.text.SimpleDateFormat;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

public class settingLogic{
	SharedPreferences mySharedPreferences;
	SharedPreferences.Editor editor;
	
	public settingLogic()
	{
		mySharedPreferences =myApplication.getInstance().getSharedPreferences("info.walsli.timestatistics",Activity.MODE_MULTI_PROCESS);
		editor = mySharedPreferences.edit();
	}
	public boolean isStatistics()
	{
		return mySharedPreferences.getBoolean("isStatistics",false);
	}
	public boolean isCallCountDown(long minutes)
	{
		return mySharedPreferences.getBoolean("iscountdown",false)&&!mySharedPreferences.getBoolean("todayremind",false)&&Long.parseLong(mySharedPreferences.getString("countdownnum","0"))<=minutes;
	}
	public boolean isModelDetermined()
	{
		if(mySharedPreferences.getInt("model",0)==0)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	public boolean isSharedPreferencesInit()
	{
		return !mySharedPreferences.getBoolean("init",true);
	}
	public int getLockNum()
	{
		return Integer.parseInt(mySharedPreferences.getString("locknum","30")); 
	}
	public void setTodayRemind(boolean b)
	{
		editor.putBoolean("todayremind", b);
		editor.apply();
	}
	public boolean isMagicLock()
	{
		return mySharedPreferences.getBoolean("isMagicLock",false);
	}
	public void setReboot(boolean whetherreboot)
	{
		editor.putBoolean("reboot",whetherreboot);
		editor.apply();
	}
	public void initSharedPreferences()
	{
	    SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss");
		String date = sDateFormat.format(new java.util.Date());
		editor.putString("begintime",date);
		editor.putLong("todayseconds",0);
		editor.putLong("allseconds",0);
		sDateFormat = new SimpleDateFormat("dd");
		date = sDateFormat.format(new java.util.Date());
		editor.putInt("date",Integer.parseInt(date));
		editor.putBoolean("init", false);
		editor.putBoolean("todayremind", false);
		editor.apply();
	}
}
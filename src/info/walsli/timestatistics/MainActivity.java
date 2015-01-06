package info.walsli.timestatistics;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.readystatesoftware.systembartint.SystemBarTintManager.SystemBarConfig;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActionBar.LayoutParams;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

public class MainActivity extends Activity {
	public boolean visiable=false;
	private ClockView clockview=null;
	IntentFilter filter;
	Thread thread;
	int width;
	timeLogic timelogic=new timeLogic();
	settingLogic settinglogic=new settingLogic();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		setContentView(R.layout.activity_main);
		findViewById(R.id.container).setBackgroundDrawable(myApplication.getInstance().getdrawable());  
		fucksmartbar();
		
		if(!settinglogic.isSharedPreferencesInit())
		{
			settinglogic.initSharedPreferences();
			Intent guideactivityIntent = new Intent(this, GuideActivity.class); 
			this.startActivity(guideactivityIntent); 
		}
		settinglogic.setReboot(true);
		if(!settinglogic.isModelDetermined())
		{
			if(getWindowManager().getDefaultDisplay().getWidth()==800)
			{
				myApplication.setModel(2);
			}
			else
			{
				myApplication.setModel(3);
			}
		}
		
		clockinit();
		
		if(!isServiceWorked())
		{
			Intent serviceIntent = new Intent(this, ScreenListenerService.class); 
			this.startService(serviceIntent); 
		}
		
		IntentFilter filter = new IntentFilter();
		filter.addAction("info.walsli.timestatistics.MainActivityFinishReceiver");
		filter.addAction("info.walsli.timestatistics.MainActivityRestartReceiver");
		filter.addAction("info.walsli.timestatistics.LockPhone");
		registerReceiver(receiver, filter);
		
		Intent intent = new Intent();    
		intent.setAction("info.walsli.timestatistics.BlankActivityFinishReceiver");    
		this.sendBroadcast(intent); 
		
		//SystemBarTintManager tintManager = new SystemBarTintManager(this);
        //tintManager.setStatusBarTintEnabled(true);
        //tintManager.setTintColor(Color.parseColor("#FFFFFF"));
	}
	
	@Override
	protected void onDestroy()
	{
		unregisterReceiver(receiver);    
		super.onDestroy();
	}
	
	private void clockinit()
	{
		this.clockview=new ClockView(this,timelogic.getTodaySeconds());  
        clockview.invalidate();  
        clockview.layout(0, 0, 0, 0);
        LayoutParams lp=new LayoutParams(0);
        this.addContentView(clockview, lp);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if(settinglogic.isStatistics())
		{
			MenuItem mnu1=menu.add(0,0,0,"统计");
	    	{

	    		mnu1.setIcon(R.drawable.statistics);
	    		mnu1.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
	    	}
		}
		MenuItem mnu2=menu.add(1,1,1,"设置");
    	{
    		mnu2.setIcon(R.drawable.setting);
    		mnu2.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    	}
		return true;
	}
	
	@Override
	public void onResume()
	{
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss");
		String date = sDateFormat.format(new java.util.Date());
		timelogic.processTime(date);
		timelogic.setBeginTime(date);
		visiable=true;
		thread=new Thread(new ProgressRunable());
		thread.start();
		super.onResume();
	}
	
	@Override
	public void onPause()
	{
		visiable=false;
		super.onPause();
	}
	
	private void fucksmartbar()
	{
		try {
            Method method = Class.forName("android.app.ActionBar").getMethod(
                    "setActionBarViewCollapsable", new Class[] { boolean.class });
            try {
                method.invoke(getActionBar(), true);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
		getActionBar().setDisplayOptions(0);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId())
		{
		case 0:
			startActivity(new Intent(this,StatisticsActivity.class));
    		return true;
		case 1:
			startActivity(new Intent(this,SettingActivity.class));
    		return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	class ProgressRunable implements Runnable
	{
		@Override
		public void run() {
			long seconds=timelogic.getTodaySeconds();
			int screenon_frequency=timelogic.getScreenonFrequency();
			Calendar c = Calendar.getInstance();  
			int hour=c.get(Calendar.HOUR_OF_DAY);
			while(visiable)
			{
				clockview.refreshclock(seconds,screenon_frequency,hour);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				seconds++;
			}
		}
	}

	public boolean isServiceWorked()
	{
		ActivityManager myManager=(ActivityManager)this.getSystemService(Context.ACTIVITY_SERVICE);
		ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) myManager.getRunningServices(100);
		for(int i = 0 ; i<runningService.size();i++)
		{
			if(runningService.get(i).service.getClassName().toString().equals("info.walsli.timestatistics.ScreenListenerService"))
			{
				return true;
			}
		}
		return false;
	}
	
	public BroadcastReceiver receiver = new BroadcastReceiver() 
	{
		@Override
		public void onReceive(final Context context, final Intent intent) 
		{
			if ("info.walsli.timestatistics.MainActivityFinishReceiver".equals(intent.getAction()))
			{
				MainActivity.this.finish();  
			} 
			else if ("info.walsli.timestatistics.MainActivityRestartReceiver".equals(intent.getAction()))
			{
				MainActivity.this.recreate();  
			} 
			else if ("info.walsli.timestatistics.Start_MainActivity".equals(intent.getAction()))
			{
				startActivity(new Intent(getApplicationContext(),MainActivity.class));
			}
			else if ("info.walsli.timestatistics.LockPhone".equals(intent.getAction()))
			{
				int num=settinglogic.getLockNum();
				int hour=num/60;;
				int minute=num%60;
				Intent intent1=new Intent();
				intent1.putExtra("hour", hour);
				intent1.putExtra("minute", minute);
				intent1.setComponent(new ComponentName("com.magic.magiclock","com.magic.magiclock.TimmingActivity"));
				intent1.setAction(Intent.ACTION_VIEW);
				intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
				getApplicationContext().startActivity(intent1);
			}
		}
	};
}

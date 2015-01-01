package info.walsli.timestatistics;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;

public class SettingActivity<MainDialog> extends PreferenceActivity implements OnPreferenceChangeListener,   
OnPreferenceClickListener{
	PreferenceCategory allseconds;
	SharedPreferences mySharedPreferences;
	SharedPreferences.Editor editor;
	DBHelper helper; 
	boolean isrestore=false;
	boolean getMagicLock=false;
	int restore_returncode=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_setting);
		getPreferenceManager().setSharedPreferencesName("info.walsli.timestatistics"); 
		addPreferencesFromResource(R.xml.setting);
		allseconds=(PreferenceCategory) findPreference("allseconds");
		mySharedPreferences =getSharedPreferences("info.walsli.timestatistics",Activity.MODE_MULTI_PROCESS);
		editor = mySharedPreferences.edit();
		setMagicLockStatus();
		setiscountdown();
		setclicklistener();
		setchangelistener();
		setallsecondssummary();
		
		helper = new DBHelper(getApplicationContext()); 
	}
	public void setMagicLockStatus()
	{
		final PackageManager packageManager = getApplicationContext().getPackageManager();
	    List< PackageInfo> pinfo = packageManager.getInstalledPackages(0);
	    List<String> pName = new ArrayList<String>();
	    if(pinfo != null){ 
            for(int i = 0; i < pinfo.size(); i++){ 
                String pn = pinfo.get(i).packageName; 
                pName.add(pn); 
            } 
        } 
	    getMagicLock=pName.contains("com.magic.magiclock");
	}
	public void setiscountdown()
	{
		CheckBoxPreference iscountdown=(CheckBoxPreference) findPreference("iscountdown");
		EditTextPreference countdownnum=(EditTextPreference) findPreference("countdownnum");
		CheckBoxPreference isMagicLock=(CheckBoxPreference) findPreference("isMagicLock");
		EditTextPreference locknum=(EditTextPreference) findPreference("locknum");
		countdownnum.setSummary(mySharedPreferences.getString("countdownnum","120"));
		locknum.setSummary(mySharedPreferences.getString("locknum","30"));
		if(iscountdown.isChecked())
		{
			countdownnum.setEnabled(true);
			isMagicLock.setEnabled(true);
			locknum.setEnabled(true);
		}
		else
		{
			countdownnum.setEnabled(false);
			isMagicLock.setEnabled(false);
			locknum.setEnabled(false);
		}
		if(!getMagicLock)
		{
			isMagicLock.setSummary("本功能需要配合“Magic手机锁”才可以正常使用，点击本按钮进入“Magic手机锁”安装页面");
			isMagicLock.setChecked(false);
			locknum.setEnabled(false);
		}
	}
	public void setallsecondssummary()
	{
		long allsecondspass=mySharedPreferences.getLong("allseconds", 0);
		String allseconds_title="手机总使用时间:";
		if(allsecondspass<60)
		{
			allseconds_title+="小于一分钟";
		}
		if(allsecondspass/86400>0)
		{
			allseconds_title+=String.valueOf(allsecondspass/86400)+"天";
			allsecondspass=allsecondspass%86400;
		}
		if(allsecondspass/3600>0)
		{
			allseconds_title+=String.valueOf(allsecondspass/3600)+"小时";
			allsecondspass=allsecondspass%3600;
		}
		if(allsecondspass/60>0)
		{
			allseconds_title+=String.valueOf(allsecondspass/60)+"分钟";
			allsecondspass=allsecondspass%60;
		}
		allseconds.setTitle(allseconds_title);
	}
	public void setclicklistener()
	{
		Preference feedback=findPreference("feedback");
		feedback.setOnPreferenceClickListener(this);
		Preference quit=findPreference("quit");
		quit.setOnPreferenceClickListener(this);
		Preference about=findPreference("about");
		about.setOnPreferenceClickListener(this);
		Preference mstore=findPreference("mstore");
		mstore.setOnPreferenceClickListener(this);
		Preference share=findPreference("share");
		share.setOnPreferenceClickListener(this);
		Preference backup=findPreference("backup");
		backup.setOnPreferenceClickListener(this);
		Preference restore=findPreference("restore");
		restore.setOnPreferenceClickListener(this);
		Preference cleardata=findPreference("cleardata");
		cleardata.setOnPreferenceClickListener(this);
		
	}
	public void setchangelistener()
	{
		CheckBoxPreference iscountdown=(CheckBoxPreference) findPreference("iscountdown");
		iscountdown.setOnPreferenceChangeListener(this);
		CheckBoxPreference isStatistics=(CheckBoxPreference) findPreference("isStatistics");
		isStatistics.setOnPreferenceChangeListener(this);
		EditTextPreference countdownnum=(EditTextPreference) findPreference("countdownnum");
		countdownnum.setOnPreferenceChangeListener(this);
		CheckBoxPreference isiconhide=(CheckBoxPreference) findPreference("isiconhide");
		isiconhide.setOnPreferenceChangeListener(this);
		CheckBoxPreference isMagicLock=(CheckBoxPreference) findPreference("isMagicLock");
		isMagicLock.setOnPreferenceChangeListener(this);
		EditTextPreference locknum=(EditTextPreference) findPreference("locknum");
		locknum.setOnPreferenceChangeListener(this);
	}
	
	@Override
	public boolean onPreferenceClick(Preference preference) {
		if(preference.getKey().equals("feedback"))
		{
			Intent data=new Intent(Intent.ACTION_SENDTO); 
			data.setData(Uri.parse("mailto:hustlizhiyuan@gmail.com")); 
			data.putExtra(Intent.EXTRA_SUBJECT, "魅时间应用反馈"); 
			data.putExtra(Intent.EXTRA_TEXT, ""); 
			startActivity(data); 
		}
		else if(preference.getKey().equals("share"))
		{
			String folderstr = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Download/timestatistics/";
			File folder = new File(folderstr);
			if(!folder.exists())
			{
				folder.mkdir();
			}
			String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Download/timestatistics/魅时间分享.png";
			Bitmap bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.backgroundmx2).copy(Bitmap.Config.ARGB_8888, true); ;
			Canvas canvas = new Canvas(bitmap); 
			Paint p=new Paint();
			p.setAntiAlias(true);
			p.setDither(true);
			p.setTypeface(myApplication.getInstance().gettypeface());
			p.setColor(Color.WHITE);
			
			long seconds=mySharedPreferences.getLong("todayseconds", 0);
			int screenon_frequency=mySharedPreferences.getInt("screenon_frequency",1);
			Calendar c = Calendar.getInstance();  
			int hour=c.get(Calendar.HOUR_OF_DAY);
			
			
			if(seconds%60!=0)
			{
				RectF oval2 = new RectF(225,250,575,600);//xyxy
				p.setStyle(Paint.Style.STROKE);
				p.setStrokeWidth((float) 4.0); 
				canvas.drawArc(oval2,(seconds%60)*6-90,360-(seconds%60)*6,false, p); 
			}
			//时刻
			String time="";
			if(seconds/3600<10)time+="0";
			time+=String.valueOf(seconds/3600);
			time+=":";
			long minutes=seconds%3600;
			if(minutes/60<10)time+="0";
			time+=String.valueOf(minutes/60);
			p.setStyle(Paint.Style.FILL);
			p.setSubpixelText(true);
			p.setTextSize(105);
			canvas.drawText(time,278,465,p);
			
			String topwords="今天我的手机被使用了";
			p.setTextSize(25);
			canvas.drawText(topwords,275,160,p);
			
			String screenon_frequency_string="使用次数  "+String.valueOf(screenon_frequency);
			canvas.drawText(screenon_frequency_string,325,700,p);
			
			String saying;
			if(seconds/3600>=2)
			{
				saying="你已经使用超过两个小时的时间，影响视力";
				canvas.drawText(saying,175,850,p);
				saying="不要只顾着低头社交，也许你抬起头就可以";
				canvas.drawText(saying,175,890,p);
				saying="发现更多的美好，抬起头来动一动吧";
				canvas.drawText(saying,175,930,p);
			}
			else
			{
				switch(hour)
				{
				case 0:
				case 1:
				case 2:
				case 3:
				case 4:
				case 5:
					saying="把活着的每一天看作生命的最后一天";
					canvas.drawText(saying,200,900,p);
					break;
				case 6:
					saying="盛年不再来  一日难再晨";
					canvas.drawText(saying,250,880,p);
					saying="及时当自勉  岁月不待人";
					canvas.drawText(saying,250,920,p);
					break;
				case 7:
					saying="早餐要吃饱";
					canvas.drawText(saying,337,900,p);
					break;
				case 8:
				case 9:
				case 10:
				case 11:
					saying="完成工作的方法是爱惜每一分钟";
					canvas.drawText(saying,225,900,p);
					break;
				case 12:
					saying="午餐要吃好";
					canvas.drawText(saying,337,900,p);
					break;
				case 13:
				case 14:
				case 15:
				case 16:
					saying="普通人只想如何度过时间";
					canvas.drawText(saying,262,880,p);
					saying="有才能的人才能利用时间";
					canvas.drawText(saying,262,920,p);
					break;
				case 17:
				case 18:
					saying="晚餐要吃少";
					canvas.drawText(saying,337,900,p);
					break;
				case 19:
				case 20:
					saying="黑夜到临的时候";
					canvas.drawText(saying,313,880,p);
					saying="没有人能够把一角阳光继续保留";
					canvas.drawText(saying,313,920,p);
					break;
				case 21:
				case 22:
				case 23:
					saying="把活着的每一天看作生命的最后一天";
					canvas.drawText(saying,200,900,p);
					break;
				default:
					saying="不要为已消逝之年华叹息";
					canvas.drawText(saying,262,880,p);
					saying="须正视欲匆匆溜走的时光";
					canvas.drawText(saying,262,920,p);
					break;
				}
			}
			
			
			
			File f = new File(path);
			try 
			{
				FileOutputStream out = new FileOutputStream(f);
			    bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
			    out.flush();
			    out.close();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			canvas=null;
			bitmap.recycle();
			bitmap=null;
			
			Intent intent = new Intent(Intent.ACTION_SEND);      
			if (f != null && f.exists() && f.isFile()) {    
			    intent.setType("image/png");    
			    Uri u = Uri.fromFile(f);    
			    intent.putExtra(Intent.EXTRA_STREAM, u);    
			}     
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);    
			startActivity(Intent.createChooser(intent, "请选择分享方式"));    
		}
		else if(preference.getKey().equals("mstore"))
		{
			Uri uri = Uri.parse("mstore:http://app.meizu.com/phone/apps/2e97acf7d20e436b856cdd5244b99308");
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
		}
		else if(preference.getKey().equals("quit"))
		{
			Intent intent = new Intent(this, ScreenListenerService.class);
			stopService(intent);
			Intent intent2 = new Intent();    
			intent2.setAction("info.walsli.timestatistics.MainActivityFinishReceiver");    
			this.sendBroadcast(intent2); 
			Intent intent3 = new Intent();    
			intent3.setAction("info.walsli.timestatistics.StatisticsActivityFinishReceiver");    
			this.sendBroadcast(intent3); 
			editor.putString("begintime","");
			editor.putBoolean("reboot",false);
			editor.apply();
			this.finish();
			
			
		}
		else if(preference.getKey().equals("about"))
		{
			Intent activityIntent = new Intent(this, AboutActivity.class); 
			this.startActivity(activityIntent); 
		}
		else if(preference.getKey().equals("backup"))
		{
			SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss");
			String date = sDateFormat.format(new java.util.Date());
			processtime(date);
			editor.putString("begintime", date);
			editor.commit();
			String folderstr = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Download/timestatistics/";
			File folder = new File(folderstr);
			if(!folder.exists())
			{
				folder.mkdir();
			}
			String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Download/timestatistics/魅时间备份.bak";
			File saveFile = new File(path);
			String pathbackup = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Download/timestatistics/魅时间备份"+date+".bak";
			File saveFilebackup = new File(pathbackup);
			if(saveFile.exists())
			{
				saveFile.delete();
			}
			try 
			{
				BufferedWriter output = new BufferedWriter(new FileWriter(saveFile,true));
				BufferedWriter outputbackup = new BufferedWriter(new FileWriter(saveFilebackup,true));
				output.append(String.valueOf(mySharedPreferences.getBoolean("iscountdown", false))+"\n");
				output.append(mySharedPreferences.getString("countdownnum","120")+"\n");
				outputbackup.append(String.valueOf(mySharedPreferences.getBoolean("iscountdown", false))+"\n");
				outputbackup.append(mySharedPreferences.getString("countdownnum","120")+"\n");
				Cursor c=helper.query("select * from timeinfo");
				while(c.moveToNext())
				{
					output.append(String.valueOf(c.getInt(1))+" "+String.valueOf(c.getInt(2))+" "+String.valueOf(c.getInt(3))+"\n");
					outputbackup.append(String.valueOf(c.getInt(1))+" "+String.valueOf(c.getInt(2))+" "+String.valueOf(c.getInt(3))+"\n");
				}
				output.flush();
				output.close();
				outputbackup.flush();
				outputbackup.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Toast.makeText(this,"备份成功！",Toast.LENGTH_SHORT).show();
		}
		else if(preference.getKey().equals("restore"))
		{
			Thread thread=new Thread(new ProgressRunable());
			thread.start();
			Toast.makeText(this,"正在 后台还原数据，请稍后，根据数据量大小还原过程可能持续几分钟，还原完毕后时间显示将恢复正常",Toast.LENGTH_LONG).show();
		}
		else if(preference.getKey().equals("cleardata"))
		{
			SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss");
			String date = sDateFormat.format(new java.util.Date());
			processtime(date);
			editor.putString("begintime", date);
			editor.commit();
			String folderstr = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Download/timestatistics/";
			File folder = new File(folderstr);
			if(!folder.exists())
			{
				folder.mkdir();
			}
			String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Download/timestatistics/魅时间备份.bak";
			File saveFile = new File(path);
			String pathbackup = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Download/timestatistics/魅时间备份"+date+".bak";
			File saveFilebackup = new File(pathbackup);
			if(saveFile.exists())
			{
				saveFile.delete();
			}
			try 
			{
				BufferedWriter output = new BufferedWriter(new FileWriter(saveFile,true));
				BufferedWriter outputbackup = new BufferedWriter(new FileWriter(saveFilebackup,true));
				output.append(String.valueOf(mySharedPreferences.getBoolean("iscountdown", false))+"\n");
				output.append(mySharedPreferences.getString("countdownnum","120")+"\n");
				outputbackup.append(String.valueOf(mySharedPreferences.getBoolean("iscountdown", false))+"\n");
				outputbackup.append(mySharedPreferences.getString("countdownnum","120")+"\n");
				Cursor c=helper.query("select * from timeinfo");
				while(c.moveToNext())
				{
					output.append(String.valueOf(c.getInt(1))+" "+String.valueOf(c.getInt(2))+" "+String.valueOf(c.getInt(3))+"\n");
					outputbackup.append(String.valueOf(c.getInt(1))+" "+String.valueOf(c.getInt(2))+" "+String.valueOf(c.getInt(3))+"\n");
				}
				output.flush();
				output.close();
				outputbackup.flush();
				outputbackup.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			helper.cleandb();
			sDateFormat = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss");
			date = sDateFormat.format(new java.util.Date());
			editor.putString("begintime",date);
			editor.putLong("todayseconds",0);
			editor.putLong("allseconds",0);
			sDateFormat = new SimpleDateFormat("dd");
			date = sDateFormat.format(new java.util.Date());
			editor.putInt("date",Integer.parseInt(date));
			editor.putBoolean("init", false);
			editor.putBoolean("todayremind", false);
			editor.apply();
			Toast.makeText(this,"清空数据完毕,之前数据已经自动备份",Toast.LENGTH_SHORT).show();
		}
		return false;
	}
	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		if(preference.getKey().equals("iscountdown"))
		{
			CheckBoxPreference iscountdown=(CheckBoxPreference) findPreference("iscountdown");
			EditTextPreference countdownnum=(EditTextPreference) findPreference("countdownnum");
			CheckBoxPreference isMagicLock=(CheckBoxPreference) findPreference("isMagicLock");
			EditTextPreference locknum=(EditTextPreference) findPreference("locknum");
			if(!iscountdown.isChecked())
			{
				countdownnum.setEnabled(true);
				isMagicLock.setEnabled(true);
				locknum.setEnabled(true);
				
			}
			else
			{
				countdownnum.setEnabled(false);
				isMagicLock.setEnabled(false);
				locknum.setEnabled(false);
			}
			return true;
		}
		else if(preference.getKey().equals("isMagicLock"))
		{
			CheckBoxPreference isMagicLock=(CheckBoxPreference) findPreference("isMagicLock");
			EditTextPreference locknum=(EditTextPreference) findPreference("locknum");
			setMagicLockStatus();
			if(getMagicLock)
			{
				if(isMagicLock.isChecked())
				{
					locknum.setEnabled(false);
				}
				else
				{
					locknum.setEnabled(true);
				}
				return true;
			}
			else
			{
				locknum.setEnabled(false);
				Uri uri = Uri.parse("mstore:http://app.meizu.com/phone/apps/7cf6dbc945dd4bd7b9f0d68d69d85946");
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
				return false;
			}
		}
		else if(preference.getKey().equals("locknum"))
		{
			EditTextPreference locknum=(EditTextPreference) findPreference("locknum");
			locknum.setSummary((CharSequence) newValue);
			return true;
		}
		else if(preference.getKey().equals("isiconhide"))
		{
			CheckBoxPreference isiconhide=(CheckBoxPreference) findPreference("isiconhide");
			
			PackageManager packageManager = getPackageManager();
	        ComponentName componentName = new ComponentName("info.walsli.timestatistics","info.walsli.timestatistics.BlankActivity");
			if(isiconhide.isChecked())
			{
				packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT,
	                    PackageManager.DONT_KILL_APP);
			}
			else
			{
				packageManager.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
	                    PackageManager.DONT_KILL_APP);
			}
			
			return true;
		}
		else if(preference.getKey().equals("isStatistics"))
		{
			Intent intent = new Intent();    
			intent.setAction("info.walsli.timestatistics.MainActivityRestartReceiver");    
			this.sendBroadcast(intent); 
			Intent intent2 = new Intent();    
			intent2.setAction("info.walsli.timestatistics.StatisticsActivityFinishReceiver");    
			this.sendBroadcast(intent2); 
			return true;
		}
		else if(preference.getKey().equals("countdownnum"))
		{
			EditTextPreference countdownnum=(EditTextPreference) findPreference("countdownnum");
			countdownnum.setSummary((CharSequence) newValue);
			return true;
		}
		return false;
	}
	public boolean processtime(String end) 
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
	public int getGapCount(Date endDate) {  
	       Calendar fromCalendar = Calendar.getInstance();     
	       fromCalendar.set(Calendar.YEAR, 2014);
	       fromCalendar.set(Calendar.MONTH, 6);
	       fromCalendar.set(Calendar.DAY_OF_MONTH, 15);
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
	public int getGapCountNow() {  
	       Calendar fromCalendar = Calendar.getInstance();     
	       fromCalendar.set(Calendar.YEAR, 2014);
	       fromCalendar.set(Calendar.MONTH, 6);
	       fromCalendar.set(Calendar.DAY_OF_MONTH, 15);
	       fromCalendar.set(Calendar.HOUR_OF_DAY, 0);    
	       fromCalendar.set(Calendar.MINUTE, 0);    
	       fromCalendar.set(Calendar.SECOND, 0);    
	       fromCalendar.set(Calendar.MILLISECOND, 0);    
	       Calendar toCalendar = Calendar.getInstance();        
	       toCalendar.set(Calendar.HOUR_OF_DAY, 0);    
	       toCalendar.set(Calendar.MINUTE, 0);    
	       toCalendar.set(Calendar.SECOND, 0);    
	       toCalendar.set(Calendar.MILLISECOND, 0);    
	       return (int) ((toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24));  
	}
	Handler mHandler=new Handler(){
		public void handleMessage(Message message)
		{
			switch(message.what)
			{
			case 1:
				Toast.makeText(getApplicationContext(), "已经在还原数据过程在，请稍后", Toast.LENGTH_SHORT).show();
				break;
			case 2:
				Toast.makeText(getApplicationContext(), "未检测到备份文件，请将备份文件置于下载文件夹并重命名为魅时间备份.bak", Toast.LENGTH_SHORT).show();
				break;
			case 3:
				Toast.makeText(getApplicationContext(), "还原完毕", Toast.LENGTH_SHORT).show();
				break;
			}
			
		}
	};
	class ProgressRunable implements Runnable
	{
		@Override
		public void run() {
			Message message=new Message();
			if(isrestore)
			{
				message.what=1;
				//Toast.makeText(getApplicationContext(),"已经在还原数据过程在，请稍后",Toast.LENGTH_SHORT).show();
			}
			else
			{
				isrestore=true;
				helper.cleandb();
				String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Download/timestatistics/魅时间备份.bak";
				File file = new File(path);
				if(!file.exists())
				{
					message.what=2;
					//Toast.makeText(getApplicationContext(),"未检测到备份文件，请将备份文件置于下载文件夹并重命名为魅时间备份.bak",Toast.LENGTH_SHORT).show();
				}
				else
				{
					BufferedReader reader = null;
			        try 
			        {
			        	
			        	reader = new BufferedReader(new FileReader(file));
			            String tempString = reader.readLine();
			            if(tempString.equals("false"))
			            {
			            	editor.putBoolean("iscountdown", false);
			            }
			            else
			            {
			            	editor.putBoolean("iscountdown", true);
			            }
			            tempString = reader.readLine();
			            editor.putString("countdownnum", tempString);
			            while ((tempString = reader.readLine()) != null)
			            {
			            	String ints[]=tempString.split(" ");
			            	helper.insert(Integer.parseInt(ints[0]), Integer.parseInt(ints[1]), Integer.parseInt(ints[2]));
			            }
			            reader.close();
			            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss");
						String date = sDateFormat.format(new java.util.Date());
						editor.putString("begintime",date);
						sDateFormat = new SimpleDateFormat("dd");
						date = sDateFormat.format(new java.util.Date());
						editor.putInt("date",Integer.parseInt(date));
						for(int i=1;i<=getGapCountNow();i++)
						{
							helper.settlement(i);
						}
						Cursor c=helper.query("select * from timeofdays where datenum="+String.valueOf(getGapCountNow()));
						while(c.moveToNext())
						{
							editor.putLong("todayseconds", c.getInt(2));
						}
						c=helper.query("select * from timeofdays");
						long allseconds=0;
						while(c.moveToNext())
						{
							allseconds+=c.getInt(2);
						}
						editor.putLong("allseconds", allseconds);
						editor.apply();
			        } 
			        catch (IOException e) 
			        {
			            e.printStackTrace();
			        } 
			        finally 
			        {
			            if (reader != null) 
			            {
			                try 
			                {
			                    reader.close();
			                } 
			                catch (IOException e1) 
			                {
			                	e1.printStackTrace();
			                }
			            }
			        }
			        message.what=3;
			        //Toast.makeText(getApplicationContext(),"还原完毕",Toast.LENGTH_SHORT).show();
			        
				}
		        isrestore=false;
		        mHandler.sendMessage(message);
			}
		}
	}
}

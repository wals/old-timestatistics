package info.walsli.timestatistics;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class BlankActivity extends Activity {
	IntentFilter filter;
	BlankActivityFinishReceiver receiver;
	timeLogic timelogic=new timeLogic();
	settingLogic settinglogic=new settingLogic();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_blank);
		receiver = new BlankActivityFinishReceiver(); 
		filter = new IntentFilter();  
        filter.addAction("info.walsli.timestatistics.BlankActivityFinishReceiver");  
        registerReceiver(receiver, filter); 
		startActivity(new Intent(this,MainActivity.class));
	}
	@Override
	protected void onDestroy()
	{     
		System.gc();
		unregisterReceiver(receiver); 
		super.onDestroy();
	}
	private class BlankActivityFinishReceiver extends BroadcastReceiver {  
	  	  
        @Override  
        public void onReceive(Context context, Intent intent) {  
            BlankActivity.this.finish();  
        }  
    } 
}

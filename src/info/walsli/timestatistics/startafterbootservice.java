package info.walsli.timestatistics;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class startafterbootservice extends BroadcastReceiver{
		static final String ACTION = "android.intent.action.BOOT_COMPLETED";
		SharedPreferences mySharedPreferences;
		@Override
		public void onReceive(Context context, Intent intent) {
			mySharedPreferences =context.getSharedPreferences("info.walsli.timestatistics",Activity.MODE_MULTI_PROCESS);
			
			if (intent.getAction().equals(ACTION)&&mySharedPreferences.getBoolean("reboot",true))
			{
				Intent serviceIntent = new Intent(context, ScreenListenerService.class); 
				context.startService(serviceIntent); 
				//startService(new Intent(getBaseContext(),screenlistenerservice.class));
			}
		}


	}
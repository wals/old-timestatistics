package info.walsli.timestatistics;
import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

public class myApplication extends Application
{
	private static myApplication instance;
	private static Typeface typeface;
	private static Bitmap mBgBitmap = null; 
	static SharedPreferences mySharedPreferences;
	static SharedPreferences.Editor editor;
	static boolean screenOn=false;
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		instance = this;
		typeface=Typeface.createFromAsset(this.getAssets(),"font.ttf");
		mBgBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.backgroundsmall);
		mySharedPreferences =getSharedPreferences("info.walsli.timestatistics",Activity.MODE_MULTI_PROCESS);
		editor = mySharedPreferences.edit();
	}
	public static int getModel()
	{
		return mySharedPreferences.getInt("model",3);
	}
	
	public static void setModel(int i)
	{
		editor.putInt("model", i);
		editor.apply();
	}
	
	public static myApplication getInstance() {
	    return instance;
	} 
	
	public Drawable getdrawable()
	{
		return new BitmapDrawable(mBgBitmap);
	}
	
	@Override
	public void onTerminate() 
	{
		super.onTerminate();
		if(mBgBitmap != null  && !mBgBitmap.isRecycled())  
        {  
            mBgBitmap.recycle();  
            mBgBitmap = null;  
        }     
	}
	
	public Typeface gettypeface()
	{
		return typeface;
	}
}
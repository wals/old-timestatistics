package info.walsli.timestatistics;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class DBHelper extends SQLiteOpenHelper {
	private static final String DB_NAME = "time.db";
	private static final String TBL_NAME = "timeinfo";
	private SQLiteDatabase db;
	
	DBHelper(Context c) {
		super(c, DB_NAME, null, 2);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		this.db = db;
		db.execSQL("create table timeinfo(_id integer primary key autoincrement,datenum integer,opentime integer,closetime integer)");
		db.execSQL("create table timeofdays(_id integer primary key autoincrement,datenum integer,todaytime integer)");
		
	}
	public void exec(String s)
	{
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(s);
		
	}
	public void cleandb()
	{
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("delete from timeofdays");
		db.execSQL("delete from timeinfo");
	}
	public void settlement(int a)
	{
		SQLiteDatabase db = getWritableDatabase();
		Cursor c=db.rawQuery("select * from timeinfo where datenum="+String.valueOf(a), null);
		if(c.getCount()!=0)
		{
			int time=0;
			while(c.moveToNext())
			{
				time+=c.getInt(3)-c.getInt(2);
			}
			Cursor temp=db.rawQuery("select * from timeofdays where datenum="+String.valueOf(a), null);
			if(temp.getCount()!=0)
			{
				db.execSQL("delete from timeofdays where datenum="+String.valueOf(a));
			}
			ContentValues values = new ContentValues();  
			values.put("datenum", a);
			values.put("todaytime", time);
			db.insert("timeofdays", null, values);
		}
		
	}
	public void insert(int a,int b,int c) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();  
		values.put("datenum", a);
		values.put("opentime", b);
		values.put("closetime", c);
		db.insert(TBL_NAME, null, values);
	}
	public Cursor query(String s) {
		SQLiteDatabase db = getWritableDatabase();
		Cursor c = db.rawQuery(s, null);
		return c;
	}
	public void close() {
		if (db != null)
			db.close();
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
}
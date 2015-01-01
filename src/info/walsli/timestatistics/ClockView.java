package info.walsli.timestatistics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.view.View;

public class ClockView extends View {
	private long seconds=0;
	private int hour=0;
	private int screenon_frequency=1;
	public ClockView(Context context,long seconds) {
		super(context);
		this.seconds=seconds;
		postInvalidate();
	}
	public Paint initPaint()
	{
		Paint p = new Paint();
		p.setAntiAlias(true);
		p.setDither(true);
		p.setColor(Color.WHITE);
		return p;
	}
	public void refreshclock(long s,int screenon_frequency,int hour)
	{
		this.seconds=s;
		this.screenon_frequency=screenon_frequency;
		this.hour=hour;
		postInvalidate();
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(myApplication.getModel()==3)
		{
			drawmx3(canvas);
		}
		else
		{
			drawmx2(canvas);
		}
		 
	}
	public void drawmx2(Canvas canvas)
	{
		Paint p=initPaint();
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
		p.setTypeface(myApplication.getInstance().gettypeface());
		canvas.drawText(time,278,465,p);
		p.setTypeface(Typeface.DEFAULT);
		
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
	}
	public void drawmx3(Canvas canvas)
	{
		Paint p=initPaint();
		//秒针
		if(seconds%60!=0)
		{
			RectF oval2 = new RectF(306,369,774,837);//xyxy
			p.setStyle(Paint.Style.STROKE);
			p.setStrokeWidth((float) 6.0); 
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
		p.setTextSize(140);
		p.setTypeface(myApplication.getInstance().gettypeface());
		canvas.drawText(time,377,663,p);
		p.setTypeface(Typeface.DEFAULT);
		
		
		String topwords="今天我的手机被使用了";
		p.setTextSize(35);
		canvas.drawText(topwords,370,260,p);
		
		String screenon_frequency_string="使用次数  "+String.valueOf(screenon_frequency);
		canvas.drawText(screenon_frequency_string,450,1060,p);
		String saying;
		if(seconds/3600>=2)
		{
			saying="你已经使用超过两个小时的时间，影响视力";
			canvas.drawText(saying,225,1250,p);
			saying="不要只顾着低头社交，也许你抬起头就可以";
			canvas.drawText(saying,225,1300,p);
			saying="发现更多的美好，抬起头来动一动吧";
			canvas.drawText(saying,225,1350,p);
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
				canvas.drawText(saying,260,1313,p);
				break;
			case 6:
				saying="盛年不再来  一日难再晨";
				canvas.drawText(saying,355,1300,p);
				saying="及时当自勉  岁月不待人";
				canvas.drawText(saying,355,1350,p);
				break;
			case 7:
				saying="早餐要吃饱";
				canvas.drawText(saying,450,1313,p);
				break;
			case 8:
			case 9:
			case 10:
			case 11:
				saying="完成工作的方法是爱惜每一分钟";
				canvas.drawText(saying,295,1313,p);
				break;
			case 12:
				saying="午餐要吃好";
				canvas.drawText(saying,450,1313,p);
				break;
			case 13:
			case 14:
			case 15:
			case 16:
				saying="普通人只想如何度过时间";
				canvas.drawText(saying,348,1300,p);
				saying="有才能的人才能利用时间";
				canvas.drawText(saying,348,1350,p);
				break;
			case 17:
			case 18:
				saying="晚餐要吃少";
				canvas.drawText(saying,450,1313,p);
				break;
			case 19:
			case 20:
				saying="黑夜到临的时候";
				canvas.drawText(saying,417,1300,p);
				saying="没有人能够把一角阳光继续保留";
				canvas.drawText(saying,295,1350,p);
				break;
			case 21:
			case 22:
			case 23:
				saying="把活着的每一天看作生命的最后一天";
				canvas.drawText(saying,260,1313,p);
				break;
			default:
				saying="不要为已消逝之年华叹息";
				canvas.drawText(saying,347,1300,p);
				saying="须正视欲匆匆溜走的时光";
				canvas.drawText(saying,347,1350,p);
				break;
			}
		}
		//jiaozhunmx3(canvas);
	}
	public void jiaozhunmx3(Canvas canvas)
	{
		Paint p=initPaint();
		canvas.drawCircle(540,603, 5, p);
		canvas.drawLine(540, 0, 540,1800, p);
		canvas.drawLine(0,900,1080,900, p);
		canvas.drawCircle(560,850, 3, p);
		canvas.drawCircle(580,850, 5, p);
	}
	public void jiaozhunmx2(Canvas canvas)
	{
		Paint p=initPaint();
		canvas.drawCircle(400,425, 4, p);
		canvas.drawLine(400, 0, 400,1280, p);
		canvas.drawLine(0,640,800,640, p);
	}
}
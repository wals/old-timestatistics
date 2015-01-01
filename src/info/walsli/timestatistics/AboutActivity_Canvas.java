package info.walsli.timestatistics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class AboutActivity_Canvas extends View {
	public AboutActivity_Canvas(Context context) {
		super(context);
		postInvalidate();
	}
	private Paint initPaint()
	{
		Paint p = new Paint();
		p.setAntiAlias(true);
		p.setDither(true);
		p.setColor(Color.WHITE);
		p.setStyle(Paint.Style.FILL);
		p.setSubpixelText(true);
		return p;
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
		String words="";
		p.setTextSize(45);
		words="起";
		canvas.drawText(words,40,60,p);
		p.setTextSize(25);
		words="我被抽中参加2014年的魅族Flyme玩家面对面武汉站";
		canvas.drawText(words,80,110,p);
		words="有幸在活动中认识了Kenai";
		canvas.drawText(words,80,150,p);
		words="他让我意识到了初学者也可以做出好的应用";
		canvas.drawText(words,80,190,p);
		words="于是梦想起航了";
		canvas.drawText(words,80,230,p);
		p.setTextSize(45);
		words="承";
		canvas.drawText(words,40,290,p);
		p.setTextSize(25);
		words="回家后思考了一晚上";
		canvas.drawText(words,80,340,p);
		words="想到我经常玩起手机来就不知道时间";
		canvas.drawText(words,80,380,p);
		words="就决定写一款可以获得每天手机使用时间的应用";
		canvas.drawText(words,80,420,p);
		p.setTextSize(45);
		words="转";
		canvas.drawText(words,40,480,p);
		p.setTextSize(25);
		words="但是开发应用并不像大神说的那么简单";
		canvas.drawText(words,80,530,p);
		words="熬过了多少通宵，吃过多少泡面";
		canvas.drawText(words,80,570,p);
		words="才能明白功能该如何实现";
		canvas.drawText(words,80,610,p);
		words="武汉炎热的天气，层出不穷的bug";
		canvas.drawText(words,80,650,p);
		words="都让成功变得遥不可及";
		canvas.drawText(words,80,690,p);
		p.setTextSize(45);
		words="合";
		canvas.drawText(words,40,750,p);
		p.setTextSize(25);
		words="不过20天地狱般的日子后我还是做到了";
		canvas.drawText(words,80,800,p);
		words="于是就有了你们手中的这款魅时间";
		canvas.drawText(words,80,840,p);
		words="她可以显示每天使用手机的时间和次数";
		canvas.drawText(words,80,880,p);
		words="作为新手开发者，我承认魅时间并不完美";
		canvas.drawText(words,80,920,p);
		words="功能也比较简陋";
		canvas.drawText(words,80,960,p);
		words="所以欢迎大家提出意见和新功能的建议";
		canvas.drawText(words,80,1000,p);
		words="如果出现bug也请大家多多担待";
		canvas.drawText(words,80,1040,p);
		words="应用商店里每条评论和每封反馈邮件我都会认真回复";
		canvas.drawText(words,80,1080,p);
	}
	public void drawmx3(Canvas canvas)
	{
		Paint p=initPaint();
		String words="";
		p.setTextSize(55);
		words="起";
		canvas.drawText(words,50,100,p);
		p.setTextSize(35);
		words="我被抽中参加2014年的魅族Flyme玩家面对面武汉站";
		canvas.drawText(words,100,170,p);
		words="有幸在活动中认识了Kenai";
		canvas.drawText(words,100,220,p);
		words="他让我意识到了初学者也可以做出好的应用";
		canvas.drawText(words,100,270,p);
		words="于是梦想起航了";
		canvas.drawText(words,100,320,p);
		p.setTextSize(55);
		words="承";
		canvas.drawText(words,50,390,p);
		p.setTextSize(35);
		words="回家后思考了一晚上";
		canvas.drawText(words,100,460,p);
		words="想到我经常玩起手机来就不知道时间";
		canvas.drawText(words,100,510,p);
		words="就决定写一款可以获得每天手机使用时间的应用";
		canvas.drawText(words,100,560,p);
		p.setTextSize(55);
		words="转";
		canvas.drawText(words,50,630,p);
		p.setTextSize(35);
		words="但是开发应用并不像大神说的那么简单";
		canvas.drawText(words,100,700,p);
		words="熬过了多少通宵，吃过多少泡面";
		canvas.drawText(words,100,750,p);
		words="才能明白功能该如何实现";
		canvas.drawText(words,100,800,p);
		words="武汉炎热的天气，层出不穷的bug";
		canvas.drawText(words,100,850,p);
		words="都让成功变得遥不可及";
		canvas.drawText(words,100,900,p);
		p.setTextSize(55);
		words="合";
		canvas.drawText(words,50,970,p);
		p.setTextSize(35);
		words="不过20天地狱般的日子后我还是做到了";
		canvas.drawText(words,100,1040,p);
		words="于是就有了你们手中的这款魅时间";
		canvas.drawText(words,100,1090,p);
		words="她可以显示每天使用手机的时间和次数";
		canvas.drawText(words,100,1140,p);
		words="作为新手开发者，我承认魅时间并不完美";
		canvas.drawText(words,100,1190,p);
		words="功能也比较简陋";
		canvas.drawText(words,100,1240,p);
		words="所以欢迎大家提出意见和新功能的建议";
		canvas.drawText(words,100,1290,p);
		words="如果出现bug也请大家多多担待";
		canvas.drawText(words,100,1340,p);
		words="应用商店里每条评论和每封反馈邮件我都会认真回复";
		canvas.drawText(words,100,1390,p);
	}
}
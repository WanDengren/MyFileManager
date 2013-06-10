package com.example.myfilemanager;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/*程序主界面的布局，采用线性布局，框架包括一个显示欢迎信息的
   TextView 以及文件列表 ListView*/
public class MainLayout extends LinearLayout{
	private ListView fileListView;
	public ListView getFileListView()
	{
		return fileListView;
	}
	//@SuppressWarnings("deprecation")
	public MainLayout(Context context)
	{
		super(context);//应用父类构造方法
		this.setOrientation(LinearLayout.VERTICAL);
	
		
		TextView textView = new TextView(context);
		
		fileListView =new ListView(context);
		
		this.addView(textView,new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));//包裹住组件
		this.addView(fileListView,new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		
		
	}

}

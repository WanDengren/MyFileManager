package com.example.myfilemanager;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/*����������Ĳ��֣��������Բ��֣���ܰ���һ����ʾ��ӭ��Ϣ��
   TextView �Լ��ļ��б� ListView*/
public class MainLayout extends LinearLayout{
	private ListView fileListView;
	public ListView getFileListView()
	{
		return fileListView;
	}
	//@SuppressWarnings("deprecation")
	public MainLayout(Context context)
	{
		super(context);//Ӧ�ø��๹�췽��
		this.setOrientation(LinearLayout.VERTICAL);
	
		
		TextView textView = new TextView(context);
		
		fileListView =new ListView(context);
		
		this.addView(textView,new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));//����ס���
		this.addView(fileListView,new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		
		
	}

}

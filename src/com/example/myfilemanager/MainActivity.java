package com.example.myfilemanager;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends TabActivity implements
		OnTabChangeListener {

	private TabHost tabHost;
	private static final String SORT = "分类浏览";
	private static final String LIST = "目录浏览";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
  		super.onCreate(savedInstanceState);
		// 使用系统的layout文件初始化界面结构
		// 获得TabHost对象
		tabHost = getTabHost();
		// 设置OnTabChangeListener,当用户切换Tab的时候被调用
		tabHost.setOnTabChangedListener(this);

		// 创建一个TabSpec，包含Indicator和content两部分
		TabHost.TabSpec sort = tabHost.newTabSpec(SORT);
		sort.setIndicator(SORT, null);
		// content设置为intent,则会启动一个Activity作为内容
		Intent s_intent = new Intent(this, SortActivity.class);
		sort.setContent(s_intent);
		// 将sort加入到TabHost对象中
		tabHost.addTab(sort);

		TabHost.TabSpec list = tabHost.newTabSpec(LIST);
		list.setIndicator(LIST, null);
		Intent l_intent = new Intent(this, ContentActivity.class);
		list.setContent(l_intent);
		tabHost.addTab(list);

		tabHost.setCurrentTab(0);

	}

	// 切换Tab的时候，此方法被调用
	public void onTabChanged(String tabId) {
		Toast.makeText(this, tabId, Toast.LENGTH_SHORT).show();
	}

	
}

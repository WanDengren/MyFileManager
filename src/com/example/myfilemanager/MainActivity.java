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
	private static final String SORT = "�������";
	private static final String LIST = "Ŀ¼���";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
  		super.onCreate(savedInstanceState);
		// ʹ��ϵͳ��layout�ļ���ʼ������ṹ
		// ���TabHost����
		tabHost = getTabHost();
		// ����OnTabChangeListener,���û��л�Tab��ʱ�򱻵���
		tabHost.setOnTabChangedListener(this);

		// ����һ��TabSpec������Indicator��content������
		TabHost.TabSpec sort = tabHost.newTabSpec(SORT);
		sort.setIndicator(SORT, null);
		// content����Ϊintent,�������һ��Activity��Ϊ����
		Intent s_intent = new Intent(this, SortActivity.class);
		sort.setContent(s_intent);
		// ��sort���뵽TabHost������
		tabHost.addTab(sort);

		TabHost.TabSpec list = tabHost.newTabSpec(LIST);
		list.setIndicator(LIST, null);
		Intent l_intent = new Intent(this, ContentActivity.class);
		list.setContent(l_intent);
		tabHost.addTab(list);

		tabHost.setCurrentTab(0);

	}

	// �л�Tab��ʱ�򣬴˷���������
	public void onTabChanged(String tabId) {
		Toast.makeText(this, tabId, Toast.LENGTH_SHORT).show();
	}

	
}

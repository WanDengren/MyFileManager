package com.example.myfilemanager;

import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
/*��ʾ�ļ�����Ĳ��֣������ļ�·������С���޸�ʱ�䣬�ɶ�����д��
 * �����Եȣ��ֱ𴴽�һ��TextView�����ô�ֱ���Բ���
 */
public class FileDetailInfoLayout extends LinearLayout{
	private TextView pathTextView;
	private TextView sizeTextView;
	private TextView updateTimeTextView;
	private TextView canReadTextView;
	private TextView canWriteTextView;
	private TextView isHiddenTextView;
	
	public FileDetailInfoLayout(Context context){
		super(context);
		this.setOrientation(LinearLayout.VERTICAL);
		this.setGravity(Gravity.CENTER_HORIZONTAL);//�����ı�����
		pathTextView = new TextView(context);
		sizeTextView = new TextView(context);
		updateTimeTextView = new TextView(context);
		canWriteTextView = new TextView(context);
		canReadTextView = new TextView(context);
		isHiddenTextView = new TextView(context);
		this.addView(pathTextView,new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		this.addView(sizeTextView,new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		this.addView(updateTimeTextView,new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		this.addView(canReadTextView,new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		this.addView(canWriteTextView,new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		this.addView(isHiddenTextView,new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		
	}
	public TextView getPathTextView(){
		return this.pathTextView;
	}
	public TextView getSizeTextView(){
		return this.sizeTextView;
	}
	public TextView getUpdateTimeTextView(){
		return this.updateTimeTextView;
	}
	public TextView getCanReadTextView(){
		return this.canReadTextView;
	}
	public TextView getCanWriteTextView(){
		return this.canWriteTextView;
	}
	public TextView getIsHiddenTextView(){
		return this.isHiddenTextView;
	}
}

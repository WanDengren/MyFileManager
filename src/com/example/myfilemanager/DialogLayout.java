package com.example.myfilemanager;

import android.content.Context;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
/*�����࣬����Ƕ��һ����Ҫ�����û�������Ϣ��Dialog*/
public class DialogLayout extends LinearLayout{
	private  TextView messageTextView;
	private  EditText inputFileNameEditText;
	
	public DialogLayout(Context context){
		super(context);
		this.setOrientation(LinearLayout.VERTICAL);
		
		this.setGravity(Gravity.CENTER_HORIZONTAL);
		messageTextView = new TextView(context);
		inputFileNameEditText= new EditText(context);
		inputFileNameEditText.setSelectAllOnFocus(true);
		
		this.addView(messageTextView,new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		this.addView(inputFileNameEditText,new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
	}
	public TextView getTextView(){
		return this.messageTextView;
	}
	public EditText getEditText(){
		return this.inputFileNameEditText;
	}
		
	

}

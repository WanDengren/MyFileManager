package com.example.myfilemanager;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/*对文件列表ListView中的每一项进行布局，采用相对布局方式*/
public class FileInfoLayout extends RelativeLayout{
	private TextView  fileNameTextView=null;
	private TextView  fileUpdateTimeTextView=null;
	private ImageView fileIconImageView=null;
	
	public FileInfoLayout(Context context,FileInfo fileInfo){
	super(context);
	fileNameTextView=new TextView(context);
	fileUpdateTimeTextView=new TextView(context);
	fileIconImageView=new ImageView(context);
	this.setPadding(30, 10, 0, 0);
	
	
	fileIconImageView.setId(1001);
	fileIconImageView.setImageDrawable(fileInfo.get_Icon());
	
	LayoutParams layoutParamsOne=new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
	layoutParamsOne.addRule(RelativeLayout.ALIGN_TOP, RelativeLayout.TRUE);
	layoutParamsOne.addRule(RelativeLayout.ALIGN_LEFT, RelativeLayout.TRUE);
	
	fileIconImageView.setLayoutParams(layoutParamsOne);
	this.addView(fileIconImageView);
	
	fileNameTextView.setId(1002);
	fileNameTextView.setText(fileInfo.get_filename());
	fileNameTextView.setTextSize(22);
	//fileNameTextView.setTextColor(ColorStateList.valueOf(0xffffff));
	
	
	LayoutParams layoutParamsTwo =new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
	layoutParamsTwo.leftMargin=10;
	
	layoutParamsTwo.addRule(RelativeLayout.RIGHT_OF,fileIconImageView.getId());
	fileNameTextView.setLayoutParams(layoutParamsTwo);
	this.addView(fileNameTextView);
	
	fileUpdateTimeTextView.setId(1003);
	fileUpdateTimeTextView.setText(fileInfo.get_last_update_time());
	fileUpdateTimeTextView.setTextSize(13);
	
	LayoutParams layoutParamsThree = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
	layoutParamsThree.addRule(RelativeLayout.ALIGN_LEFT, fileNameTextView.getId());
	layoutParamsThree.addRule(RelativeLayout.BELOW, fileNameTextView.getId());
	fileUpdateTimeTextView.setLayoutParams(layoutParamsThree);
	
	this.addView(fileUpdateTimeTextView);	
	
	}
	public void setFileName(String fileName)
	{
		this.fileNameTextView.setText(fileName);
	}
	public void setFileUpdateTime(String fileUpdateTime)
	{
		this.fileUpdateTimeTextView.setText(fileUpdateTime);
	}
	public void setFileIcon(Drawable fileIcon)
	{
		this.fileIconImageView.setImageDrawable(fileIcon);
	}
}

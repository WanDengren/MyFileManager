package com.example.myfilemanager;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/*文件信息的适配器，为文件列表中的每一项创建View*/
public class FileInfoAdapter extends BaseAdapter{
	public Context context;
	public List<FileInfo> fileList=new ArrayList<FileInfo>();
	public FileInfoAdapter(Context context)
	{
		this.context=context;
	}
	public void addFileItem(FileInfo fileItem)
	{
		fileList.add(fileItem);
	}
	public boolean isAllSelectable()
	{
		return false;
	}
	public boolean isSelcetable(int position)
	{
		return fileList.get(position).isSelectable();
		
	}
	public void setFileList(List<FileInfo> filelist)
	{
		this.fileList=filelist;
	}
	
	
	public int getCount()
	{
		return fileList.size();
	}
	public Object getItem(int position)
	{
		return fileList.get(position);
	}
	public long getItemId(int position)
	{
		return position;
	}
	
	@Override
	public View getView(int position,View converView,ViewGroup parent)
	{
		FileInfoLayout fileView;
		if(converView==null)
		{
			fileView=new FileInfoLayout(context, fileList.get(position));
			
		}
		else {
			fileView=(FileInfoLayout)converView;
			fileView.setFileName(fileList.get(position).get_filename());
			fileView.setFileIcon(fileList.get(position).get_Icon());
			fileView.setFileUpdateTime(fileList.get(position).get_last_update_time());
			
		}
		return fileView;
	}
}

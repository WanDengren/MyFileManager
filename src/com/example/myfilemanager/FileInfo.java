package com.example.myfilemanager;

import android.graphics.drawable.Drawable;
/*�ļ�������Ϣ�� ���û��ȡ�ļ������ơ��޸�ʱ�䡢�Լ������ļ���ͼ��*/
public class FileInfo implements Comparable<FileInfo>{
	private String fileName="";
	private String fileLastUpdateTime="";
	private Drawable fileIcon=null;
	private boolean Selectable=true;

	public FileInfo(String name,String updatetime,Drawable Icon)
	{
		this.fileName=name;
		this.fileLastUpdateTime=updatetime;
		this.fileIcon=Icon;
	}
	public String get_filename()
	{
		return fileName;
	}
	public String get_last_update_time()
	{
		return fileLastUpdateTime;
	}
	public Drawable get_Icon()
	{
		return fileIcon;
	}
	public void set_fileName(String name)
	{
		this.fileName=name;
	}
	public void set_last_update_time(String update_time)
	{
		this.fileLastUpdateTime=update_time;
	}
	public void set_Icon(Drawable Icon)
	{
		this.fileIcon=Icon;
	}
	public boolean isSelectable()
	{
		return Selectable;
	}
	public void setSelectable(boolean select)
	{
		this.Selectable=select;
	}
	@Override
	public int compareTo(FileInfo anotherfile)
	{
		if(this.fileName!=null&&anotherfile!=null)
		{
			return this.fileName.compareTo(anotherfile.get_filename());
		}
		else
			throw new IllegalArgumentException("�ļ������ڣ�");
		
	}
}

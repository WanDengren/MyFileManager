package com.example.myfilemanager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;


/*程序的主屏幕*/
public class ContentActivity extends Activity {
	//文件列表的视图
	private ListView fileListView = null;
	//文件信息的列表
	private List<FileInfo> fileList = new ArrayList<FileInfo>();
	//当前路径的目录
	private File currentDirectory = new File("/");
	//复制或剪切时记录
	private File tmpFile =null;
	//判断粘贴时是剪切还是复制
	private boolean isCut = false;
	
	//菜单
	private final int menu_new = 0;
	private final int menu_del = 1;
	private final int menu_paste = 2;
	private final int menu_root = 3;
	private final int menu_uplevel = 4;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		//menu.add(0,ADD_ID,1, R.string.menu_add);
		//第一个参数表示菜单项的一个分组号，第二个表示该菜单项的ID，最后一个表示应用的字符串资源的id，这里不太清楚第三个参数的意义是什么。
		menu.add(0, menu_new, 0, "新建文件夹");//.setIcon(this.getResources().getDrawable(R.drawable.addfolderr));
		menu.add(0, menu_del, 0, "删除目录");// .setIcon(R.drawable.delete);
		menu.add(0, menu_paste, 0, "粘贴");// .setIcon(R.drawable.paste);
		menu.add(0, menu_root, 0, "根目录");// .setIcon(R.drawable.goroot);
		menu.add(0, menu_uplevel, 0, "返回");// .setIcon(R.drawable.uponelevel);
		//menu.setGroupEnabled(2,false);
		return true;
	}

	/** Called when the activity is first created. */
	// @SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.main);
		MainLayout mainlayout = new MainLayout(this);
		mainlayout.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		setContentView(mainlayout);
		fileListView = mainlayout.getFileListView();
		//创建根目录
		creatRootFile();
		
		//文件单击响应事件
		fileListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String selectedFileString = fileList.get(position)
						.get_filename();
				if (selectedFileString.equals(getString(R.string.current_dir))) {
					openTheFile(currentDirectory);
				} else if (selectedFileString
						.equals(getString(R.string.up_one_level))) {
					upLevel();
				} else {
					File selectedFile = null;
					String pathString = currentDirectory.getAbsolutePath();
					if (!currentDirectory.equals("/")) {
						pathString += "/";
					}
					selectedFile = new File(pathString + selectedFileString);
					if (selectedFile != null) {
						openTheFile(selectedFile);
					}
				}

			}
		});
		//文件长按事件的处理
		fileListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				String selectedFileString = fileList.get(position)
						.get_filename();
				if (selectedFileString.equals(getString(R.string.current_dir))) {
					openTheFile(currentDirectory);
				} else if (selectedFileString
						.equals(getString(R.string.up_one_level))) {
					upLevel();
				} else {
					File selectedFile = null;
					String pathString = currentDirectory.getAbsolutePath();
					if (currentDirectory.getParent() != null) {
						pathString += "/";
					}
					selectedFile = new File(pathString
							+ fileList.get(position).get_filename());
					if (selectedFile != null)
						longClickOpenTheFile(selectedFile);

				}
				return true;
			}
		});

	}
	//检查文件类型
	public boolean checkFileType(String fileName, String[] extendNames) {
		for (String aEnd : extendNames) {
			if (fileName.endsWith(aEnd))
				return true;
		}
		return false;
	}
	//根据获得的文件目录设置文件信息列表的每一项
	public void fillListView(File[] files) {
		this.fileList.clear();

		fileList.add(new FileInfo(this.getString(R.string.current_dir), "", this
				.getResources().getDrawable(R.drawable.folder)));
		if (this.currentDirectory.getParent() != null) {
			fileList.add(new FileInfo(this.getString(R.string.up_one_level), "",
					this.getResources().getDrawable(R.drawable.uponelevel)));
		}
		Drawable icon = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		List<FileInfo> foldList = new ArrayList<FileInfo>();
		List<FileInfo> othersList = new ArrayList<FileInfo>();
		for (File file : files) {
			String fileName = file.getName();
			Date date = new Date(file.lastModified());
			String updateTime = dateFormat.format(date);

			if (file.isDirectory()) {
				icon = getResources().getDrawable(R.drawable.folder);
				foldList.add(new FileInfo(fileName, updateTime, icon));
			} else {
				if (checkFileType(fileName,
						getResources().getStringArray(R.array.fileEndingAudio))) {
					icon = getResources().getDrawable(R.drawable.audio);

				} else if (checkFileType(fileName, getResources()
						.getStringArray(R.array.fileEndingImage))) {
					icon = getResources()
							.getDrawable(R.drawable.image);
				} else if (checkFileType(fileName, getResources()
						.getStringArray(R.array.fileEndingVideo))) {
					icon = getResources().getDrawable(R.drawable.video);

				} else if (checkFileType(fileName, getResources()
						.getStringArray(R.array.fileEndingPackage))) {
					icon = getResources().getDrawable(R.drawable.packed);
				} else if (checkFileType(fileName, getResources()
						.getStringArray(R.array.fileEndingWebText))) {
					icon = getResources().getDrawable(R.drawable.webtext);
				} else {

					icon = getResources().getDrawable(R.drawable.text);
				}
				othersList.add(new FileInfo(fileName, updateTime, icon));
			}

		}
		//按字母排序
		Collections.sort(foldList);
		Collections.sort(othersList);

		for (int i = 0; i < foldList.size(); i++) {
			fileList.add(foldList.get(i));

		}
		for (int i = 0; i < othersList.size(); i++) {
			fileList.add(othersList.get(i));
		}
		FileInfoAdapter adapter = new FileInfoAdapter(this);
		adapter.setFileList(fileList);
		fileListView.setAdapter(adapter);

	}

	public void openTheFile(File file) {

		if (file.isDirectory()) {
			this.currentDirectory = file;
			this.setTitle(file.getAbsolutePath());
			fillListView(file.listFiles());
		} else {
			openFile(file);
		}
	}
	//长按事件的处理
	public void longClickOpenTheFile(File file) {
		if (file.isDirectory()) {
			this.currentDirectory = file;
			this.setTitle(file.getAbsolutePath());
			fillListView(file.listFiles());
		} else {
			openFileOperateMenu(file);
		}
	}
	//创建根目录
	public void creatRootFile() {
		openTheFile(new File("/"));
	}
	//返回上一级
	public void upLevel() {
		if (this.currentDirectory.getParent() != null) {
			openTheFile(this.currentDirectory.getParentFile());
		}
	}

	//长按后弹出选择对话框
	public void openFileOperateMenu(File file) {
		String[] menu = { "打开", "删除", "复制", "剪切", "重命名", "属性" };
		new AlertDialog.Builder(ContentActivity.this).setTitle("请选择操作：")
				.setItems(menu, new FileClickListener(file)).show();
	}

	//重载返回键操作
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if (currentDirectory.getParent() == null)
				ContentActivity.this.finish();
			else {
				upLevel();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	//显示提示信息对话框
	public void showMessageDialog(String title, String message) {
		Builder builder = new Builder(this);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton(android.R.string.ok,
				new AlertDialog.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		builder.setCancelable(false);
		builder.create();
		builder.show();
	}
	
    public void showMessageDialog(String title,View view){
    	Builder builder = new Builder(this);
    	builder.setTitle(title);
    	builder.setView(view);
    	builder.setPositiveButton(android.R.string.ok,
				new AlertDialog.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
    	builder.setCancelable(false);
    	builder.create();
    	builder.show();
    }
    //二次确认对话框
	public void confirmDialog(String title, String message,
			DialogInterface.OnClickListener positiveButtonEventHanddle,
			DialogInterface.OnClickListener negativeButtonEventHanddle) {
		Builder builder = new Builder(this);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton(android.R.string.ok,
				positiveButtonEventHanddle);
		builder.setNegativeButton(android.R.string.cancel,
				negativeButtonEventHanddle);
		builder.setCancelable(false);
		builder.create();
		builder.show();
	}
	//自定义对话框
	public void customDialog(String title, View view,
			DialogInterface.OnClickListener positiveButtonHanddle,
			DialogInterface.OnClickListener negativeButtonHanddle,
			DialogInterface.OnCancelListener cancelButtonHanddle) {
		Builder builder = new Builder(this);
		builder.setTitle(title);
		builder.setView(view);
		builder.setPositiveButton(android.R.string.ok, positiveButtonHanddle);
		builder.setNegativeButton(android.R.string.cancel,
				negativeButtonHanddle);
		builder.setOnCancelListener(cancelButtonHanddle);
		builder.show();
	}
	//删除文件或文件夹调用的方法
	public static void deleteAll(File file) throws IOException{
		if(!file.exists()){
			throw new IOException(file.getName()+"文件不存在");
		}
		boolean is_del=true;
		if(!(is_del=file.delete()))
		{
			File tmp[]=file.listFiles();
			for(int i=0;i<tmp.length;i++){
				if(tmp[i].isDirectory()){
					deleteAll(tmp[i]);			
				}
				is_del=tmp[i].delete();
				
			}
			is_del=file.delete();
			
		}
		if(!is_del)
		{
			throw new IOException("无法删除"+file.getName());
		}
	}
	//移动文件
	public static void moveFile(String source,String destination){
		new File(source).renameTo(new File(destination));
	}
	public static void moveFile(File source,File destination){
		source.renameTo(destination);
	}
	//粘贴文件的流操作
	public static void copyFile(File source,File destination){
		InputStream input =null;
		OutputStream output =null;
		BufferedInputStream byteInput=null;
		BufferedOutputStream byteOutput =null;
		try{
			input = new FileInputStream(source);
			output = new FileOutputStream(destination);
			byteInput = new BufferedInputStream(input);
			byteOutput = new BufferedOutputStream(output);
			byte[] b = new byte[8192];
			int len =byteInput.read(b);
			while(len!=-1){
				byteOutput.write(b,0,len);
				len= byteInput.read(b);
				
			}
		}
		catch(FileNotFoundException a){
			a.printStackTrace();
			
		}
		catch(IOException c){
			c.printStackTrace();
		}
		finally{
			try{
				if(byteInput!=null){
					byteInput.close();
				}
				if(byteOutput!=null){
					byteOutput.close();
				}
			}
			catch(IOException d){
				d.printStackTrace();		
			}
		}
	}
	//打开指定文件
	protected void openFile(File file) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// 设置intent的Action属性
		intent.setAction(Intent.ACTION_VIEW);
	
		MimeTypeMap aMap = MimeTypeMap.getSingleton();
		String fileName = file.getName();
		int index=0;
		for(int i=fileName.length()-1;i>=0;i--)
		{
			if(fileName.charAt(i)=='.'){
				
				index=i+1;
				break;
			}
		}
		String extensionString=fileName.substring(index);
		//System.out.println(extensionString);
		// 获取文件file的MIME类型
		String type=aMap.getMimeTypeFromExtension(extensionString);
		// 设置intent的data和Type属性
		intent.setDataAndType(/* uri */Uri.fromFile(file), type);
		// 跳转
		startActivity(intent);
	}
	//重命名
	private void renameFile(final File file){
		final DialogLayout renamedDialogLayout = new DialogLayout(this);
		renamedDialogLayout.getTextView().setText("请输入新的文件名");
		renamedDialogLayout.getEditText().setText(file.getName());
		this.customDialog("重命名", renamedDialogLayout, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO 自动生成的方法存根
				String newFileNameString = renamedDialogLayout.getEditText().getText().toString();
				if(!newFileNameString.equals(file.getName())){
					String nowDirectory = currentDirectory.getAbsolutePath();
					if(!nowDirectory.equals("/")){
						nowDirectory+="/";
					}
					final String allNameString = nowDirectory+ newFileNameString;
					if(new File(allNameString).exists()){
						confirmDialog("重命名", "文件名重复，是否覆盖？", 
						new DialogInterface.OnClickListener() {
						
							public void onClick(DialogInterface dialog,int which) {
								// TODO 自动生成的方法存根
								boolean flag = file.renameTo(new File(allNameString));
								if(flag){
									openTheFile(currentDirectory);
								}
								else{
								showMessageDialog("重命名", "重命名失败");
								}
								
							}
						}, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO 自动生成的方法存根
								dialog.cancel();
							}
						});
						
					}
					else{
						boolean flag = file.renameTo(new File(allNameString));
						if(flag){
							openTheFile(currentDirectory);
						}
						else {
							showMessageDialog("重命名", "重命名失败");
						}
					}
				}
				
			}
		}, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO 自动生成的方法存根
				dialog.cancel();
				
			}
		}, new DialogInterface.OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO 自动生成的方法存根
				dialog.cancel();
			}
		});
	}
	//删除文件
	private void deleteFile(final File file){
		this.confirmDialog("删除文件", "确定要删除吗？", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO 自动生成的方法存根
				try{
					deleteAll(file);
					showMessageDialog("删除文件：", "删除文件成功！");
					openTheFile(currentDirectory);
				}
				catch(IOException e){
					e.printStackTrace();
					showMessageDialog("删除文件：", "删除文件："+file.getName()+"失败！");
				}
			}
		}, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO 自动生成的方法存根
				dialog.cancel();
			}
		});
		
	}
	//显示指定文件详细信息
	private void displayFileInfo(File file) {
		FileDetailInfoLayout layout = new FileDetailInfoLayout(this);
		String pathString = file.getAbsolutePath();

		layout.getPathTextView().setText("位置：" + pathString);
		long size = file.length();
		String sizeString = String.valueOf(size);
		layout.getSizeTextView().setText("大小：" + sizeString + "B");
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		Date date = new Date(file.lastModified());
		String updateTime = dateFormat.format(date);

		layout.getUpdateTimeTextView().setText("最后修改时间：" + updateTime);
		if (file.canRead())
			layout.getCanReadTextView().setText("可读：是");
		else
			layout.getCanReadTextView().setText("可读：否");
		if (file.canWrite())
			layout.getCanWriteTextView().setText("可写：是");
		else {
			layout.getCanWriteTextView().setText("可写：否");
		}
		if (file.isHidden())
			layout.getIsHiddenTextView().setText("隐藏：否");
		else {
			layout.getIsHiddenTextView().setText("隐藏：否");
		}
		this.showMessageDialog(file.getName(), layout);

	}
	//定义内部类继承对话框的点击事件接口
	public class FileClickListener implements DialogInterface.OnClickListener{
		File file;
		public FileClickListener(File selectedFile){
				file = selectedFile;
		}
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO 自动生成的方法存根
			if(which==0){
				openFile(file);
			}
			else if(which==1){
				deleteFile(file);
			}
			else if(which==2){
				tmpFile=file;
				isCut=false;
			}
			else if(which==3){
				tmpFile=file;
				isCut=true;
				
			}
			else if(which==4){
				renameFile(file);
			}
			else if(which==5){
				displayFileInfo(file);
			}
		}
		
	}
	//创建新目录
	private void creatFile(){
		final DialogLayout layout = new DialogLayout(this);
		layout.getTextView().setText("请输入目录名：");
		this.customDialog("新建目录", layout, 
				new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						// TODO 自动生成的方法存根
						String newFileName= layout.getEditText().getText().toString();
						String pathString = currentDirectory.getAbsolutePath();
						if(!pathString.equals("/")){
							pathString+="/";
						}
						pathString+=newFileName;
						final File file = new File(pathString);
						if(file.exists()){
							showMessageDialog("新建目录", "目录已存在");
						}
						else{
							boolean creat= file.mkdirs();
							if(creat){
								showMessageDialog("新建目录", "创建目录成功！");
								openTheFile(currentDirectory);
							}
							else{
								showMessageDialog("新建目录", "创建目录"+newFileName+"失败！");
							}
						}
						
					}
				}, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO 自动生成的方法存根
						dialog.cancel();
					}
				}, new DialogInterface.OnCancelListener() {
					
					@Override
					public void onCancel(DialogInterface dialog) {
						// TODO 自动生成的方法存根
						dialog.cancel();
					}
				});
	}
	//删除当前目录
	public void deleteCatalog(){
		final File nowDirectory = new File(this.currentDirectory.getAbsolutePath());
		
		if(nowDirectory.getName().equals("/")){
			showMessageDialog("删除目录", "根目录,无法删除");
		}
		else{
			final File parentFile = nowDirectory.getParentFile();
			this.confirmDialog("删除目录", "确定要删除当前目录吗？",
					new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO 自动生成的方法存根
							try{
								deleteAll(nowDirectory);
								showMessageDialog("删除目录", "删除目录成功!");
								currentDirectory= parentFile;
								openTheFile(currentDirectory);
							}
							catch(IOException e){
								e.printStackTrace();
								showMessageDialog("删除目录", "删除失败！");
							}
						}
					}, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO 自动生成的方法存根
							dialog.cancel();
						}
					});
			
		}
 	}
	//粘贴文件
	public void pastFile() {
		if (tmpFile == null) {
			this.showMessageDialog("提示", "没有要粘贴的文件");
		} else {
			String nowDirectory = currentDirectory.getAbsolutePath();
			if (!nowDirectory.equals("/")) {
				nowDirectory += "/";
			}
			final File targetFile = new File(nowDirectory + tmpFile.getName());
			if (!isCut) {
				if (targetFile.exists()) {
					this.confirmDialog("复制", "存在相同名称文件，是否覆盖？",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO 自动生成的方法存根
									copyFile(tmpFile, targetFile);
									showMessageDialog("复制", "复制文件成功！");
									openTheFile(currentDirectory);
								}
							}, new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO 自动生成的方法存根
									dialog.cancel();
								}
							});
				} else {
					copyFile(tmpFile, targetFile);
					showMessageDialog("复制", "复制文件成功！");
					openTheFile(currentDirectory);
				}

			} else {
				if (targetFile.exists()) {
					this.confirmDialog("剪切", "存在相同名称文件，是否覆盖？",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO 自动生成的方法存根
									moveFile(tmpFile, targetFile);
									showMessageDialog("剪切", "剪切文件成功！");
									openTheFile(currentDirectory);
									tmpFile = null;

								}
							}, new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO 自动生成的方法存根
									dialog.cancel();
								}
							});
				} else {
					moveFile(tmpFile, targetFile);
					showMessageDialog("剪切", "剪切文件成功！");
					openTheFile(currentDirectory);
					tmpFile = null;

				}
			}
		}
	}
	//菜单选择处理
	public boolean onOptionsItemSelected(MenuItem menuItem){
		super.onOptionsItemSelected(menuItem);
		switch (menuItem.getItemId()) {
		case menu_new:
			creatFile();
			break;
		case menu_del:
			deleteCatalog();
			break;
		case menu_paste:
			pastFile();
			break;
		case menu_root:
			this.creatRootFile();
			break;
		case menu_uplevel:
			upLevel();
			break;
		default:
			break;
		}
		return true;
	}
}

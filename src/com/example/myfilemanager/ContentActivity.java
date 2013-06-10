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


/*���������Ļ*/
public class ContentActivity extends Activity {
	//�ļ��б����ͼ
	private ListView fileListView = null;
	//�ļ���Ϣ���б�
	private List<FileInfo> fileList = new ArrayList<FileInfo>();
	//��ǰ·����Ŀ¼
	private File currentDirectory = new File("/");
	//���ƻ����ʱ��¼
	private File tmpFile =null;
	//�ж�ճ��ʱ�Ǽ��л��Ǹ���
	private boolean isCut = false;
	
	//�˵�
	private final int menu_new = 0;
	private final int menu_del = 1;
	private final int menu_paste = 2;
	private final int menu_root = 3;
	private final int menu_uplevel = 4;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		//menu.add(0,ADD_ID,1, R.string.menu_add);
		//��һ��������ʾ�˵����һ������ţ��ڶ�����ʾ�ò˵����ID�����һ����ʾӦ�õ��ַ�����Դ��id�����ﲻ̫���������������������ʲô��
		menu.add(0, menu_new, 0, "�½��ļ���");//.setIcon(this.getResources().getDrawable(R.drawable.addfolderr));
		menu.add(0, menu_del, 0, "ɾ��Ŀ¼");// .setIcon(R.drawable.delete);
		menu.add(0, menu_paste, 0, "ճ��");// .setIcon(R.drawable.paste);
		menu.add(0, menu_root, 0, "��Ŀ¼");// .setIcon(R.drawable.goroot);
		menu.add(0, menu_uplevel, 0, "����");// .setIcon(R.drawable.uponelevel);
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
		//������Ŀ¼
		creatRootFile();
		
		//�ļ�������Ӧ�¼�
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
		//�ļ������¼��Ĵ���
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
	//����ļ�����
	public boolean checkFileType(String fileName, String[] extendNames) {
		for (String aEnd : extendNames) {
			if (fileName.endsWith(aEnd))
				return true;
		}
		return false;
	}
	//���ݻ�õ��ļ�Ŀ¼�����ļ���Ϣ�б��ÿһ��
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
		//����ĸ����
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
	//�����¼��Ĵ���
	public void longClickOpenTheFile(File file) {
		if (file.isDirectory()) {
			this.currentDirectory = file;
			this.setTitle(file.getAbsolutePath());
			fillListView(file.listFiles());
		} else {
			openFileOperateMenu(file);
		}
	}
	//������Ŀ¼
	public void creatRootFile() {
		openTheFile(new File("/"));
	}
	//������һ��
	public void upLevel() {
		if (this.currentDirectory.getParent() != null) {
			openTheFile(this.currentDirectory.getParentFile());
		}
	}

	//�����󵯳�ѡ��Ի���
	public void openFileOperateMenu(File file) {
		String[] menu = { "��", "ɾ��", "����", "����", "������", "����" };
		new AlertDialog.Builder(ContentActivity.this).setTitle("��ѡ�������")
				.setItems(menu, new FileClickListener(file)).show();
	}

	//���ط��ؼ�����
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
	//��ʾ��ʾ��Ϣ�Ի���
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
    //����ȷ�϶Ի���
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
	//�Զ���Ի���
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
	//ɾ���ļ����ļ��е��õķ���
	public static void deleteAll(File file) throws IOException{
		if(!file.exists()){
			throw new IOException(file.getName()+"�ļ�������");
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
			throw new IOException("�޷�ɾ��"+file.getName());
		}
	}
	//�ƶ��ļ�
	public static void moveFile(String source,String destination){
		new File(source).renameTo(new File(destination));
	}
	public static void moveFile(File source,File destination){
		source.renameTo(destination);
	}
	//ճ���ļ���������
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
	//��ָ���ļ�
	protected void openFile(File file) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// ����intent��Action����
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
		// ��ȡ�ļ�file��MIME����
		String type=aMap.getMimeTypeFromExtension(extensionString);
		// ����intent��data��Type����
		intent.setDataAndType(/* uri */Uri.fromFile(file), type);
		// ��ת
		startActivity(intent);
	}
	//������
	private void renameFile(final File file){
		final DialogLayout renamedDialogLayout = new DialogLayout(this);
		renamedDialogLayout.getTextView().setText("�������µ��ļ���");
		renamedDialogLayout.getEditText().setText(file.getName());
		this.customDialog("������", renamedDialogLayout, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO �Զ����ɵķ������
				String newFileNameString = renamedDialogLayout.getEditText().getText().toString();
				if(!newFileNameString.equals(file.getName())){
					String nowDirectory = currentDirectory.getAbsolutePath();
					if(!nowDirectory.equals("/")){
						nowDirectory+="/";
					}
					final String allNameString = nowDirectory+ newFileNameString;
					if(new File(allNameString).exists()){
						confirmDialog("������", "�ļ����ظ����Ƿ񸲸ǣ�", 
						new DialogInterface.OnClickListener() {
						
							public void onClick(DialogInterface dialog,int which) {
								// TODO �Զ����ɵķ������
								boolean flag = file.renameTo(new File(allNameString));
								if(flag){
									openTheFile(currentDirectory);
								}
								else{
								showMessageDialog("������", "������ʧ��");
								}
								
							}
						}, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO �Զ����ɵķ������
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
							showMessageDialog("������", "������ʧ��");
						}
					}
				}
				
			}
		}, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO �Զ����ɵķ������
				dialog.cancel();
				
			}
		}, new DialogInterface.OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO �Զ����ɵķ������
				dialog.cancel();
			}
		});
	}
	//ɾ���ļ�
	private void deleteFile(final File file){
		this.confirmDialog("ɾ���ļ�", "ȷ��Ҫɾ����", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO �Զ����ɵķ������
				try{
					deleteAll(file);
					showMessageDialog("ɾ���ļ���", "ɾ���ļ��ɹ���");
					openTheFile(currentDirectory);
				}
				catch(IOException e){
					e.printStackTrace();
					showMessageDialog("ɾ���ļ���", "ɾ���ļ���"+file.getName()+"ʧ�ܣ�");
				}
			}
		}, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO �Զ����ɵķ������
				dialog.cancel();
			}
		});
		
	}
	//��ʾָ���ļ���ϸ��Ϣ
	private void displayFileInfo(File file) {
		FileDetailInfoLayout layout = new FileDetailInfoLayout(this);
		String pathString = file.getAbsolutePath();

		layout.getPathTextView().setText("λ�ã�" + pathString);
		long size = file.length();
		String sizeString = String.valueOf(size);
		layout.getSizeTextView().setText("��С��" + sizeString + "B");
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		Date date = new Date(file.lastModified());
		String updateTime = dateFormat.format(date);

		layout.getUpdateTimeTextView().setText("����޸�ʱ�䣺" + updateTime);
		if (file.canRead())
			layout.getCanReadTextView().setText("�ɶ�����");
		else
			layout.getCanReadTextView().setText("�ɶ�����");
		if (file.canWrite())
			layout.getCanWriteTextView().setText("��д����");
		else {
			layout.getCanWriteTextView().setText("��д����");
		}
		if (file.isHidden())
			layout.getIsHiddenTextView().setText("���أ���");
		else {
			layout.getIsHiddenTextView().setText("���أ���");
		}
		this.showMessageDialog(file.getName(), layout);

	}
	//�����ڲ���̳жԻ���ĵ���¼��ӿ�
	public class FileClickListener implements DialogInterface.OnClickListener{
		File file;
		public FileClickListener(File selectedFile){
				file = selectedFile;
		}
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO �Զ����ɵķ������
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
	//������Ŀ¼
	private void creatFile(){
		final DialogLayout layout = new DialogLayout(this);
		layout.getTextView().setText("������Ŀ¼����");
		this.customDialog("�½�Ŀ¼", layout, 
				new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						// TODO �Զ����ɵķ������
						String newFileName= layout.getEditText().getText().toString();
						String pathString = currentDirectory.getAbsolutePath();
						if(!pathString.equals("/")){
							pathString+="/";
						}
						pathString+=newFileName;
						final File file = new File(pathString);
						if(file.exists()){
							showMessageDialog("�½�Ŀ¼", "Ŀ¼�Ѵ���");
						}
						else{
							boolean creat= file.mkdirs();
							if(creat){
								showMessageDialog("�½�Ŀ¼", "����Ŀ¼�ɹ���");
								openTheFile(currentDirectory);
							}
							else{
								showMessageDialog("�½�Ŀ¼", "����Ŀ¼"+newFileName+"ʧ�ܣ�");
							}
						}
						
					}
				}, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO �Զ����ɵķ������
						dialog.cancel();
					}
				}, new DialogInterface.OnCancelListener() {
					
					@Override
					public void onCancel(DialogInterface dialog) {
						// TODO �Զ����ɵķ������
						dialog.cancel();
					}
				});
	}
	//ɾ����ǰĿ¼
	public void deleteCatalog(){
		final File nowDirectory = new File(this.currentDirectory.getAbsolutePath());
		
		if(nowDirectory.getName().equals("/")){
			showMessageDialog("ɾ��Ŀ¼", "��Ŀ¼,�޷�ɾ��");
		}
		else{
			final File parentFile = nowDirectory.getParentFile();
			this.confirmDialog("ɾ��Ŀ¼", "ȷ��Ҫɾ����ǰĿ¼��",
					new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO �Զ����ɵķ������
							try{
								deleteAll(nowDirectory);
								showMessageDialog("ɾ��Ŀ¼", "ɾ��Ŀ¼�ɹ�!");
								currentDirectory= parentFile;
								openTheFile(currentDirectory);
							}
							catch(IOException e){
								e.printStackTrace();
								showMessageDialog("ɾ��Ŀ¼", "ɾ��ʧ�ܣ�");
							}
						}
					}, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO �Զ����ɵķ������
							dialog.cancel();
						}
					});
			
		}
 	}
	//ճ���ļ�
	public void pastFile() {
		if (tmpFile == null) {
			this.showMessageDialog("��ʾ", "û��Ҫճ�����ļ�");
		} else {
			String nowDirectory = currentDirectory.getAbsolutePath();
			if (!nowDirectory.equals("/")) {
				nowDirectory += "/";
			}
			final File targetFile = new File(nowDirectory + tmpFile.getName());
			if (!isCut) {
				if (targetFile.exists()) {
					this.confirmDialog("����", "������ͬ�����ļ����Ƿ񸲸ǣ�",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO �Զ����ɵķ������
									copyFile(tmpFile, targetFile);
									showMessageDialog("����", "�����ļ��ɹ���");
									openTheFile(currentDirectory);
								}
							}, new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO �Զ����ɵķ������
									dialog.cancel();
								}
							});
				} else {
					copyFile(tmpFile, targetFile);
					showMessageDialog("����", "�����ļ��ɹ���");
					openTheFile(currentDirectory);
				}

			} else {
				if (targetFile.exists()) {
					this.confirmDialog("����", "������ͬ�����ļ����Ƿ񸲸ǣ�",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO �Զ����ɵķ������
									moveFile(tmpFile, targetFile);
									showMessageDialog("����", "�����ļ��ɹ���");
									openTheFile(currentDirectory);
									tmpFile = null;

								}
							}, new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO �Զ����ɵķ������
									dialog.cancel();
								}
							});
				} else {
					moveFile(tmpFile, targetFile);
					showMessageDialog("����", "�����ļ��ɹ���");
					openTheFile(currentDirectory);
					tmpFile = null;

				}
			}
		}
	}
	//�˵�ѡ����
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

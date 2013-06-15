package com.example.myfilemanager;

import java.io.File;

import android.app.ListActivity;
import android.view.ContextMenu;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.database.Cursor;
import android.os.Bundle;
import android.content.ContentResolver;
import android.content.Intent;
import android.provider.MediaStore;
import android.widget.SimpleCursorAdapter;
import android.widget.ListView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.app.Dialog;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.app.ProgressDialog;
import android.net.Uri;
import android.widget.Toast;
import android.content.ContentUris;


public class VedioActivity extends ListActivity implements
		OnCreateContextMenuListener {

	private Cursor cursor;
	private static final int OPTION_ITEM_EXIT = 1;
	private static final int OPTION_ITEM_SCAN = 2;
	private static final int SHOW_EXIT_DIALOG = 0;
	private static final int SHOW_SCAN_DIALOG = 1;
	private static final int SHOW_DELETE_DIALOG = 2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置界面布局
		setContentView(R.layout.songs);
		ContentResolver resolver = getContentResolver();
		// 从Content Provider中获得SD卡上的音乐列表
		cursor = resolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
				null, null, null, MediaStore.Video.Media.DEFAULT_SORT_ORDER);
		String[] cols = new String[] { MediaStore.Video.Media.TITLE,
				MediaStore.Video.Media.ARTIST, };
		int[] ids = new int[] { R.id.trackname, R.id.artist };
		if (cursor != null)
			startManagingCursor(cursor);
		// 创建Adapter并绑定到ListView
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				R.layout.song_list, cursor, cols, ids);
		registerForContextMenu(getListView());
		setListAdapter(adapter);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		int id = item.getItemId();
		switch (id) {
		case R.id.action_settings:
			break;
		case R.id.ctx_delete:
			cursor.moveToPosition(info.position);
			showDialog(SHOW_DELETE_DIALOG);
		case R.id.ctx_property:
			break;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// 从res/menu/main.xml创建Menu
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.main, menu);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case SHOW_EXIT_DIALOG:
			return new AlertDialog.Builder(this)
					.setTitle(R.string.exit_titile)
					.setNegativeButton(R.string.button_cancel,
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									dismissDialog(SHOW_EXIT_DIALOG);
								}

							})
					.setPositiveButton(R.string.button_ok,
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									finish();
								}

							}).create();
		case SHOW_SCAN_DIALOG:
			ProgressDialog dialog = new ProgressDialog(this);
			dialog.setMessage(getText(R.string.scan_message));
			dialog.setIndeterminate(true);
			dialog.setButton(getText(R.string.button_cancel),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dismissDialog(SHOW_SCAN_DIALOG);
						}

					});
			return dialog;
		case SHOW_DELETE_DIALOG:
			return new AlertDialog.Builder(this)
					.setTitle(R.string.delete_message)
					.setNegativeButton(R.string.button_cancel,
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									dismissDialog(SHOW_DELETE_DIALOG);
								}

							})
					.setPositiveButton(R.string.button_ok,
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									ContentResolver resolver = getContentResolver();
									int songId = cursor.getInt(cursor
											.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
									String path = cursor.getString(cursor
											.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
									// 获得指定id歌曲的Uri
									Uri ringUri = ContentUris
											.withAppendedId(
													MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
													songId);
									// 删除数据库中的记录
									resolver.delete(ringUri, null, null);
									// 删除SD卡上的文件
									File file = new File(path);
									if (file.exists())	
										file.delete();
									// 通知用户歌曲已经被删除
									Toast.makeText(VedioActivity.this,
											R.string.file_deleted,
											Toast.LENGTH_SHORT).show();
								}

							}).create();

		}
		return null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// 增加"退出"菜单
		menu.add(0, OPTION_ITEM_EXIT, 0, R.string.option_exit);
		// 增加"扫描"菜单
		menu.add(0, OPTION_ITEM_SCAN, 1, R.string.option_scan);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		switch (itemId) {
		case OPTION_ITEM_EXIT:
			showDialog(SHOW_EXIT_DIALOG);
			break;
		case OPTION_ITEM_SCAN:
			showDialog(SHOW_SCAN_DIALOG);
			break;
		}
		return true;
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		cursor.moveToPosition(position);
		String title = cursor.getString(cursor
				.getColumnIndex(MediaStore.Video.Media.TITLE));
		Toast.makeText(this, title, Toast.LENGTH_SHORT).show();
		String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
		File afile = new File(path);
		openFile(afile);
		
	}
	
	protected void openFile(File aFile)
	{
		Intent intent = new Intent();
		intent.setAction(android.content.Intent.ACTION_VIEW);
		File file = new File(aFile.getAbsolutePath());
		// 取得文件名
		String fileName = file.getName();
		// 根据不同的文件类型来打开文件

			intent.setDataAndType(Uri.fromFile(file), "application/*");
			
		startActivity(intent);
	}
}
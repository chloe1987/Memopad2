package sample.application.memopad;

import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.content.Intent;
import android.view.MenuInflater;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.app.AlertDialog;
import android.content.DialogInterface;



public class Memolist extends ListActivity {
	
	public static final String[] cols= {"title","memo",android.provider.BaseColumns._ID,};
	public MemoDBHelper memos;

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		super.onListItemClick(l, v, position, id);
		memos=new MemoDBHelper(this);
		SQLiteDatabase db=memos.getWritableDatabase();
		Cursor cursor=db.query("memoDB",cols,"_ID="+String.valueOf(id),null,null, null, null);
		startManagingCursor(cursor);
		int idx=cursor.getColumnIndex("memo");
		cursor.moveToFirst();
		Intent i=new Intent();
		i.putExtra("text", cursor.getString(idx));
		setResult(RESULT_OK,i);
		memos.close();
		finish();
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.memolist);
		this.showMemos(getMemos());
		
		ListView lv = (ListView) this.findViewById(android.R.id.list);
		this.registerForContextMenu(lv);  
	
	}

	private Cursor getMemos() {
		memos=new MemoDBHelper(this);
		SQLiteDatabase db=memos.getReadableDatabase();
		Cursor cursor=db.query("memoDB",cols,null,null,null, null, null);
		startManagingCursor(cursor);
		return cursor;

					
		}


	private void showMemos(Cursor cursor) {
		if(cursor!=null){
			String[] from={"title"};
			int[] to={android.R.id.text1};
			SimpleCursorAdapter adapter=new SimpleCursorAdapter(
					this, android.R.layout.simple_list_item_1,
					cursor, from, to);
			setListAdapter(adapter);
		}
		this.memos.close();
		
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO 自動生成されたメソッド・スタブ
		AdapterContextMenuInfo info =
				(AdapterContextMenuInfo) item.getMenuInfo();
		Cursor cursor=getMemos();
		startManagingCursor(cursor);
		cursor.moveToPosition(info.position);
		final int columnid=cursor.getInt(2);
		
		AlertDialog.Builder ab=new AlertDialog.Builder(this);
		ab.setTitle(R.string.memodb_delete);
		ab.setMessage(R.string.memodb_confirm_delete);
		ab.setPositiveButton(R.string.button_ok,
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				SQLiteDatabase db=memos.getWritableDatabase();
				db.delete("memoDB", "_id="+columnid, null);
				db.close();
				showMemos(getMemos());
				
			}
		});
		ab.setNegativeButton(R.string.button_cancel,
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which){
				
			}
		});
		ab.show();
		
		return super.onContextItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO 自動生成されたメソッド・スタブ
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater mi=getMenuInflater();
		mi.inflate(R.menu.contextmenu, menu);
		}

		
	}


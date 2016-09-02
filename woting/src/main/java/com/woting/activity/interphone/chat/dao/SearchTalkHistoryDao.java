package com.woting.activity.interphone.chat.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.woting.activity.interphone.chat.model.DBTalkHistorary;
import com.woting.common.database.SqliteHelper;
import com.woting.util.CommonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 对聊天历史列表的操作
 * @author 辛龙
 *2016年1月15日
 */
public class SearchTalkHistoryDao {
	private SqliteHelper helper;
	private Context context;

	//构造方法
	public SearchTalkHistoryDao(Context context) {
		helper = new SqliteHelper(context);
		this.context=context;
	}

	/**
	 * 插入搜索历史表一条数据
	 * @param history
	 */
	public void addTalkHistory(DBTalkHistorary history) {
		//通过helper的实现对象获取可操作的数据库db
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL("insert into talkhistory(bjuserid,type,id,addtime) values(?,?,?,?)",
				new Object[] {history.getBJUserId(),history.getTyPe(), history.getID(),history.getAddTime()});//sql语句
		db.close();//关闭数据库对象
	}


	/**
	 * 查询数据库里的数据，无参查询语句 供特定使用
	 * @return
	 */
	public List<DBTalkHistorary> queryHistory() {
		List<DBTalkHistorary> mylist = new ArrayList<DBTalkHistorary>();
		SQLiteDatabase db = helper.getReadableDatabase();
		String userid = CommonUtils.getUserId(context);
		Cursor cursor = null;
		try {
			cursor = db.rawQuery("Select * from talkhistory  where bjuserid=? order by addtime desc", new String[]{userid});
			while (cursor.moveToNext()) {
				String bjuserid = cursor.getString(1);
				String type = cursor.getString(2);
				String id = cursor.getString(3);
				String addtime = cursor.getString(4);
				//把每个对象都放到history对象里
				DBTalkHistorary h = new DBTalkHistorary(bjuserid,  type,  id, addtime);		
				mylist.add(h);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (db != null) {
				db.close();
			}
		}
		return mylist;
	}

	/**
	 * 删除数据库表中的数据
	 * @param id
	 * @return
	 */
	public void deleteHistory(String id) {
		SQLiteDatabase db = helper.getReadableDatabase();
		String userid = CommonUtils.getUserId(context);
		String uid = id;
		db.execSQL("Delete from talkhistory where id=? and bjuserid=?",
				new String[] { uid ,userid});
		db.close();
	}
}

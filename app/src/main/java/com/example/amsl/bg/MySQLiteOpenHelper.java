package com.example.amsl.bg;

/**
 * Created by amsl on 16/08/29.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    /** アクセスするデータベース名 */
    private static final String DB_NAME = "example.db";
    /** DBのバージョン */
    private static final int DB_VERSION = 1;
    /** create table文 */
    private static final String createTableString = "create table personal_data(" +
		"_id integer primary key autoincrement, name text, ave float," + 
		"result integer, delete_flg integer, count integer default 0," +
		"last_ave float, last_res integer, over_flg integer default 0," +
		create60seq("ave", "double") + ", " + create60seq("res", "integer") + ", " +
		create60seq("scr", "integer") + ")";

    public MySQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTableString);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO 自動生成されたメソッド・スタブ

    }

	private String create60seq(String str, String typ)
	{
		String rtn = "";

		for(int i = 0; i < 59; ++i){
			rtn += String.format("%s%d %s, ", str, i, typ);
		}
		rtn += String.format("%s%d %s", str, 59, typ);

		return rtn;
	}
}


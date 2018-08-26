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
    private static final String createTableString = "create table personal_data(_id integer primary key autoincrement, name text, ave float, result integer, delete_flg integer, count integer default 0," +
            "last_ave float, last_res integer, over_flg integer default 0," +
            "ave0 float, ave1 float, ave2 float, ave3 float, ave4 float, ave5 float, ave6 float, ave7 float, ave8 float, ave9 float, ave10 float, ave11 float, ave12 float, ave13 float, ave14 float," +
            "ave15 float, ave16 float, ave17 float, ave18 float, ave19 float, ave20 float, ave21 float, ave22 float, ave23 float, ave24 float, ave25 float, ave26 float, ave27 float, ave28 float, ave29 float," +
            "ave30 float, ave31 float, ave32 float, ave33 float, ave34 float, ave35 float, ave36 float, ave37 float, ave38 float, ave39 float, ave40 float, ave41 float, ave42 float, ave43 float, ave44 float," +
            "ave45 float, ave46 float, ave47 float, ave48 float, ave49 float, ave50 float, ave51 float, ave52 float, ave53 float, ave54 float, ave55 float, ave56 float, ave57 float, ave58 float, ave59 float," +
            "res0 integer, res1 integer, res2 integer, res3 integer, res4 integer, res5 integer, res6 integer, res7 integer, res8 integer, res9 integer," +
            "res10 integer, res11 integer, res12 integer, res13 integer, res14 integer, res15 integer, res16 integer, res17 integer, res18 integer, res19 integer," +
            "res20 integer, res21 integer, res22 integer, res23 integer, res24 integer, res25 integer, res26 integer, res27 integer, res28 integer, res29 integer," +
            "res30 integer, res31 integer, res32 integer, res33 integer, res34 integer, res35 integer, res36 integer, res37 integer, res38 integer, res39 integer," +
            "res40 integer, res41 integer, res42 integer, res43 integer, res44 integer, res45 integer, res46 integer, res47 integer, res48 integer, res49 integer," +
            "res50 integer, res51 integer, res52 integer, res53 integer, res54 integer, res55 integer, res56 integer, res57 integer, res58 integer, res59 integer," +
            "scr0 integer, scr1 integer, scr2 integer, scr3 integer, scr4 integer, scr5 integer, scr6 integer, scr7 integer, scr8 integer, scr9 integer," +
            "scr10 integer, scr11 integer, scr12 integer, scr13 integer, scr14 integer, scr15 integer, scr16 integer, scr17 integer, scr18 integer, scr19 integer," +
            "scr20 integer, scr21 integer, scr22 integer, scr23 integer, scr24 integer, scr25 integer, scr26 integer, scr27 integer, scr28 integer, scr29 integer," +
            "scr30 integer, scr31 integer, scr32 integer, scr33 integer, scr34 integer, scr35 integer, scr36 integer, scr37 integer, scr38 integer, scr39 integer," +
            "scr40 integer, scr41 integer, scr42 integer, scr43 integer, scr44 integer, scr45 integer, scr46 integer, scr47 integer, scr48 integer, scr49 integer," +
            "scr50 integer, scr51 integer, scr52 integer, scr53 integer, scr54 integer, scr55 integer, scr56 integer, scr57 integer, scr58 integer, scr59 integer)";

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

}

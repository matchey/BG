package com.example.amsl.bg;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.database.sqlite.SQLiteDatabase;
import android.database.SQLException;
import android.database.Cursor;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.util.Log;

import java.util.Locale;
import java.util.List;
import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity implements OnClickListener {

    /** データベース */
    private SQLiteDatabase db;

    public MySQLiteOpenHelper helper = new MySQLiteOpenHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showLast();
    }

    public void onClick(View view){
        switch (view.getId()) {
            case R.id.radioPersonal:
                initSpinners(initPesonal());
                break;
            case R.id.buttonDelete:
                // インスタンス作成
                //MySQLiteOpenHelper helper = new MySQLiteOpenHelper(this);
                // 読み書き出来るように開く
                db = helper.getWritableDatabase();

                AlertDialog.Builder alert03 = new AlertDialog.Builder(this);
                //ダイアログタイトルをセット
                //alert03.setTitle("ここにタイトルを設定");
                //ダイアログメッセージをセット
                alert03.setMessage("are you sure you want to delete?");
                // アラートダイアログのボタンがクリックされた時に呼び出されるコールバックリスナーを登録します
                alert03.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which) {
                        //OKボタンが押された時の処理
                        //Toast.makeText(HistoryActivity.this, "OK選択", Toast.LENGTH_LONG).show();
                        Spinner spinner = (Spinner)findViewById(R.id.spinnerPerson);
                        // 選択されているアイテムを取得
                        String item = (String)spinner.getSelectedItem();
                        spinner.setSelection(0);
                        //db.delete("personal_data", "name=?", new String[] { item });
                        String sql = String.format(Locale.getDefault(), "DELETE FROM personal_data WHERE (name = '%s')", item);
                        try {
                            db.execSQL(sql);
                            setContentView(R.layout.personal_history);
                            findViewById(R.id.radioPersonal).setOnClickListener(HistoryActivity.this);
                            findViewById(R.id.radioLast).setOnClickListener(HistoryActivity.this);
                            findViewById(R.id.buttonDelete).setOnClickListener(HistoryActivity.this);
                            findViewById(R.id.buttonClose).setOnClickListener(HistoryActivity.this);

                            // レコード1設定
                            ContentValues values = new ContentValues();
                            // レコードを検索してカーソルを作成
                            Cursor cursor = db.query("personal_data", new String[] { "_id", "name",
                            }, null, null, null, null, "name ASC");
                            List<String> rec_name = new ArrayList<String>();
                            while (cursor.moveToNext()) {
                                rec_name.add(cursor.getString(cursor.getColumnIndex("name")));
                            }
                            // カーソルクローズ
                            cursor.close();
                            initSpinners(rec_name);
                        } catch (SQLException e) {
                            Log.e("ERROR", e.toString());
                        }
                    }});
                // アラートダイアログのボタンがクリックされた時に呼び出されるコールバックリスナーを登録します
                alert03.setNegativeButton("NO", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which) {
                        //NOボタンが押された時の処理
                        //Toast.makeText(HistoryActivity.this, "NO選択", Toast.LENGTH_LONG).show();
                    }});
                alert03.show();
                // DBクローズ
                //db.close();
                // MySQLiteOpenHelperクローズ
                //helper.close();呼ぶとダメ！

                break;
            case R.id.radioLast:
                showLast();
                break;
            case R.id.buttonClose:
                finish();
                break;
            default:
                break;
        }
    }

    private void initSpinners(List<String> rec_name) {

        // Adapterの作成
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(HistoryActivity.this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Adapterにアイテムを追加
        adapter.add("select player");
        for(int i=0; i<rec_name.size(); i++){
            adapter.add(rec_name.get(i));
        }
        Spinner spinner = (Spinner)findViewById(R.id.spinnerPerson);
        // SpinnerにAdapterを設定
        spinner.setAdapter(adapter);
        //((Spinner) findViewById(R.id.spinnerPerson)).setSelection(0);
        spinner.setOnItemSelectedListener(new SpinnerSelectedListener());
    }
    public class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener{
        public void onItemSelected(AdapterView parent,View view, int position,long id) {

            //initPesonal();

            // Spinner を取得
            Spinner spinner = (Spinner) parent;
            // 選択されたアイテムのテキストを取得
            String name = spinner.getSelectedItem().toString();

            // インスタンス作成
            //MySQLiteOpenHelper helper = new MySQLiteOpenHelper(getApplication());
            // 読み書き出来るように開く
            db = helper.getWritableDatabase();
            // レコード1設定
            ContentValues values = new ContentValues();
            // レコードを検索してカーソルを作成
            Cursor cursor = db.query("personal_data", new String[] { "_id", "ave", "result", "count", "over_flg",
                    //"ave0", "ave1", "ave2", "ave3", "ave4", "ave5", "ave6", "ave7", "ave8", "ave9",
                    //"ave10", "ave11", "ave12", "ave13", "ave14", "ave15", "ave16", "ave17", "ave18", "ave19",
                    //"ave20", "ave21", "ave22", "ave23", "ave24", "ave25", "ave26", "ave27", "ave28", "ave29",
                    "scr0", "scr1", "scr2", "scr3", "scr4", "scr5", "scr6", "scr7", "scr8", "scr9",
                    "scr10", "scr11", "scr12", "scr13", "scr14", "scr15", "scr16", "scr17", "scr18", "scr19",
                    "scr20", "scr21", "scr22", "scr23", "scr24", "scr25", "scr26", "scr27", "scr28", "scr29",
                    "scr30", "scr31", "scr32", "scr33", "scr34", "scr35", "scr36", "scr37", "scr38", "scr39",
                    "scr40", "scr41", "scr42", "scr43", "scr44", "scr45", "scr46", "scr47", "scr48", "scr49",
                    "scr50", "scr51", "scr52", "scr53", "scr54", "scr55", "scr56", "scr57", "scr58", "scr59",
                    "res0", "res1", "res2", "res3", "res4", "res5", "res6", "res7", "res8", "res9",
                    "res10", "res11", "res12", "res13", "res14", "res15", "res16", "res17", "res18", "res19",
                    "res20", "res21", "res22", "res23", "res24", "res25", "res26", "res27", "res28", "res29",
                    "res30", "res31", "res32", "res33", "res34", "res35", "res36", "res37", "res38", "res39",
                    "res40", "res41", "res42", "res43", "res44", "res45", "res46", "res47", "res48", "res49",
                    "res50", "res51", "res52", "res53", "res54", "res55", "res56", "res57", "res58", "res59"},
                    "name = ?", new String[] { name }, null, null, null);
            List<Float> rec_ave = new ArrayList<Float>();
            List<Integer> rec_res = new ArrayList<Integer>();
            List<Integer> rec_cnt = new ArrayList<Integer>();
            List<Integer> rec_flg = new ArrayList<Integer>();
            List<Float> each_scr = new ArrayList<Float>();
            List<Integer> each_res = new ArrayList<Integer>();
            if (cursor.moveToNext()) {
                rec_ave.add(cursor.getFloat(cursor.getColumnIndex("ave")));
                rec_res.add(cursor.getInt(cursor.getColumnIndex("result")));
                rec_cnt.add(cursor.getInt(cursor.getColumnIndex("count")));
                rec_flg.add(cursor.getInt(cursor.getColumnIndex("over_flg")));
                int cnt = rec_cnt.get(0);
                for(int i=0; i<60; i++){
                    String scr = "scr"+i;
                    String res = "res"+i;
                    each_scr.add(cursor.getFloat(cursor.getColumnIndex(scr)));
                    each_res.add(cursor.getInt(cursor.getColumnIndex(res)));
                    if(rec_flg.get(0)==0 && i == cnt-1){
                        break;
                    }
                }
                //TableLayout tl = (TableLayout)findViewById(R.id.layoutHistory);
                //tl.removeAllViews();

                TextView ave_text = (TextView)findViewById(R.id.textAve);
                TextView total_text = (TextView)findViewById(R.id.textTotal);
                String str = String.format(Locale.getDefault(), "average: %4.1f", rec_ave.get(0));
                ave_text.setText(str);
                ave_text.setTextSize(22.0f);

                str = String.format(Locale.getDefault(), "total: %5d yen", rec_res.get(0));
                total_text.setText(str);
                total_text.setTextSize(22.0f);

                ViewGroup vg = (ViewGroup) findViewById(R.id.layoutHistory);
                vg.removeAllViews();
                // 行を追加
                getLayoutInflater().inflate(R.layout.history_table, vg);


                TableRow tr = (TableRow) vg.getChildAt(0);
                ((TextView) (tr.getChildAt(1))).setText("score");
                ((TextView) (tr.getChildAt(2))).setText("result");

                // 文字設定
                //TableRow tr = (TableRow) vg.getChildAt(1);

                //String str = String.format(Locale.getDefault(), "TOTAL");
                //String str = "TOTAL";
                //((TextView) (tr.getChildAt(0))).setText(player[i].name);
                //((TextView) (tr.getChildAt(0))).setText(str);
                //((TextView) (tr.getChildAt(0))).setTextSize(22.0f);

                //str = String.format(Locale.getDefault(), "ave: %4.1f", rec_ave.get(0));
                //str = String.format(Locale.getDefault(), "%4.1f", 1f * player[i].average);
                //((TextView) (tr.getChildAt(1))).setText(str);
                //((TextView) (tr.getChildAt(1))).setTextSize(22.0f);

                //str = String.format(Locale.getDefault(), "%5d yen", rec_res.get(0));
                //((TextView) (tr.getChildAt(2))).setText(str);
                //((TextView) (tr.getChildAt(2))).setTextSize(22.0f);

                for (int i = 0; i < 60; i++) {
                    int last_game = ((cnt -1 -i) % 60) < 0 ? ((cnt -1 -i) % 60 + 60)  : ((cnt -1 -i) % 60);
                    float last_scr = each_scr.get(last_game);
                    //if(last_scr==0f){
                        //break;
                    //}
                    // 行を追加
                    getLayoutInflater().inflate(R.layout.history_table, vg);
                    // 文字設定
                    tr = (TableRow) vg.getChildAt(i + 1);

                    str = String.format(Locale.getDefault(), "%2d", i+1);
                    ((TextView) (tr.getChildAt(0))).setText(str);

                    str = String.format(Locale.getDefault(), "%4.1f", last_scr);
                    ((TextView) (tr.getChildAt(1))).setText(str);

                    str = String.format(Locale.getDefault(), "%5d yen", each_res.get(last_game));
                    ((TextView) (tr.getChildAt(2))).setText(str);

                    if(rec_flg.get(0)==0 && i == cnt-1){
                        break;
                    }
                }
            }else{
                TableLayout tl = (TableLayout)findViewById(R.id.layoutHistory);
                tl.removeAllViews();
                TextView ave_text = (TextView)findViewById(R.id.textAve);
                TextView total_text = (TextView)findViewById(R.id.textTotal);
                //String str = String.format(Locale.getDefault(), "average: %4.1f", rec_ave.get(0));
                ave_text.setText("");
                //ave_text.setTextSize(22.0f);

                //str = String.format(Locale.getDefault(), "total: %5d yen", rec_res.get(0));
                total_text.setText("");
                //total_text.setTextSize(22.0f);
            }
            // カーソルクローズ
            cursor.close();
        }
        // 何も選択されなかった時の動作
        public void onNothingSelected(AdapterView parent) {
        }
    }
    public void showLast(){
        setContentView(R.layout.activity_history);

        findViewById(R.id.radioPersonal).setOnClickListener(HistoryActivity.this);
        findViewById(R.id.radioLast).setOnClickListener(HistoryActivity.this);
        findViewById(R.id.buttonClose).setOnClickListener(HistoryActivity.this);
        ViewGroup vg = (ViewGroup) findViewById(R.id.layoutHistory);

        // インスタンス作成
        //MySQLiteOpenHelper helper = new MySQLiteOpenHelper(this);
        // 読み書き出来るように開く
        db = helper.getWritableDatabase();
        // レコード1設定
        ContentValues values = new ContentValues();
        // レコードを検索してカーソルを作成
        Cursor cursor = db.query("personal_data", new String[] { "_id", "name",
                        "last_ave", "last_res" }, "delete_flg = ?", new String[] { "0" }, null, null,
                "last_res ASC");
        List<String> rec_name = new ArrayList<String>();
        List<Float> rec_ave = new ArrayList<Float>();
        List<Integer> rec_result = new ArrayList<Integer>();
        // カーソルから値を取り出す
        while (cursor.moveToNext()) {
            rec_name.add(cursor.getString(cursor.getColumnIndex("name")));
            rec_ave.add(cursor.getFloat(cursor.getColumnIndex("last_ave")));
            rec_result.add(cursor.getInt(cursor.getColumnIndex("last_res")));
        }
        for (int i = 0; i < rec_name.size(); i++) {
            // 行を追加
            getLayoutInflater().inflate(R.layout.history_table, vg);
            // 文字設定
            TableRow tr = (TableRow) vg.getChildAt(i + 1);

            String str = String.format(Locale.getDefault(), "%s", rec_name.get(i));
            //((TextView) (tr.getChildAt(0))).setText(player[i].name);
            ((TextView) (tr.getChildAt(0))).setText(str);

            str = String.format(Locale.getDefault(), "%4.1f", rec_ave.get(i));
            //str = String.format(Locale.getDefault(), "%4.1f", 1f * player[i].average);
            ((TextView) (tr.getChildAt(1))).setText(str);

            str = String.format(Locale.getDefault(), "%5d yen", rec_result.get(i));
            ((TextView) (tr.getChildAt(2))).setText(str);
        }
        // カーソルクローズ
        cursor.close();
    }
    public List<String> initPesonal(){
        setContentView(R.layout.personal_history);
        findViewById(R.id.radioPersonal).setOnClickListener(HistoryActivity.this);
        findViewById(R.id.radioLast).setOnClickListener(HistoryActivity.this);
        findViewById(R.id.buttonDelete).setOnClickListener(HistoryActivity.this);
        findViewById(R.id.buttonClose).setOnClickListener(HistoryActivity.this);


        db = helper.getWritableDatabase();
        // レコード1設定
        ContentValues values = new ContentValues();
        // レコードを検索してカーソルを作成
        Cursor cursor = db.query("personal_data", new String[] { "_id", "name",
        }, null, null, null, null, "name ASC");
        List<String> rec_name = new ArrayList<String>();
        while (cursor.moveToNext()) {
            rec_name.add(cursor.getString(cursor.getColumnIndex("name")));
        }
        // カーソルクローズ
        cursor.close();

        return rec_name;
    }
}

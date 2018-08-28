package com.example.amsl.bg;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.content.Intent;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.Serializable;
import java.util.Locale;
import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.CheckBox;
import android.view.MotionEvent;

import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.Toast;
import java.util.*;

import static java.lang.Math.exp;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private InputMethodManager inputMethodManager;
    private RelativeLayout mainLayout;
    private SQLiteDatabase db;

    private int num=0;
    private boolean num_flag=true;
    private boolean team_flag=true;
    private boolean auto_flag=true;
    private int num_team = 0;
    private int counter = 0;
    public Integer[] base_rate = {10};
    public Integer[] rate = {10};
    public int rate_count = 0;

    public int[] player_points = new int[20]; //for one game
    public int[] handicap = new int[20]; //for one game
    Map <Integer, Pair<Integer, Integer> > team_points = new HashMap< Integer, Pair<Integer,Integer> >(); //<"team name", "team score, number of members">

    public static final int MENU_SELECT_LAST = 0;
    public static final int MENU_SELECT_TEAM = 1;
    //public static final int MENU_SELECT_AVE = 2;

    public static Toast toast;

    //MyIO myIO = new MyIO();
    Player[] player = new Player[20];

    // player_points = new int[20];
    // handicap = new int[20];


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_player);

        inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        mainLayout = (RelativeLayout)findViewById(R.id.backGround);


        findViewById(R.id.buttonNum).setOnClickListener(this);
        //findViewById(R.id.buttonName).setOnClickListener(this);
        Button name_button = (Button)findViewById(R.id.buttonName);
        name_button.setVisibility(View.GONE);
    }

    @Override
    protected void onPause() {
        super.onPause();  // 常にスーパクラスは最初に呼ぶ。
        //System.out.println("in onPause");
        if(counter>0){
            // インスタンス作成
            MySQLiteOpenHelper helper = new MySQLiteOpenHelper(this);
            // 読み書き出来るように開く
            db = helper.getWritableDatabase();
            ContentValues updateValues = new ContentValues();
            updateValues.put("delete_flg", 1);
            db.update("personal_data", updateValues, "delete_flg=?", new String[] { "0" });
            Cursor cursor;
            for(int i=0; i<num; i++) {
                cursor = db.query("personal_data", new String[]{"_id", "name"},
                        "name = ?", new String[]{ player[i].get_name() }, null, null, null);
                // カーソルから値を取り出す
                if (cursor.moveToNext()) {
                    ContentValues values = new ContentValues();
                    values.put("last_ave", 1f*player[i].sum / counter);
                    values.put("last_res", player[i].income_expenditure);
                    values.put("delete_flg", 0);
                    db.update("personal_data", values, "name=?", new String[] { player[i].get_name() });
                }else{
                    ContentValues values = new ContentValues();
                    values.put("name", player[i].get_name());
                    values.put("last_ave", 1f*player[i].sum / counter);
                    values.put("last_res", player[i].income_expenditure);
                    values.put("delete_flg", 0);
                    db.insert("personal_data", null, values);
                }
                cursor.close();
            }
        }
    }

    public void onClick(View view){
        switch (view.getId()) {
            case R.id.buttonNum:
                //Toast.makeText(MainActivity.this, "クリックされました！", Toast.LENGTH_LONG).show();
                if(num_flag) {
                    EditText input = (EditText) findViewById(R.id.editNum);
                    String inputStr = input.getText().toString();
                    if(inputStr.length() != 0) {
                        num = Integer.parseInt(inputStr);
                        // MyIO.set(inputStr);
                    }
                    if(num>0 && num<21) {
                        inputMethodManager.hideSoftInputFromWindow(mainLayout.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        mainLayout.requestFocus();
                        // TableLayoutのグループを取得
                        ViewGroup vg = (ViewGroup) findViewById(R.id.layoutName);
                        for (int i = 0; i < num; i++) {
                            // 行を追加
                            getLayoutInflater().inflate(R.layout.edit_add, vg);
                            // 文字設定
                            TableRow tr = (TableRow) vg.getChildAt(i);
                            String str = String.format(Locale.getDefault(), "input player%d's name", i + 1);
                            ((EditText) (tr.getChildAt(0))).setHint(str);

                            ((EditText) (tr.getChildAt(0))).setInputType(InputType.TYPE_CLASS_TEXT);
                            //int etId = getResources().getIdentifier("et","id","com.example.amsl.bowlingamb")
                            ((EditText) (tr.getChildAt(0))).setId(i+10);
                        }
                        for(int i=0;i<num;i++){
                            player[i] = new Player();
                            handicap[i] = 0;
                            player_points[i] = 0;

                        }
                        num_flag=false;
                        Button start_button = (Button)findViewById(R.id.buttonName);
                        start_button.setVisibility(View.VISIBLE);
                        start_button.setOnClickListener(this);
                    }
                }
                break;
            case R.id.buttonName:
                if(nextPage(10)) {
                    setContentView(R.layout.set_team_auto);
                    mainLayout = (RelativeLayout)findViewById(R.id.backGround);
                    findViewById(R.id.buttonTeam).setOnClickListener(this);
                    //findViewById(R.id.buttonStart).setOnClickListener(this);
                    findViewById(R.id.radioManual).setOnClickListener(this);
                    findViewById(R.id.radioAuto).setOnClickListener(this);
                    Button start_button = (Button)findViewById(R.id.buttonStart);
                    start_button.setVisibility(View.GONE);
                    team_flag = true;
                }

                break;
            case R.id.radioManual:
                if(team_flag) {
                    setContentView(R.layout.set_team_manual);
                    mainLayout = (RelativeLayout)findViewById(R.id.backGround);
                    findViewById(R.id.buttonTeamManual).setOnClickListener(this);
                    findViewById(R.id.radioAuto).setOnClickListener(this);
                    findViewById(R.id.radioManual).setOnClickListener(this);
                    // TableLayoutのグループを取得
                    ViewGroup vg = (ViewGroup) findViewById(R.id.layoutTeamManual);
                    for (int i = 0; i < num; i++) {
                        // 行を追加
                        getLayoutInflater().inflate(R.layout.edit_add, vg);
                        // 文字設定
                        TableRow tr = (TableRow) vg.getChildAt(i);
                        String str = String.format(Locale.getDefault(), "input %s's team", player[i].name);
                        ((EditText) (tr.getChildAt(0))).setHint(str);
                        ((EditText) (tr.getChildAt(0))).setInputType(InputType.TYPE_CLASS_NUMBER);
                        //int etId = getResources().getIdentifier("et","id","com.example.amsl.bowlingamb")
                        ((EditText) (tr.getChildAt(0))).setId(i + 20);
                    }
                }
                break;
            case R.id.radioAuto:
                if(team_flag) {
                    setContentView(R.layout.set_team_auto);
                    mainLayout = (RelativeLayout)findViewById(R.id.backGround);
                    findViewById(R.id.radioManual).setOnClickListener(this);
                    findViewById(R.id.radioAuto).setOnClickListener(this);
                    findViewById(R.id.buttonTeam).setOnClickListener(this);
                    findViewById(R.id.buttonStart).setOnClickListener(this);
                    if(num_team!=0) {
                        ((EditText)findViewById(R.id.editText)).setText(String.valueOf(num_team));
                    }
                    Button start_button = (Button)findViewById(R.id.buttonStart);
                    start_button.setVisibility(View.GONE);
                }
                break;
            case R.id.buttonTeam:
                if(auto_flag) {
                    // MyIO.set("1");
                    team_flag = false;
                    EditText input = (EditText) findViewById(R.id.editText);
                    String inputStr = input.getText().toString();
                    if (inputStr.length() != 0) {
                        num_team = Integer.parseInt(inputStr);
                        // MyIO.set(inputStr);
                    }
                    if (!(num_team < 1 || num_team > num)) {
                        auto_flag = false;
                        inputMethodManager.hideSoftInputFromWindow(mainLayout.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        mainLayout.requestFocus();

                        set_team_auto();
                        ViewGroup vg = (ViewGroup) findViewById(R.id.layoutTeamAuto);

                        //TableLayout tableLayout = new TableLayout(this);
                        //setContentView(tableLayout, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));

                        for (int i = 0; i < 1; i++) {
                            TableRow tableRow = new TableRow(this);
                            for (int iCol = 0; iCol < num_team; iCol++) {
                                TextView text = new TextView(this);
                                text.setText(String.format(Locale.getDefault(), "team%d", iCol+1));
                                tableRow.addView(text);
                            }
                            vg.addView(tableRow);
                        }
                        //for (int iRow = 0; iRow < 3; iRow++) {
                        int i = 0;
                        while (true) {
                            TableRow tableRow = new TableRow(this);
                            for (int iCol = 0; iCol < num_team; iCol++) {
                                TextView text = new TextView(this);
                                //text.setText(String.format("[%d,%d]", iRow, iCol));
                                text.setText(player[i].get_name());
                                tableRow.addView(text);
                                i++;
                                if (i == num) {
                                    break;
                                }
                            }
                            vg.addView(tableRow);
                            //tableLayout.addView(tableRow);
                            if (i == num) {
                                i = 0;
                                break;
                            }
                        }
                        Button start_button = (Button)findViewById(R.id.buttonStart);
                        start_button.setVisibility(View.VISIBLE);
                        start_button.setOnClickListener(this);
                    }
                }
                //setContentView(R.layout.set_score);
                break;
            case R.id.buttonStart:
                rate[0] = set_rate(base_rate[0]);
                setContentView(R.layout.set_score);
                mainLayout = (RelativeLayout)findViewById(R.id.backGround);
                findViewById(R.id.buttonNext).setOnClickListener(this);
                findViewById(R.id.buttonFinish).setOnClickListener(this);
                findViewById(R.id.checkHandi).setOnClickListener(this);
                Button rate_button = (Button)findViewById(R.id.buttonRate);
                rate_button.setOnClickListener(this);
                rate_button.setText(String.format(Locale.getDefault(), "x %d", rate[0]));
                TextView textView = (TextView)findViewById(R.id.textCnt);
                textView.setText(String.format(Locale.getDefault(), "input player's score of %d%s game", counter+1, ordinal_num(counter+1)));
                ViewGroup vg = (ViewGroup) findViewById(R.id.layoutScore);
                for (int i = 0; i < num; i++) {
                    // 行を追加
                    getLayoutInflater().inflate(R.layout.edit_add, vg);
                    // 文字設定
                    TableRow tr = (TableRow) vg.getChildAt(i);
                    String str = String.format(Locale.getDefault(), "input %s's score", player[i].name);
                    ((EditText) (tr.getChildAt(0))).setHint(str);
                    ((EditText) (tr.getChildAt(0))).setInputType(InputType.TYPE_CLASS_NUMBER);
                    //int etId = getResources().getIdentifier("et","id","com.example.amsl.bowlingamb")
                    ((EditText) (tr.getChildAt(0))).setId(i + 30);
                }
                break;
            case R.id.buttonTeamManual:
                if(gamePage(20)) {
                    rate[0] = set_rate(base_rate[0]);
                    setContentView(R.layout.set_score);
                    mainLayout = (RelativeLayout)findViewById(R.id.backGround);
                    findViewById(R.id.buttonNext).setOnClickListener(this);
                    findViewById(R.id.buttonFinish).setOnClickListener(this);
                    findViewById(R.id.checkHandi).setOnClickListener(this);
                    rate_button = (Button)findViewById(R.id.buttonRate);
                    rate_button.setOnClickListener(this);
                    rate_button.setText(String.format(Locale.getDefault(), "x %d", rate[0]));
                    textView = (TextView)findViewById(R.id.textCnt);
                    textView.setText(String.format(Locale.getDefault(), "input player's score of %d %s game", counter+1, ordinal_num(counter+1)));

                    //ViewGroup vg2 = (ViewGroup) findViewById(R.id.layoutScore);
                    vg = (ViewGroup) findViewById(R.id.layoutScore);
                    for (int i = 0; i < num; i++) {
                        // 行を追加
                        getLayoutInflater().inflate(R.layout.edit_add, vg);
                        // 文字設定
                        TableRow tr = (TableRow) vg.getChildAt(i);
                        String str = String.format(Locale.getDefault(), "input %s's score", player[i].name);
                        ((EditText) (tr.getChildAt(0))).setHint(str);
                        ((EditText) (tr.getChildAt(0))).setInputType(InputType.TYPE_CLASS_NUMBER);
                        //int etId = getResources().getIdentifier("et","id","com.example.amsl.bowlingamb")
                        ((EditText) (tr.getChildAt(0))).setId(i + 30);
                    }
                }
                break;
            case R.id.buttonRate:
                if(toast != null){
                    toast.cancel();
                }
                rate[0] = set_rate(base_rate[0]);
                rate_button = (Button)findViewById(R.id.buttonRate);
                rate_button.setText(String.format(Locale.getDefault(), "x %d", rate[0]));
                if( isClickEvent() ){
                    rate_count = 0;
                    toast = Toast.makeText(MainActivity.this, "can't change rate in this ver (auto set)", Toast.LENGTH_SHORT);
                    //Toast.makeText(MainActivity.this, "can't change rate in this ver (auto set)", Toast.LENGTH_SHORT).show();
                    toast.show();
                }else{
                    toast.cancel();
                    rate_count++;
                }
                if(rate_count > 2){
                    rate_count = 0;
                    //Toast.makeText(MainActivity.this, "=====", Toast.LENGTH_LONG).show();
                    final EditText editView = new EditText(MainActivity.this);
                    editView.setInputType(InputType.TYPE_CLASS_NUMBER);
                    editView.setGravity(Gravity.CENTER);
                    new AlertDialog.Builder(MainActivity.this)
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setTitle("change rate")
                            //setViewにてビューを設定します。
                            .setView(editView)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    //入力した文字をトースト出力する
                                    //Toast.makeText(MainActivity.this, editView.getText().toString(), Toast.LENGTH_LONG).show();
                                    String inputStr = editView.getText().toString();
                                    if(inputStr.length() != 0) {
                                        base_rate[0] = Integer.parseInt(inputStr);
                                        rate[0] = set_rate(base_rate[0]);
                                        Button rate_button = (Button)findViewById(R.id.buttonRate);
                                        rate_button.setText(String.format(Locale.getDefault(), "x %d", rate[0]));
                                        // MyIO.set(inputStr);
                                    }
                                }
                            })
                            .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                }
                            })
                            .show();
                }
                break;
            case R.id.buttonNext:
                if(gameNext(30)) {
                    boolean handi_flag = false;
                    if( ((CheckBox)findViewById(R.id.checkHandi)).isChecked() ) {
                        handi_flag = true;
                    }
                    counter++;
                    team_calc(counter, handi_flag);
                    player_calc(rate[0]);


                    set_db_each_score();


                    setContentView(R.layout.set_team_auto);
                    mainLayout = (RelativeLayout)findViewById(R.id.backGround);
                    findViewById(R.id.radioManual).setOnClickListener(this);
                    findViewById(R.id.radioAuto).setOnClickListener(this);
                    findViewById(R.id.buttonTeam).setOnClickListener(this);
                    //findViewById(R.id.buttonStart).setOnClickListener(this);
                    if(num_team!=0) {
                        ((EditText)findViewById(R.id.editText)).setText(String.valueOf(num_team));
                    }
                    Button start_button = (Button)findViewById(R.id.buttonStart);
                    start_button.setVisibility(View.GONE);
                    team_flag = true;
                    auto_flag = true;
                    team_points.clear();
                }
                break;
            case R.id.buttonFinish:
                if(gameNext(30)) {
                    boolean handi_flag = false;
                    if( ((CheckBox)findViewById(R.id.checkHandi)).isChecked() ) {
                        handi_flag = true;
                    }
                    counter++;
                    team_calc(counter, handi_flag);
                    player_calc(rate[0]);

                    set_db_each_score();

                    setContentView(R.layout.show_result);

                    Calendar cal = Calendar.getInstance();       //カレンダーを取得

                    int iYear = cal.get(Calendar.YEAR);         //年を取得
                    int iMonth = cal.get(Calendar.MONTH) + 1;       //月を取得
                    int iDate = cal.get(Calendar.DATE);         //日を取得

                    String strDay = iYear + "/" + iMonth + "/" + iDate + " :    ";
                    String strGames = counter + " games";
                    String strDay_games = strDay + strGames;
                    ((TextView)findViewById(R.id.textDate)).setText(strDay_games);

                    findViewById(R.id.buttonExit).setOnClickListener(this);
                    vg = (ViewGroup) findViewById(R.id.layoutResult);
                    for (int i = 0; i < num; i++) {
                        getLayoutInflater().inflate(R.layout.result, vg);
                        TableRow tr = (TableRow) vg.getChildAt(i+1);
                        ((TextView) (tr.getChildAt(0))).setText(player[i].name);
                        String str = String.format(Locale.getDefault(), "%4.1f", 1f*player[i].sum/counter);
                        ((TextView) (tr.getChildAt(1))).setText(str);
                        str = String.format(Locale.getDefault(), "%5d yen", (int)player[i].income_expenditure);
                        ((TextView) (tr.getChildAt(2))).setText(str);
                    }
                }
                break;
            case R.id.buttonExit:
                // インスタンス作成
                //MySQLiteOpenHelper helper = new MySQLiteOpenHelper(this);
                // 読み書き出来るように開く
                //db = helper.getWritableDatabase();
                //ContentValues updateValues = new ContentValues();
                // レコードを検索してカーソルを作成
                //Cursor cursor;
                //for(int i=0; i<num; i++) {
                    //cursor = db.query("personal_data", new String[]{"_id", "name", "result", "ave",
                    //        "count", "over_flg"}, "name = ?", new String[]{ player[i].get_name() }, null, null, null);
                    //List<Float> rec_ave = new ArrayList<Float>();
                    //List<Integer> rec_cnt = new ArrayList<Integer>();
                    //List<Integer> rec_res = new ArrayList<Integer>();
                    //List<Integer> rec_flg = new ArrayList<Integer>();
                    //float last_ave = 1f*player[i].sum / counter;
                    // カーソルから値を取り出す
                    //if (cursor.moveToNext()) {
                        //rec_ave.add(cursor.getFloat(cursor.getColumnIndex("ave")));
                        //rec_cnt.add(cursor.getInt(cursor.getColumnIndex("count")));
                        //rec_res.add(cursor.getInt(cursor.getColumnIndex("result")));
                        //rec_flg.add(cursor.getInt(cursor.getColumnIndex("over_flg")));
                        //int game_cnt = rec_cnt.get(0);
                        //float average;
                        //if(rec_flg.get(0)==1){
                            //average = (30f * rec_ave.get(0) + last_ave) / 31f;
                        //}else{
                            //average = (1f * game_cnt * rec_ave.get(0) + last_ave) / (game_cnt + 1);
                        //}
                        //int result = rec_res.get(0) + (int)player[i].income_expenditure;
                        //String ave = "ave"+game_cnt;
                        //String res = "res"+game_cnt;
                        //ContentValues values = new ContentValues();
                        //values.put("ave", average);
                        //values.put("result", result);
                        //values.put(ave, last_ave);
                        //values.put(res, player[i].income_expenditure);
                        //if(game_cnt==29){
                            //values.put("over_flg", 1);
                        //}
                        //values.put("count", (game_cnt +1)%30 );
                        //db.update("personal_data", values, "name=?", new String[] { player[i].get_name() });
                    //}else{
                        //ContentValues values = new ContentValues();
                        //values.put("name", player[i].get_name());
                        //values.put("ave", last_ave);
                        //values.put("result", player[i].income_expenditure);
                        //values.put("ave0", last_ave);
                        //values.put("res0", player[i].income_expenditure);
                        //values.put("count", 1);
                        //db.insert("personal_data", null, values);
                    //}
                    //cursor.close();
                //}
                //Intent intent = new Intent(this, MainActivity.class);
                //startActivityForResult(intent, 0);
                finish();
                break;
            default:
                break;
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        inputMethodManager.hideSoftInputFromWindow(mainLayout.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        mainLayout.requestFocus();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        menu.add(0,MENU_SELECT_LAST,1,"interim result");
        //menu.add(0,MENU_SELECT_AVE,2,"show average");
        menu.add(0,MENU_SELECT_TEAM,2,"reconfirm team");
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case MENU_SELECT_LAST:
                Intent intent = new Intent(this, InterimResultActivity.class);
                intent.putExtra("NUMBER",num);
                intent.putExtra("PLAYER",player);
                intent.putExtra("COUNT",counter);
                startActivityForResult(intent, 0);
                break;
            case MENU_SELECT_TEAM:
                Intent intent_team = new Intent(this, TeamActivity.class);
                intent_team.putExtra("p_NUM",num);
                intent_team.putExtra("t_NUM",num_team);
                intent_team.putExtra("PLAYER",player);
                //intent_team.putExtra("COUNT",counter);
                startActivityForResult(intent_team, 0);
                break;
            //case MENU_SELECT_AVE:
            //    break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode != KeyEvent.KEYCODE_BACK){
            return super.onKeyDown(keyCode, event);
        }else{
            Intent intent = new Intent(this, InterimResultActivity.class);
            intent.putExtra("NUMBER",num);
            intent.putExtra("PLAYER",player);
            intent.putExtra("COUNT",counter);
            //intent.putExtra("HANDICAP", handicap);
            startActivityForResult(intent, 0);
            return false;
        }
    }

    private boolean nextPage(int start_id){
        boolean flag = false;
        for(int i=0;i<num;i++) {
            EditText input = (EditText) findViewById(i+start_id);
            if(input.getText().toString().equals("")){
                break;
            }
            if(i==num-1){
                //Intent intent = new Intent(getApplication(), MainActivity.class);
                //startActivity(intent);
                //setContentView(R.layout.page_change);
                flag = true;
            }
        }
        //int strtID = start_id;
        if(flag) {
            for (int i = 0; i < num; i++) {
                String str = ((EditText) findViewById(i + start_id)).getText().toString();
                player[i].set_name(str);
            }
            // MyIO.set("");
        }
        return flag;
    }
    private boolean gamePage(int start_id){
        boolean flag = false;
        for(int i=0;i<num;i++) {
            EditText input = (EditText) findViewById(i+start_id);
            if(input.getText().toString().equals("")){
                break;
            }
            if(i==num-1){
                //Intent intent = new Intent(getApplication(), MainActivity.class);
                //startActivity(intent);
                //setContentView(R.layout.page_change);
                flag = true;
            }
        }
        //int strtID = start_id;
        if(flag) {
            // MyIO.set("2");
            for (int i = 0; i < num; i++) {
                String str = ((EditText) findViewById(i + start_id)).getText().toString();
                player[i].set_team(Integer.parseInt(str));
            }
            // MyIO.set("");
        }
        return flag;
    }
    private boolean gameNext(int start_id){
        boolean flag = false;
        for(int i=0;i<num;i++) {
            EditText input = (EditText) findViewById(i+start_id);
            if(input.getText().toString().equals("")){
                break;
            }
            if(i==num-1){
                //Intent intent = new Intent(getApplication(), MainActivity.class);
                //startActivity(intent);
                //setContentView(R.layout.page_change);
                flag = true;
            }
        }
        //int strtID = start_id;
        if(flag) {
            for (int i = 0; i < num; i++) {
                String str = ((EditText) findViewById(i + start_id)).getText().toString();
                player[i].set_points(Integer.parseInt(str));
                //           MyIO.set(str);//スレッド間通信しよう！！
            }
            // MyIO.set("");
        }
        return flag;
    }

    public void set_db_each_score(){
        // インスタンス作成
        MySQLiteOpenHelper helper = new MySQLiteOpenHelper(this);
        // 読み書き出来るように開く
        db = helper.getWritableDatabase();
        //ContentValues updateValues = new ContentValues();
        // レコードを検索してカーソルを作成
        Cursor cursor;
        for(int i=0; i<num; i++) {
            cursor = db.query("personal_data", new String[]{"_id", "name", "result", "ave",
                    "count", "over_flg"}, "name = ?", new String[]{ player[i].get_name() }, null, null, null);
            List<Float> rec_ave = new ArrayList<Float>();
            List<Integer> rec_cnt = new ArrayList<Integer>();
            List<Integer> rec_res = new ArrayList<Integer>();
            List<Integer> rec_flg = new ArrayList<Integer>();
            int last_income = (int)player[i].income_expenditure -player[i].last_result;
            // カーソルから値を取り出す
            if (cursor.moveToNext()) {
                rec_ave.add(cursor.getFloat(cursor.getColumnIndex("ave")));
                rec_cnt.add(cursor.getInt(cursor.getColumnIndex("count")));
                rec_res.add(cursor.getInt(cursor.getColumnIndex("result")));
                rec_flg.add(cursor.getInt(cursor.getColumnIndex("over_flg")));
                int game_cnt = rec_cnt.get(0);
                float average;
                if(rec_flg.get(0)==1){
                    average = (60f * rec_ave.get(0) + player[i].scratch) / 61f;
                }else{
                    average = (1f * game_cnt * rec_ave.get(0) + player[i].scratch) / (game_cnt + 1);
                }
                int result = rec_res.get(0) + last_income;
                String scr = "scr"+game_cnt;
                String res = "res"+game_cnt;
                ContentValues values = new ContentValues();
                values.put("ave", average);
                values.put("result", result);
                values.put(scr, player[i].scratch);
                values.put(res, last_income);
                if(game_cnt==59){
                    values.put("over_flg", 1);
                }
                values.put("count", (game_cnt +1)%60 );
                db.update("personal_data", values, "name=?", new String[] { player[i].get_name() });
            }else{
                ContentValues values = new ContentValues();
                values.put("name", player[i].get_name());
                values.put("ave", player[i].scratch);
                values.put("result", last_income);
                values.put("scr0", player[i].scratch);
                values.put("res0", last_income);
                values.put("count", 1);
                db.insert("personal_data", null, values);
            }
            cursor.close();
        }
    }

    private static final long CLICK_DELAY = 1000;
    private static long mOldClickTime;
    public static boolean isClickEvent(){
        long time = System.currentTimeMillis();
        if(time - mOldClickTime < CLICK_DELAY){
            return false;
        }
        mOldClickTime = time;
        return true;
    }
    private String ordinal_num(int number){
        int check = number % 10;
        String rtn = "th";
        switch (check){
            case 1:
                rtn = "st";
                break;
            case 2:
                rtn = "nd";
                break;
            case 3:
                rtn = "rd";
                break;
            default:
                break;
        }
        return rtn;
    }

    public static <T> void shuffle(T ary[], int size)/*{{{*/
    {
        // int j = 0;
        // T t;
        // srand((unsigned int)time(NULL));
        // for(int i=0; i<size; i++){
        //     j = (int)(Math.random()*1000000000)%size;
        //     t = ary[i];
        //     ary[i] = ary[j];
        //     ary[j] = t;
        // }
        Random rand  = new Random();

        for(int i = size - 1; i > 0; i--){
            int j = rand.nextInt(i + 1);
            if(i != j) {
                T t = ary[i];
                ary[i] = ary[j];
                ary[j] = t;
            }
        }
    }/*}}}*/

    void set_team_auto()// throws IOException/*{{{*/
    {
        int team_num = num_team;//number of teams
        int team = 1;
        shuffle(player, num);
        // shuffle<Player>(player, num);
        for(int i=0;i<num;i++){
            player[i].set_team(team);
            team += 1;
            if(team > team_num){
                team = 1;
            }
        }
    }/*}}}*/

    int set_rate(int base_rate)/*{{{*/
    {
        int rate = base_rate;
        // boolean flag = false;
        if(Math.random()*10 < 2){
            if(Math.random()*10 < 2){
                rate += 5;
            }else if(Math.random()*10 < 3){
                rate += 10;
            }else{
                rate += 20;
            }
            if(Math.random()*100 < 2){
                rate = 50;
            }
            // flag = true;
        }
        // if(flag){
        //     rate += 10 * Math.floor((Math.random()*2)+1);
        // }
        return rate;
    }/*}}}*/
/*}}}*/

    void set_handi(int count)/*{{{*/
    {
        for(int i=0; i<num; i++){ //remove latest score
            player[i].add_point(-player_points[i]);
            team_points.get( player[i].get_team() ).first -= player_points[i];
            team_points.get( player[i].get_team() ).second -= 1;
        }
        calc_handi(count);
        for(int i=0; i<num; i++){
            player_points[i] += handicap[i];
            player[i].add_point(player_points[i]);
            team_points.get( player[i].get_team() ).first += player_points[i];
            team_points.get( player[i].get_team() ).second += 1;
        }
    }/*}}}*/
    void team_calc(int count, boolean handi_flag)// throws IOException/*{{{*/
    {
        int sum = 0; //sum points every team
        // Vector<int> team_name; //soted for num of mem;
        ArrayList<Integer> team_name = new ArrayList<Integer>(); //soted for num of mem;
        // Map<int, Pair<int, int> >::iterator it;
        // Map<int, Pair<int, int> >::iterator ite;
        boolean flag = false;
        // MyIO.get();
        while(!flag){
            for(int i=0; i<num; i++){ //set score to each team
                player_points[i] = player[i].input_point();
                player[i].add_sum(player_points[i]);
                player[i].scratch = player_points[i];
                Pair<Integer, Integer> pair = new Pair<Integer, Integer> (player_points[i], 1);
                if(team_points.containsKey( player[i].get_team() ) ){
                    // Pair<Integer, Integer> pair = new Pair<Integer, Integer> (team_points.get( player[i].get_team() ).first + player_points[i], team_points.get( player[i].get_team() ).second + 1);
                    pair.first = team_points.get( player[i].get_team() ).first + player_points[i];
                    pair.second = team_points.get( player[i].get_team() ).second + 1;
                }
                team_points.put( player[i].get_team(), pair );
                // team_points.get( player[i].get_team() ).first += player_points[i];
                // team_points.get( player[i].get_team() ).second += 1;
            }
            // flag = check(count);
            if(handi_flag){
                set_handi(count);
            }
            flag = true;
        }
        for(int i=0; i<num; i++){ //set score to each team
            player[i].calc_ave(count);
        }
        // for(int key : team_points.keySet()){team_name.push_back(key);} //set team name
        for(Iterator<Integer> iterator = team_points.keySet().iterator(); iterator.hasNext();){team_name.add(iterator.next());} //set team name
        // for(it=team_points.begin(); it!=team_points.end(); it++){team_name.push_back(it->first);} //set team name
        sort_by_sn(team_points, team_name, 1); //score:0, num of men:1
        int num_teams = team_name.size(); //number of teams
        float ratio = 1.0f; //ratio of members
        int num_team_max = 1;
        for(int i=0; i<num_teams; i++){
            if(team_points.get(team_name.get(i)).second > num_team_max ){
                num_team_max = team_points.get(team_name.get(i)).second;
            }
        }
        //for(int i=0; i<num_teams; i++){
        //    System.out.println("team"+team_name.get(i)+" num of mem = "+team_points.get(team_name.get(i)).second);
        //}
        for(int i=0; i<num_teams-1; i++){
            // ratio = 1.0f*team_points.get(team_name.get(i)).second / team_points.get(team_name.get(i+1)).second;
            ratio = 1.0f * num_team_max / team_points.get(team_name.get(i+1)).second;
            team_points.get(team_name.get(i+1)).first = (int)(ratio*team_points.get( team_name.get(i+1) ).first.intValue()+0.5f);
            sum += team_points.get(team_name.get(i)).first;
            //System.out.println("ratio =" + ratio);
        }
        sum += team_points.get(team_name.get(num_teams-1)).first;
        int ave = sum / num_teams;
        sum = 0;
        for(Pair<Integer,Integer> value : team_points.values()){ //calc each team's income and expenditure
            value.first -= ave;
            sum += value.first;
        }
        // for(it=team_points.begin(); it!=team_points.end(); it++){ //calc each team's income and expenditure
        // 	it->second.first -= ave;
        // 	sum += it->second.first;
        // }
        sort_by_sn(team_points, team_name, 0); //score:0, num of mem:1
        for(int i=0; i<num_teams-1; i++){
            if(sum==0){break;}
            sum--;
            team_points.get(team_name.get(i)).first -= 1;
        }
    }/*}}}*/
    void player_calc(int rate)/*{{{*/ {
        int sign = 1;
        int score_abs = 0;
        int player_inout[] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}; //each player's income and expenditure
        // Map<int, Pair<int, int> >::iterator it;
        int n = 0; //use in for loop
        int sum = 0; //check total +-
        // if(rate % 10 == 5){
        //     rate *= 2;
        // }
        if (rate < 0) {
            rate = 0;
        }
        if (num_team < 3) {
            rate *= 2;
        }
        // my_sort<Player, int>(player, num, player_points, sign+1);
        // my_sort(player, num, player_points, (sign+1!=0));
        p_sort(player, num, player_points, (sign+1!=0));
        // for(it=team_points.begin(); it!=team_points.end(); it++){ //calc each player's income and expenditure
        for(Map.Entry<Integer, Pair<Integer,Integer>> entry : team_points.entrySet()){ //calc each player's income and expenditure
            score_abs = entry.getValue().first;
            // if(rate % 10 != 5){
            //     score_abs *= 2;
            // }
            if(rate % 10 == 0){
                score_abs *= rate/10;
            }else if(rate % 10 == 5){
                score_abs = score_abs*(rate + 5)/10 - score_abs/2;
            }
            sum += score_abs;
            sign = score_abs < 0 ? -1 : 1;
            score_abs *= sign; //to absolute
            while( score_abs>0 ){
                for(int i=0; score_abs!=0 && i<num ; i++){
                    n = sign+1!=0 ? i : num-1-i;
                    if( player[n].get_team() == entry.getKey() ){
                        score_abs -= 1;
                        player_inout[n] += sign;
                    }
                }
            }
        }
        if(sum < 0){
            player_inout[0] -= sum;
        }else if(sum > 0){
            player_inout[num-1] -= sum;
        }
        for(int i=0;i<num;i++){
            player[i].last_result = (int)player[i].income_expenditure;
            if(rate % 10 == 0){
                player[i].add_income_expenditure(player_inout[i] * 10);
            }else if(rate % 10 == 5){
                player[i].add_income_expenditure(player_inout[i] * 10);
            }else{
                player[i].add_income_expenditure(player_inout[i] * rate);
            }
        }
        // my_sort<Player, int>(player, num, player_inout, true);
        p_sort(player, num, player_points, (sign+1!=0));
        // my_sort(player, num, player_points, sign+1);
        // for(int i=0;i<num;i++){//last result show
        // 	player[i].show_income_expenditure();
        // }
    }/*}}}*/
    void calc_handi(int count)/*{{{*/
    {
        if(count==1) {
            for(int i=1;i<num;i++){
                player[i].average = 167;
            }
        }
        float ave = 0;
        float ave_s = 0;
        float max = player[0].get_ave();
        double ratio = 0;
        final double w0 = 0.7;
        final double r  = 0.3;
        final double k  = 0.85;
        for(int i=1;i<num;i++){
            ave = player[i].get_ave();
            if(max < ave){
                max = ave;
            }
        }
        // max = (int)max - (int)max%10;
        for(int i=0; i<num; i++){
            ave = player[i].get_ave();
            ave_s = player[i].getAverage_s();
            // ratio = k / (1 + ((k-w0)/w0)*exp(-r*(ave - ave_s)));
            ratio = k / (1 + ((k-w0)/w0)*exp(-r*count));
            // ave += (10.0f / 3.00001f) * 3f;
            // ave = (int)ave - (int)ave %10;
            // handicap[i] = (int)max - (int)ave;
            // handicap[i] = (int)(max - ave_s) - (int)(max - ave_s)%10;
            // handicap[i] = (int)(max - ave) - (int)(max - ave)%10;
            // ratio = 1.0;
            handicap[i] = (int)(max - (ratio*ave_s + (1.0-ratio)*ave));
            handicap[i] -= handicap[i]%10;
            handicap[i] = handicap[i]<0 ? 0 : handicap[i];
            // player[i].handi = handicap[i];
            // System.out.println("ave_s[" + i + "]" + ave_s);
            // System.out.println("ave[" + i + "]" + ave);
            // System.out.println("handi[" + i + "]" + handicap[i]);
        }
    }/*}}}*/

    void p_sort(Player[] player, int size, int[] points, boolean reverse)/*{{{*/
    {
        Player p;
        int point;
        int sign = reverse ? 1:-1; //true:DESCENDING, false:ASCENDING;
        for(int i=0; i<size; i++){
            for(int j=i; j<size; j++){
                if(sign * points[i] < sign * points[j]){
                    p = player[i];
                    point = points[i];

                    player[i] = player[j];
                    points[i] = points[j];

                    player[j] = p;
                    points[j] = point;
                }
            }
        }
    }/*}}}*/
    ArrayList<Integer> sort_by_sn(Map <Integer, Pair<Integer, Integer> > team_points, ArrayList<Integer> team_name, int s_n)//score_numOFmem:0_1/*{{{*/
    {
        int tmp; //sort swap tempolary
        boolean flag = false;
        // sort(team_name.begin(),team_name.end()); //sort in ascending order by name ([1]1,[2]2,[0]3,...)
        Collections.sort(team_name); //sort in ascending order by name ([1]1,[2]2,[0]3,...)
        int len = team_name.size();

        // srand((unsigned int)time(NULL));rand();

        ArrayList<Integer> t_name=new ArrayList<Integer>();
        for(int i=0;i<len;i++){t_name.add(team_name.get(i));}

        switch(s_n){
            case 0: //sort in ASCENDING order by SCORE
                for(int i=0;i<len;i++){
                    flag = false;
                    for(int j=len-1;j>i;j--){
                        //if( team_points.get(team_name.get(i)).first >= team_points.get(team_name.get(j)).first ){
                        if( team_points.get(t_name.get(i)).first >= team_points.get(t_name.get(j)).first ){
                            flag = true;
                            //if(team_points.get(team_name.get(i)).first == team_points.get(team_name.get(j)).first){
                            if(team_points.get(t_name.get(i)).first == team_points.get(t_name.get(j)).first){
                                flag = ((int)(Math.random()*100000) % 2) != 0;
                            }
                        }
                        if(flag){
                            tmp = t_name.get(i);
                            t_name.set(i, t_name.get(j));
                            t_name.set(j, tmp); //swap
                        }
                    }
                }
                break;
            case 1: //sort in DESCENDING order by NUMBER OF MEMBERS
                for(int i=0;i<len;i++){
                    flag = false;
                    for(int j=len-1;j>i;j--){
                        //if( team_points.get(team_name.get(i)).second <= team_points.get(team_name.get(j)).second ){
                        if( team_points.get(t_name.get(i)).second <= team_points.get(t_name.get(j)).second ){
                            flag = true;
                            //if(team_points.get(team_name.get(i)).second == team_points.get(team_name.get(j)).second){
                            if(team_points.get(t_name.get(i)).second == team_points.get(t_name.get(j)).second){
                                // flag = rand()%2 ? true : false;
                                flag = ((int)(Math.random()*100000) % 2) != 0;
                            }
                        }
                        if(flag){
                            tmp = t_name.get(i);  t_name.set(i, t_name.get(j));  t_name.set(j, tmp); //swap
                        }
                    }
                }
                break;
            default:
                break;
        };
        for(int i=0;i<len;i++){
            team_name.set(i, t_name.get(i));
        }
        return t_name;
    }/*}}}*/

};

//bowling scoreから賭けのプラスマイナスを表示するプログラム(Player class)
class Player implements Serializable
{
    String name;//player name
    int points;//player's latest score
    int scratch;//player's latest scratch score
    int team;//player's team
    float income_expenditure;//income and expenditure
    int last_result;//income and expenditure
    float average; //player's score average(include handicap)
    float average_s; //player's score average(scratch)
    // int handi;//player's latest handicap
    int sum; //player's score sum
    Player(){ points = 0; scratch = 0; sum = 0; };
    String get_name(){return name;};
    int get_points(){return points;};
    int get_team(){return team;};
    float get_ave(){return average;};
    float getAverage_s(){return average_s;};
    int get_sum(){return sum;};
    void set_points(int x){ points = x;};
    void set_name(String x){ name = x; };
    void set_team(int x){ team = x;};
    void add_income_expenditure(int x){ income_expenditure += x; };
    void add_point(int x){ points += x; };
    void add_sum(int x){ sum += x; };
    int input_point()// throws IOException/*{{{*/
    {
        return points;
    }/*}}}*/
    float calc_ave(int count)/*{{{*/
    {
        average = ( (1.0f*count) * average + points ) / (count + 1);
        average_s = ( (count -1.0f ) * average_s + scratch ) / count;
        return average;
    }/*}}}*/
};

class Pair<F, S> {
    public F first;
    public S second;

    Pair (F first, S second) {
        this.first = first;
        this.second = second;
    }
}

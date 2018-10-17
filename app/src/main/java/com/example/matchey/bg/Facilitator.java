package com.example.matchey.bg;

import android.support.v7.app.AlertDialog;

import android.content.Context;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.text.InputType;

import android.view.View;
import android.view.Gravity;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.ArrayList;
import java.util.Random;

public class Facilitator
{
    private int nplayers = 0; // number of players
    private int nteams = 0;   // number of teams
    private int game_count = 0;		// count of games
    private int rate_tap_count = 0;		// count of taps
    private int count4reset = 0;		// count of games
    private static final int ID_BEGIN = 64; // viewID of EditText
    private static final int ID_NAME  = 1;  // viewID of EditText
    private static final int ID_TEAM  = 2;  // viewID of EditText
    private static final int ID_SCORE = 3;  // viewID of EditText

    private InputMethodManager inputMethodManager;
    private RelativeLayout mainLayout;
    private SQLiteDatabase db;
    private static Random rand = new Random();
    private static Toast toast;

    private Player[] players;
    private Calculator calc = new Calculator();

    private MainActivity ma;

    void initViews(MainActivity activity)
    {
        ma = activity;

        ma.setContentView(R.layout.set_player);

        inputMethodManager = (InputMethodManager)ma.getSystemService(Context.INPUT_METHOD_SERVICE);
        mainLayout = (RelativeLayout)ma.findViewById(R.id.backGround);

        ma.findViewById(R.id.buttonNum).setOnClickListener(ma);
        Button name_button = (Button)ma.findViewById(R.id.buttonName);
        name_button.setVisibility(View.GONE);
    }

    void inputNames()
    {
        EditText input = (EditText)ma.findViewById(R.id.editNum);
        String inputStr = input.getText().toString();
        if(inputStr.length() != 0){
            nplayers = Integer.parseInt(inputStr);
        }
        if(nplayers > 0){
            players = new Player[nplayers];
            calc.setNumPlayer(nplayers);

            inputMethodManager.hideSoftInputFromWindow
                    (mainLayout.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            mainLayout.requestFocus();
            // TableLayoutのグループを取得
            ViewGroup vg = (ViewGroup)ma.findViewById(R.id.layoutName);
            for(int i = 0; i < nplayers; ++i){
                players[i] = new Player();
                // 行を追加
                ma.getLayoutInflater().inflate(R.layout.edit_add, vg);
                // 文字設定
                TableRow tr = (TableRow)vg.getChildAt(i);
                String str = String.format(Locale.getDefault(), "input player%d's name", i + 1);
                ((EditText)(tr.getChildAt(0))).setHint(str);

                ((EditText)(tr.getChildAt(0))).setInputType(InputType.TYPE_CLASS_TEXT);
                ((EditText)(tr.getChildAt(0))).setId(ID_BEGIN*ID_NAME + i);
            }
            Button start_button = (Button)ma.findViewById(R.id.buttonName);
            start_button.setVisibility(View.VISIBLE);
            start_button.setOnClickListener(ma);
        }
    }

    void setPlayers()
    {
        if(checkFill(ID_NAME)){
            for(int i = 0; i < nplayers; ++i){
                String str = ((EditText)ma.findViewById(ID_BEGIN*ID_NAME + i)).getText().toString();
                players[i].setName(str);
            }
            inputNumTeam();
        }
    }

    void inputNumTeam()
    {
        ma.setContentView(R.layout.set_team_auto);
        mainLayout = (RelativeLayout)ma.findViewById(R.id.backGround);
        ma.findViewById(R.id.radioManual).setOnClickListener(ma);
        ma.findViewById(R.id.radioAuto).setOnClickListener(ma);
        ma.findViewById(R.id.buttonTeam).setOnClickListener(ma);
        ma.findViewById(R.id.buttonStartAuto).setOnClickListener(ma);
        if(nteams != 0){
            ((EditText)ma.findViewById(R.id.editText)).setText(String.valueOf(nteams));
        }
        Button start_button = (Button)ma.findViewById(R.id.buttonStartAuto);
        start_button.setVisibility(View.GONE);
    }

    void inputTeams() // 2列にする name ___
    {
        ma.setContentView(R.layout.set_team_manual);
        mainLayout = (RelativeLayout)ma.findViewById(R.id.backGround);
        ma.findViewById(R.id.buttonStartManual).setOnClickListener(ma);
        ma.findViewById(R.id.radioAuto).setOnClickListener(ma);
        ma.findViewById(R.id.radioManual).setOnClickListener(ma);
        // TableLayoutのグループを取得
        ViewGroup vg = (ViewGroup)ma.findViewById(R.id.layoutTeamManual);
        for(int i = 0; i < nplayers; ++i){
            // 行を追加
            ma.getLayoutInflater().inflate(R.layout.edit_add, vg);
            // 文字設定
            TableRow tr = (TableRow)vg.getChildAt(i);
            String str = String.format(Locale.getDefault(), "input %s's team", players[i].getName());
            ((EditText)(tr.getChildAt(0))).setHint(str);
            ((EditText)(tr.getChildAt(0))).setInputType(InputType.TYPE_CLASS_NUMBER);
            ((EditText)(tr.getChildAt(0))).setId(ID_BEGIN*ID_TEAM + i);
        }
    }

    void setTeamAuto()
    {
        // layout幅 -> 文字サイズ小さくするか, スクロールできるようにするか
        EditText input = (EditText)ma.findViewById(R.id.editText);
        String inputStr = input.getText().toString();
        if(inputStr.length() != 0){
            nteams = Integer.parseInt(inputStr);
        }
        if(!(nteams < 1 || nplayers < nteams)){
            inputMethodManager.hideSoftInputFromWindow
                    (mainLayout.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            mainLayout.requestFocus();

            assignTeam();

            ViewGroup vg = (ViewGroup)ma.findViewById(R.id.layoutTeamAuto);

            for(int i = 0; i < 1; ++i){
                TableRow tableRow = new TableRow(ma);
                for(int iCol = 0; iCol != nteams; ++iCol){
                    TextView text = new TextView(ma);
                    text.setText(String.format(Locale.getDefault(), "team%d", iCol+1));
                    tableRow.addView(text);
                }
                vg.addView(tableRow);
            }
            int i = 0;
            while(true){
                TableRow tableRow = new TableRow(ma);
                for(int iCol = 0; iCol != nteams; ++iCol){
                    TextView text = new TextView(ma);
                    text.setText(players[i].getName());
                    tableRow.addView(text);
                    ++i;
                    if(i == nplayers){
                        break;
                    }
                }
                vg.addView(tableRow);
                if(i == nplayers){
                    i = 0;
                    break;
                }
            }
            Button start_button = (Button)ma.findViewById(R.id.buttonStartAuto);
            start_button.setVisibility(View.VISIBLE);
            start_button.setOnClickListener(ma);
        }
    }

    void setTeamManual() // string ならPlayerには0~nで割り当て
    {
        if(checkFill(ID_TEAM)){
            for(int i = 0; i < nplayers; ++i){
                String str = ((EditText)ma.findViewById(ID_BEGIN*ID_TEAM + i)).getText().toString();
                players[i].setTeam(Integer.parseInt(str));
            }
            inputScores();
        }
    }

    void inputScores()
    {
        inputScores(false);
    }


    void setRate()
    {
        if(toast != null){
            toast.cancel();
        }
        calc.setRate();
        Button rate_button = (Button)ma.findViewById(R.id.buttonRate);
        rate_button.setText(String.format(Locale.getDefault(), "x %d", calc.getRate()));
        if( isClickEvent() ){
            rate_tap_count = 0;
            toast = Toast.makeText(ma,
                    "can't change rate in this ver (auto set)", Toast.LENGTH_SHORT);
            toast.show();
        }else{
            toast.cancel();
            ++rate_tap_count;
        }
        if(rate_tap_count > 2){
            rate_tap_count = 0;
            final EditText editView = new EditText(ma);
            editView.setInputType(InputType.TYPE_CLASS_NUMBER);
            editView.setGravity(Gravity.CENTER);
            new AlertDialog.Builder(ma)
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setTitle("change rate")
                    .setView(editView) //setViewにてビューを設定
                    .setPositiveButton("OK", new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int whichButton){
                            //入力した文字をトースト出力する
                            String inputStr = editView.getText().toString();
                            if(inputStr.length() != 0){
                                calc.setRate(Integer.parseInt(inputStr));
                                Button rate_button = (Button)ma.findViewById(R.id.buttonRate);
                                rate_button.setText(String.format(Locale.getDefault(),"x %d",
                                        calc.getRate()));
                            }
                        }
                    })
                    .setNegativeButton("キャンセル", new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int whichButton){ }
                    })
                    .show();
        }
    }

    boolean setScores()
    {
        if(checkFill(ID_SCORE)){
            ++game_count;
            if( ((CheckBox)ma.findViewById(R.id.checkHandi)).isChecked() ) {
				// if(flag_auto){
                calc.setHandicap(players);
				// }else{ // 入力できるようにする
				// 	if(checkFill(ID_HANDI)){
				// 		for(int i = 0;i < nplayers; ++i){
				// 		}
				// 	}
				// }
            }
            for(int i = 0; i < nplayers; i++){
                String str = ((EditText)ma.findViewById(ID_BEGIN*ID_SCORE + i)).getText().toString();
                players[i].setScratch(Integer.parseInt(str), game_count);
            }
            // calc.setCount(game_count);
            calc.teamCalc(players);
            calc.playerCalc(players);

            writeDBPersonal();

            return true;
        }else{
            return false;
        }
    }

    void showResult()
    {
        ma.setContentView(R.layout.show_result);

        Calendar cal = Calendar.getInstance();       //カレンダーを取得

        int iYear = cal.get(Calendar.YEAR);         //年を取得
        int iMonth = cal.get(Calendar.MONTH) + 1;       //月を取得
        int iDate = cal.get(Calendar.DATE);         //日を取得

        String strDay = iYear + "/" + iMonth + "/" + iDate + " :    ";
        String strGames = game_count + " games";
        String strDay_games = strDay + strGames;
        ((TextView)ma.findViewById(R.id.textDate)).setText(strDay_games);

        ma.findViewById(R.id.buttonExit).setOnClickListener(ma);
        ViewGroup vg = (ViewGroup)ma.findViewById(R.id.layoutResult);
        for(int i = 0; i < nplayers; i++){
            ma.getLayoutInflater().inflate(R.layout.result, vg);
            TableRow tr = (TableRow)vg.getChildAt(i+1);
            ((TextView)(tr.getChildAt(0))).setText(players[i].getName());
            String str = String.format(Locale.getDefault(), "%4.1f", players[i].getAveScratch());
            ((TextView)(tr.getChildAt(1))).setText(str);
            str = String.format(Locale.getDefault(),
                    "%5d yen", (int)players[i].getIncomeExpenditure());
            ((TextView)(tr.getChildAt(2))).setText(str);
        }
    }

    void onTouch()
    {
        inputMethodManager.hideSoftInputFromWindow
                (mainLayout.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        mainLayout.requestFocus();
    }

    void showLastResult() // 前回の収支確認
    {
        Intent intent = new Intent(ma, InterimResultActivity.class);
        intent.putExtra("NUMBER", nplayers);
        intent.putExtra("PLAYER", players);
        intent.putExtra("COUNT",  game_count);
        ma.startActivityForResult(intent, 0);
    }

    void showLastTeam()
    {
        Intent intent_team = new Intent(ma, TeamActivity.class);
        intent_team.putExtra("p_NUM", nplayers);
        intent_team.putExtra("t_NUM", nteams);
        intent_team.putExtra("PLAYER", players);
        ma.startActivityForResult(intent_team, 0);
    }

    void resetLastScores()
    {
        if( (game_count == count4reset) && (game_count > 0) ){
            --game_count;
            --count4reset;
            inputScores(true);
            deleteDBLast();
            for(int i = 0; i < nplayers; ++i){
				players[i].resetLastScore(game_count);
			}
        }
    }

    void writeDBLast()
    {
        if(game_count > 0){
            MySQLiteOpenHelper helper = new MySQLiteOpenHelper(ma); // インスタンス作成
            db = helper.getWritableDatabase(); // 読み書き出来るように開く
            ContentValues updateValues = new ContentValues();
            updateValues.put("delete_flg", 1);
            db.update("personal_data", updateValues, "delete_flg=?", new String[] { "0" });
            Cursor cursor;
            for(int i = 0; i < nplayers; ++i){
                cursor = db.query("personal_data", new String[]{"_id", "name"},
                        "name = ?", new String[]{ players[i].getName() }, null, null, null);
                ContentValues values = new ContentValues();
                values.put("last_ave", players[i].getAveScratch());
                values.put("last_res", players[i].getIncomeExpenditure());
                values.put("delete_flg", 0);
                if(cursor.moveToNext()){ // カーソルから値を取り出す
                    db.update("personal_data", values,
                            "name=?", new String[] { players[i].getName() });
                }else{
                    values.put("name", players[i].getName());
                    db.insert("personal_data", null, values);
                }
                cursor.close();
            }
        }
    }

    // private
    private void inputScores(boolean flag_reset)
    {
        ++count4reset;
        calc.setRate();
        ma.setContentView(R.layout.set_score);
        mainLayout = (RelativeLayout)ma.findViewById(R.id.backGround);
        ma.findViewById(R.id.buttonNext).setOnClickListener(ma);
        ma.findViewById(R.id.buttonFinish).setOnClickListener(ma);
        ma.findViewById(R.id.checkHandi).setOnClickListener(ma);
        Button rate_button = (Button)ma.findViewById(R.id.buttonRate);
        rate_button.setOnClickListener(ma);
        rate_button.setText(String.format(Locale.getDefault(), "x %d", calc.getRate()));
        TextView textView = (TextView)ma.findViewById(R.id.textCnt);
        textView.setText(String.format(Locale.getDefault(),
                "input player's score of %d %s game", game_count+1, ordinalNum(game_count+1)));
        ViewGroup vg = (ViewGroup)ma.findViewById(R.id.layoutScore);

//        for(int i = 0; i != nplayers; ++i){
//            TableRow tableRow = new TableRow(ma);
//            TextView text = new TextView(ma);
//
//            text.setText(String.format(Locale.getDefault(), players[i].getName()));
//            tableRow.addView(text);
//            vg.addView(tableRow);
//
//
//        }

        for(int i = 0; i != nplayers; ++i){
            ma.getLayoutInflater().inflate(R.layout.edit_add, vg); // 行を追加
            TableRow tr = (TableRow)vg.getChildAt(i); // 文字設定
            String str;
            if(flag_reset){
                str = String.format(Locale.getDefault(),
                        "input %s's score (%d)", players[i].getName(), players[i].getScratch());
            }else{
                str = String.format(Locale.getDefault(),
                        "input %s's score", players[i].getName());
            }
            ((EditText)(tr.getChildAt(0))).setHint(str);
            ((EditText)(tr.getChildAt(0))).setInputType(InputType.TYPE_CLASS_NUMBER);
            ((EditText)(tr.getChildAt(0))).setId(ID_BEGIN*ID_SCORE + i);
        }
    }

    private boolean checkFill(int type) // EditTextが全部埋まってるか
    {
        boolean flag = false;
        for(int i = 0;i < nplayers; ++i){
            EditText input = (EditText)ma.findViewById(ID_BEGIN*type + i);
            if(input.getText().toString().equals("")){
                break;
            }
            if(i == nplayers - 1){
                flag = true;
            }
        }
        return flag;
    }

    private void assignTeam() // throws IOException
    {
        shuffle(players, nplayers);

        int team = 0;

        for(int i = 0; i < nplayers; ++i){
            players[i].setTeam(team);
            ++team;
            if(team == nteams){
                team = 0;
            }
        }
    }

    private static <T> void shuffle(T ary[], int size)
    {
        for(int i = size - 1; i > 0; --i){
            int j = rand.nextInt(i + 1);
            if(i != j){
                T t = ary[i];
                ary[i] = ary[j];
                ary[j] = t;
            }
        } // Fisher–Yates
    }

    private void writeDBPersonal()
    {
        MySQLiteOpenHelper helper = new MySQLiteOpenHelper(ma); // インスタンス作成
        db = helper.getWritableDatabase(); // 読み書き出来るように開く
        Cursor cursor; // レコードを検索してカーソルを作成
        for(int i = 0; i < nplayers; i++){
            cursor = db.query("personal_data", new String[]{"_id", "name", "result", "ave",
                            "count", "over_flg"}, "name = ?", new String[]{ players[i].getName() },
                    null, null, null);
            List<Double>  rec_ave = new ArrayList<Double>();
            List<Integer> rec_cnt = new ArrayList<Integer>();
            List<Integer> rec_res = new ArrayList<Integer>();
            List<Integer> rec_flg = new ArrayList<Integer>();


            if(cursor.moveToNext()){ // カーソルから値を取り出す
                rec_ave.add(cursor.getDouble(cursor.getColumnIndex("ave")));
                rec_cnt.add(cursor.getInt(cursor.getColumnIndex("count")));
                rec_res.add(cursor.getInt(cursor.getColumnIndex("result")));
                rec_flg.add(cursor.getInt(cursor.getColumnIndex("over_flg")));
                int game_cnt = rec_cnt.get(0);
                double average;
                if(rec_flg.get(0) == 1){
                    average = (60.0 * rec_ave.get(0) + players[i].getScratch()) / 61.0;
                }else{
                    average
                            = (1.0 * game_cnt * rec_ave.get(0) + players[i].getScratch()) / (game_cnt + 1);
                }
                int result = rec_res.get(0) + players[i].getIncomeExpenditure();
                String scr = "scr"+game_cnt;
                String res = "res"+game_cnt;
                ContentValues values = new ContentValues();
                values.put("ave", average);
                values.put("result", result);
                values.put(scr, players[i].getScratch());
                values.put(res, players[i].getIncomeExpenditure());
                if(game_cnt == 59){
                    values.put("over_flg", 1);
                }
                values.put("count", (game_cnt + 1) % 60 );
                db.update("personal_data", values, "name=?", new String[] { players[i].getName() });
            }else{
                ContentValues values = new ContentValues();
                values.put("name", players[i].getName());
                values.put("ave", players[i].getScratch());
                values.put("result", players[i].getIncomeExpenditure());
                values.put("scr0", players[i].getScratch());
                values.put("res0", players[i].getIncomeExpenditure());
                values.put("count", 1);
                db.insert("personal_data", null, values);
            }
            cursor.close();
        }
    }

    private void deleteDBLast()
    {
        MySQLiteOpenHelper helper = new MySQLiteOpenHelper(ma); // インスタンス作成
        db = helper.getWritableDatabase(); // 読み書き出来るように開く
        Cursor cursor; // レコードを検索してカーソルを作成
        for(int i = 0; i < nplayers; i++){
            cursor = db.query("personal_data", new String[]{"_id", "name", "result", "ave",
                            "count", "over_flg"}, "name = ?", new String[]{ players[i].getName() },
                    null, null, null);
            List<Double>  rec_ave = new ArrayList<Double>();
            List<Integer> rec_cnt = new ArrayList<Integer>();
            List<Integer> rec_res = new ArrayList<Integer>();
            List<Integer> rec_flg = new ArrayList<Integer>();

            if(cursor.moveToNext()){
                rec_ave.add(cursor.getDouble(cursor.getColumnIndex("ave")));
                rec_cnt.add(cursor.getInt(cursor.getColumnIndex("count")));
                rec_res.add(cursor.getInt(cursor.getColumnIndex("result")));
                rec_flg.add(cursor.getInt(cursor.getColumnIndex("over_flg")));

                double average;
                int result;
                int game_cnt = rec_cnt.get(0);

                if(game_cnt == 0){
                    game_cnt = 59;
                }else{
                    game_cnt -= 1;
                }

                if(rec_flg.get(0) == 1){
                    average = (61.0 * rec_ave.get(0) - players[i].getScratch() ) / 60.0;
                }else if(game_cnt == 0){
                    average = 0;
                }else{
                    average
                            = (1.0 * (game_cnt + 1) * rec_ave.get(0) - players[i].getScratch()) / game_cnt;
                }

                result = rec_res.get(0) - players[i].getIncomeExpenditure();

                ContentValues values = new ContentValues();
                values.put("ave", average);
                values.put("result", result);
                values.put("count", game_cnt);
                db.update("personal_data", values, "name=?", new String[] { players[i].getName() });
            }
            cursor.close();
        }
    }

    private static final long CLICK_DELAY = 1000;
    private static long mOldClickTime;
    private static boolean isClickEvent() // 連続タップ検出
    {
        long time = System.currentTimeMillis();
        if(time - mOldClickTime < CLICK_DELAY){
            return false;
        }
        mOldClickTime = time;
        return true;
    }

    private String ordinalNum(int number)
    {
        int check = number % 10;
        String rtn = "th";
        switch(check){
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
}

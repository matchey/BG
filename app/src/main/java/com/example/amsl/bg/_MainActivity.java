// package com.example.amsl.bg;
//
// import android.content.Context;
// import android.support.v7.app.AppCompatActivity;
// import android.support.v7.app.AlertDialog;
// import android.os.Bundle;
// import android.content.Intent;
// import android.content.ContentValues;
// import android.content.DialogInterface;
// import android.database.Cursor;
// import android.view.Gravity;
// import android.view.Menu;
// import android.view.MenuItem;
// import android.widget.TextView;
//
// import java.io.Serializable;
// import java.util.Locale;
// import java.util.ArrayList;
// import java.util.List;
// import java.lang.Math;
//
// import android.text.InputType;
// import android.view.KeyEvent;
// import android.view.View;
// import android.view.ViewGroup;
// import android.view.inputmethod.InputMethodManager;
// import android.widget.RelativeLayout;
// import android.widget.EditText;
// import android.widget.TableRow;
// import android.widget.CheckBox;
// import android.view.MotionEvent;
//
// import android.widget.Button;
// import android.view.View.OnClickListener;
// import java.util.*;
//

public class MainActivity extends AppCompatActivity implements OnClickListener
{
    public static final int MENU_SELECT_LAST  = 0;
    public static final int MENU_SELECT_TEAM  = 1;
    public static final int MENU_SELECT_RESET = 2;

	Facilitator process = new Facilitator();

    @Override
    protected void onCreate(Bundle savedInstanceState) // アプリ初期化
	{
        super.onCreate(savedInstanceState);
		process.initViews();
    }

    public void onClick(View view) // 各ボタンタップ時の挙動
	{
        switch (view.getId()) {
            case R.id.buttonNum: // 人数決定
				process.inputNames();
                break;

            case R.id.buttonName: // 名前決定
				process.setPlayers();
                break;

            case R.id.radioAuto: // autoの画面表示
				process.inputNumTeam();
                break;

            case R.id.radioManual: // manualの画面表示
				process.inputTeams();
                break;

            case R.id.buttonTeam: // autoでチーム決定
				process.setTeamAuto();
                break;

            // case R.id.buttonStart:
            case R.id.buttonStartAuto: // スコア入力画面表示
				process.inputScores();
                break;

            // case R.id.buttonTeamManual:
            case R.id.buttonStartManual: // スコア入力画面表示
				process.setTeamManual();
                break;

            case R.id.buttonRate: // rate変更
				process.setRate();
                break;

            case R.id.buttonNext: // チーム分け画面表示
				if(process.setScores()){
					process.inputNumTeam();
				}
                break;

            case R.id.buttonFinish: // 結果画面表示
				if(process.setScores()){
					process.showResult();
				}
                break;

            case R.id.buttonExit:
                finish();
                break;

            default:
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
	{
        inputMethodManager.hideSoftInputFromWindow
			(mainLayout.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        mainLayout.requestFocus();

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
	{
        menu.add(0, MENU_SELECT_LAST,  1, "interim result");
        menu.add(0, MENU_SELECT_TEAM,  2, "reconfirm team");
        menu.add(0, MENU_SELECT_RESET, 3, "reset last scores");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) // 表示画面に移行
	{
        switch (item.getItemId()){
            case MENU_SELECT_LAST:
				process.showLastReslult();
				break;

            case MENU_SELECT_TEAM:
				process.showLastTeam();
                break;

            case MENU_SELECT_RESET:
				break;

            default:
				process.resetLastScore();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) // 戻るバタン押した時select_lastと同じ
	{
        if(keyCode != KeyEvent.KEYCODE_BACK){
            return super.onKeyDown(keyCode, event);
        }else{
			process.showLastReslult();
            return false;
        }
    }

    @Override
    protected void onPause() // 中断処理
	{
		super.onPause(); // 常にスーパクラスは最初に呼ぶ。
		process.writeDBLast();
    }
}; // class MainActivity


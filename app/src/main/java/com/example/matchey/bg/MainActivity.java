package com.example.matchey.bg;

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;

import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    public static final int MENU_SELECT_LAST  = 0;
    public static final int MENU_SELECT_TEAM  = 1;
    public static final int MENU_SELECT_RESET = 2;

	Facilitator process;

    @Override
    protected void onCreate(Bundle savedInstanceState) // アプリ初期化
	{
        super.onCreate(savedInstanceState);
        process = new Facilitator();

        Intent intent = getIntent();
        // Player[] players = (Player[]) intent.getSerializableExtra("PLAYER");
        // int counter = intent.getIntExtra("COUNT",1);
        // int nplayers = intent.getIntExtra("NUMBER",0);
        int base_rate = intent.getIntExtra("BASE", 10);
        double[] prob = intent.getDoubleArrayExtra("PROB");
        double[] ratio = intent.getDoubleArrayExtra("RATIO");
        process.setConfig(base_rate, prob, ratio);

		process.initViews(this);
    }

    public void onClick(View view) // 各ボタンタップ時の挙動
	{
        switch(view.getId()){
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
        process.onTouch();

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
				process.showLastResult();
				break;

            case MENU_SELECT_TEAM:
				process.showLastTeam();
                break;

            case MENU_SELECT_RESET:
				process.resetLastScores();
				break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) // 戻るバタン押した時select_lastと同じ
	{
        if(keyCode != KeyEvent.KEYCODE_BACK){
            return super.onKeyDown(keyCode, event);
        }else{
			process.showLastResult();
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


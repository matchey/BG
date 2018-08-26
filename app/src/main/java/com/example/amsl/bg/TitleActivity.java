package com.example.amsl.bg;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.view.View.OnClickListener;

public class TitleActivity extends AppCompatActivity implements OnClickListener{

    private Button button_newgame, button_history;
    private TextView text_title;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);

        mOldClickTime = System.currentTimeMillis();

        button_newgame=(Button)findViewById(R.id.buttonNewgame);
        button_newgame.setOnClickListener(this);

        button_history=(Button)findViewById(R.id.buttonHistory);
        button_history.setOnClickListener(this);

        text_title=(TextView)findViewById(R.id.textTitle);
        text_title.setOnClickListener(this);
    }
    //ボタンクリック時
    public void onClick(View v) {

        if(v==button_newgame){
            Intent intent = new Intent(this, MainActivity.class);
            startActivityForResult(intent, 0);
            finish();
        }else if(v==button_history){
            //if(!CommonUtils,isClickEvent()){
            Intent intent = new Intent(this, HistoryActivity.class);
            //intent.putExtra("NUMBER",num);
            //intent.putExtra("PLAYER",player);
            //intent.putExtra("COUNT",counter);
            startActivityForResult(intent, 0);
        }else if(v==text_title){
            if( isClickEvent() ){
                TextView tv = (TextView) findViewById(R.id.textHistory);
                tv.setText(" version\n pre alpha");
                //tv.setText("<-comming\n  \t  soon!");
                count = 0;
            }else{
                count++;
            }
            if(count > 0){
                count = 0;
                Intent intent = new Intent(this, AdminActivity.class);
                startActivityForResult(intent, 0);
            }
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
}

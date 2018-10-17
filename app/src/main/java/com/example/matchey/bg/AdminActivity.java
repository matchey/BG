package com.example.matchey.bg;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Context;
import android.content.Intent;
import android.view.inputmethod.InputMethodManager;
import android.view.View;
import android.widget.RelativeLayout;

public class AdminActivity extends AppCompatActivity implements View.OnclickListener
{
    private InputMethodManager inputMethodManager;
    private RelativeLayout mainLayout;

    private int ID_BEGIN = 64; // viewID of EditText
    private static final NRATIO = 5;
	private int base_rate = 10;
	private double prob[] = {0.05, 0.1, 0.15, 0.02, 0.0};
	private double ratio[] = {1.5, 2.0, 3.0, 5.0, 10.0};

    private int rate_tap_count = 0;		// count of taps

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        mainLayout = (RelativeLayout)findViewById(R.id.backGround);

        findViewById(R.id.textTap).setOnClickListener(this);

        // TextView taphere = (TextView)findViewById(R.id.textTap);
        // findViewById(R.id.buttonNum).setOnClickListener(this);
        // Button name_button = (Button)findViewById(R.id.buttonName);
        // name_button.setVisibility(View.GONE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
	{
        inputMethodManager.hideSoftInputFromWindow
                (mainLayout.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        mainLayout.requestFocus(); // tapでキーボードが消えるように

        return true;
    }

    public void onClick(View view)
    {
        switch(view.getId()){
            case R.id.textTap:
                setContentView(R.layout.activity_config);
                findViewById(R.id.buttonStart).setOnClickListener(this);
                findViewById(R.id.buttonBack).setOnClickListener(this);
                // ID_BEGIN = (EditText)findViewById(R.id.editConfig).getID();
                break;

            case R.id.buttonStart:
                if(setConfig()){
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra("BASE", base_rate);
                    intent.putExtra("PROB", prob);
                    intent.putExtra("RATIO", ratio);
                    startActivityForResult(intent, 0);
                    finish();
                }
                break;

            case R.id.buttonBack:
                Intent intent = new Intent(this, MainActivity.class);
                startActivityForResult(intent, 0);
                finish();
                break;

            default:
                break;
        }
    }

    private boolean setConfig() // EditTextが全部埋まってるか
    {
        int NITEMS = NRATIO * 2;

        for(int i = 0; i != NITEMS; ++i){
            EditText input = (EditText)findViewById(ID_BEGIN + i);
            if(input.getText().toString().equals("")){
                return false;
            }
        }

        for(int i = 0; i != NRATIO; ++i){
            EditText input = (EditText)findViewById(ID_BEGIN + i);
            prob[i] = Integer.parseInt(input);
        }

        for(int i = 0; i != NRATIO; ++i){
            EditText input = (EditText)findViewById(ID_BEGIN + NRATIO + i);
            ratio[i] = Integer.parseInt(input);
        }

        return true;
    }

    private toSetting()
    {
        if( isClickEvent() ){
            rate_tap_count = 0;
        }else{
            ++rate_tap_count;
        }
        if(rate_tap_count > 2){
            rate_tap_count = 0;
        }
    }

    private static final long CLICK_DELAY = 1000;
    private static long mOldClickTime = System.currentTimeMillis();
    private static boolean isClickEvent() // 連続タップ検出
    {
        long time = System.currentTimeMillis();
        if(time - mOldClickTime < CLICK_DELAY){
            return false;
        }
        mOldClickTime = time;
        return true;
    }
}


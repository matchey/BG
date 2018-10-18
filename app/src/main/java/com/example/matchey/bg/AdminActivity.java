package com.example.matchey.bg;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
// import android.widget.TableLayout;

import java.util.Locale;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener
{
    private InputMethodManager inputMethodManager;
    private RelativeLayout mainLayout;

    private static final int NRATIO = 5;
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
                if(toSetting()){
                    setContentView(R.layout.config_setting);
                    mainLayout = (RelativeLayout) findViewById(R.id.backGround);
                    findViewById(R.id.buttonStart).setOnClickListener(this);
                    findViewById(R.id.buttonClose).setOnClickListener(this);
                    // ID_BASE = (EditText)findViewById(R.id.editConfig).getID();
                }
                break;

            case R.id.buttonStart:
                if(setConfig()){
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra("BASE", base_rate);
                    intent.putExtra("PROB", prob);
                    intent.putExtra("RATIO", ratio);
                    startActivityForResult(intent, 0);
                    finish();
                }else {
                    resetConfig();
                }
                break;

            case R.id.buttonClose:
//                Intent intent = new Intent(this, MainActivity.class);
//                startActivityForResult(intent, 0);
                finish();
                break;

            default:
                break;
        }
    }

    private boolean setConfig() // EditTextが全部埋まってるか
    {
        String etAlias;
        int etId;
        EditText input;

        etId= R.id.base_rate;
        input = (EditText)findViewById(etId);
        if(input.getText().toString().equals("")){ return false; }
        base_rate = Integer.parseInt(input.getText().toString());

        for(int i = 0; i != NRATIO; ++i){
            etAlias = String.format(Locale.US, "prob%d", i);
            Log.v("string", etAlias);
            etId = getResources().getIdentifier(etAlias, "id", "com.example.matchey.bg");

            input = (EditText)findViewById(etId);
            if(input.getText().toString().equals("")){ return false; }
            prob[i] = Double.parseDouble(input.getText().toString());
        }

        for(int i = 0; i != NRATIO; ++i){
            etAlias = String.format(Locale.US, "ratio%d", i); // parseIntはできる...
            etId = getResources().getIdentifier(etAlias, "id", "com.example.matchey.bg");

            input = (EditText)findViewById(etId);
            if(input.getText().toString().equals("")){ return false; }
            ratio[i] = Double.parseDouble(input.getText().toString());
        }

        return true;
    }

    private void resetConfig()
    {
        int base_init = 10;
        double prob_init[] = {0.05, 0.1, 0.15, 0.02, 0.0};
        double ratio_init[] = {1.5, 2.0, 3.0, 5.0, 10.0};

        base_rate = base_init;
        prob = prob_init.clone();
        ratio = ratio_init.clone();
    }

    private boolean toSetting()
    {
        boolean rtn = false;

        if( isClickEvent() ){
            rate_tap_count = 0;
        }else{
            ++rate_tap_count;
        }
        if(rate_tap_count > 1){
            rate_tap_count = 0;
            rtn = true;
        }

        return rtn;
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


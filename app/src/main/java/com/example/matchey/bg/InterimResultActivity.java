package com.example.matchey.bg;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;
import android.content.Intent;

import java.util.Locale;

import static java.lang.Math.exp;

public class InterimResultActivity extends AppCompatActivity implements OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interim_result);

        Intent intent = getIntent();
        Player[] players = (Player[]) intent.getSerializableExtra("PLAYER");
        int counter = intent.getIntExtra("COUNT",1);
        int nplayers = intent.getIntExtra("NUMBER",0);
        //int[] handicap = intent.getIntArrayExtra("HANDICAP");

        if(counter<1){
            counter = 1;
        }

        findViewById(R.id.buttonBack).setOnClickListener(this);
        ViewGroup vg = (ViewGroup) findViewById(R.id.layoutInterim);

        if(nplayers > 0) {

            for (int i = 0; i < nplayers; i++) {
                // 行を追加
                getLayoutInflater().inflate(R.layout.interim_result, vg);
                // 文字設定

                TableRow tr = (TableRow) vg.getChildAt(i + 1);

                String str = String.format(Locale.getDefault(), "%s(%d)", players[i].getName(), players[i].getHandicap());
                //((TextView) (tr.getChildAt(0))).setText(player[i].name);
                ((TextView) (tr.getChildAt(0))).setText(str);

                str = String.format(Locale.getDefault(), "%4.1f", 1f * players[i].getAverage());
                //str = String.format(Locale.getDefault(), "%4.1f", 1f * player[i].average);
                ((TextView) (tr.getChildAt(1))).setText(str);

                str = String.format(Locale.getDefault(), "%5d yen", (int) players[i].getIncomeExpenditure());
                ((TextView) (tr.getChildAt(2))).setText(str);

                str = String.format(Locale.getDefault(), "%5d yen", (int) players[i].getSumIE());
                ((TextView) (tr.getChildAt(3))).setText(str);
            }
        }
    }
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.buttonBack:
                finish();
                break;
            default:
                break;
        }
    }
}

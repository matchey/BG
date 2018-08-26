package com.example.amsl.bg;

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
        Player[] player = (Player[]) intent.getSerializableExtra("PLAYER");
        int counter = intent.getIntExtra("COUNT",1);
        int num = intent.getIntExtra("NUMBER",0);
        //int[] handicap = intent.getIntArrayExtra("HANDICAP");

        if(counter<1){
            counter = 1;
        }

        findViewById(R.id.buttonBack).setOnClickListener(this);
        ViewGroup vg = (ViewGroup) findViewById(R.id.layoutInterim);

        if(num>0) {
            float ave = 0;
            float ave_s = 0;
            float max = player[0].get_ave();
            double ratio = 0;
            final double w0 = 0.0001;
            final double r  = 0.25;
            final double k  = 0.8;
            for (int i = 1; i < num; i++) {
                ave = player[i].get_ave();
                if (max < ave) {
                    max = ave;
                }
            }
            max = (int)max - (int)max % 10;
            for (int i = 0; i < num; i++) {
                // 行を追加
                getLayoutInflater().inflate(R.layout.interim_result, vg);
                // 文字設定
                ave = player[i].get_ave();
                ave_s = player[i].getAverage_s();
                ratio = k / (1 + ((k-w0)/w0)*exp(-r*(ave - ave_s)));
                int handicap = (int)(max - (ratio*ave_s + (1.0-ratio)*ave));
                handicap -= handicap%10;
                handicap = handicap<0 ? 0 : handicap;
                // int handicap = player[i].handi;
                // ave = player[i].average;
                // ave += (10.0f / 3.00001f) * 3f;
                // ave = (int)ave - (int)ave % 10;
                // int handicap = (int)max - (int)ave;
                // handicap = handicap < 0 ? 0 : handicap;
                TableRow tr = (TableRow) vg.getChildAt(i + 1);

                String str = String.format(Locale.getDefault(), "%s(%d)", player[i].name, handicap);
                //((TextView) (tr.getChildAt(0))).setText(player[i].name);
                ((TextView) (tr.getChildAt(0))).setText(str);

                str = String.format(Locale.getDefault(), "%4.1f", 1f * player[i].sum / counter);
                //str = String.format(Locale.getDefault(), "%4.1f", 1f * player[i].average);
                ((TextView) (tr.getChildAt(1))).setText(str);

                str = String.format(Locale.getDefault(), "%5d yen", (int) player[i].income_expenditure - (int) player[i].last_result);
                ((TextView) (tr.getChildAt(2))).setText(str);

                str = String.format(Locale.getDefault(), "%5d yen", (int) player[i].income_expenditure);
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

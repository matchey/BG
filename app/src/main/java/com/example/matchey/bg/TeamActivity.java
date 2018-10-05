package com.example.matchey.bg;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Locale;

public class TeamActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        findViewById(R.id.buttonBackToGame).setOnClickListener(this);

        Intent intent = getIntent();
        Player[] players = (Player[]) intent.getSerializableExtra("PLAYER");
        int num = intent.getIntExtra("p_NUM",0);
        int num_team = intent.getIntExtra("t_NUM",0);
        //int counter = intent.getIntExtra("COUNT",1);

        ViewGroup vg = (ViewGroup) findViewById(R.id.layoutTeamConfirm);

        //TableLayout tableLayout = new TableLayout(this);
        //setContentView(tableLayout, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));

        if(num > 0) {
            for (int i = 0; i < 1; i++) {
                TableRow tableRow = new TableRow(this);
                for (int iCol = 0; iCol < num_team; iCol++) {
                    TextView text = new TextView(this);
                    text.setText(String.format(Locale.getDefault(), "team%d", iCol + 1));
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
                    text.setText(players[i].getName());
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
        }
    }
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.buttonBackToGame:
                finish();
                break;
            default:
                break;
        }
    }
}

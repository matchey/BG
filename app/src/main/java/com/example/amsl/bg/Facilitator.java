package com.example.amsl.bg;

import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

class Facilitator
{
    private int nplayer = 0; // number of players
    private int nteam = 0;   // number of teams
	private int game_count = 0;		// count of games
	private int rate_tap_count = 0;		// count of taps
	private int count4reset = 0;		// count of games
	private static final ID_BEGIN = 64; // viewID of EditText
	private static final ID_NAME  = 1;  // viewID of EditText
	private static final ID_TEAM  = 2;  // viewID of EditText
	private static final ID_SCORE = 3;  // viewID of EditText

    private InputMethodManager inputMethodManager;
    private RelativeLayout mainLayout;
    private SQLiteDatabase db;
	private Random rand = new Random();
    public static Toast toast;

	private Player[] players;
	private Calculator calc = new Calculator();

	Facilitator(){}

	void initViews()
	{
        setContentView(R.layout.set_player);

        inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        mainLayout = (RelativeLayout)findViewById(R.id.backGround);

        findViewById(R.id.buttonNum).setOnClickListener(this);
        Button name_button = (Button)findViewById(R.id.buttonName);
        name_button.setVisibility(View.GONE);
	}

	void inputNames()
	{
		EditText input = (EditText)findViewById(R.id.editNum);
		String inputStr = input.getText().toString();
		if(inputStr.length() != 0){
			nplayer = Integer.parseInt(inputStr);
		}
		if(nplayer > 0){
			calc.setNumPlayer(nplayer);

			inputMethodManager.hideSoftInputFromWindow
							(mainLayout.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			mainLayout.requestFocus();
			// TableLayoutのグループを取得
			ViewGroup vg = (ViewGroup)findViewById(R.id.layoutName);
			for(int i = 0; i < nplayer; ++i){
				// 行を追加
				getLayoutInflater().inflate(R.layout.edit_add, vg);
				// 文字設定
				TableRow tr = (TableRow)vg.getChildAt(i);
				String str = String.format(Locale.getDefault(), "input player%d's name", i + 1);
				((EditText)(tr.getChildAt(0))).setHint(str);

				((EditText)(tr.getChildAt(0))).setInputType(InputType.TYPE_CLASS_TEXT);
				((EditText)(tr.getChildAt(0))).setId(ID_BEGIN*ID_NAME + i);
			}
			Button start_button = (Button)findViewById(R.id.buttonName);
			start_button.setVisibility(View.VISIBLE);
			start_button.setOnClickListener(this);
		}
	}

	void setPlayers()
	{
		if(checkFill(ID_NAME)){
			players = new Player[nplayer];
			for(int i = 0; i < nplayer; ++i){
				players[i] = new Player();
                String str = ((EditText)findViewById(ID_BEGIN*ID_NAME + i)).getText().toString();
                players[i].setTeam(Integer.parseInt(str));
			}
			inputNumTeam();
		}
	}

	void inputNumTeam()
	{
		setContentView(R.layout.set_team_auto);
		mainLayout = (RelativeLayout)findViewById(R.id.backGround);
		findViewById(R.id.radioManual).setOnClickListener(this);
		findViewById(R.id.radioAuto).setOnClickListener(this);
		findViewById(R.id.buttonTeam).setOnClickListener(this);
		findViewById(R.id.buttonStart).setOnClickListener(this);
		if(nteam != 0){
			((EditText)findViewById(R.id.editText)).setText(String.valueOf(nteam));
		}
		Button start_button = (Button)findViewById(R.id.buttonStart);
		start_button.setVisibility(View.GONE);
	}

	void inputTeams() // 2列にする name ___ 
	{
		setContentView(R.layout.set_team_manual);
		mainLayout = (RelativeLayout)findViewById(R.id.backGround);
		findViewById(R.id.buttonTeamManual).setOnClickListener(this);
		findViewById(R.id.radioAuto).setOnClickListener(this);
		findViewById(R.id.radioManual).setOnClickListener(this);
		// TableLayoutのグループを取得
		ViewGroup vg = (ViewGroup)findViewById(R.id.layoutTeamManual);
		for(int i = 0; i < nplayer; ++i){
			// 行を追加
			getLayoutInflater().inflate(R.layout.edit_add, vg);
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
		EditText input = (EditText)findViewById(R.id.editText);
		String inputStr = input.getText().toString();
		if(inputStr.length() != 0){
			nteam = Integer.parseInt(inputStr);
		}
		if(!(nteam < 1 || nplayer < nteam)){
			inputMethodManager.hideSoftInputFromWindow
							(mainLayout.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			mainLayout.requestFocus();

			assignTeam();

			ViewGroup vg = (ViewGroup)findViewById(R.id.layoutTeamAuto);

			for(int i = 0; i < 1; ++i){
				TableRow tableRow = new TableRow(this);
				for(int iCol = 0; iCol < nteam; ++iCol){
					TextView text = new TextView(this);
					text.setText(String.format(Locale.getDefault(), "team%d", iCol+1));
					tableRow.addView(text);
				}
				vg.addView(tableRow);
			}
			int i = 0;
			while(true){
				TableRow tableRow = new TableRow(this);
				for(int iCol = 0; iCol < nteam; ++iCol){
					TextView text = new TextView(this);
					text.setText(players[i].getName());
					tableRow.addView(text);
					++i;
					if(i == nplayer){
						break;
					}
				}
				vg.addView(tableRow);
				if(i == nplayer){
					i = 0;
					break;
				}
			}
			Button start_button = (Button)findViewById(R.id.buttonStart);
			start_button.setVisibility(View.VISIBLE);
			start_button.setOnClickListener(this);
		}
	}

	void setTeamManual() // string ならPlayerには0~nで割り当て
	{
		if(checkFill(ID_TEAM)){
            for(int i = 0; i < nplayer; ++i){
                String str = ((EditText)findViewById(ID_BEGIN*ID_TEAM + i)).getText().toString();
                players[i].setTeam(Integer.parseInt(str));
            }
			inputScores();
		}
	}

	void inputScores()
	{
		inputScore(false);
	}

	void inputScores(boolean flag_reset)
	{
		++count4reset;
		calc.setRate();
		setContentView(R.layout.set_score);
		mainLayout = (RelativeLayout)findViewById(R.id.backGround);
		findViewById(R.id.buttonNext).setOnClickListener(this);
		findViewById(R.id.buttonFinish).setOnClickListener(this);
		findViewById(R.id.checkHandi).setOnClickListener(this);
		rate_button = (Button)findViewById(R.id.buttonRate);
		rate_button.setOnClickListener(this);
		rate_button.setText(String.format(Locale.getDefault(), "x %d", calc.getRate()));
		textView = (TextView)findViewById(R.id.textCnt);
		textView.setText(String.format(Locale.getDefault(),
					"input player's score of %d %s game", game_count+1, ordinalNum(game_count+1)));
		vg = (ViewGroup)findViewById(R.id.layoutScore);
		for(int i = 0; i < nplayer; ++i){
			getLayoutInflater().inflate(R.layout.edit_add, vg); // 行を追加
			TableRow tr = (TableRow)vg.getChildAt(i); // 文字設定
			if(flag_reset){
				String str = String.format(Locale.getDefault(),
							"input %s's score (%d)", players[i].getName(), players[i].getScratch());
			}else{
				String str = String.format(Locale.getDefault(),
								"input %s's score", players[i].getName());
			}
			((EditText)(tr.getChildAt(0))).setHint(str);
			((EditText)(tr.getChildAt(0))).setInputType(InputType.TYPE_CLASS_NUMBER);
			((EditText)(tr.getChildAt(0))).setId(ID_BEGIN*ID_SCORE + i);
		}
	}

	void setRate()
	{
		if(toast != null){
			toast.cancel();
		}
		calc.setRate();
		rate_button = (Button)findViewById(R.id.buttonRate);
		rate_button.setText(String.format(Locale.getDefault(), "x %d", calc.getRate()));
		if( isClickEvent() ){
			rate_tap_count = 0;
			toast = Toast.makeText(MainActivity.this,
					"can't change rate in this ver (auto set)", Toast.LENGTH_SHORT);
			toast.show();
		}else{
			toast.cancel();
			++rate_tap_count;
		}
		if(rate_tap_count > 2){
			rate_tap_count = 0;
			final EditText editView = new EditText(MainActivity.this);
			editView.setInputType(InputType.TYPE_CLASS_NUMBER);
			editView.setGravity(Gravity.CENTER);
			new AlertDialog.Builder(MainActivity.this)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setTitle("change rate")
				.setView(editView) //setViewにてビューを設定
				.setPositiveButton("OK", new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int whichButton){
						//入力した文字をトースト出力する
						String inputStr = editView.getText().toString();
						if(inputStr.length() != 0){
							calc.setRate(Integer.parseInt(inputStr));
							Button rate_button = (Button)findViewById(R.id.buttonRate);
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
			if( ((CheckBox)findViewById(R.id.checkHandi)).isChecked() ){
				// for(int i = 0;i < nplayer; ++i){
				calc.setHandicap(); // 入力できるようにする
				// }
			}
            for(int i = 0; i < nplayer; i++){
                String str = ((EditText)findViewById(ID_BEGIN*ID_SCORE + i)).getText().toString();
                players[i].setScratch(Integer.parseInt(str));
            }
			++game_count;
			calc.setCount(game_count);
			calc.teamCalc();
			calc.playerCalc();

			writeDBPersonal();

			return true;
		}else{
			return false;
		}
	}

	void showReslut()
	{
		setContentView(R.layout.show_result);

		Calendar cal = Calendar.getInstance();       //カレンダーを取得

		int iYear = cal.get(Calendar.YEAR);         //年を取得
		int iMonth = cal.get(Calendar.MONTH) + 1;       //月を取得
		int iDate = cal.get(Calendar.DATE);         //日を取得

		String strDay = iYear + "/" + iMonth + "/" + iDate + " :    ";
		String strGames = game_count + " games";
		String strDay_games = strDay + strGames;
		((TextView)findViewById(R.id.textDate)).setText(strDay_games);

		findViewById(R.id.buttonExit).setOnClickListener(this);
		vg = (ViewGroup)findViewById(R.id.layoutResult);
		for(int i = 0; i < nplayer; i++){
			getLayoutInflater().inflate(R.layout.result, vg);
			TableRow tr = (TableRow)vg.getChildAt(i+1);
			((TextView)(tr.getChildAt(0))).setText(players[i].getName());
			String str = String.format(Locale.getDefault(), "%4.1f", players[i].getAveScratch());
			((TextView)(tr.getChildAt(1))).setText(str);
			str = String.format(Locale.getDefault(),
												"%5d yen", (int)players[i].getIncomeExpenditure());
			((TextView)(tr.getChildAt(2))).setText(str);
		}
	}

	void showLastReslult() // 前回の収支確認
	{
		Intent intent = new Intent(this, InterimResultActivity.class);
		intent.putExtra("NUMBER", nplayer);
		intent.putExtra("PLAYER", players);
		intent.putExtra("COUNT",  game_count);
		startActivityForResult(intent, 0);
	}

	void showLastTeam()
	{
		Intent intent_team = new Intent(this, TeamActivity.class);
		intent_team.putExtra("p_NUM", nplayer);
		intent_team.putExtra("t_NUM", nteam);
		intent_team.putExtra("PLAYER", players);
		startActivityForResult(intent_team, 0);
	}

	void resetLastScore()
	{
		if( (game_count == count4reset) && (game_count > 0) ){
			--game_count;
			--count4reset;
			inputScores(true);
			deleteDBLast();
			calc.resetLastScore();
		}
	}

	void writeDBLast()
	{
        if(game_count > 0){
			MySQLiteOpenHelper helper = new MySQLiteOpenHelper(this); // インスタンス作成
			db = helper.getWritableDatabase(); // 読み書き出来るように開く
			ContentValues updateValues = new ContentValues();
			updateValues.put("delete_flg", 1);
			db.update("personal_data", updateValues, "delete_flg=?", new String[] { "0" });
			Cursor cursor;
			for(int i = 0; i < nplayer; ++i){
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
    private boolean checkFill(int type) // EditTextが全部埋まってるか
	{
        boolean flag = false;
        for(int i = 0;i < nplayer; ++i){
            EditText input = (EditText)findViewById(ID_BEGIN*type + i);
            if(input.getText().toString().equals("")){
                break;
            }
            if(i == nplayer - 1){
                flag = true;
            }
        }
        return flag;
	}

    private void assignTeam() // throws IOException
    {
        int team = 1;

        shuffle(players, nperson);

        for(int i = 0; i < nperson; ++i){
            players[i].setTeam(team);
            team += 1;
            if(team > nteam){
                team = 1;
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
        MySQLiteOpenHelper helper = new MySQLiteOpenHelper(this); // インスタンス作成
        db = helper.getWritableDatabase(); // 読み書き出来るように開く
        Cursor cursor; // レコードを検索してカーソルを作成
        for(int i = 0; i < nplayer; i++){
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
        MySQLiteOpenHelper helper = new MySQLiteOpenHelper(this); // インスタンス作成
        db = helper.getWritableDatabase(); // 読み書き出来るように開く
        Cursor cursor; // レコードを検索してカーソルを作成
        for(int i = 0; i < nplayer; i++){
            cursor = db.query("personal_data", new String[]{"_id", "name", "result", "ave",
                    "count", "over_flg"}, "name = ?", new String[]{ players[i].getName() },
																		null, null, null);
            List<Double>  rec_ave = new ArrayList<Double>();
            List<Integer> rec_cnt = new ArrayList<Integer>();
            List<Integer> rec_res = new ArrayList<Integer>();
            List<Integer> rec_flg = new ArrayList<Integer>();

            if(cursor.moveToNext()){
				rec_ave.add(cursor.getFloat(cursor.getColumnIndex("ave")));
				rec_cnt.add(cursor.getInt(cursor.getColumnIndex("count")));
				rec_res.add(cursor.getInt(cursor.getColumnIndex("result")));
				rec_flg.add(cursor.getInt(cursor.getColumnIndex("over_flg")));

				float average;
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
				db.update("personal_data", values, "name=?", new String[] { player[i].get_name() });
            }
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
};


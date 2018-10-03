package com.example.amsl.bg;

import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

class Facilitator
{
    private int nplayer = 0; // number of players
    private int nteam = 0; // number of teams
	private int game_count = 0;		// count of games

    private InputMethodManager inputMethodManager;
    private RelativeLayout mainLayout;
    private SQLiteDatabase db;
	private Random rand = new Random();
    public static Toast toast;

	private Player[] players;
	private Calculator calc = new Calculator();

	Facilitator(){
	}

	void initViews()
	{
        setContentView(R.layout.set_player);

        inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        mainLayout = (RelativeLayout)findViewById(R.id.backGround);

        findViewById(R.id.buttonNum).setOnClickListener(this);
        Button name_button = (Button)findViewById(R.id.buttonName);
        name_button.setVisibility(View.GONE);
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
			for(int i = 0; i < nplayer; i++) {
				cursor = db.query("personal_data", new String[]{"_id", "name"},
				       		"name = ?", new String[]{ players[i].getName() }, null, null, null);
				ContentValues values = new ContentValues();
				values.put("last_ave", players[i].getAveScratch());
				values.put("last_res", players[i].getIncomeExpenditure());
				values.put("delete_flg", 0);
				if (cursor.moveToNext()) { // カーソルから値を取り出す
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

	void inputNames()
	{
	}

	void setPlayers()
	{
	}

	void inputTeams()
	{
	}

	void inputNumTeam()
	{
	}

	void setTeamManual()
	{
		// string ならPlayerには0~nで割り当て
	}

	void setTeamAuto()
	{
		// layout幅 -> 文字サイズ小さくするか, スクロールできるようにするか
	}

	void inputScores()
	{
	}

	void setRate()
	{
	}

	void setScores()
	{
	}

	void showReslut()
	{
	}

	void showLastReslult()
	{
	}

	void showLastTeam()
	{
	}

	void resetLastScore()
	{
		calc.resetLastScore();
	}

	// private
    // private boolean nextPage(int start_id) // 名前が人数分入っていたら
    // private boolean gamePage(int start_id) // 全員にチーム入力されていたら
    // private boolean gameNext(int start_id) // 全員分スコアが入力されていたら
	private void writeDBPersonal()
	{
	}
    // private void set_db_each_score() // ゲームごとにDBに追加
    // private static final long CLICK_DELAY = 1000;
    // private static long mOldClickTime;
    // private static boolean isClickEvent() // 連続タップ検出
	// {
    //     long time = System.currentTimeMillis();
    //     if(time - mOldClickTime < CLICK_DELAY){
    //         return false;
    //     }
    //     mOldClickTime = time;
    //     return true;
    // }
    // private String ordinal_num(int number)
	// {
    //     int check = number % 10;
    //     String rtn = "th";
    //     switch (check){
    //         case 1:
    //             rtn = "st";
    //             break;
    //         case 2:
    //             rtn = "nd";
    //             break;
    //         case 3:
    //             rtn = "rd";
    //             break;
    //         default:
    //             break;
    //     }
    //     return rtn;
    // }
    // private void set_team_auto()// throws IOException
    // {
    //     int team_num = num_team;//number of teams
    //     int team = 1;
    //     shuffle(player, nperson);
    //     // shuffle<Player>(player, nperson);
    //     for(int i=0;i<nperson;i++){
    //         player[i].set_team(team);
    //         team += 1;
    //         if(team > team_num){
    //             team = 1;
    //         }
    //     }
    // }
    // private static <T> void shuffle(T ary[], int size)
    // {
    //
    //     for(int i = size - 1; i > 0; i--){
    //         int j = rand.nextInt(i + 1);
    //         if(i != j) {
    //             T t = ary[i];
    //             ary[i] = ary[j];
    //             ary[j] = t;
    //         }
    //     } // Fisher–Yates
    // }
};


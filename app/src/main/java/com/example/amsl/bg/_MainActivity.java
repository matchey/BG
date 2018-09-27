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
// import android.database.sqlite.SQLiteDatabase;
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
// import android.widget.Toast;
// import java.util.*;
//
// import static java.lang.Math.exp;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private InputMethodManager inputMethodManager;
    private RelativeLayout mainLayout;
    private SQLiteDatabase db;

    private int nperson = 0; // number of players
    private boolean num_flag = true;
    private boolean team_flag = true;
    private boolean auto_flag = true;
    private int num_team = 0;
    private int counter = 0;
    public Integer[] base_rate = {10};
    public Integer[] rate = {10};
    public int rate_count = 0;

    public int[] player_points = new int[20]; //for one game
    public int[] handicap = new int[20]; //for one game
    Map <Integer, Pair<Integer, Integer> > team_points = new HashMap< Integer, Pair<Integer,Integer> >(); //<"team name", "team score, number of members">

    public static final int MENU_SELECT_LAST = 0;
    public static final int MENU_SELECT_TEAM = 1;

    public static Toast toast;

    Player[] player; // = new Player[20]; // Facilitatorの中で動的に確保

    @Override
    protected void onCreate(Bundle savedInstanceState) {
		// アプリ初期化
    }

    @Override
    protected void onPause() {
		// 中断処理
    }

    public void onClick(View view){ // 各ボタンタップ時の挙動
        switch (view.getId()) {
            case R.id.buttonNum:
				// 人数決定
                break;
            case R.id.buttonName:
				// 名前決定
                break;
            case R.id.radioManual:
				// manualの画面表示
                break;
            case R.id.radioAuto:
				// autoの画面表示
                break;
            case R.id.buttonTeam:
				// autoでチーム決定
                break;
            case R.id.buttonStart:
				// スコア入力画面表示
                break;
            case R.id.buttonTeamManual:
				// スコア入力画面表示
                break;
            case R.id.buttonRate:
				// rate変更
                break;
            case R.id.buttonNext:
				// チーム分け画面表示
                break;
            case R.id.buttonFinish:
				// 結果画面表示
                break;
            case R.id.buttonExit:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        inputMethodManager.hideSoftInputFromWindow(mainLayout.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        mainLayout.requestFocus();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){ // option menuにexit追加しよう
        menu.add(0,MENU_SELECT_LAST,1,"interim result");
        //menu.add(0,MENU_SELECT_AVE,2,"show average");
        menu.add(0,MENU_SELECT_TEAM,2,"reconfirm team");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){ // 表示画面に移行
        switch (item.getItemId()){
            case MENU_SELECT_LAST:
                Intent intent = new Intent(this, InterimResultActivity.class);
                intent.putExtra("NUMBER",nperson);
                intent.putExtra("PLAYER",player);
                intent.putExtra("COUNT",counter);
                startActivityForResult(intent, 0);
                break;
            case MENU_SELECT_TEAM:
                Intent intent_team = new Intent(this, TeamActivity.class);
                intent_team.putExtra("p_NUM",nperson);
                intent_team.putExtra("t_NUM",num_team);
                intent_team.putExtra("PLAYER",player);
                //intent_team.putExtra("COUNT",counter);
                startActivityForResult(intent_team, 0);
                break;
            //case MENU_SELECT_AVE:
            //    break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event){ // 戻るバタン押した時select_lastと同じ
        if(keyCode != KeyEvent.KEYCODE_BACK){
            return super.onKeyDown(keyCode, event);
        }else{
            Intent intent = new Intent(this, InterimResultActivity.class);
            intent.putExtra("NUMBER",nperson);
            intent.putExtra("PLAYER",player);
            intent.putExtra("COUNT",counter);
            //intent.putExtra("HANDICAP", handicap);
            startActivityForResult(intent, 0);
            return false;
        }
    }

    private boolean nextPage(int start_id){ // 名前が人数分入っていたら
        boolean flag = false;
        for(int i=0;i<nperson;i++) {
            EditText input = (EditText) findViewById(i+start_id);
            if(input.getText().toString().equals("")){
                break;
            }
            if(i==nperson-1){
                //Intent intent = new Intent(getApplication(), MainActivity.class);
                //startActivity(intent);
                //setContentView(R.layout.page_change);
                flag = true;
            }
        }
        //int strtID = start_id;
        if(flag) {
            for (int i = 0; i < nperson; i++) {
                String str = ((EditText) findViewById(i + start_id)).getText().toString();
                player[i].setName(str);
            }
            // MyIO.set("");
        }
        return flag;
    }

    private boolean gamePage(int start_id){ // 全員にチーム入力されていたら
        boolean flag = false;
        for(int i=0;i<nperson;i++) {
            EditText input = (EditText) findViewById(i+start_id);
            if(input.getText().toString().equals("")){
                break;
            }
            if(i==nperson-1){
                flag = true;
            }
        }
        if(flag) {
            for (int i = 0; i < nperson; i++) {
                String str = ((EditText) findViewById(i + start_id)).getText().toString();
                player[i].set_team(Integer.parseInt(str));
            }
        }
        return flag;
    }

    private boolean gameNext(int start_id){ // 全員分スコアが入力されていたら
        boolean flag = false;
        for(int i=0;i<nperson;i++) {
            EditText input = (EditText) findViewById(i+start_id);
            if(input.getText().toString().equals("")){
                break;
            }
            if(i==nperson-1){
                flag = true;
            }
        }
        if(flag) {
            for (int i = 0; i < nperson; i++) {
                String str = ((EditText) findViewById(i + start_id)).getText().toString();
                player[i].setScore(Integer.parseInt(str));
            }
        }
        return flag;
    }

    public void set_db_each_score(){ // ゲームごとにDBに追加
        // インスタンス作成
        MySQLiteOpenHelper helper = new MySQLiteOpenHelper(this);
        // 読み書き出来るように開く
        db = helper.getWritableDatabase();
        //ContentValues updateValues = new ContentValues();
        // レコードを検索してカーソルを作成
        Cursor cursor;
        for(int i=0; i<nperson; i++) {
            cursor = db.query("personal_data", new String[]{"_id", "name", "result", "ave",
                    "count", "over_flg"}, "name = ?", new String[]{ player[i].getName() }, null, null, null);
            List<Float> rec_ave = new ArrayList<Float>();
            List<Integer> rec_cnt = new ArrayList<Integer>();
            List<Integer> rec_res = new ArrayList<Integer>();
            List<Integer> rec_flg = new ArrayList<Integer>();
            int last_income = (int)player[i].income_expenditure -player[i].last_result;
            // カーソルから値を取り出す
            if (cursor.moveToNext()) {
                rec_ave.add(cursor.getFloat(cursor.getColumnIndex("ave")));
                rec_cnt.add(cursor.getInt(cursor.getColumnIndex("count")));
                rec_res.add(cursor.getInt(cursor.getColumnIndex("result")));
                rec_flg.add(cursor.getInt(cursor.getColumnIndex("over_flg")));
                int game_cnt = rec_cnt.get(0);
                float average;
                if(rec_flg.get(0)==1){
                    average = (60f * rec_ave.get(0) + player[i].scratch) / 61f;
                }else{
                    average = (1f * game_cnt * rec_ave.get(0) + player[i].scratch) / (game_cnt + 1);
                }
                int result = rec_res.get(0) + last_income;
                String scr = "scr"+game_cnt;
                String res = "res"+game_cnt;
                ContentValues values = new ContentValues();
                values.put("ave", average);
                values.put("result", result);
                values.put(scr, player[i].scratch);
                values.put(res, last_income);
                if(game_cnt==59){
                    values.put("over_flg", 1);
                }
                values.put("count", (game_cnt +1)%60 );
                db.update("personal_data", values, "name=?", new String[] { player[i].getName() });
            }else{
                ContentValues values = new ContentValues();
                values.put("name", player[i].getName());
                values.put("ave", player[i].scratch);
                values.put("result", last_income);
                values.put("scr0", player[i].scratch);
                values.put("res0", last_income);
                values.put("count", 1);
                db.insert("personal_data", null, values);
            }
            cursor.close();
        }
    }

    private static final long CLICK_DELAY = 1000;
    private static long mOldClickTime;
    public static boolean isClickEvent(){ // 連続タップ検出
        long time = System.currentTimeMillis();
        if(time - mOldClickTime < CLICK_DELAY){
            return false;
        }
        mOldClickTime = time;
        return true;
    }

    private String ordinal_num(int number){
        int check = number % 10;
        String rtn = "th";
        switch (check){
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

    public static <T> void shuffle(T ary[], int size)
    {
        Random rand  = new Random();

        for(int i = size - 1; i > 0; i--){
            int j = rand.nextInt(i + 1);
            if(i != j) {
                T t = ary[i];
                ary[i] = ary[j];
                ary[j] = t;
            }
        } // Fisher–Yates
    }

    void set_team_auto()// throws IOException
    {
        int team_num = num_team;//number of teams
        int team = 1;
        shuffle(player, nperson);
        // shuffle<Player>(player, nperson);
        for(int i=0;i<nperson;i++){
            player[i].set_team(team);
            team += 1;
            if(team > team_num){
                team = 1;
            }
        }
    }

    int set_rate(int base_rate)
    {
        int rate = base_rate;
        // boolean flag = false;
        if(Math.random()*10 < 2){
            if(Math.random()*10 < 2){
                rate += 5;
            }else if(Math.random()*10 < 3){
                rate += 10;
            }else{
                rate += 20;
            }
            if(Math.random()*100 < 2){
                rate = 50;
            }
            // flag = true;
        }

        return rate;
    }

    void set_handi(int count)
    {
        for(int i=0; i<nperson; i++){ //remove latest score
            player[i].add_point(-player_points[i]);
            team_points.get( player[i].getTeam() ).first -= player_points[i];
            team_points.get( player[i].getTeam() ).second -= 1;
        }
        calc_handi(count);
        for(int i=0; i<nperson; i++){
            player_points[i] += handicap[i];
            player[i].add_point(player_points[i]);
            team_points.get( player[i].getTeam() ).first += player_points[i];
            team_points.get( player[i].getTeam() ).second += 1;
        }
    }

    void team_calc(int count, boolean handi_flag)// throws IOException
    {
		// teamの合計計算
    }

    void player_calc(int rate)
	{
        int sign = 1;
        int score_abs = 0;
        int player_inout[] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}; //each player's income and expenditure
        int n = 0; //use in for loop
        int sum = 0; //check total +-

        if (rate < 0) {
            rate = 0;
        }

        if (num_team < 3) {
            rate *= 2;
        }

        p_sort(player, nperson, player_points, (sign+1!=0));

        for(Map.Entry<Integer, Pair<Integer,Integer>> entry : team_points.entrySet()){ //calc each player's income and expenditure
            score_abs = entry.getValue().first;
            if(rate % 10 == 0){
                score_abs *= rate/10;
            }else if(rate % 10 == 5){
                score_abs = score_abs*(rate + 5)/10 - score_abs/2;
            }
            sum += score_abs;
            sign = score_abs < 0 ? -1 : 1;
            score_abs *= sign; //to absolute
            while( score_abs>0 ){
                for(int i=0; score_abs!=0 && i<nperson ; i++){
                    n = sign+1!=0 ? i : nperson-1-i;
                    if( player[n].getTeam() == entry.getKey() ){
                        score_abs -= 1;
                        player_inout[n] += sign;
                    }
                }
            }
        }

        if(sum < 0){
            player_inout[0] -= sum;
        }else if(sum > 0){
            player_inout[nperson-1] -= sum;
        }

        for(int i=0;i<nperson;i++){
            player[i].last_result = (int)player[i].income_expenditure;
            if(rate % 10 == 0){
                player[i].add_income_expenditure(player_inout[i] * 10);
            }else if(rate % 10 == 5){
                player[i].add_income_expenditure(player_inout[i] * 10);
            }else{
                player[i].add_income_expenditure(player_inout[i] * rate);
            }
        }

        p_sort(player, nperson, player_points, (sign+1!=0));
    }

    void calc_handi(int count)
    {
        if(count==1) {
            for(int i=1;i<nperson;i++){
                player[i].average = 167;
            }
        }
        float ave = 0;
        float ave_s = 0;
        float max = player[0].getAverage();
        double ratio = 0;
        final double w0 = 0.7;
        final double r  = 0.3;
        final double k  = 0.85;
        for(int i=1;i<nperson;i++){
            ave = player[i].getAverage();
            if(max < ave){
                max = ave;
            }
        }
        // max = (int)max - (int)max%10;
        for(int i=0; i<nperson; i++){
            ave = player[i].getAverage();
            ave_s = player[i].getAverage_s();
            ratio = k / (1 + ((k-w0)/w0)*exp(-r*count));
            handicap[i] = (int)(max - (ratio*ave_s + (1.0-ratio)*ave));
            handicap[i] -= handicap[i]%10;
            handicap[i] = handicap[i]<0 ? 0 : handicap[i];
        }
    }

    void p_sort(Player[] player, int size, int[] points, boolean reverse)
    {
        Player p;
        int point;
        int sign = reverse ? 1:-1; //true:DESCENDING, false:ASCENDING;
        for(int i=0; i<size; i++){
            for(int j=i; j<size; j++){
                if(sign * points[i] < sign * points[j]){
                    p = player[i];
                    point = points[i];

                    player[i] = player[j];
                    points[i] = points[j];

                    player[j] = p;
                    points[j] = point;
                }
            }
        }
    }

    ArrayList<Integer> sort_by_sn(Map <Integer, Pair<Integer, Integer> > team_points, ArrayList<Integer> team_name, int s_n)//score_numOFmem:0_1
    {
        int tmp; //sort swap tempolary
        boolean flag = false;
        Collections.sort(team_name); //sort in ascending order by name ([1]1,[2]2,[0]3,...)
        int len = team_name.size();


        ArrayList<Integer> t_name=new ArrayList<Integer>();

        for(int i=0;i<len;i++){t_name.add(team_name.get(i));}

        switch(s_n){
            case 0: //sort in ASCENDING order by SCORE
                break;
            case 1: //sort in DESCENDING order by NUMBER OF MEMBERS
                break;
            default:
                break;
        };

        for(int i=0;i<len;i++){
            team_name.set(i, t_name.get(i));
        }

        return t_name;
    }
}; // class MainActivity


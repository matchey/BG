
class Facilitator
{
    private int nplayer = 0; // number of players
    private int nteam = 0; // number of teams
	Player[] players;
	Calculator calc = new Calculator();

	// ++Player.game_count;

	Facilitator(){
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
    private boolean gamePage(int start_id){ // 全員にチーム入力されていたら
    private boolean gameNext(int start_id){ // 全員分スコアが入力されていたら
    private void set_db_each_score(){ // ゲームごとにDBに追加
    private static final long CLICK_DELAY = 1000;
    private static long mOldClickTime;
    private static boolean isClickEvent(){ // 連続タップ検出
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
    private void set_team_auto()// throws IOException
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
    private static <T> void shuffle(T ary[], int size)
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
};


package com.example.matchey.bg;

import java.lang.Math;
import static java.lang.Math.exp;

class Calculator
{
	private int rate = 10;
	private int base_rate = 10;
	private static final double prob[] = {0.05, 0.12, 0.2, 0.01, 0.0};
	private static final double ratio[] = {1.5, 2.0, 3.0, 5.0, 10.0};
	// private int game_count = 0;
	private int nplayers = 0;

	private Map<int, Team> teams = new HashMap<int, Team>;

	int getRate() { return rate; }

	void setRate()
	{
        rate = base_rate;

		int size = prob.length;

		double sum = 0.0;
		for(int i = 0; i != size; ++i){
			sum += prob[i];
		}

		double t = Math.random() * sum;

		double p = 0.0;
		for(int i = 0; i != size; ++i){
			p += prob[i];
			if(t <= p){
				rate = (int)(ratio[i] * base_rate);
				break;
			}
		}
	}

	void setRate(int base)
	{
		base_rate = base;
		setRate();
	}

	// void setCount(int counter)
	// {
	// 	game_count = counter;
	// }

	void setNumPlayer(int num)
	{
		nplayers = num;
	}

	void setHandicap(Player[] players)
	{
		double max = players[0].getAveScratch();
		for(int i = 1; i != nplayers; ++i){
			if(max < players[i].getAveScratch()){
				max = players[i].getAveScratch();
			}
		}

		for(int i = 0; i != nplayers; ++i){
			int handicap = (int)(max - players[i].getAveScratch());
			handicap -= handicap % 10;
			players[i].setHandicap(handicap);
		}
	}

	void teamCalc(Player[] players)
	{
		teams.clear();

		for(int i = 0; i != nplayers; ++i){ // Map teamsを用意
			Team team = new Team();
			teams.put(players[i].getTeam(), team);
		}

		for(int i = 0; i != nplayers; ++i){ // それぞれのチーム合計スコアをセット
			teams.get(players[i].getTeam()).addScore(players[i].getScore());
		}

		ArrayList<int> sorted = new ArrayList<>();

		// int team_id = 0;
		int score_sum = 0;
		int nplayers_max = 0;
		for(Entry<int, Team> t: teams.entrySet()){ // 人数maxをセット
			if(nplayers_max < t.getValue().getNumPlayer()){
				nplayers_max = t.getValue().getNumPlayer();
			}
			score_sum += t.getValue().getSum();
			// t.getValue().setId(team_id++);
			sorted.add(t.getKey());
		}

		double score_ave = 1.0 * score_sum / nplayers; // 全チームのアベをセット

		int sum_IE = 0;
		for(Team t : teams){ // チームごとの収支をセット
			double ie = rate * (t.getAverage() - score_ave) * nplayers_max;
			t.setIE(ie);
			sum_IE += t.getIE();
		}

		// 過不足を調整
		sign = Integer.signum(sum_IE); // import java.lang.Integer.signum
		sort(teams, sorted, sign);
		while(sum_IE != 0){
			for(Entry<int, Team> t: teams.entrySet()){
				t.getValue.addIE(-sign);
			}
			sum_IE -= sign;
		}
	}

	void playerCalc(Player[] players)
	{
		Arrays.sort(players, (a,b)-> a.getScore() - b.getScore());

		for(Entry<int, Team> t: teams.entrySet()){
			int ie_abs = t.getSign() * t.getIE();
			while(0 != ie_abs){
				for(int i = 0; i != nplayers; ++i){
					int id = i;
					if(t.getSign() < 0){
						id = (nplayers - 1) - i; // 逆順
					}
					if(t.getKey() == players[i].getTeam()){
						players[i].addIE(t.getValue().getSign());
						--ie_abs;
					}
				}
			}
		}
	}
	
	// private
	private void sort(Map<int, Team> map, ArrayList<int> arr, int predicate)
	{
		int nteams = arr.size();
		// bubble sort
		for(int i = 0; i < nteams-1; ++i){
			for(int j = i+1; j < nteams; ++j){
				if( 0 < predicate * (map[arr[i]] - map[arr[j]]) ){
					int tmp = arr[i];
					arr[i] = arr[j];
					arr[j] = tmp;
				}
			}
		}
	}

	// private void sort(Player[] players)
	// {
	// 	// bubble sort
	// 	for(int i = 0; i < nplayers-1; ++i){
	// 		for(int j = i+1; j < nplayers; ++j){
	// 			if(){
	// 				Player tmp = players[i].clone();
	// 			}
	// 		}
	// 	}
	// }
};


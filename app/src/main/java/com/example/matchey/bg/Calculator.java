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
	private int nplayer = 0;

	private Map<int, Team> teams = new HashMap<int, Team>;

	int getRate() { return rate; }

	void setRate()
	{
        rate = base_rate;

		int size = prob.length;

		double sum = 0.0;
		for(int i = 0; i < size; ++i){
			sum += prob[i];
		}

		double t = Math.random() * sum;

		double p = 0.0;
		for(int i = 0; i < size; ++i){
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
		nplayer = num;
	}

	void setHandicap(Player[] players)
	{
		double max = players[0].getAveScratch();
		for(int i = 1; i < nplayer; ++i){
			if(max < players[i].getAveScratch()){
				max = players[i].getAveScratch();
			}
		}

		for(int i = 0; i < nplayer; ++i){
			int handicap = (int)(max - players[i].getAveScratch());
			handicap -= handicap % 10;
			players[i].setHandicap(handicap);
		}
	}

	void teamCalc(Player[] players)
	{
		teams.clear();

		Team team = new Team();
		for(int i = 0; i < nplayer; ++i){ // Map teamsを用意
			teams.put(players[i].getTeam(), team);
		}


		for(int i = 0; i < nplayer; ++i){ // それぞれのチーム合計スコアをセット
			teams.get(players[i].getTeam()).addScore(players[i].getScore());
		}

		ArrayList<int> sorted = new ArrayList<>();

		int score_sum = 0;
		int nplayer_max = 0;
		for(Entry<int, Team> t: map.entrySet()){ // 人数maxをセット
			if(nplayer_max < t.getValue().getNumPlayer()){
				nplayer_max = t.getValue().getNumPlayer();
			}
			score_sum += t.getValue().getSum();
			sorted.add(t.getKey());
		}

		double score_ave = 1.0 * score_sum / nplayer; // 全チームのアベをセット

		int sum_IE = 0;
		for(Team t : teams){ // チームごとの収支をセット
			double ie = rate * (t.getAverage() - score_ave) * nplayer_max;
			t.setIE(ie);
			sum_IE += t.getIE();
		}

		// 過不足を調整
	}

	void playerCalc(Player[] players)
	{
		for(Entry<int, Team> t: map.entrySet()){
			if(t.getSign() < 0){
				Arrays.sort(players, (a,b)-> b.getScore() - a.getScore());
			}else{
				Arrays.sort(players, (a,b)-> a.getScore() - b.getScore());
			}
			for(int i = 0; i < nplayer; ++i){
				if(t.getKey() == players[i].getTeam()){
					players[i].addIE(t.getValue().getSign());
				}
			}
		}
	}
	
	// private
	// private void sort(Player[] players)
	// {
	// 	// bubble sort
	// 	for(int i = 0; i < nplayer-1; ++i){
	// 		for(int j = i+1; j < nplayer; ++j){
	// 			if(){
	// 				Player tmp = players[i].clone();
	// 			}
	// 		}
	// 	}
	// }
};


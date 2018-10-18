package com.example.matchey.bg;

// import android.util.Log;
import android.util.SparseArray;
import java.lang.Math;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

class Calculator
{
	private int rate = 10;
	private int base_rate = 10;
	private double prob[] = {0.05, 0.1, 0.15, 0.02, 0.0};
	private double ratio[] = {1.5, 2.0, 3.0, 5.0, 10.0};
	private int nplayers = 0;
	private int nteams = 0;

	private SparseArray<Team> teams = new SparseArray<>();

	int getRate() { return rate; }

	void setConfig(int _b, double[] _p, double[] _r)
	{
		base_rate = _b;
		prob = _p.clone();
		ratio = _r.clone();
	}

	void setRate()
	{
        rate = base_rate;

		int size = prob.length;

		double sum = 0.0;
		for(int i = 0; i != size; ++i){
			sum += prob[i];
		}

		if(sum > 1){
            for(int i = 0; i != size; ++i){
                prob[i] /= sum;
            }

        }

		double t = Math.random();

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

	void setNumPlayer(int num)
	{
		nplayers = num;
	}

	void setHandicap(Player[] players)
	{
        int range = 10; // const

		double ave_min = players[0].getAveScratch();
		double ave_max = players[0].getAveScratch();
		for(int i = 1; i != nplayers; ++i){
			if(ave_min > players[i].getAveScratch()){
				ave_min = players[i].getAveScratch();
			}
			if(ave_max < players[i].getAveScratch()){
				ave_max = players[i].getAveScratch();
			}
		}

        double diff_max = ave_max - ave_min;

        if(range < diff_max){
            for(int i = 0; i != nplayers; ++i){
                double diff = max - players[i].getAveScratch();
                double target = max - range * diff / diff_max;
                int handicap = (int)(target - players[i].getAveScratch());
                handicap -= handicap % 10;
                players[i].setHandicap(handicap);
            }
        }else{
            setHandicap(players, 0);
        }
	}

	void setHandicap(Player[] players, int handicap)
    {
		for(int i = 0; i != nplayers; ++i){
			players[i].setHandicap(handicap);
		}
    }

	void teamCalc(Player[] players)
	{
		teams.clear();

		for(int i = 0; i != nplayers; ++i){ // Map teamsを用意
			teams.put(players[i].getTeam(), new Team());
		}

		nteams = teams.size();

		for(int i = 0; i != nplayers; ++i){ // それぞれのチーム合計スコアをセット
			teams.get( players[i].getTeam() ).addScore( players[i].getScore() ); // teamから検索
		}

		List<Integer> sorted = new ArrayList<>();

		int score_sum = 0;
		int nplayers_max = 0;
		for(int i = 0; i != nteams; ++i){ // 人数maxをセット
			if(nplayers_max < teams.valueAt(i).getNumPlayer()){
				nplayers_max = teams.valueAt(i).getNumPlayer();
			}
			score_sum += teams.valueAt(i).getSum();
			sorted.add(teams.keyAt(i));
		}

		double score_ave = 1.0 * score_sum / nplayers; // 全チームのアベをセット

		int sum_IE = 0;
		for(int i = 0; i != nteams; ++i){ // チームごとの収支をセット
			double ie = rate * (teams.valueAt(i).getAverage() - score_ave)
					* nplayers_max / teams.valueAt(i).getNumPlayer() / 10.0;
			if(nteams == 2) {
				ie *= 2.0;
			}
			teams.valueAt(i).setIE(ie);
			sum_IE += teams.valueAt(i).getIE();
		}

		// 過不足を調整
		int sign = Integer.signum(sum_IE);
		sort(teams, sorted, sign);
		boolean flag = (sum_IE != 0);
		while(flag){
			for(int team : sorted){
				teams.get(team).addIE(-sign);
				sum_IE -= sign;
				flag = (sum_IE != 0);
				if(!flag){
					break;
				}
			}
		}
	}

	void playerCalc(Player[] players)
	{
		int[] ie = new int[nplayers];

		// Arrays.sort(players, (a,b)-> a.getScore() - b.getScore());
		Arrays.sort(players, playerComparator);

		for(int i = 0; i != nteams; ++i){
			int sign = teams.valueAt(i).getSign();
			int ie_abs =  sign * teams.valueAt(i).getIE();
			boolean flag = (ie_abs != 0);
			while(flag){
				int id = sign < 0 ? nplayers - 1 : 0;
				for(int loop_counter = 0; flag && loop_counter != nplayers; ++loop_counter){
					if(teams.keyAt(i) == players[id].getTeam()){
						ie[id] += sign;
						--ie_abs;
						flag = (ie_abs != 0);
					}
					id += sign;
				}
			}
		}

		for(int i = 0; i != nplayers; ++i){
			players[i].setIncomeExpenditure(ie[i] * 10);
		}
	}
	
	// private
	private Comparator<Player> playerComparator = new Comparator<Player>()
	{
		@Override
		public int compare(Player p1, Player p2)
		{
			return p2.getScore() - p1.getScore();
		}
	};

	private void sort(SparseArray<Team> t, List<Integer> names,  int predicate)
	{
		// bubble sort
		for(int i = 0; i != nteams-1; ++i){
			for(int j = i+1; j != nteams; ++j){
				if( 0 < predicate * (t.get(names.get(i)).getIE() - t.get(names.get(j)).getIE()) ){
					int tmp = names.get(i);
					names.set(i, names.get(j)) ;
					names.set(j, tmp);
				}
			}
		}
	}
};


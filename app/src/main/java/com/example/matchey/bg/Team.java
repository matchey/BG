package com.example.matchey.bg;

class Team
{
    // private int name;			// team's name
    private int nplayer = 0;		// number of players in team
    private int sum = 0; 			// team's score sum
    private double average = 0.0;	// team's score average (include handicap)
    private int IE = 0;				// sum of income and expenditure
	private	int sign = 0;			// sign of income and expenditure

    // Team(int n){ name = n; }
	// void init(){
	// 	nplayer = 0;
	// 	sum = 0;
	// 	average = 0;
	// 	sum_IE = 0;
	// }

    void addScore(int x)
	{
		++nplayer;
		sum += x;
		average = 0.0;
	}

	void setIE(double x)
	{
		if(x < 0){
			sign = -1;
		}else if(x > 0){
			sign = 1;
		}else{
			sign = 0;
		}

		IE = (int)(x + sign * 0.9);
	}

	int getNumPlayer(){ return nplayer; }

	int getSum(){ return sum; }

    double getAverage()
	{
		if(average == 0 && nplayer > 0){
			average = 1.0 * sum / nplayer;
		}

		return average;
	}

	int getIE(){ return IE; }
};


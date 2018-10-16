package com.example.matchey.bg;

// Calculatorから呼び出すclass
class Team
{
    // private int id;					// 0, 1, ..., nteams
    private int nplayers = 0;		// number of players in team
    private int sum = 0; 			// team's score sum
    private double average = 0.0;	// team's score average (include handicap)
    private int IE = 0;				// sum of income and expenditure
	private	int sign = 0;			// sign of income and expenditure

    // Team(int n){ name = n; }
	// void init(){
	// 	nplayers = 0;
	// 	sum = 0;
	// 	average = 0;
	// 	sum_IE = 0;
	// }
	// void setId(int id)
	// {
	// 	id = x;
	// }

    void addScore(int x)
	{
		++nplayers;
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

	void addIE(int x)
	{
		IE += x;
	}

	int getNumPlayer(){ return nplayers; }

	int getSum(){ return sum; }

    double getAverage()
	{
		if(average == 0 && nplayers > 0){
			average = 1.0 * sum / nplayers;
		}

		return average;
	}

	int getIE(){ return IE; }

	int getSign(){ return sign; }
};


package com.example.amsl.bg;

class Player implements Serializable
{
    private String name;			// player's name
    private int team;				// player's team
    private int scratch;			// player's latest scratch score
    private int score;				// player's latest score (include handicap)
    private int sum_scratch = 0;	// player's scratch sum
    private int sum = 0; 			// player's score sum
    private double ave_scratch;		// player's score average (scratch)
    private double average;			// player's score average (include handicap)
    private int handicap = 0;		// player's latest handicap
    private int income_expenditure;	// last income and expenditure
    private int sum_IE = 0;			// sum of income and expenditure

    Player()
	{
		// scratch = 0;
		// score = 0;
		// sum_scratch = 0;
		// sum = 0;
		// income_expenditure = 0;
	}

    String getName(){ return name; }
    int getTeam(){ return team; }
    // int getScore(){ return score; }
    double getAverage(){ return average; }
    double getAveScratch(){ return ave_scratch; }
	int getIncomeExpenditure(){ return income_expenditure; }
	 
    void setName(String x){ name = x; }
    void setTeam(int x){ team = x; }
    void setHandicap(int x){ handicap = x; }

    void setScratch(int x, int game_count)
	{
		scratch = x;
		score = scratch + handicap;
		sum_scratch += scratch;
		sum += score;
		ave_scratch = 1.0 * sum_scratch / game_count;
		average = 1.0 * sum / game_count;
	}

    void setIncomeExpenditure(int x)
	{
		income_expenditure = x;
		sum_IE += income_expenditure;
	}
};


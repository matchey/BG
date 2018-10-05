package com.example.matchey.bg;

import java.lang.Math;
import static java.lang.Math.exp;

class Calculator
{
	private int rate = 10;
	private int base_rate = 10;
	private static final double prob[] = {0.05, 0.12, 0.2, 0.01, 0.0};
	private static final double ratio[] = {1.5, 2.0, 3.0, 5.0, 10.0};
	private int game_count = 0;
	private int nplayer = 0;

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

	void setCount(int counter)
	{
		game_count = counter;
	}

	void setNumPlayer(int num)
	{
		nplayer = num;
	}

	void teamCalc(Player[] players)
	{
	}

	void playerCalc(Player[] players)
	{
	}
	
	void setHandicap(Player[] players)
	{
	}

	void calcHandicap(Player[] players)
	{
	}

	void resetLastScore(Player[] players)
	{
	//     --countしてsum_IEからlast引いて新しいのをセットし直す.
	}
};


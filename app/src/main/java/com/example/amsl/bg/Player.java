
class Player implements Serializable
{
    String name;//player name
    int points;//player's latest score
    int scratch;//player's latest scratch score
    int team;//player's team
    float income_expenditure;//income and expenditure
    int last_result;//income and expenditure
    float average; //player's score average(include handicap)
    float average_s; //player's score average(scratch)
    int handicap;//player's latest handicap
    int sum; //player's score sum

    Player(){ points = 0; scratch = 0; sum = 0; };
    String get_name(){return name;};
    int get_points(){return points;};
    int get_team(){return team;};
    float get_ave(){return average;};
    float getAverage_s(){return average_s;};
    int get_sum(){return sum;};
    void set_points(int x){ points = x;};
    void set_name(String x){ name = x; };
    void set_team(int x){ team = x;};
    void add_income_expenditure(int x){ income_expenditure += x; };
    void add_point(int x){ points += x; };
    void add_sum(int x){ sum += x; };

    int input_point()// throws IOException
    {
        return points;
    }

    float calc_ave(int count)
    {
        average = ( (1.0f*count) * average + points ) / (count + 1);
        average_s = ( (count -1.0f ) * average_s + scratch ) / count;
        return average;
    }
};


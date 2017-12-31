#ifndef GAMEANALYZER_H
#define GAMEANALYZER_H
#include <vector>
#include <iostream>

using namespace std;

class GameAnalyzer
{
public:
    GameAnalyzer();
    static const vector<long unsigned int> longLines;
    static bool hasEnded(long board);
    static bool isWon(long board);
    static bool isLost(long board);
};

#endif // GAMEANALYZER_H

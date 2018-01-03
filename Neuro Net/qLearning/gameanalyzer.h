#ifndef GAMEANALYZER_H
#define GAMEANALYZER_H
#include <vector>
#include <iostream>

using namespace std;

class GameAnalyzer
{
public:
    GameAnalyzer();
    static const vector<uint64_t> longLines;
    static bool hasEnded(long boardP1, long boardP2);
    static bool isWon(long boardP1);
};

#endif // GAMEANALYZER_H

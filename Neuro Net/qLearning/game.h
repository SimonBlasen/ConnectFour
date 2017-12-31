#ifndef GAME_H
#define GAME_H

#include "qplayer.h"
#include "gameanalyzer.h"
#include <iostream>

using namespace std;

class Game
{
public:
    Game();
    void reset();
    void gameLoop();
    static void play();


private:

    static const int gamesAmount;
    double score;
    bool isNewGame;
    bool isP1;
    long board;
    QPlayer player;

};

#endif // GAME_H

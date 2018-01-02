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

    static const int GAMES_AMOUNT;
    double score;
    bool isNewGame;
    bool isP1;

    //long board;
    long boardP1;
    long boardP2;
    QPlayer player1;
    QPlayer player2;

};

#endif // GAME_H

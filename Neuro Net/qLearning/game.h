#ifndef GAME_H
#define GAME_H

#include "qplayer.h"
#include "gameanalyzer.h"
#include <iostream>
#include <fstream>

using namespace std;

class Game
{
public:
    Game();
    void reset();
    void gameLoop();
    static void play();
    void visualizeUnity(bool p1sMove, long move);


private:

    static const int PRINT_INFO_BATCHES;
    int print_info_counter;

    int p1_won;
    int p2_won;


    static const int GAMES_AMOUNT;
    float scoreP1;
    float scoreP2;
    bool isNewGame;
    bool isP1;

    //long board;
    long boardP1;
    long boardP2;
    QPlayer player1;
    QPlayer player2;

};

#endif // GAME_H

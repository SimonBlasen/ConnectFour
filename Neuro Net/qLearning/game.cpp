#include "game.h"
const int Game::GAMES_AMOUNT = 10;
Game::Game()
{
    this->scoreP1 = 0;
    this->scoreP2 = 0;
    this->isNewGame = true;
    this->isP1 = true; // needed?
    this->boardP1 = 0x0L;
    this->boardP2 = 0x0L;
}

void Game::reset(){
    scoreP1 = 0;
    scoreP2 = 0;
    isNewGame = true;
    boardP1 = 0x0L;
    boardP2 = 0x0L;
}

void Game::play(){
    Game g;
    for(int i = 0; i < GAMES_AMOUNT; i++){
        while (!GameAnalyzer::hasEnded(g.boardP1, g.boardP2)) {
            //visualize?
            //DEBUG
            cout << "Board = " <<  g.boardP1 << endl;
            g.gameLoop();
        }
        cout << "has ended" << endl;
        g.reset();

        g.player1.applyReward(g.scoreP1,g.boardP1,g.boardP2,g.isNewGame);
        g.player2.applyReward(g.scoreP2,g.boardP2,g.boardP1,g.isNewGame);
    }
}


void Game::gameLoop(){

    long move = 0;

    if(isP1){
        if(GameAnalyzer::isWon(boardP1)){
            scoreP1 += 1;
        }
        else if(GameAnalyzer::isWon(boardP2)){
            scoreP1 += -1;
        }
    }
    else{
        if(GameAnalyzer::isWon(boardP2)){
            scoreP2 += 1;
        }
        else if(GameAnalyzer::isWon(boardP1)){
            scoreP2 += -1;
        }
    }


    if(isP1){
        player1.applyReward(scoreP1,boardP1,boardP2,isNewGame);
        move = player1.getInput(scoreP1,boardP1,boardP2,isNewGame);
        boardP1 = boardP1 | move;
    }
    else{
        player2.applyReward(scoreP2,boardP2,boardP1,isNewGame);
        move = player2.getInput(scoreP2,boardP2,boardP1,isNewGame);
        boardP2 = boardP2 | move;
    }

    //Debug
    if(isP1){
        cout << "Current Player = 1" << endl;
    }
    else{
        cout << "Current Player = 2" << endl;
    }

    cout << "Coosen move = " << move << endl;




    if(isNewGame){
        isNewGame = false;
    }
    isP1 = !isP1;
}





#include "game.h"
const int Game::GAMES_AMOUNT = 1;
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
            cout << g.boardP1 << endl;
            g.gameLoop();
        }
    }
}


void Game::gameLoop(){

    long move = 0;

    if(isP1){
        move = player1.getInput(scoreP1,boardP1,boardP2,isNewGame);
        boardP1 = boardP1 | move;
    }
    else{
        move = player2.getInput(scoreP2,boardP2,boardP1,isNewGame);
        boardP2 = boardP2 | move;
    }

    //Debug
    cout << "Current Player = " << isP1 ? "1"  : "2" << endl;
    cout << "Coosen move = " << move << endl;

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



    if(isNewGame){
        isNewGame = false;
    }
    isP1 = !isP1;
}





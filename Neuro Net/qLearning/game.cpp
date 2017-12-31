#include "game.h"
const int Game::gamesAmount = 1;

Game::Game()
{
    this->score = 0;
    this->isNewGame = true;
    this->isP1 = true; // needed?
    this->board = 0x0L;
}

void Game::reset(){
    this->board = 0x0L;
    this->score = 0;
    this->isNewGame = true;
}

void Game::play(){
    Game g;
    for(int i = 0; i < gamesAmount; i++){
        while (!GameAnalyzer::hasEnded(g.board)) {
            //visualize?
            cout << g.board << endl;
            g.gameLoop();
        }
    }
}


void Game::gameLoop(){

    long move = player.getInput();

    //Debug
    cout << "Coosen move = " << move << endl;
    cout << "Board = " << board << endl;

    board  = board | move;

    if(GameAnalyzer::isWon(board)){
        score += 1;
    }
    else if(GameAnalyzer::isLost(board)){
        score += -1;
    }

    if(isNewGame){
        isNewGame = false;
    }
}





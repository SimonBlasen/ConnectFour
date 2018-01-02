#include "game.h"
const int Game::GAMES_AMOUNT = 1;
Game::Game()
{
    this->score = 0;
    this->isNewGame = true;
    this->isP1 = true; // needed?
    this->boardP1 = 0x0L;
    this->boardP2 = 0x0L;
}

void Game::reset(){
    this->score = 0;
    this->isNewGame = true;
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
        move = player1.getInput(score,boardP1,boardP2,isNewGame);
        boardP1 = boardP1 | move;
    }
    else{
        move = player2.getInput(score,boardP2,boardP1,isNewGame);
        boardP2 = boardP2 | move;
    }

    //Debug
    cout << "Coosen move = " << move << endl;



    if(GameAnalyzer::isWon(boardP1, boardP2)){
        score += 1;
    }
    else if(GameAnalyzer::isLost(boardP1, boardP2)){
        score += -1;
    }

    if(isNewGame){
        isNewGame = false;
    }
    isP1 = !isP1;
}





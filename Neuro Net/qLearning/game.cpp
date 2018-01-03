#include "game.h"
const int Game::GAMES_AMOUNT = 1000000;

const int Game::PRINT_INFO_BATCHES = 100;



Game::Game()
{
    this->scoreP1 = 0;
    this->scoreP2 = 0;
    this->isNewGame = true;
    this->isP1 = true; // needed?
    this->boardP1 = 0x0L;
    this->boardP2 = 0x0L;

    print_info_counter = 0;

    p1_won = 0;
    p2_won = 0;


    srand (time(NULL));
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
            //cout << "Board = " <<  g.boardP1 << endl;
            g.gameLoop();
            g.print_info_counter++;
            if (g.print_info_counter >= Game::PRINT_INFO_BATCHES)
            {
                g.print_info_counter = 0;
                cout << "Info" << endl;
                cout << "Win rate:    " << g.p1_won << ":" << g.p2_won << endl;
                cout << "Runs:        " << g.player1.getRuns() <<  endl;
            }
        }



        //if(g.isP1){
            if(GameAnalyzer::isWon(g.boardP1)){
                g.scoreP1 += 1;
                g.scoreP2 += -1;
                g.p1_won++;
            }
            else if(GameAnalyzer::isWon(g.boardP2)){
                g.scoreP1 += -1;
                g.scoreP2 += 1;
                g.p2_won++;
            }
        /*}
        else{
            if(GameAnalyzer::isWon(g.boardP2)){
                g.scoreP2 += 1;
                g.p2_won++;
            }
            else if(GameAnalyzer::isWon(g.boardP1)){
                g.scoreP2 += -1;
                g.p1_won++;
            }
        }*/





        //cout << "has ended" << endl;

        g.player1.applyReward(g.scoreP1,g.boardP1,g.boardP2,g.isNewGame);
        g.player2.applyReward(g.scoreP2,g.boardP2,g.boardP1,g.isNewGame);




        //cout << "Ich bin keine Nikon" << endl;





        g.reset();

       /*
        std::ofstream openedFile;
        openedFile.open("unity-visualisation-file.txt");
        openedFile << "4,4\n";
        openedFile.close();
        */
    }
}


void Game::gameLoop(){

    long move = 0;


    if(isP1){
        player1.applyReward(scoreP1,boardP1,boardP2,isNewGame);
        //cout << "Ich bin keine Nikon" << endl;
        move = player1.getInput(scoreP1,boardP1,boardP2,isNewGame);
        boardP1 = boardP1 | move;
    }
    else{
        player2.applyReward(scoreP2,boardP2,boardP1,isNewGame);
        //cout << "Ich bin keine Nikon" << endl;
        move = player2.getInput(scoreP2,boardP2,boardP1,isNewGame);
        boardP2 = boardP2 | move;
    }



    //visualizeUnity(isP1, move);

    //Debug
    if(isP1){
        //cout << "Current Player = 1" << endl;
    }
    else{
        //cout << "Current Player = 2" << endl;
    }

    //cout << "Coosen move = " << move << endl;




    if(isNewGame){
        isNewGame = false;
    }
    isP1 = !isP1;



    //Sleep(10);
}


void Game::visualizeUnity(bool p1sMove, long move)
{
    std::ofstream openedFile;
    openedFile.open("unity-visualisation-file.txt", std::ios_base::app);

    int index = std::log2l(move);
    int x = index % 4;
    int y = (index / 4) % 4;
    int z = index / 16;

    //cout << "Index: " << index << " -> (" << x << "," << y << "," << z << ")" << endl;

    openedFile << (p1sMove ? "0" : "1") << "," << x << "," << y << "," << z << "\n";

    openedFile.close();
}

























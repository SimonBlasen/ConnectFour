#ifndef MEMORYITEM_H
#define MEMORYITEM_H
#include <vector>
#include <iostream>

using namespace std;

class MemoryItem
{
public:
    MemoryItem(double reward, long oldState, long newState);


    double getReward() const;
    void setReward(double value);

    long getOldInput() const;
    void setOldInput(long board);

    long getNewInput() const;
    void setNewInput(long board);

private:
    double mReward;
    long mOldInput;
    long mNewInput;

};

#endif // MEMORYITEM_H

#ifndef MEMORYITEM_H
#define MEMORYITEM_H
#include <vector>
#include <iostream>

using namespace std;

class MemoryItem
{
public:
    MemoryItem(double reward, long oldStateOwn, long oldStateEnemy, long newStateOwn, long newStateEnemy);




    long getNewStateEnemy() const;
    void setNewStateEnemy(long value);

    long getNewStateOwn() const;
    void setNewStateOwn(long value);

    long getOldStateEnemy() const;
    void setOldStateEnemy(long value);

    long getOldStateOwn() const;

    double getReward() const;
    void setReward(double reward);

private:
    double mReward;
    long mOldStateOwn;
    long mOldStateEnemy;
    long mNewStateOwn;
    long mNewStateEnemy;

};

#endif // MEMORYITEM_H

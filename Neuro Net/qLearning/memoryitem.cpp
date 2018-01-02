#include "memoryitem.h"

MemoryItem::MemoryItem(double reward, long oldStateOwn, long oldStateEnemy, long newStateOwn, long newStateEnemy)
{
    mReward = reward;
    mOldStateOwn = oldStateOwn;
    mOldStateEnemy = oldStateEnemy;
    mNewStateOwn = newStateOwn;
    mNewStateEnemy = newStateEnemy;


}


long MemoryItem::getNewStateEnemy() const
{
    return mNewStateEnemy;
}

void MemoryItem::setNewStateEnemy(long value)
{
    mNewStateEnemy = value;
}

long MemoryItem::getNewStateOwn() const
{
    return mNewStateOwn;
}

void MemoryItem::setNewStateOwn(long value)
{
    mNewStateOwn = value;
}

long MemoryItem::getOldStateEnemy() const
{
    return mOldStateEnemy;
}

void MemoryItem::setOldStateEnemy(long value)
{
    mOldStateEnemy = value;
}

long MemoryItem::getOldStateOwn() const
{
    return mOldStateOwn;
}

double MemoryItem::getReward() const
{
    return mReward;
}

void MemoryItem::setReward(double reward)
{
    mReward = reward;
}

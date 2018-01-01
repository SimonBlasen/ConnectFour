#include "memoryitem.h"

MemoryItem::MemoryItem(double reward, long oldState, long newState)
{
    mReward = reward;
    mOldInput = oldState;
    mNewInput = newState;

}

double MemoryItem::getReward() const
{
    return mReward;
}

void MemoryItem::setReward(double value)
{
    mReward = value;
}

long MemoryItem::getOldInput() const
{
    return mOldInput;
}

void MemoryItem::setOldInput(long board)
{
    mOldInput = board;
}

long MemoryItem::getNewInput() const
{
    return mNewInput;
}

void MemoryItem::setNewInput(long board)
{
    mNewInput = board;
}

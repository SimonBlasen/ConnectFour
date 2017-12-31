#include "memoryitem.h"

memoryItem::memoryItem(double reward, vector<int> oldState, vector<int> newState)
{
    mReward = reward;
    mOldInput = oldState;
    mNewInput = newState;

}

double memoryItem::getReward() const
{
    return mReward;
}

void memoryItem::setReward(double value)
{
    mReward = value;
}

vector<int> memoryItem::getOldInput() const
{
    return mOldInput;
}

void memoryItem::setOldInput(const vector<int> &value)
{
    mOldInput = value;
}

vector<int> memoryItem::getNewInput() const
{
    return mNewInput;
}

void memoryItem::setNewInput(const vector<int> &value)
{
    mNewInput = value;
}

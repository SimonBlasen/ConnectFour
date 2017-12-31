#ifndef MEMORYITEM_H
#define MEMORYITEM_H
#include <vector>
#include <iostream>

using namespace std;

class memoryItem
{
public:
    memoryItem(double reward, vector<int> oldState, vector<int> newState);


    double getReward() const;
    void setReward(double value);

    vector<int> getOldInput() const;
    void setOldInput(const vector<int> &value);

    vector<int> getNewInput() const;
    void setNewInput(const vector<int> &value);

private:
    double mReward;
    vector<int> mOldInput;
    vector<int> mNewInput;

};

#endif // MEMORYITEM_H

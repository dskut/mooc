/*
Social network connectivity. 
Given a social network containing N members and a log file 
containing M timestamps at which times pairs of members 
formed friendships, design an algorithm to determine the 
earliest time at which all members are connected (i.e., every 
member is a friend of a friend of a friend ... of a friend). 
Assume that the log file is sorted by timestamp and that friendship 
is an equivalence relation. The running time of your algorithm 
should be MlogN or better and use extra space proportional to N
*/

#include <iostream>
#include <vector>
using namespace std;

struct TFriendship {
    int First;
    int Second;
    int When;

    TFriendship(int first, int second, int when)
        : First(first)
        , Second(second)
        , When(when)
    {}
};

class TSocialGraph {
private:
    vector<int> Parents;
    vector<int> Sizes;

    int Root(int person) {
        while (Parents[person] != person) {
            int parent = Parents[person];
            Parents[person] = Parents[parent];
            person = parent;
        }
        return person;
    }

    void Union(int first, int second) {
        int firstRoot = Root(first);
        int secondRoot = Root(second);
        if (firstRoot == secondRoot) {
            return;
        }
        if (Sizes[firstRoot] >= Sizes[secondRoot]) {
            Parents[secondRoot] = firstRoot;
            Sizes[firstRoot] += Sizes[secondRoot];
        } else {
            Parents[firstRoot] = secondRoot;
            Sizes[secondRoot] += Sizes[firstRoot];
        }
    }

    bool IsConnected() {
        return Sizes[Root(0)] == Parents.size();
    }

public:
    TSocialGraph(size_t persons)
        : Parents(persons, 0)
        , Sizes(persons, 1)
    {
        for (size_t i = 0; i < persons; ++i) {
            Parents[i] = i;
        }
    }

    int GetConnectionTime(const vector<TFriendship>& fr) {
        for (int i = 0; i < fr.size(); ++i) {
            Union(fr[i].First, fr[i].Second);
            if (IsConnected()) {
                return fr[i].When;
            }
        }
        return -1;
    }
};
int main() {
    TSocialGraph graph(7);
    vector<TFriendship> friendships;
    friendships.push_back(TFriendship(0, 1, 1));
    friendships.push_back(TFriendship(3, 4, 2));
    friendships.push_back(TFriendship(3, 2, 3));
    friendships.push_back(TFriendship(5, 6, 4));
    friendships.push_back(TFriendship(4, 0, 5));
    friendships.push_back(TFriendship(2, 1, 6));
    friendships.push_back(TFriendship(2, 6, 7));
    friendships.push_back(TFriendship(3, 5, 8));
    friendships.push_back(TFriendship(2, 5, 9));
    friendships.push_back(TFriendship(2, 4, 10));
    friendships.push_back(TFriendship(0, 6, 11));
    cout << graph.GetConnectionTime(friendships) << endl; // 7
    return 0;
}

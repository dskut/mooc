
#include <iostream>
#include <vector>
using namespace std;

class TSet {
private:
    vector<int> Parents;
    vector<int> Sizes;
    vector<int> Maxs;

    int Root(int x) {
        while (Parents[x] != x) {
            int parent = Parents[x];
            Parents[x] = Parents[parent];
            x = parent;
        }
        return x;
    }

public:
    TSet(size_t size)
        : Parents(size)
        , Sizes(size, 1)
        , Maxs(size)
    {
        for (size_t i = 0; i < size; ++i) {
            Parents[i] = i;
            Maxs[i] = i;
        }
    }

    void Remove(int x) {
        int root = Root(x);
        int nextRoot = Root(x+1);
        if (Sizes[root] >= Sizes[nextRoot]) {
            Parents[nextRoot] = root;
            Sizes[root] += Sizes[nextRoot];
            Maxs[root] = max(Maxs[root], Maxs[nextRoot]);
        } else {
            Parents[root] = nextRoot;
            Sizes[nextRoot] += Sizes[root];
            Maxs[nextRoot] = max(Maxs[root], Maxs[nextRoot]);
        }
    }

    int Successor(int x) {
        return Maxs[Root(x)];
    }
};

int main() {
    TSet set(10);
    cout << set.Successor(1) << endl; // 1
    set.Remove(4);
    cout << set.Successor(4) << endl; // 5
    set.Remove(6);
    set.Remove(7);
    cout << set.Successor(6) << endl; // 8
    set.Remove(8);
    cout << set.Successor(7) << endl; // 9
    return 0;
}

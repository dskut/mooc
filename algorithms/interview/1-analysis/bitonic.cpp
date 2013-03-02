
#include <vector>
#include <iostream>
using namespace std;

ostream& operator<<(ostream& o, const vector<int>& v) {
    for (size_t i = 0; i < v.size(); ++i) {
        o << v[i] << " ";
    }
    return o;
}

int FindBitonicMaxIndex(const vector<int>& v, int start, int end) {
    if (end == start) {
        return start;
    }
    int mid = start + (end-start)/2;
    if (v[mid] < v[mid+1]) {
        return FindBitonicMaxIndex(v, mid+1, end);
    } else {
        return FindBitonicMaxIndex(v, start, mid);
    }
}

int BinarySearchInc(const vector<int>& v, int x, int start, int end) {
    if (start >= end) {
        return v[start] == x ? start : -1;
    }
    int mid = start + (end-start)/2;
    if (v[mid] < x) {
        BinarySearchInc(v, x, mid+1, end);
    } else if (v[mid] > x) {
        BinarySearchInc(v, x, start, mid-1);
    } else {
        return mid;
    }
}

int BinarySearchDec(const vector<int>& v, int x, int start, int end) {
    if (start >= end) {
        return v[start] == x ? start : -1;
    }
    int mid = start + (end-start)/2;
    if (v[mid] < x) {
        BinarySearchDec(v, x, start, mid-1);
    } else if (v[mid] > x) {
        BinarySearchDec(v, x, mid+1, end);
    } else {
        return mid;
    }
}

int SearchBitonicMax(const vector<int>& v, int x) {
    int maxInd = FindBitonicMaxIndex(v, 0, v.size() - 1);
    if (v[maxInd] == x) {
        return maxInd;
    }
    int inc = BinarySearchInc(v, x, 0, maxInd-1);
    if (inc != -1) {
        return inc;
    } else {
        return BinarySearchDec(v, x, maxInd+1, v.size() - 1);
    }
}

int SearchBitonicFaster(const vector<int>& v, int x, int start, int end) {
    if (start >= end) {
        return v[start] == x ? start : -1;
    }
    int mid = start + (end-start)/2;
    if (v[mid] == x) {
        return mid;
    }
    if (v[mid] < v[mid+1]) {
        int inc = BinarySearchInc(v, x, start, mid-1);
        if (inc != -1) {
            return inc;
        } else {
            return SearchBitonicFaster(v, x, mid+1, end);
        }
    } else {
        // decrease path
        if (x < v[mid]) {
            return BinarySearchDec(v, x, mid+1, end);
        } else {
            return SearchBitonicFaster(v, x, start, mid-1);
        }
    }
}

int SearchBitonic(const vector<int>& v, int x) {
    //return SearchBitonicMax(v, x); 
    return SearchBitonicFaster(v, x, 0, v.size() - 1);
}

int main() {
    vector<int> v;
    v.push_back(-2);
    v.push_back(1);
    v.push_back(3);
    v.push_back(4);
    v.push_back(5);
    v.push_back(9);
    v.push_back(12);
    v.push_back(15);
    v.push_back(16);
    v.push_back(19);
    v.push_back(20); // max
    v.push_back(17);
    v.push_back(11);
    v.push_back(10);
    v.push_back(7);
    v.push_back(6);
    v.push_back(0);
    cout << v << endl;
    int x = 4;
    cout << "search " << x << ": " << SearchBitonic(v, x) << endl;
    x = 20;
    cout << "search " << x << ": " << SearchBitonic(v, x) << endl;
    x = 6;
    cout << "search " << x << ": " << SearchBitonic(v, x) << endl;
    x = 2;
    cout << "search " << x << ": " << SearchBitonic(v, x) << endl;
    return 0;
}

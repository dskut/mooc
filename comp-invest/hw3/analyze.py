#! /usr/bin/env python

import sys

def main():
    if len(sys.argv) < 3:
        print("usage: " + sys.argv[0] + " <values.csv> <symbol>")
        exit(1)

    values_path = sys.argv[1]
    benchmark_symbol = sys.argv[2]

    print values_path, benchmark_symbol


main()

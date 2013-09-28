#! /usr/bin/env python

import sys

def main():
    if len(sys.argv) < 4:
        print("usage: " + sys.argv[0] + " <cash> <orders.csv> <values.csv>")
        exit(1)

    cash = int(sys.argv[1])
    orders_path = sys.argv[2]
    values_path = sys.argv[3]

    print cash, orders_path, values_path

main()

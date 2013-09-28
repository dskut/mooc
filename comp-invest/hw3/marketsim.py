#! /usr/bin/env python

import sys
import pandas as pd
import numpy as np
import math
import copy
import QSTK.qstkutil.qsdateutil as du
import datetime as dt
import QSTK.qstkutil.DataAccess as da
import QSTK.qstkutil.tsutil as tsu
import QSTK.qstkstudy.EventProfiler as ep

def order_comparer(first, second):
    if first['year'] != second['year']:
        return cmp(first['year'], second['year'])
    if first['month'] != second['month']:
        return cmp(first['month'], second['month'])
    return cmp(first['day'], second['day'])

def read_orders(orders_path):
    orders_file = open(orders_path)
    res = []
    for line in orders_file:
        data = line.strip().split(',')
        order = {}
        order['year'] = int(data[0])
        order['month'] = int(data[1])
        order['day'] = int(data[2])
        order['symbol'] = data[3]
        order['action'] = data[4].lower()
        order['shares'] = int(data[5])
        res.append(order)
    res.sort(cmp = order_comparer)
    return res

def get_order_datetime(order):
    return dt.datetime(order['year'], order['month'], order['day'], 16)

def get_symbols(orders):
    return list(set([order['symbol'] for order in orders]))

def read_prices(dt_start, dt_end, symbols):
    ldt_timestamps = du.getNYSEdays(dt_start, dt_end, dt.timedelta(hours=16))
    dataobj = da.DataAccess('Yahoo')
    ls_keys = ['open', 'high', 'low', 'close', 'volume', 'actual_close']
    ldf_data = dataobj.get_data(ldt_timestamps, symbols, ls_keys)
    d_data = dict(zip(ls_keys, ldf_data))
    prices = d_data['actual_close'].values
    sym2prices = []
    for price in prices:
        sym2prices.append(dict(zip(symbols, price)))
    res = dict(zip(ldt_timestamps, sym2prices))
    return res

def execute(cash, order, prices):
    dt = get_order_datetime(order)
    price = float(prices[dt][order['symbol']])
    val = price * order['shares']
    if order['action'] == 'buy':
        return cash - val
    else:
        return cash + val

def main():
    if len(sys.argv) < 4:
        print("usage: " + sys.argv[0] + " <cash> <orders.csv> <values.csv>")
        exit(1)

    cash = int(sys.argv[1])
    orders_path = sys.argv[2]
    values_path = sys.argv[3]

    orders = read_orders(orders_path)
    prices = read_prices(get_order_datetime(orders[0]), get_order_datetime(orders[-1]), get_symbols(orders))
    
    print cash
    for order in orders:
        cash = execute(cash, order, prices)
        print order['year'], order['month'], order['day'], cash 

main()

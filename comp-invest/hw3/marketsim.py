#! /usr/bin/env python

import sys
import os
import operator
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

def get_order_datetime(order, hour=16):
    return dt.datetime(order['year'], order['month'], order['day'], hour)

def read_orders(orders_path):
    orders_file = open(orders_path)
    res = []
    for line in orders_file:
        data = line.strip().split(',')
        order = {}
        order['year'] = int(data[0])
        order['month'] = int(data[1])
        order['day'] = int(data[2])
        order['datetime'] = get_order_datetime(order)
        order['symbol'] = data[3]
        order['action'] = data[4].lower()
        order['shares'] = int(data[5])
        res.append(order)
    res.sort(cmp = order_comparer)
    orders_file.close()
    return res

def get_symbols(orders):
    return list(set([order['symbol'] for order in orders]))

def get_timestamps(start, end):
    return du.getNYSEdays(start, end, dt.timedelta(hours=16))

def read_prices(timestamps, symbols):
    dataobj = da.DataAccess('Yahoo')
    ls_keys = ['open', 'high', 'low', 'close', 'volume', 'actual_close']
    ldf_data = dataobj.get_data(timestamps, symbols, ls_keys)
    d_data = dict(zip(ls_keys, ldf_data))
    prices = d_data['close'].values
    sym2prices = []
    for price in prices:
        sym2prices.append(dict(zip(symbols, price)))
    res = dict(zip(timestamps, sym2prices))
    return res

def group_by_day(orders):
    date2orders = {} 
    for order in orders:
        dt = order["datetime"]
        if dt not in date2orders:
            date2orders[dt] = []
        date2orders[dt].append(order)
    return date2orders

def make_order(order, portfolio, prices):
    dt = order["datetime"]
    symbol = order['symbol']
    shares = order['shares']
    if symbol not in portfolio:
        portfolio[symbol] = 0
    price = float(prices[dt][symbol])
    val = price * shares
    if order['action'] == 'buy':
        portfolio[symbol] += shares
        return -val
    else:
        portfolio[symbol] -= shares
        return val

def make_orders(dt, orders_grouped, portfolio, prices):
    if dt not in orders_grouped:
        return 0
    res = 0
    orders = orders_grouped[dt]
    for order in orders:
        res += make_order(order, portfolio, prices)
    return res

def eval_portfolio(portfolio, dt, prices):
    res = 0
    sym2prices = prices[dt]
    for symbol, shares in portfolio.iteritems():
        price = float(sym2prices[symbol])
        res += price * shares
    return res

def main():
    if len(sys.argv) < 4:
        print("usage: " + sys.argv[0] + " <cash> <orders.csv> <values.csv>")
        exit(1)

    cash = float(sys.argv[1])
    orders_path = sys.argv[2]
    values_path = sys.argv[3]

    orders = read_orders(orders_path)
    timestamps = get_timestamps(get_order_datetime(orders[0]), get_order_datetime(orders[-1]))
    prices = read_prices(timestamps, get_symbols(orders))
    orders_grouped = group_by_day(orders)
    
    out_file = open(values_path, 'w')
    portfolio = {}
    for dt in timestamps:
        cash += make_orders(dt, orders_grouped, portfolio, prices)
        value = eval_portfolio(portfolio, dt, prices)
        total = cash + value
        s = "{0}, {1}, {2}, {3}".format(dt.year, dt.month, dt.day, total)
        print s
        out_file.write(s + os.linesep)
    out_file.close()

main()

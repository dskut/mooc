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
    return res

def get_symbols(orders):
    return list(set([order['symbol'] for order in orders]))

def read_prices(dt_start, dt_end, symbols):
    ldt_timestamps = du.getNYSEdays(dt_start, dt_end, dt.timedelta(hours=16))
    dataobj = da.DataAccess('Yahoo')
    ls_keys = ['open', 'high', 'low', 'close', 'volume', 'actual_close']
    ldf_data = dataobj.get_data(ldt_timestamps, symbols, ls_keys)
    d_data = dict(zip(ls_keys, ldf_data))
    prices = d_data['close'].values
    sym2prices = []
    for price in prices:
        sym2prices.append(dict(zip(symbols, price)))
    res = dict(zip(ldt_timestamps, sym2prices))
    return res

def group_by_day(orders):
    date2orders = {} 
    for order in orders:
        dt = order["datetime"]
        if dt not in date2orders:
            date2orders[dt] = []
        date2orders[dt].append(order)
    return date2orders

class DailyOrders:
    def __init__(self, date, orders):
        self.date = date
        self.orders = orders

    def __repr__(self):
        return str(self.date) + " => " + str(self.orders) + "\n"

def flatten(date2orders):
    res = []
    for date, orders in date2orders.iteritems():
        dailyOrders = DailyOrders(date, orders)
        res.append(dailyOrders)
    res.sort(key = operator.attrgetter("date"))
    return res

def execute_order(order, prices):
    dt = order["datetime"]
    price = float(prices[dt][order['symbol']])
    val = price * order['shares']
    if order['action'] == 'buy':
        return -val
    else:
        return val

def execute(daily_orders, prices):
    res = 0
    for order in daily_orders.orders:
        res += execute_order(order, prices)
    return res

def main():
    if len(sys.argv) < 4:
        print("usage: " + sys.argv[0] + " <cash> <orders.csv> <values.csv>")
        exit(1)

    cash = float(sys.argv[1])
    orders_path = sys.argv[2]
    values_path = sys.argv[3]

    orders = read_orders(orders_path)
    prices = read_prices(get_order_datetime(orders[0]), get_order_datetime(orders[-1]), get_symbols(orders))
    orders_grouped = group_by_day(orders)
    orders_by_date_list = flatten(orders_grouped)
    
    out_file = open(values_path, 'w')
    for daily_orders in orders_by_date_list:
        cash += execute(daily_orders, prices)
        dt = daily_orders.date
        s = "{0}, {1}, {2}, {3}".format(dt.year, dt.month, dt.day, cash)
        out_file.write(s + os.linesep)

main()

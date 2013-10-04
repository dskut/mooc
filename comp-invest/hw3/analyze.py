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

def get_order_datetime(value, hour=16):
    return dt.datetime(value['year'], value['month'], value['day'], hour)

def read_values(values_path):
    values_file = open(values_path)
    res = []
    prev_value = None
    for line in values_file:
        data = line.strip().split(',')
        value = {}
        value['year'] = int(data[0])
        value['month'] = int(data[1])
        value['day'] = int(data[2])
        dt = get_order_datetime(value)
        val = float(data[3])
        value['value'] = val
        if prev_value:
            value['return'] = val/prev_value - 1 
        else:
            value['return'] = 0
        prev_value = val
        prev_date = dt
        res.append(value)
    values_file.close()
    return res

def get_data_range(values):
    return get_order_datetime(values[0]), get_order_datetime(values[-1])

def get_symbol_values(values, symbol):
    dt_start, dt_end = get_data_range(values)
    ldt_timestamps = du.getNYSEdays(dt_start, dt_end, dt.timedelta(hours=16))
    dataobj = da.DataAccess('Yahoo')
    ls_keys = ['open', 'high', 'low', 'close', 'volume', 'actual_close']
    ldf_data = dataobj.get_data(ldt_timestamps, [symbol], ls_keys)
    d_data = dict(zip(ls_keys, ldf_data))
    return d_data['close']

def get_total_return(values):
    return values[-1]["value"] / values[0]["value"]

def get_total_bench_return(values):
    return values.values[-1][0] / values.values[0][0]

def get_daily_returns(values):
    res = []
    timestamps = values.index
    for i in xrange(1, len(timestamps)):
        price_today = values.ix[timestamps[i]]
        price_yest = values.ix[timestamps[i - 1]]
        daily_return = (price_today / price_yest) - 1
        res.append(daily_return)
    return res

def get_avg(values):
    daily_returns = [v['return'] for v in values[0:]]
    return np.mean(daily_returns)

def get_bench_avg(bench_values):
    daily_returns = get_daily_returns(bench_values)
    return np.mean(daily_returns)

def get_stdev(values):
    daily_returns = [v['return'] for v in values[0:]]
    return np.std(daily_returns)

def get_bench_stdev(bench_values):
    daily_returns = get_daily_returns(bench_values)
    return np.std(daily_returns)

def analyze(values, benchmark_values, symbol):
    print "last value =", values[-1]

    data_range = get_data_range(values)
    print "data range =", data_range

    print "fund total return =", get_total_return(values)
    print "%s total return = %s" % (symbol, get_total_bench_return(benchmark_values))

    print "fund std dev =", get_stdev(values)
    print "%s std dev = %s" % (symbol, get_bench_stdev(benchmark_values))

    print "fund avg daily =", get_avg(values)
    print "%s avg daily return = %s" % (symbol, get_bench_avg(benchmark_values))


def main():
    if len(sys.argv) < 3:
        print("usage: " + sys.argv[0] + " <values.csv> <symbol>")
        exit(1)

    values_path = sys.argv[1]
    benchmark_symbol = sys.argv[2]

    values = read_values(values_path)
    benchmark_values = get_symbol_values(values, benchmark_symbol)
    analyze(values, benchmark_values, benchmark_symbol)

main()

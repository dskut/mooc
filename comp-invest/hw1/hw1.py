#! /usr/bin/env python

import QSTK.qstkutil.qsdateutil as du
import QSTK.qstkutil.tsutil as tsu
import QSTK.qstkutil.DataAccess as da

import datetime as dt
import matplotlib.pyplot as plt
import pandas as pd
import numpy as np
import math

DAYS_IN_YEAR = 252

def get_normalized_prices(symbols, start, end):
    dt_timeofday = dt.timedelta(hours=16)
    ldt_timestamps = du.getNYSEdays(start, end, dt_timeofday)

    c_dataobj = da.DataAccess('Yahoo', cachestalltime=0)
    ls_keys = ['close']
    ldf_data = c_dataobj.get_data(ldt_timestamps, symbols, ls_keys)
    d_data = dict(zip(ls_keys, ldf_data))

    na_price = d_data['close'].values
    na_normalized_price = na_price / na_price[0, :]
    return na_normalized_price

def simulate(na_normalized_price, allocations):
    daily_val = na_normalized_price * allocations
    cum_ret = np.sum(daily_val, axis=1)
    res_cum_ret = cum_ret[-1]

    daily_rets = cum_ret.copy()
    tsu.returnize0(daily_rets)

    stdev = np.std(daily_rets)
    avg = np.average(daily_rets)
    sharpe = avg / stdev * math.sqrt(DAYS_IN_YEAR)

    return res_cum_ret, stdev, avg, sharpe

def optimize(start, end, symbols):
    best_sharpe = -100
    min_sharpe = 100

    na_normalized_price = get_normalized_prices(symbols, start, end)

    for a in xrange(0, 11):
        for b in xrange(0, 11):
            for c in xrange(0, 11):
                for d in xrange(0, 11):
                    if a + b + c + d != 10:
                        continue
                    allocs = [a/10.0, b/10.0, c/10.0, d/10.0]
                    cum_ret, std, avg, sharpe = simulate(na_normalized_price, allocs)
                    if best_sharpe < sharpe:
                        best_sharpe = sharpe
                        best_std = std
                        best_avg = avg
                        best_cum_ret = cum_ret
                        best_allocs = allocs
                    if min_sharpe > sharpe:
                        min_sharpe = sharpe
    print "min_sharpe =", min_sharpe
    return best_cum_ret, best_std, best_avg, best_sharpe, best_allocs

def run(symbols, start, end):
    res_cum_ret, stdev, avg, sharpe, best_allocs = optimize(start, end, symbols)
    print "cum ret =", res_cum_ret
    print "stdev =", stdev
    print "avg =", avg
    print "sharpe =", sharpe
    print "best_allocs =", best_allocs

symbols = ["AAPL", "GLD", "GOOG", "XOM"]
start = dt.datetime(2011, 1, 1)
end = dt.datetime(2011, 12, 31)
run(symbols, start, end)

symbols = ["AXP", "HPQ", "IBM", "HNZ"]
start = dt.datetime(2010, 1, 1)
end = dt.datetime(2010, 12, 31)
run(symbols, start, end)

symbols = ["BRCM", "ADBE", "AMD", "ADI"]
start = dt.datetime(2010, 1, 1)
end = dt.datetime(2010, 12, 31)
run(symbols, start, end)

symbols = ["BRCM", "TXN", "AMD", "ADI"]
start = dt.datetime(2011, 1, 1)
end = dt.datetime(2011, 12, 31)
run(symbols, start, end)


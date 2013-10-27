#! /usr/bin/env python

import os
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

def get_bands(prices):
    means = pd.rolling_mean(prices,20,min_periods=20)
    stdevs = pd.rolling_std(prices,20,min_periods=20)
    return (prices - means) / stdevs

def handle_event(symbol, timestamps, ts_index):
    buy_ts = timestamps[ts_index]
    sell_ts_index = min(ts_index + 5, len(timestamps) - 1)
    sell_ts = timestamps[sell_ts_index]
    print "{0},{1},{2},{3},BUY,100,".format(buy_ts.year, buy_ts.month, buy_ts.day, symbol)
    print "{0},{1},{2},{3},SELL,100,".format(sell_ts.year, sell_ts.month, sell_ts.day, symbol)

def find_events(symbols, d_data):
    df_close = d_data['actual_close']
    ts_market = df_close['SPY']

    timestamps = df_close.index

    bands_market = get_bands(ts_market)

    for sym in symbols:
        bands = get_bands(df_close[sym])
        for i in range(1, len(timestamps)):
            today = timestamps[i]
            yest = timestamps[i-1]

            band_today = bands.ix[today]
            band_yest = bands.ix[yest]
            band_market = bands_market.ix[today]

            if band_today < -2.0 and band_yest >= -2.0 and band_market >= 1.5:
                handle_event(sym, timestamps, i)

def run(dt_start, dt_end, symbols_list):
    dataobj = da.DataAccess('Yahoo')
    sys.stderr.write("Get symbols from list\n")
    symbols = dataobj.get_symbols_from_list(symbols_list)
    symbols.append('SPY')

    timestamps = du.getNYSEdays(dt_start, dt_end, dt.timedelta(hours=16))
    keys = ['open', 'high', 'low', 'close', 'volume', 'actual_close']
    sys.stderr.write("Get data\n")
    ldf_data = dataobj.get_data(timestamps, symbols, keys)
    d_data = dict(zip(keys, ldf_data))

    for s_key in keys:
        d_data[s_key] = d_data[s_key].fillna(method='ffill')
        d_data[s_key] = d_data[s_key].fillna(method='bfill')
        d_data[s_key] = d_data[s_key].fillna(1.0)

    sys.stderr.write("Finding Events\n")
    find_events(symbols, d_data)

def main():
    start = dt.datetime(2008, 1, 1)
    end = dt.datetime(2009, 12, 31)
    run(start, end, 'sp5002012')

main()

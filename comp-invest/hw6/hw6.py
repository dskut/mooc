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

def find_events(symbols, d_data):
    df_close = d_data['actual_close']
    ts_market = df_close['SPY']

    df_events = copy.deepcopy(df_close)
    df_events = df_events * np.NAN
    timestamps = df_close.index

    bands_market = get_bands(ts_market)

    for sym in symbols:
        bands = get_bands(df_close[sym])
        for i in range(21, len(timestamps)):
            today = timestamps[i]
            yest = timestamps[i-1]

            band_today = bands.ix[today]
            band_yest = bands.ix[yest]
            band_market = bands_market.ix[today]

            if band_today < -2.0 and band_yest >= -2.0 and band_market >= 1.4:
                df_events[sym].ix[timestamps[i]] = 1

    return df_events

def run(dt_start, dt_end, symbols_list, out_path):
    dataobj = da.DataAccess('Yahoo')
    print "Get symbols from list"
    symbols = dataobj.get_symbols_from_list(symbols_list)
    symbols.append('SPY')

    timestamps = du.getNYSEdays(dt_start, dt_end, dt.timedelta(hours=16))
    keys = ['open', 'high', 'low', 'close', 'volume', 'actual_close']
    print "Get data"
    ldf_data = dataobj.get_data(timestamps, symbols, keys)
    d_data = dict(zip(keys, ldf_data))

    for s_key in keys:
        d_data[s_key] = d_data[s_key].fillna(method='ffill')
        d_data[s_key] = d_data[s_key].fillna(method='bfill')
        d_data[s_key] = d_data[s_key].fillna(1.0)

    print "Finding Events"
    events = find_events(symbols, d_data)

    print "Creating Study"
    ep.eventprofiler(events, d_data, i_lookback=20, i_lookforward=20,
                s_filename=out_path, b_market_neutral=True, b_errorbars=True,
                s_market_sym='SPY')

def main():
    start = dt.datetime(2008, 1, 1)
    end = dt.datetime(2009, 12, 31)
    run(start, end, 'sp5002012', 'bands-prod.pdf')

main()

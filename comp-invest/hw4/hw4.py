#!/usr/bin/env python

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

def find_events(ls_symbols, d_data, border):
    ''' Finding the event dataframe '''
    df_close = d_data['actual_close']
    ts_market = df_close['SPY']

    sys.stderr.write("Finding Events\n")

    events = {}

    # Time stamps for the event range
    ldt_timestamps = df_close.index

    for s_sym in ls_symbols:
        for i in range(1, len(ldt_timestamps)):
            today = ldt_timestamps[i]
            yesterday = ldt_timestamps[i-1]
            # Calculating the returns for this timestamp
            f_symprice_today = df_close[s_sym].ix[today]
            f_symprice_yest = df_close[s_sym].ix[yesterday]
            f_marketprice_today = ts_market.ix[today]
            f_marketprice_yest = ts_market.ix[yesterday]
            f_symreturn_today = (f_symprice_today / f_symprice_yest) - 1
            f_marketreturn_today = (f_marketprice_today / f_marketprice_yest) - 1

            # Event is found if the symbol is down more then 3% while the
            # market is up more then 2%
            #if f_symreturn_today <= -0.03 and f_marketreturn_today >= 0.02:
            #    df_events[s_sym].ix[ldt_timestamps[i]] = 1

            if f_symprice_yest >= border and f_symprice_today < border:
                if i in events: 
                    events[i].append(s_sym)
                else:
                    events[i] = [s_sym]

    return events


def run(symbols_list, border):
    ls_symbols = dataobj.get_symbols_from_list(symbols_list)
    ls_symbols.append('SPY')

    ls_keys = ['open', 'high', 'low', 'close', 'volume', 'actual_close']
    ldf_data = dataobj.get_data(ldt_timestamps, ls_symbols, ls_keys)
    d_data = dict(zip(ls_keys, ldf_data))

    for s_key in ls_keys:
        d_data[s_key] = d_data[s_key].fillna(method='ffill')
        d_data[s_key] = d_data[s_key].fillna(method='bfill')
        d_data[s_key] = d_data[s_key].fillna(1.0)

    events = find_events(ls_symbols, d_data, border)
    for ts_index, symbols in events.iteritems():
        buy_ts = ldt_timestamps[ts_index]
        sell_ts_index = min(ts_index + 5, len(ldt_timestamps) - 1)
        sell_ts = ldt_timestamps[sell_ts_index]
        for symbol in symbols:
            print "{0},{1},{2},{3},BUY,100,".format(buy_ts.year, buy_ts.month, buy_ts.day, symbol)
            print "{0},{1},{2},{3},SELL,100,".format(sell_ts.year, sell_ts.month, sell_ts.day, symbol)

if __name__ == '__main__':
    if len(sys.argv) < 2:
        sys.stderr.write("Usage: " + sys.argv[0] + "<border value>\n")
        exit(1)
    border = float(sys.argv[1])
    dt_start = dt.datetime(2008, 1, 1)
    dt_end = dt.datetime(2009, 12, 31)
    ldt_timestamps = du.getNYSEdays(dt_start, dt_end, dt.timedelta(hours=16))

    dataobj = da.DataAccess('Yahoo')
    run('sp5002012', border)

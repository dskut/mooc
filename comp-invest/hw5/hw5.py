#!/usr/bin/python 
import QSTK.qstkutil.tsutil as tsu
import QSTK.qstkutil.qsdateutil as du
import QSTK.qstkutil.DataAccess as da
import datetime as dt
import matplotlib.pyplot as plt
import pandas
from pylab import *

def read_prices(symbols):
    startday = dt.datetime(2010,1,1)
    endday = dt.datetime(2010,12,31)
    timeofday=dt.timedelta(hours=16)
    timestamps = du.getNYSEdays(startday,endday,timeofday)

    dataobj = da.DataAccess('Yahoo') # cachestalltime=0
    voldata = dataobj.get_data(timestamps, symbols, "volume")
    adjcloses = dataobj.get_data(timestamps, symbols, "close")
    actualclose = dataobj.get_data(timestamps, symbols, "actual_close")

    adjcloses = adjcloses.fillna(value=0)
    adjcloses = adjcloses.fillna(method='backfill')
    return timestamps, adjcloses


def draw(symtoplot, adjcloses, means):
    plt.clf()
    plot(adjcloses.index,adjcloses[symtoplot].values,label=symtoplot)
    plot(adjcloses.index,means[symtoplot].values)
    plt.legend([symtoplot,'Moving Avg.'])
    plt.ylabel('Adjusted Close')
    savefig("movingavg-goog2010.png", format='png')

def main():
    symbols = ["AAPL", "GOOG","IBM","MSFT"]
    timestamps, adjcloses = read_prices(symbols)
    means = pandas.rolling_mean(adjcloses,20,min_periods=20)
    stdevs = pandas.rolling_std(adjcloses,20,min_periods=20)
    bands = (adjcloses - means) / stdevs
    #draw(symbol, adjcloses, means)
    for i in xrange(0, len(adjcloses.index)):
        ts = timestamps[i]
        sys.stdout.write(str(ts))
        for symbol in symbols:
            symbol_bands = bands[symbol]
            sys.stdout.write("\t" + str(symbol_bands.ix[ts]))
        sys.stdout.write("\n")

main()

package com.gmail.develop.jcant.termocalculator.tables;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class GradTable {

    protected List<Pair<Double, Double>> pData;

    public GradTable() {
        pData = new ArrayList<>();
    }

    public void add(double tempr, double param) {
        pData.add(new Pair<Double, Double>(tempr, param));
    }

    public double getParamMin() {
        if (pData.isEmpty()) return 0;
        return pData.get(0).second;
    }

    public double getParamMax() {
        if (pData.isEmpty()) return 0;
        return pData.get(pData.size() - 1).second;
    }

    public double getTemperature(double paramValue) {
        double result =-99999;
        if (pData.isEmpty()) return result;

        Pair pairLess = pData.get(0);
        Pair pairMore = pairLess;

        for (Pair pair: pData) {
            if ((double)pair.second == paramValue) {
                result = (double)pair.first;
                return result;
            }

            if ((double)pair.second > paramValue) {
                pairMore = pair;
                break;
            }
            else {
                pairLess = pair;
            }
        }

        result = (((paramValue - (double)pairLess.second) * ((double)pairMore.first - (double)pairLess.first)) / ((double)pairMore.second - (double)pairLess.second)) + (double)pairLess.first;
        return result;
    }
}

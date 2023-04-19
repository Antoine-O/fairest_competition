package com.qc.competition.service.structure;

/**
 * Created by Duncan on 07/03/2017.
 */
public class StatisticsStructure {
    public Long max;
    public Long min;
    public Long avg;
    public Long sum;
    public Long count;

    void computeAverage() {
        if (sum != null && count != null) {
            if (count > 0)
                avg = sum / count;
            else
                avg = 0L;
        }
    }

    public void reset() {
        max = null;
        min = null;
        avg = null;
        sum = null;
        count = null;

    }

    @Override
    public String toString() {
        return "{" +
                ", sum=" + sum +
                ", count=" + count +
                ", min=" + min +
                ", max=" + max +
                ", avg=" + avg +
                '}';
    }
}

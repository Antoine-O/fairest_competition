package com.qc.competition.utils;

/**
 * Created by Duncan on 12/11/2016.
 */
public class MutableLong {
    private long val;


    public MutableLong(long val) {
        this.val = val;
    }

    public MutableLong(int val) {
        this.val = val;
    }

    public long get() {
        return val;
    }

    public long inc() {
        val++;
        return val;
    }

    public long dec() {
        if (val > 0) val--;
        return val;
    }

    public void set(long val) {
        this.val = val;
    }

    public String toString() {
        return Long.toString(val);
    }
}

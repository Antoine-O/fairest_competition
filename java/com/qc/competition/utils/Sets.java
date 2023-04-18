package com.qc.competition.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

/**
 * Created by Duncan on 30/09/2015.
 */
public class Sets {

    public static SortedSet sort(SortedSet sortedSet) {
        List list = new ArrayList<>(sortedSet);
        sortedSet.clear();
        sortedSet.addAll(list);
//        sortedSet.addAll(sortedSetTmp);
        return sortedSet;
    }
}

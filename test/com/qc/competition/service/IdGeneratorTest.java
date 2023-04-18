package com.qc.competition.service;

import com.qc.competition.service.structure.IdGenerator;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by Duncan on 02/05/2017.
 */
public class IdGeneratorTest {
    private static Logger LOGGER = Logger.getLogger(IdGeneratorTest.class);


    @Test
    public void testUnicity() {
        IdGenerator idGenerator = new IdGenerator();
        SortedSet<Integer> integerSortedSet = new TreeSet<>();
        int generationCount = 100000;
        for (int i = 0; i < generationCount; i++) {
//            Integer value = Integer.valueOf(idGenerator.getId());
//            if (!integerSortedSet.contains(value))
            integerSortedSet.add(Integer.valueOf(idGenerator.getId()));
//            System.out.println(value);
        }
        Assert.assertEquals(integerSortedSet.size(), generationCount);
    }

}

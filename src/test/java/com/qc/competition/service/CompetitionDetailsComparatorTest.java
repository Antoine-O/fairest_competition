package com.qc.competition.service;

import com.qc.competition.service.structure.CompetitionInstance;
import com.qc.competition.service.structure.CompetitionPlay;
import com.qc.competition.service.structure.Duration;
import com.qc.competition.service.structure.ParticipantSingle;
import com.qc.competition.service.template.automatic.participation.optimization.CompetitionInstanceComparator;
import com.qc.competition.utils.Sets;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by Duncan on 24/09/2015.
 */
public class CompetitionDetailsComparatorTest {

    private void testSorting(List<CompetitionInstance> competitionInstanceTestList, Duration competitionExpectedDuration) {
        competitionInstanceTestList.sort(new CompetitionInstanceComparator(competitionExpectedDuration));
        for (int i = 0; i < competitionInstanceTestList.size(); i++) {
            if (competitionInstanceTestList.get(i).localId.compareTo("" + i + 1) != 0)
                Assert.fail("i:" + i + ",localId:" + competitionInstanceTestList.get(0).localId);
        }
    }

    private CompetitionInstanceTest createCompetitionInstanceTest(String localId, Duration expectedGlobalMinimumDuration, Duration expectedGlobalAverageDuration, Duration expectedGlobalMaximumDuration, Duration expectedParticipantMinimumDuration, Duration expectedParticipantAverageDuration, Duration expectedParticipantMaximumDuration, long expectedParticipantMinimumPlay, long expectedParticipantAveragePlay, long expectedParticipantMaximumPlay) {
        CompetitionInstanceTest competitionInstanceTest = new CompetitionInstanceTest();
        competitionInstanceTest.localId = localId;
//        competitionInstanceTest.setExpectedGlobalAverageDuration(expectedGlobalAverageDuration);
//        competitionInstanceTest.setExpectedGlobalMinimumDuration(expectedGlobalMinimumDuration);
//        competitionInstanceTest.setExpectedGlobalMaximumDuration(expectedGlobalMaximumDuration);
        competitionInstanceTest.setExpectedParticipantAverageDuration(expectedGlobalAverageDuration);
        competitionInstanceTest.setExpectedParticipantMinimumDuration(expectedGlobalMinimumDuration);
        competitionInstanceTest.setExpectedParticipantMaximumDuration(expectedGlobalMaximumDuration);
        competitionInstanceTest.setExpectedParticipantMinimumPlay(expectedParticipantMinimumPlay);
        competitionInstanceTest.setExpectedParticipantAveragePlay(expectedParticipantAveragePlay);
        competitionInstanceTest.setExpectedParticipantMaximumPlay(expectedParticipantMaximumPlay);
        return competitionInstanceTest;
    }


    @Test
    public void testCompetitionAverageDuration1() {
        Duration competitionExpectedDuration = Duration.ofMinutes(5 * 60);
        CompetitionInstanceTest competitionInstanceTestA = createCompetitionInstanceTest("" + 3, Duration.ofMinutes(2 * 60), Duration.ofMinutes(3 * 60), Duration.ofMinutes(5 * 60), Duration.ofMinutes(3 * 60), Duration.ofMinutes(4 * 60), Duration.ofMinutes(5 * 60), 10, 12, 15);
        CompetitionInstanceTest competitionInstanceTestB = createCompetitionInstanceTest("" + 2, Duration.ofMinutes(3 * 60), Duration.ofMinutes(4 * 60), Duration.ofMinutes(6 * 60), Duration.ofMinutes(3 * 60), Duration.ofMinutes(4 * 60), Duration.ofMinutes(5 * 60), 10, 12, 15);
        CompetitionInstanceTest competitionInstanceTestC = createCompetitionInstanceTest("" + 1, Duration.ofMinutes(4 * 60), Duration.ofMinutes(5 * 60), Duration.ofMinutes(7 * 60), Duration.ofMinutes(3 * 60), Duration.ofMinutes(4 * 60), Duration.ofMinutes(5 * 60), 10, 12, 15);
        List<CompetitionInstance> competitionInstanceTestList = new ArrayList<>();
        competitionInstanceTestList.add(competitionInstanceTestA);
        competitionInstanceTestList.add(competitionInstanceTestB);
        competitionInstanceTestList.add(competitionInstanceTestC);
        testSorting(competitionInstanceTestList, competitionExpectedDuration);


    }

    @Test
    public void testCompetitionAverageDuration2() {
        Duration competitionExpectedDuration = Duration.ofMinutes(5 * 60);
        CompetitionInstanceTest competitionInstanceA = createCompetitionInstanceTest("" + 4, Duration.ofMinutes(2 * 60), Duration.ofMinutes(3 * 60), Duration.ofMinutes(5 * 60), Duration.ofMinutes(3 * 60), Duration.ofMinutes(4 * 60), Duration.ofMinutes(5 * 60), 10, 12, 15);
        CompetitionInstanceTest competitionInstanceB = createCompetitionInstanceTest("" + 2, Duration.ofMinutes(3 * 60), Duration.ofMinutes(4 * 60), Duration.ofMinutes(6 * 60), Duration.ofMinutes(3 * 60), Duration.ofMinutes(4 * 60), Duration.ofMinutes(5 * 60), 10, 12, 15);
        CompetitionInstanceTest competitionInstanceC = createCompetitionInstanceTest("" + 1, Duration.ofMinutes(4 * 60), Duration.ofMinutes(5 * 60), Duration.ofMinutes(7 * 60), Duration.ofMinutes(3 * 60), Duration.ofMinutes(4 * 60), Duration.ofMinutes(5 * 60), 10, 12, 15);
        CompetitionInstanceTest competitionInstanceD = createCompetitionInstanceTest("" + 3, Duration.ofMinutes(5 * 60), Duration.ofMinutes(6 * 60), Duration.ofMinutes(7 * 60), Duration.ofMinutes(3 * 60), Duration.ofMinutes(4 * 60), Duration.ofMinutes(5 * 60), 10, 12, 15);
        CompetitionInstanceTest competitionInstanceE = createCompetitionInstanceTest("" + 5, Duration.ofMinutes(6 * 60), Duration.ofMinutes(7 * 60), Duration.ofMinutes(7 * 60), Duration.ofMinutes(3 * 60), Duration.ofMinutes(4 * 60), Duration.ofMinutes(5 * 60), 10, 12, 15);
        CompetitionInstanceTest competitionInstanceF = createCompetitionInstanceTest("" + 6, Duration.ofMinutes(7 * 60), Duration.ofMinutes(8 * 60), Duration.ofMinutes(7 * 60), Duration.ofMinutes(3 * 60), Duration.ofMinutes(4 * 60), Duration.ofMinutes(5 * 60), 10, 12, 15);
        List<CompetitionInstance> competitionInstanceTestList = new ArrayList<>();
        competitionInstanceTestList.add(competitionInstanceA);
        competitionInstanceTestList.add(competitionInstanceB);
        competitionInstanceTestList.add(competitionInstanceC);
        competitionInstanceTestList.add(competitionInstanceD);
        competitionInstanceTestList.add(competitionInstanceE);
        competitionInstanceTestList.add(competitionInstanceF);
        testSorting(competitionInstanceTestList, competitionExpectedDuration);
    }


    @Test
    public void testParticipantMinimumPlay() {
        Duration competitionExpectedDuration = Duration.ofMinutes(5 * 60);
        CompetitionInstanceTest competitionInstanceTestA = createCompetitionInstanceTest("" + 3, Duration.ofMinutes(2 * 60), Duration.ofMinutes(5 * 60), Duration.ofMinutes(5 * 60), Duration.ofMinutes(3 * 60), Duration.ofMinutes(4 * 60), Duration.ofMinutes(5 * 60), 10, 12, 15);
        CompetitionInstanceTest competitionInstanceTestB = createCompetitionInstanceTest("" + 2, Duration.ofMinutes(3 * 60), Duration.ofMinutes(5 * 60), Duration.ofMinutes(6 * 60), Duration.ofMinutes(3 * 60), Duration.ofMinutes(4 * 60), Duration.ofMinutes(5 * 60), 11, 12, 15);
        CompetitionInstanceTest competitionInstanceTestC = createCompetitionInstanceTest("" + 1, Duration.ofMinutes(4 * 60), Duration.ofMinutes(5 * 60), Duration.ofMinutes(7 * 60), Duration.ofMinutes(3 * 60), Duration.ofMinutes(4 * 60), Duration.ofMinutes(5 * 60), 12, 12, 15);
        List<CompetitionInstance> competitionInstanceTestList = new ArrayList<>();
        competitionInstanceTestList.add(competitionInstanceTestA);
        competitionInstanceTestList.add(competitionInstanceTestB);
        competitionInstanceTestList.add(competitionInstanceTestC);
        testSorting(competitionInstanceTestList, competitionExpectedDuration);
    }

    @Test
    public void testParticipantMaximumPlay() {
        Duration competitionExpectedDuration = Duration.ofMinutes(5 * 60);
        CompetitionInstanceTest competitionInstanceTestA = createCompetitionInstanceTest("" + 3, Duration.ofMinutes(2 * 60), Duration.ofMinutes(5 * 60), Duration.ofMinutes(5 * 60), Duration.ofMinutes(3 * 60), Duration.ofMinutes(4 * 60), Duration.ofMinutes(5 * 60), 10, 12, 17);
        CompetitionInstanceTest competitionInstanceTestB = createCompetitionInstanceTest("" + 2, Duration.ofMinutes(3 * 60), Duration.ofMinutes(5 * 60), Duration.ofMinutes(6 * 60), Duration.ofMinutes(3 * 60), Duration.ofMinutes(4 * 60), Duration.ofMinutes(5 * 60), 10, 12, 16);
        CompetitionInstanceTest competitionInstanceTestC = createCompetitionInstanceTest("" + 1, Duration.ofMinutes(4 * 60), Duration.ofMinutes(5 * 60), Duration.ofMinutes(7 * 60), Duration.ofMinutes(3 * 60), Duration.ofMinutes(4 * 60), Duration.ofMinutes(5 * 60), 10, 12, 15);
        List<CompetitionInstance> competitionInstanceTestList = new ArrayList<>();
        competitionInstanceTestList.add(competitionInstanceTestA);
        competitionInstanceTestList.add(competitionInstanceTestB);
        competitionInstanceTestList.add(competitionInstanceTestC);
        testSorting(competitionInstanceTestList, competitionExpectedDuration);
    }


    @Test
    public void testParticipantAveragePlay() {
        Duration competitionExpectedDuration = Duration.ofMinutes(5 * 60);
        CompetitionInstanceTest competitionInstanceTestA = createCompetitionInstanceTest("" + 3, Duration.ofMinutes(2 * 60), Duration.ofMinutes(5 * 60), Duration.ofMinutes(5 * 60), Duration.ofMinutes(3 * 60), Duration.ofMinutes(4 * 60), Duration.ofMinutes(5 * 60), 10, 12, 15);
        CompetitionInstanceTest competitionInstanceTestB = createCompetitionInstanceTest("" + 2, Duration.ofMinutes(3 * 60), Duration.ofMinutes(5 * 60), Duration.ofMinutes(6 * 60), Duration.ofMinutes(3 * 60), Duration.ofMinutes(4 * 60), Duration.ofMinutes(5 * 60), 10, 13, 15);
        CompetitionInstanceTest competitionInstanceTestC = createCompetitionInstanceTest("" + 1, Duration.ofMinutes(4 * 60), Duration.ofMinutes(5 * 60), Duration.ofMinutes(7 * 60), Duration.ofMinutes(3 * 60), Duration.ofMinutes(4 * 60), Duration.ofMinutes(5 * 60), 10, 14, 15);
        List<CompetitionInstance> competitionInstanceTestList = new ArrayList<>();
        competitionInstanceTestList.add(competitionInstanceTestA);
        competitionInstanceTestList.add(competitionInstanceTestB);
        competitionInstanceTestList.add(competitionInstanceTestC);
        testSorting(competitionInstanceTestList, competitionExpectedDuration);
    }


    @Test
    public void testSortingCompetitionPlay() {
        CompetitionInstance competitionInstance = new CompetitionInstance();
        CompetitionPlay competitionPlay1 = competitionInstance.createCompetitionPlay();
        CompetitionPlay competitionPlay2 = competitionInstance.createCompetitionPlay();
        CompetitionPlay competitionPlay3 = competitionInstance.createCompetitionPlay();
        CompetitionPlay competitionPlay4 = competitionInstance.createCompetitionPlay();
        CompetitionPlay competitionPlay5 = competitionInstance.createCompetitionPlay();
        CompetitionPlay competitionPlay6 = competitionInstance.createCompetitionPlay();
        CompetitionPlay competitionPlay7 = competitionInstance.createCompetitionPlay();
        SortedSet<CompetitionPlay> competitionPlays = new TreeSet<>();
        competitionPlays.add(competitionPlay2);
        competitionPlays.add(competitionPlay1);
        competitionPlays.add(competitionPlay4);
        competitionPlays.add(competitionPlay3);
        competitionPlays.add(competitionPlay6);
        competitionPlays.add(competitionPlay5);
        competitionPlays.add(competitionPlay7);
        Sets.sort(competitionPlays);
        for (CompetitionPlay competitionPlay : competitionPlays) {
            System.out.println(competitionPlay.localId);
        }
    }

    @Test
    public void testSortingParticipant() {
        CompetitionInstance competitionInstance = new CompetitionInstance();
        ParticipantSingle participantSingle1 = competitionInstance.createParticipantSingle();
        ParticipantSingle participantSingle2 = competitionInstance.createParticipantSingle();
        ParticipantSingle participantSingle3 = competitionInstance.createParticipantSingle();
        ParticipantSingle participantSingle4 = competitionInstance.createParticipantSingle();
        ParticipantSingle participantSingle5 = competitionInstance.createParticipantSingle();
        ParticipantSingle participantSingle6 = competitionInstance.createParticipantSingle();
        ParticipantSingle participantSingle7 = competitionInstance.createParticipantSingle();
        SortedSet<ParticipantSingle> participantSingles = new TreeSet<>();
        participantSingles.add(participantSingle2);
        participantSingles.add(participantSingle1);
        participantSingles.add(participantSingle4);
        participantSingles.add(participantSingle3);
        participantSingles.add(participantSingle6);
        participantSingles.add(participantSingle5);
        participantSingles.add(participantSingle7);
        Sets.sort(participantSingles);
        for (ParticipantSingle participantSingle : participantSingles) {
            System.out.println(participantSingle.localId);
        }
    }
}

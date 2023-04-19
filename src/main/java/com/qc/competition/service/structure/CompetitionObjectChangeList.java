package com.qc.competition.service.structure;

import java.util.List;

/**
 * Created by Duncan on 03/04/2017.
 */
public interface CompetitionObjectChangeList {
    List<String> getCompetitionPlayIds();

    List<String> getCompetitionMatchIds();

    List<String> getCompetitionRoundIds();

    List<String> getCompetitionGroupIds();

    List<String> getCompetitionSeedIds();

    List<String> getCompetitionPhaseIds();

    List<String> getCompetitionInstanceIds();

    List<String> getCompetitionPlayIdsRemove();

    List<String> getCompetitionMatchIdsRemove();

    List<String> getCompetitionRoundIdsRemove();

    List<String> getCompetitionGroupIdsRemove();

    List<String> getCompetitionSeedIdsRemove();

    List<String> getCompetitionPhaseIdsRemove();

    List<String> getCompetitionInstanceIdsRemove();

    List<Integer> getCompetitionPlayDatabaseIdsRemove();

    List<Integer> getCompetitionMatchDatabaseIdsRemove();

    List<Integer> getCompetitionRoundDatabaseIdsRemove();

    List<Integer> getCompetitionGroupDatabaseIdsRemove();

    List<Integer> getCompetitionSeedDatabaseIdsRemove();

    List<Integer> getCompetitionPhaseDatabaseIdsRemove();

    List<Integer> getCompetitionInstanceDatabaseIdsRemove();

    boolean isEmpty();
}

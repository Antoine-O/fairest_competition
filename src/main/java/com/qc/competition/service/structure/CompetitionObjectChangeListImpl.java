package com.qc.competition.service.structure;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class CompetitionObjectChangeListImpl implements CompetitionObjectChangeList {
    Map<Class, List<String>> classIntegerMap = new Hashtable<>();
    Map<Class, List<String>> classIntegerRemoveMap = new Hashtable<>();
    Map<Class, List<Integer>> classIntegerDatabaseMapRemove = new Hashtable<>();

    public CompetitionObjectChangeListImpl() {

    }

    public void add(Object o) {
        if (!classIntegerMap.containsKey(o.getClass())) {
            classIntegerMap.put(o.getClass(), new ArrayList<>());
        }
        if (!classIntegerRemoveMap.containsKey(o.getClass())) {
            classIntegerRemoveMap.put(o.getClass(), new ArrayList<>());
        }
        if (!classIntegerDatabaseMapRemove.containsKey(o.getClass())) {
            classIntegerDatabaseMapRemove.put(o.getClass(), new ArrayList<>());
        }

        if (!classIntegerMap.get(o.getClass()).contains(((CompetitionObjectWithResult) o).getLocalId())) {
            CompetitionObjectWithResult competitionObjectWithResult = (CompetitionObjectWithResult) o;
            if (competitionObjectWithResult.deleted) {
                classIntegerRemoveMap.get(o.getClass()).add(competitionObjectWithResult.getLocalId());
                classIntegerDatabaseMapRemove.get(o.getClass()).add(competitionObjectWithResult.getDatabaseId());
            } else {
                classIntegerMap.get(o.getClass()).add(competitionObjectWithResult.getLocalId());
            }
        }
    }

    @Override
    public List<String> getCompetitionPlayIds() {
        return getCompetitionIdsForClass(CompetitionPlay.class);
    }

    @Override
    public List<String> getCompetitionMatchIds() {
        return getCompetitionIdsForClass(CompetitionMatch.class);
    }

    @Override
    public List<String> getCompetitionRoundIds() {
        return getCompetitionIdsForClass(CompetitionRound.class);
    }

    @Override
    public List<String> getCompetitionGroupIds() {
        return getCompetitionIdsForClass(CompetitionGroup.class);
    }

    @Override
    public List<String> getCompetitionSeedIds() {
        return getCompetitionIdsForClass(CompetitionSeed.class);
    }

    @Override
    public List<String> getCompetitionPhaseIds() {
        return getCompetitionIdsForClass(CompetitionPhase.class);
    }

    @Override
    public List<String> getCompetitionInstanceIds() {
        return getCompetitionIdsForClass(CompetitionInstance.class);
    }

    private List<String> getCompetitionIdsForClass(Class aClass) {
        List<String> ids = new ArrayList<>();
        if (classIntegerMap.containsKey(aClass)) {
            ids.addAll(classIntegerMap.get(aClass));
        }
        return ids;
    }

    public boolean isEmpty() {
        return classIntegerMap.isEmpty() && classIntegerRemoveMap.isEmpty();
    }


    @Override
    public List<String> getCompetitionPlayIdsRemove() {
        return getCompetitionIdsRemoveForClass(CompetitionPlay.class);
    }

    @Override
    public List<String> getCompetitionMatchIdsRemove() {
        return getCompetitionIdsRemoveForClass(CompetitionMatch.class);
    }

    @Override
    public List<String> getCompetitionRoundIdsRemove() {
        return getCompetitionIdsRemoveForClass(CompetitionRound.class);
    }

    @Override
    public List<String> getCompetitionGroupIdsRemove() {
        return getCompetitionIdsRemoveForClass(CompetitionGroup.class);
    }

    @Override
    public List<String> getCompetitionSeedIdsRemove() {
        return getCompetitionIdsRemoveForClass(CompetitionSeed.class);
    }

    @Override
    public List<String> getCompetitionPhaseIdsRemove() {
        return getCompetitionIdsRemoveForClass(CompetitionPhase.class);
    }

    @Override
    public List<String> getCompetitionInstanceIdsRemove() {
        return getCompetitionIdsRemoveForClass(CompetitionInstance.class);
    }

    private List<String> getCompetitionIdsRemoveForClass(Class aClass) {
        List<String> ids = new ArrayList<>();
        if (classIntegerRemoveMap.containsKey(aClass)) {
            ids.addAll(classIntegerRemoveMap.get(aClass));
        }
        return ids;
    }


    @Override
    public List<Integer> getCompetitionPlayDatabaseIdsRemove() {
        return getCompetitionDatabaseIdsRemoveForClass(CompetitionPlay.class);
    }

    @Override
    public List<Integer> getCompetitionMatchDatabaseIdsRemove() {
        return getCompetitionDatabaseIdsRemoveForClass(CompetitionMatch.class);
    }

    @Override
    public List<Integer> getCompetitionRoundDatabaseIdsRemove() {
        return getCompetitionDatabaseIdsRemoveForClass(CompetitionRound.class);
    }

    @Override
    public List<Integer> getCompetitionGroupDatabaseIdsRemove() {
        return getCompetitionDatabaseIdsRemoveForClass(CompetitionGroup.class);
    }

    @Override
    public List<Integer> getCompetitionSeedDatabaseIdsRemove() {
        return getCompetitionDatabaseIdsRemoveForClass(CompetitionSeed.class);
    }

    @Override
    public List<Integer> getCompetitionPhaseDatabaseIdsRemove() {
        return getCompetitionDatabaseIdsRemoveForClass(CompetitionPhase.class);
    }

    @Override
    public List<Integer> getCompetitionInstanceDatabaseIdsRemove() {
        return getCompetitionDatabaseIdsRemoveForClass(CompetitionInstance.class);
    }

    private List<Integer> getCompetitionDatabaseIdsRemoveForClass(Class aClass) {
        List<Integer> ids = new ArrayList<>();
        if (classIntegerDatabaseMapRemove.containsKey(aClass)) {
            ids.addAll(classIntegerDatabaseMapRemove.get(aClass));
        }
        return ids;
    }
}

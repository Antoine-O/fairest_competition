package com.qc.competition.service.structure;



import java.util.*;

/**
 * Created by Duncan on 10/10/2015.
 */
public class CompetitionObserver implements Observer, CompetitionObjectChangeList {

    Map<Class, List<String>> classIntegerMap = new Hashtable<>();
    Map<Class, List<String>> classIntegerMapRemove = new Hashtable<>();
    Map<Class, List<Integer>> classIntegerDatabaseMapRemove = new Hashtable<>();

    @Override
    public void update(Observable o, Object arg) {
        Class aClass = o.getClass();
        if (!classIntegerMap.containsKey(aClass)) {
            classIntegerMap.put(aClass, new ArrayList<>());
        }
        if (!classIntegerMapRemove.containsKey(aClass)) {
            classIntegerMapRemove.put(aClass, new ArrayList<>());
        }
        if (!classIntegerDatabaseMapRemove.containsKey(aClass)) {
            classIntegerDatabaseMapRemove.put(aClass, new ArrayList<>());
        }
        CompetitionObjectWithResult competitionObjectWithResult = (CompetitionObjectWithResult) o;
        String localId = competitionObjectWithResult.getLocalId();
        Integer databaseId = competitionObjectWithResult.getDatabaseId();
        if (competitionObjectWithResult.deleted) {
            if (localId != null && !classIntegerMapRemove.get(aClass).contains(localId)) {
                classIntegerMapRemove.get(aClass).add(localId);
                if (databaseId != null && !classIntegerDatabaseMapRemove.get(aClass).contains(databaseId)) {
                    classIntegerDatabaseMapRemove.get(aClass).add(databaseId);
                }
            }
        } else {
            if (localId != null && !classIntegerMap.get(aClass).contains(localId)) {
                classIntegerMap.get(aClass).add(localId);
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

    private List<String> getCompetitionIdsForClass(Class aClass) {
        List<String> ids = new ArrayList<>();
        if (classIntegerMap.containsKey(aClass)) {
            ids.addAll(classIntegerMap.get(aClass));
        }
        return ids;
    }

    private List<String> getCompetitionIdsRemoveForClass(Class aClass) {
        List<String> ids = new ArrayList<>();
        if (classIntegerMapRemove.containsKey(aClass)) {
            ids.addAll(classIntegerMapRemove.get(aClass));
        }
        return ids;
    }

    private List<Integer> getCompetitionDatabaseIdsRemoveForClass(Class aClass) {
        List<Integer> ids = new ArrayList<>();
        if (classIntegerDatabaseMapRemove.containsKey(aClass)) {
            ids.addAll(classIntegerDatabaseMapRemove.get(aClass));
        }
        return ids;
    }

    public boolean isEmpty() {
        return classIntegerMap.isEmpty();
    }

}

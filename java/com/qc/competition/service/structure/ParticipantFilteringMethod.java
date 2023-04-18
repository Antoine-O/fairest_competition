package com.qc.competition.service.structure;

import com.qc.competition.db.entity.competition.PlayVersusType;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by Duncan on 05/01/2015.
 */
public enum ParticipantFilteringMethod {
    ALL, HIGH_PASS, LOW_PASS;

    SortedSet<ParticipantResult> filterParticipantResults(List<ParticipantResult> participantResults, int value, Unit unit) {
        SortedSet<ParticipantResult> participantResultsFiltered = new TreeSet<>();
        switch (this) {
            case ALL:
                participantResultsFiltered.addAll(participantResults);
                break;
            case HIGH_PASS:
            case LOW_PASS:
                int quantity = unit.toQuantity(value, participantResults.size());
                if (quantity > participantResults.size())
                    quantity = participantResults.size();
                ParticipantResult[] participantResultArray = new ParticipantResult[participantResults.size()];
                participantResults.toArray(participantResultArray);
                for (int i = 0; i < quantity; i++) {
                    if (this.compareTo(LOW_PASS) == 0)
                        participantResultsFiltered.add(participantResultArray[participantResultArray.length - 1 - i]);
                    if (this.compareTo(HIGH_PASS) == 0)
                        participantResultsFiltered.add(participantResultArray[i]);
                }
                break;
        }
        return participantResultsFiltered;
    }

    public int computeFilteredParticipantSize(int numberOfParticipantMatch, PlayVersusType playVersusType, int participantSize, int value, Unit unit, Integer minGroupSize, Integer maxGroupSize) {
        int filteredParticipantSize = 0;
        SortedSet<Integer> optimalGroupSizes = new TreeSet<>();
        for (int i = minGroupSize; i <= maxGroupSize; i = i + numberOfParticipantMatch) {
            optimalGroupSizes.add(i);
        }
        switch (this) {
            case ALL:
                filteredParticipantSize = participantSize;
                break;
            case HIGH_PASS:
            case LOW_PASS:
                filteredParticipantSize = unit.toQuantity(value, participantSize);
                int modulo = filteredParticipantSize % numberOfParticipantMatch;
                if (filteredParticipantSize % numberOfParticipantMatch != 0) {
                    filteredParticipantSize = filteredParticipantSize + numberOfParticipantMatch - modulo;
                }
                boolean ok = false;
                while (!ok && filteredParticipantSize < participantSize) {
                    for (Integer optimalGroupSize : optimalGroupSizes) {
                        if (filteredParticipantSize % optimalGroupSize == 0) {
                            ok = true;
                        }
                    }
                    if (!ok)
                        filteredParticipantSize = filteredParticipantSize + numberOfParticipantMatch;
                }


                if (filteredParticipantSize > participantSize)
                    filteredParticipantSize = participantSize;
                break;
        }
        return filteredParticipantSize;
    }


    public int getFilteredParticipantSize(int numberOfParticipantMatch, PlayVersusType playVersusType, int participantSize, int value, Unit unit) {
        int filteredParticipantSize = 0;
        switch (this) {
            case ALL:
                filteredParticipantSize = participantSize;
                break;
            case HIGH_PASS:
            case LOW_PASS:
                filteredParticipantSize = unit.toQuantity(value, participantSize);
                if (filteredParticipantSize > participantSize)
                    filteredParticipantSize = participantSize;
                break;
        }
        return filteredParticipantSize;
    }
}

package com.qc.competition.service.structure;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;

public enum TournamentFormat {
    SWISS,
    ROUND_ROBIN,
    SINGLE_ELIMINATION,
    DOUBLE_ELIMINATION,
    TRIPLE_ELIMINATION,
    QUADRUPLE_ELIMINATION,
    ANY_ELIMINATION,
    LADDER,
    NONE;

    public static final int MAX_MATCHES_PER_ROUND_FOR_ROUND_ROBIN = 32;
    private static Set<TournamentFormat> ALL = new HashSet<>();
    private static Set<TournamentFormat> ALL_ELIMINATION = new HashSet<>();
    private static Set<TournamentFormat> ALL_ELIMINATION_STRICT = new HashSet<>();
    private static Set<TournamentFormat> ALL_BUT_ELIMINATION = new HashSet<>();

    static {
        ALL.add(TournamentFormat.ANY_ELIMINATION);
        ALL.add(TournamentFormat.SINGLE_ELIMINATION);
        ALL.add(TournamentFormat.DOUBLE_ELIMINATION);
        ALL.add(TournamentFormat.TRIPLE_ELIMINATION);
        ALL.add(TournamentFormat.QUADRUPLE_ELIMINATION);
        ALL.add(TournamentFormat.SWISS);
        ALL.add(TournamentFormat.ROUND_ROBIN);

        ALL_ELIMINATION.add(TournamentFormat.ANY_ELIMINATION);
        ALL_ELIMINATION.add(TournamentFormat.SINGLE_ELIMINATION);
        ALL_ELIMINATION.add(TournamentFormat.DOUBLE_ELIMINATION);
        ALL_ELIMINATION.add(TournamentFormat.TRIPLE_ELIMINATION);
        ALL_ELIMINATION.add(TournamentFormat.QUADRUPLE_ELIMINATION);

        ALL_ELIMINATION_STRICT.add(TournamentFormat.SINGLE_ELIMINATION);
        ALL_ELIMINATION_STRICT.add(TournamentFormat.DOUBLE_ELIMINATION);
        ALL_ELIMINATION_STRICT.add(TournamentFormat.TRIPLE_ELIMINATION);
        ALL_ELIMINATION_STRICT.add(TournamentFormat.QUADRUPLE_ELIMINATION);

        ALL_BUT_ELIMINATION.add(TournamentFormat.ROUND_ROBIN);
        ALL_BUT_ELIMINATION.add(TournamentFormat.SWISS);
    }

    public static Set<TournamentFormat> allElimination() {
        return ALL_ELIMINATION;
    }

    public static Set<TournamentFormat> all() {
        return ALL;
    }

    public static Set<TournamentFormat> allButElimination() {
        return ALL_BUT_ELIMINATION;
    }

    public static boolean allowEliminationFormat(Set<TournamentFormat> tournamentFormats) {
        return allowEliminationFormat(tournamentFormats, null);
    }

    public static boolean allowEliminationFormat(Set<TournamentFormat> tournamentFormats, Integer level) {
        boolean result = false;
        for (TournamentFormat tournamentFormat : tournamentFormats) {
            result = allowEliminationFormat(tournamentFormat, level);
            if (result)
                break;
        }
        return result;
    }

    public static boolean allowEliminationFormat(TournamentFormat tournamentFormat, Integer level) {
        boolean result = false;
        switch (tournamentFormat) {
            case SINGLE_ELIMINATION:
                if (level == null || level.compareTo(1) == 0)
                    result = true;
                break;
            case DOUBLE_ELIMINATION:
                if (level == null || level.compareTo(2) == 0)
                    result = true;
                break;
            case TRIPLE_ELIMINATION:
                if (level == null || level.compareTo(3) == 0)
                    result = true;
                break;
            case QUADRUPLE_ELIMINATION:
                if (level == null || level.compareTo(4) == 0)
                    result = true;
                break;
            case ANY_ELIMINATION:
                if (level == null)
                    result = true;
                break;

        }
        return result;
    }

    public static boolean allowAtLeastOneFormat(Set<TournamentFormat> tournamentFormats) {
        boolean result = false;
        for (TournamentFormat tournamentFormat : tournamentFormats) {
            if (tournamentFormat != null && tournamentFormat.compareTo(NONE) != 0) {
                result = true;
                break;
            }
        }
        return result;
    }

    public static TournamentFormat eliminationWithNumberOfBrackets(int numberOfBrackets) {
        TournamentFormat tournamentFormat = null;
        for (TournamentFormat tournamentFormatElt : ALL_ELIMINATION_STRICT) {
            if (allowEliminationFormat(tournamentFormatElt, numberOfBrackets)) {
                tournamentFormat = tournamentFormatElt;
                break;
            }
        }

        return tournamentFormat;
    }

    public static boolean allowFormat(SortedSet<TournamentFormat> tournamentFormats, TournamentFormat tournamentFormat) {
        boolean allow = false;
        if (tournamentFormats != null) {
            for (TournamentFormat tournamentFormatCurrent : tournamentFormats) {
                if (tournamentFormatCurrent.compareTo(tournamentFormat) == 0) {
                    allow = true;
                    break;
                }
            }
        }
        return allow;
    }

    public static boolean checkRoundRobinCompatibility(Integer groupSizeMaximum, Integer numberOfParticipantMatch) {
        return groupSizeMaximum / numberOfParticipantMatch <= MAX_MATCHES_PER_ROUND_FOR_ROUND_ROBIN && numberOfParticipantMatch <= 4 || groupSizeMaximum / numberOfParticipantMatch <= 2;
    }

    public static int getMaximumBrackets(int remainingParticipants, Integer numberOfParticipantMatch) {
        int numberOfBrackets = (int) Math.round(Math.log(remainingParticipants));
        if (numberOfBrackets <= 0)
            numberOfBrackets = 1;
        if (numberOfBrackets > 2)
            numberOfBrackets = (int) Math.round(Math.max(2, (double) numberOfBrackets / Math.sqrt(numberOfBrackets)));

        if (remainingParticipants % numberOfParticipantMatch != 0 && numberOfBrackets == 1)
            numberOfBrackets++;
        return numberOfBrackets;

    }

    public Integer getEliminationLevel() {
        Integer eliminationLevel = null;
        switch (this) {
            case ANY_ELIMINATION:
            case SINGLE_ELIMINATION:
                eliminationLevel = 1;
                break;
            case DOUBLE_ELIMINATION:
                eliminationLevel = 2;
                break;
            case TRIPLE_ELIMINATION:
                eliminationLevel = 3;
                break;
            case QUADRUPLE_ELIMINATION:
                eliminationLevel = 4;
                break;
        }
        return eliminationLevel;
    }
}

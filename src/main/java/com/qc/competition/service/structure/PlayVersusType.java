package com.qc.competition.service.structure;

import java.util.*;

/**
 * Created by Duncan on 18/02/2015.
 */
public enum PlayVersusType {
    ONE_SINGLE_PLAYER(1, 1),
    ONE_VS_ONE(1, 2),
    TWO_VS_TWO(2, 2),
    THREE_VS_THREE(3, 2),
    FOUR_VS_FOUR(4, 2),
    FIVE_VS_FIVE(5, 2),
    SIX_VS_SIX(6, 2),
    SEVEN_VS_SEVEN(7, 2),
    EIGHT_VS_EIGHT(8, 2),
    NINE_VS_NINE(9, 2),
    TEN_VS_TEN(10, 2),
    ELEVEN_VS_ELEVEN(11, 2),
    TWELVE_VS_TWELVE(12, 2),

    FREE_FOR_ALL_3_TEAMS_1_PARTICIPANT(1, 3),
    FREE_FOR_ALL_4_TEAMS_1_PARTICIPANT(1, 4),
    FREE_FOR_ALL_5_TEAMS_1_PARTICIPANT(1, 5),
    FREE_FOR_ALL_6_TEAMS_1_PARTICIPANT(1, 6),
    FREE_FOR_ALL_7_TEAMS_1_PARTICIPANT(1, 7),
    FREE_FOR_ALL_8_TEAMS_1_PARTICIPANT(1, 8),
    FREE_FOR_ALL_9_TEAMS_1_PARTICIPANT(1, 9),
    FREE_FOR_ALL_10_TEAMS_1_PARTICIPANT(1, 10),
    FREE_FOR_ALL_11_TEAMS_1_PARTICIPANT(1, 11),
    FREE_FOR_ALL_12_TEAMS_1_PARTICIPANT(1, 12),
    FREE_FOR_ALL_13_TEAMS_1_PARTICIPANT(1, 13),
    FREE_FOR_ALL_14_TEAMS_1_PARTICIPANT(1, 14),
    FREE_FOR_ALL_15_TEAMS_1_PARTICIPANT(1, 15),
    FREE_FOR_ALL_16_TEAMS_1_PARTICIPANT(1, 16),
    FREE_FOR_ALL_17_TEAMS_1_PARTICIPANT(1, 17),
    FREE_FOR_ALL_18_TEAMS_1_PARTICIPANT(1, 18),
    FREE_FOR_ALL_19_TEAMS_1_PARTICIPANT(1, 19),
    FREE_FOR_ALL_20_TEAMS_1_PARTICIPANT(1, 20),
    FREE_FOR_ALL_21_TEAMS_1_PARTICIPANT(1, 21),
    FREE_FOR_ALL_22_TEAMS_1_PARTICIPANT(1, 22),
    FREE_FOR_ALL_23_TEAMS_1_PARTICIPANT(1, 23),
    FREE_FOR_ALL_24_TEAMS_1_PARTICIPANT(1, 24),
    FREE_FOR_ALL_25_TEAMS_1_PARTICIPANT(1, 25),
    FREE_FOR_ALL_26_TEAMS_1_PARTICIPANT(1, 26),
    FREE_FOR_ALL_27_TEAMS_1_PARTICIPANT(1, 27),
    FREE_FOR_ALL_28_TEAMS_1_PARTICIPANT(1, 28),
    FREE_FOR_ALL_29_TEAMS_1_PARTICIPANT(1, 29),
    FREE_FOR_ALL_30_TEAMS_1_PARTICIPANT(1, 30),
    FREE_FOR_ALL_31_TEAMS_1_PARTICIPANT(1, 31),
    FREE_FOR_ALL_32_TEAMS_1_PARTICIPANT(1, 32),
    FREE_FOR_ALL_33_TEAMS_1_PARTICIPANT(1, 33),
    FREE_FOR_ALL_34_TEAMS_1_PARTICIPANT(1, 34),
    FREE_FOR_ALL_35_TEAMS_1_PARTICIPANT(1, 35),
    FREE_FOR_ALL_36_TEAMS_1_PARTICIPANT(1, 36),
    FREE_FOR_ALL_37_TEAMS_1_PARTICIPANT(1, 37),
    FREE_FOR_ALL_38_TEAMS_1_PARTICIPANT(1, 38),
    FREE_FOR_ALL_39_TEAMS_1_PARTICIPANT(1, 39),
    FREE_FOR_ALL_40_TEAMS_1_PARTICIPANT(1, 40),
    FREE_FOR_ALL_41_TEAMS_1_PARTICIPANT(1, 41),
    FREE_FOR_ALL_42_TEAMS_1_PARTICIPANT(1, 42),
    FREE_FOR_ALL_43_TEAMS_1_PARTICIPANT(1, 43),
    FREE_FOR_ALL_44_TEAMS_1_PARTICIPANT(1, 44),
    FREE_FOR_ALL_45_TEAMS_1_PARTICIPANT(1, 45),
    FREE_FOR_ALL_46_TEAMS_1_PARTICIPANT(1, 46),
    FREE_FOR_ALL_47_TEAMS_1_PARTICIPANT(1, 47),
    FREE_FOR_ALL_48_TEAMS_1_PARTICIPANT(1, 48),
    FREE_FOR_ALL_49_TEAMS_1_PARTICIPANT(1, 49),
    FREE_FOR_ALL_50_TEAMS_1_PARTICIPANT(1, 50),
    FREE_FOR_ALL_100_TEAMS_1_PARTICIPANT(1, 100),

    FREE_FOR_ALL_3_TEAMS_2_PARTICIPANTS(2, 3),
    FREE_FOR_ALL_4_TEAMS_2_PARTICIPANTS(2, 4),
    FREE_FOR_ALL_5_TEAMS_2_PARTICIPANTS(2, 5),
    FREE_FOR_ALL_6_TEAMS_2_PARTICIPANTS(2, 6),
    FREE_FOR_ALL_7_TEAMS_2_PARTICIPANTS(2, 7),
    FREE_FOR_ALL_8_TEAMS_2_PARTICIPANTS(2, 8),
    FREE_FOR_ALL_9_TEAMS_2_PARTICIPANTS(2, 9),
    FREE_FOR_ALL_10_TEAMS_2_PARTICIPANTS(2, 10),
    FREE_FOR_ALL_11_TEAMS_2_PARTICIPANTS(2, 11),
    FREE_FOR_ALL_12_TEAMS_2_PARTICIPANTS(2, 12),
    FREE_FOR_ALL_13_TEAMS_2_PARTICIPANTS(2, 13),
    FREE_FOR_ALL_14_TEAMS_2_PARTICIPANTS(2, 14),
    FREE_FOR_ALL_15_TEAMS_2_PARTICIPANTS(2, 15),
    FREE_FOR_ALL_16_TEAMS_2_PARTICIPANTS(2, 16),
    FREE_FOR_ALL_17_TEAMS_2_PARTICIPANTS(2, 17),
    FREE_FOR_ALL_18_TEAMS_2_PARTICIPANTS(2, 18),
    FREE_FOR_ALL_19_TEAMS_2_PARTICIPANTS(2, 19),
    FREE_FOR_ALL_20_TEAMS_2_PARTICIPANTS(2, 20),
    FREE_FOR_ALL_21_TEAMS_2_PARTICIPANTS(2, 21),
    FREE_FOR_ALL_22_TEAMS_2_PARTICIPANTS(2, 22),
    FREE_FOR_ALL_23_TEAMS_2_PARTICIPANTS(2, 23),
    FREE_FOR_ALL_24_TEAMS_2_PARTICIPANTS(2, 24),
    FREE_FOR_ALL_25_TEAMS_2_PARTICIPANTS(2, 25),
    FREE_FOR_ALL_26_TEAMS_2_PARTICIPANTS(2, 26),
    FREE_FOR_ALL_27_TEAMS_2_PARTICIPANTS(2, 27),
    FREE_FOR_ALL_28_TEAMS_2_PARTICIPANTS(2, 28),
    FREE_FOR_ALL_29_TEAMS_2_PARTICIPANTS(2, 29),
    FREE_FOR_ALL_30_TEAMS_2_PARTICIPANTS(2, 30),
    FREE_FOR_ALL_31_TEAMS_2_PARTICIPANTS(2, 31),
    FREE_FOR_ALL_32_TEAMS_2_PARTICIPANTS(2, 32),
    FREE_FOR_ALL_33_TEAMS_2_PARTICIPANTS(2, 33),
    FREE_FOR_ALL_34_TEAMS_2_PARTICIPANTS(2, 34),
    FREE_FOR_ALL_35_TEAMS_2_PARTICIPANTS(2, 35),
    FREE_FOR_ALL_36_TEAMS_2_PARTICIPANTS(2, 36),
    FREE_FOR_ALL_37_TEAMS_2_PARTICIPANTS(2, 37),
    FREE_FOR_ALL_38_TEAMS_2_PARTICIPANTS(2, 38),
    FREE_FOR_ALL_39_TEAMS_2_PARTICIPANTS(2, 39),
    FREE_FOR_ALL_40_TEAMS_2_PARTICIPANTS(2, 40),
    FREE_FOR_ALL_41_TEAMS_2_PARTICIPANTS(2, 41),
    FREE_FOR_ALL_42_TEAMS_2_PARTICIPANTS(2, 42),
    FREE_FOR_ALL_43_TEAMS_2_PARTICIPANTS(2, 43),
    FREE_FOR_ALL_44_TEAMS_2_PARTICIPANTS(2, 44),
    FREE_FOR_ALL_45_TEAMS_2_PARTICIPANTS(2, 45),
    FREE_FOR_ALL_46_TEAMS_2_PARTICIPANTS(2, 46),
    FREE_FOR_ALL_47_TEAMS_2_PARTICIPANTS(2, 47),
    FREE_FOR_ALL_48_TEAMS_2_PARTICIPANTS(2, 48),
    FREE_FOR_ALL_49_TEAMS_2_PARTICIPANTS(2, 49),
    FREE_FOR_ALL_50_TEAMS_2_PARTICIPANTS(2, 50),


    FREE_FOR_ALL_3_TEAMS_3_PARTICIPANTS(3, 3),
    FREE_FOR_ALL_4_TEAMS_3_PARTICIPANTS(3, 4),
    FREE_FOR_ALL_5_TEAMS_3_PARTICIPANTS(3, 5),
    FREE_FOR_ALL_6_TEAMS_3_PARTICIPANTS(3, 6),
    FREE_FOR_ALL_7_TEAMS_3_PARTICIPANTS(3, 7),
    FREE_FOR_ALL_8_TEAMS_3_PARTICIPANTS(3, 8),
    FREE_FOR_ALL_9_TEAMS_3_PARTICIPANTS(3, 9),
    FREE_FOR_ALL_10_TEAMS_3_PARTICIPANTS(3, 10),
    FREE_FOR_ALL_11_TEAMS_3_PARTICIPANTS(3, 11),
    FREE_FOR_ALL_12_TEAMS_3_PARTICIPANTS(3, 12),
    FREE_FOR_ALL_13_TEAMS_3_PARTICIPANTS(3, 13),
    FREE_FOR_ALL_14_TEAMS_3_PARTICIPANTS(3, 14),
    FREE_FOR_ALL_15_TEAMS_3_PARTICIPANTS(3, 15),
    FREE_FOR_ALL_16_TEAMS_3_PARTICIPANTS(3, 16),
    FREE_FOR_ALL_17_TEAMS_3_PARTICIPANTS(3, 17),
    FREE_FOR_ALL_18_TEAMS_3_PARTICIPANTS(3, 18),
    FREE_FOR_ALL_19_TEAMS_3_PARTICIPANTS(3, 19),
    FREE_FOR_ALL_20_TEAMS_3_PARTICIPANTS(3, 20),
    FREE_FOR_ALL_21_TEAMS_3_PARTICIPANTS(3, 21),
    FREE_FOR_ALL_22_TEAMS_3_PARTICIPANTS(3, 22),
    FREE_FOR_ALL_23_TEAMS_3_PARTICIPANTS(3, 23),
    FREE_FOR_ALL_24_TEAMS_3_PARTICIPANTS(3, 24),
    FREE_FOR_ALL_25_TEAMS_3_PARTICIPANTS(3, 25),
    FREE_FOR_ALL_26_TEAMS_3_PARTICIPANTS(3, 26),
    FREE_FOR_ALL_27_TEAMS_3_PARTICIPANTS(3, 27),
    FREE_FOR_ALL_28_TEAMS_3_PARTICIPANTS(3, 28),
    FREE_FOR_ALL_29_TEAMS_3_PARTICIPANTS(3, 29),
    FREE_FOR_ALL_30_TEAMS_3_PARTICIPANTS(3, 30),
    FREE_FOR_ALL_31_TEAMS_3_PARTICIPANTS(3, 31),
    FREE_FOR_ALL_32_TEAMS_3_PARTICIPANTS(3, 32),
    FREE_FOR_ALL_33_TEAMS_3_PARTICIPANTS(3, 33),
    FREE_FOR_ALL_34_TEAMS_3_PARTICIPANTS(3, 34),
    FREE_FOR_ALL_35_TEAMS_3_PARTICIPANTS(3, 35),
    FREE_FOR_ALL_36_TEAMS_3_PARTICIPANTS(3, 36),
    FREE_FOR_ALL_37_TEAMS_3_PARTICIPANTS(3, 37),
    FREE_FOR_ALL_38_TEAMS_3_PARTICIPANTS(3, 38),
    FREE_FOR_ALL_39_TEAMS_3_PARTICIPANTS(3, 39),
    FREE_FOR_ALL_40_TEAMS_3_PARTICIPANTS(3, 40),
    FREE_FOR_ALL_41_TEAMS_3_PARTICIPANTS(3, 41),
    FREE_FOR_ALL_42_TEAMS_3_PARTICIPANTS(3, 42),
    FREE_FOR_ALL_43_TEAMS_3_PARTICIPANTS(3, 43),
    FREE_FOR_ALL_44_TEAMS_3_PARTICIPANTS(3, 44),
    FREE_FOR_ALL_45_TEAMS_3_PARTICIPANTS(3, 45),
    FREE_FOR_ALL_46_TEAMS_3_PARTICIPANTS(3, 46),
    FREE_FOR_ALL_47_TEAMS_3_PARTICIPANTS(3, 47),
    FREE_FOR_ALL_48_TEAMS_3_PARTICIPANTS(3, 48),
    FREE_FOR_ALL_49_TEAMS_3_PARTICIPANTS(3, 49),
    FREE_FOR_ALL_50_TEAMS_3_PARTICIPANTS(3, 50),


    FREE_FOR_ALL_3_TEAMS_4_PARTICIPANTS(4, 3),
    FREE_FOR_ALL_4_TEAMS_4_PARTICIPANTS(4, 4),
    FREE_FOR_ALL_5_TEAMS_4_PARTICIPANTS(4, 5),
    FREE_FOR_ALL_6_TEAMS_4_PARTICIPANTS(4, 6),
    FREE_FOR_ALL_7_TEAMS_4_PARTICIPANTS(4, 7),
    FREE_FOR_ALL_8_TEAMS_4_PARTICIPANTS(4, 8),
    FREE_FOR_ALL_9_TEAMS_4_PARTICIPANTS(4, 9),
    FREE_FOR_ALL_10_TEAMS_4_PARTICIPANTS(4, 10),
    FREE_FOR_ALL_11_TEAMS_4_PARTICIPANTS(4, 11),
    FREE_FOR_ALL_12_TEAMS_4_PARTICIPANTS(4, 12),
    FREE_FOR_ALL_13_TEAMS_4_PARTICIPANTS(4, 13),
    FREE_FOR_ALL_14_TEAMS_4_PARTICIPANTS(4, 14),
    FREE_FOR_ALL_15_TEAMS_4_PARTICIPANTS(4, 15),
    FREE_FOR_ALL_16_TEAMS_4_PARTICIPANTS(4, 16),
    FREE_FOR_ALL_17_TEAMS_4_PARTICIPANTS(4, 17),
    FREE_FOR_ALL_18_TEAMS_4_PARTICIPANTS(4, 18),
    FREE_FOR_ALL_19_TEAMS_4_PARTICIPANTS(4, 19),
    FREE_FOR_ALL_20_TEAMS_4_PARTICIPANTS(4, 20),
    FREE_FOR_ALL_21_TEAMS_4_PARTICIPANTS(4, 21),
    FREE_FOR_ALL_22_TEAMS_4_PARTICIPANTS(4, 22),
    FREE_FOR_ALL_23_TEAMS_4_PARTICIPANTS(4, 23),
    FREE_FOR_ALL_24_TEAMS_4_PARTICIPANTS(4, 24),
    FREE_FOR_ALL_25_TEAMS_4_PARTICIPANTS(4, 25),
    FREE_FOR_ALL_26_TEAMS_4_PARTICIPANTS(4, 26),
    FREE_FOR_ALL_27_TEAMS_4_PARTICIPANTS(4, 27),
    FREE_FOR_ALL_28_TEAMS_4_PARTICIPANTS(4, 28),
    FREE_FOR_ALL_29_TEAMS_4_PARTICIPANTS(4, 29),
    FREE_FOR_ALL_30_TEAMS_4_PARTICIPANTS(4, 30),
    FREE_FOR_ALL_31_TEAMS_4_PARTICIPANTS(4, 31),
    FREE_FOR_ALL_32_TEAMS_4_PARTICIPANTS(4, 32),
    FREE_FOR_ALL_33_TEAMS_4_PARTICIPANTS(4, 33),
    FREE_FOR_ALL_34_TEAMS_4_PARTICIPANTS(4, 34),
    FREE_FOR_ALL_35_TEAMS_4_PARTICIPANTS(4, 35),
    FREE_FOR_ALL_36_TEAMS_4_PARTICIPANTS(4, 36),
    FREE_FOR_ALL_37_TEAMS_4_PARTICIPANTS(4, 37),
    FREE_FOR_ALL_38_TEAMS_4_PARTICIPANTS(4, 38),
    FREE_FOR_ALL_39_TEAMS_4_PARTICIPANTS(4, 39),
    FREE_FOR_ALL_40_TEAMS_4_PARTICIPANTS(4, 40),
    FREE_FOR_ALL_41_TEAMS_4_PARTICIPANTS(4, 41),
    FREE_FOR_ALL_42_TEAMS_4_PARTICIPANTS(4, 42),
    FREE_FOR_ALL_43_TEAMS_4_PARTICIPANTS(4, 43),
    FREE_FOR_ALL_44_TEAMS_4_PARTICIPANTS(4, 44),
    FREE_FOR_ALL_45_TEAMS_4_PARTICIPANTS(4, 45),
    FREE_FOR_ALL_46_TEAMS_4_PARTICIPANTS(4, 46),
    FREE_FOR_ALL_47_TEAMS_4_PARTICIPANTS(4, 47),
    FREE_FOR_ALL_48_TEAMS_4_PARTICIPANTS(4, 48),
    FREE_FOR_ALL_49_TEAMS_4_PARTICIPANTS(4, 49),
    FREE_FOR_ALL_50_TEAMS_4_PARTICIPANTS(4, 50),
    ;

    //    private static Logger LOGGER_fillParticipantPairing = Logger.getLogger(PlayVersusType.class.getName() + ".fillParticipantPairing");
//    private static Logger LOGGER_getParticipantPairing = Logger.getLogger(PlayVersusType.class.getName() + ".getParticipantPairing");
    public int numberOfTeam;
    public int teamSize;

    PlayVersusType(int teamSize, int numberOfTeam) {
        this.teamSize = teamSize;
        this.numberOfTeam = numberOfTeam;
    }

    public static PlayVersusType getValueFor(int numberOfTeam, int teamSize) {
        PlayVersusType playVersusTypeResult = null;
        for (PlayVersusType playVersusType : PlayVersusType.values()) {
            if (playVersusType.numberOfTeam == numberOfTeam && playVersusType.teamSize == teamSize) {
                playVersusTypeResult = playVersusType;
                break;
            }
        }
        return playVersusTypeResult;
    }

    public int comparePriorityTo(PlayVersusType playVersusType) {
        int compare = Integer.compare(this.numberOfTeam, playVersusType.numberOfTeam);
        if (compare == 0)
            compare = Integer.compare(this.teamSize, playVersusType.teamSize);
        return compare;
    }

    public void fillParticipantPairing(CompetitionPlay competitionPlay) {
//        Logger logger = LOGGER_fillParticipantPairing;
        ParticipantPairing participantPairingMatch = competitionPlay.getCompetitionMatch().participantPairing;
        SortedSet<ParticipantPairing> participantPairings = new TreeSet<>();
        ParticipantPairing participantPairing = null;
        int round = competitionPlay.round;
//        switch (this) {
//            case SIX_VS_SIX:
//            case FIVE_VS_FIVE:
//            case FOUR_VS_FOUR:
//            case ONE_VS_ONE:
//                //    case ONE_VS_ONE_VS_ONE:
//            case TWO_VS_TWO:
//            case THREE_VS_THREE:
//            case FREE_FOR_ALL_4_TEAMS_1_PARTICIPANT: {
        SortedMap<ParticipantTeam, SortedSet<ParticipantTeamMember>> participantTeamMembersMap = new TreeMap<>();
        List<Participant> participants = participantPairingMatch.getRealParticipantsAsArray();
        for (Participant participant : participants) {
            if (participant instanceof ParticipantTeam) {
                ParticipantTeam participantTeam = (ParticipantTeam) participant;
//                    if (participant instanceof ParticipantTeam && !(participant instanceof ParticipantTeamVoid) )
                participantTeamMembersMap.put(participantTeam, participantTeam.participantTeamMembers);
            }
        }
        participantPairing = getParticipantPairing(competitionPlay.getCompetitionInstance(), participantTeamMembersMap, round);
//                break;
//            }
//
//        }
        participantPairing.removeParticipantTeamVoid();
        participantPairing.setCompetitionPlay(competitionPlay);

    }

    public ParticipantPairing getParticipantPairing(CompetitionInstance competitionInstance, SortedMap<ParticipantTeam, SortedSet<ParticipantTeamMember>> participantTeamMembersMap, int round) {
//        Logger logger = LOGGER_getParticipantPairing;
        ParticipantPairing participantPairing = null;
//        switch (this) {
//            case FIVE_VS_FIVE:
//            case FOUR_VS_FOUR:
//            case ONE_VS_ONE:
//            case SIX_VS_SIX:
//            case TWO_VS_TWO:
//            case THREE_VS_THREE:
//            case FREE_FOR_ALL_4_TEAMS_1_PARTICIPANT: {


        SortedMap<Integer, SortedMap<ParticipantTeam, SortedSet<ParticipantTeamMember>>> participantTeamMembersPlayMapByRound = new TreeMap<>();

        int indexRowStart = 0;
        int indexColStart = 0;
        int indexRowEnd = 0;
        int indexColEnd = 0;
        int indexRow = 0;
        int indexCol = 0;
        int numberOfTeamCorrected = Math.min(numberOfTeam, participantTeamMembersMap.keySet().size());
        if (numberOfTeamCorrected > 0) {
            for (int i = 0; i < round; i++) {
                indexColStart = i % participantTeamMembersMap.keySet().size();
                indexColEnd = (i + numberOfTeamCorrected) % participantTeamMembersMap.keySet().size();
                participantTeamMembersPlayMapByRound.put(i + 1,
                        new TreeMap<>());
                indexCol = 0;
                for (ParticipantTeam participantTeam : participantTeamMembersMap.keySet()) {
                    if ((indexColStart <= indexColEnd && indexCol >= indexColStart && indexCol < indexColEnd)
                            || (indexColStart > indexColEnd && (indexCol >= indexColStart || indexCol < indexColEnd)) || (indexColEnd == indexColStart && numberOfTeamCorrected > 0)) {
                        participantTeamMembersPlayMapByRound.get(i + 1).put(participantTeam, new TreeSet<>());
                        indexRowStart = (i * teamSize) % participantTeamMembersMap.get(participantTeam).size();
                        indexRowEnd = (indexRowStart + teamSize) % participantTeamMembersMap.get(participantTeam).size();
                        indexRow = 0;
                        for (ParticipantTeamMember participantTeamMember : participantTeamMembersMap.get(participantTeam)) {
                            if ((indexRowStart <= indexRowEnd && indexRow >= indexRowStart && indexRow < indexRowEnd)
                                    || (indexRowStart > indexRowEnd && (indexRow >= indexRowStart || indexRow < indexRowEnd)) || (indexRowEnd == indexRowStart && teamSize > 0)) {
                                participantTeamMembersPlayMapByRound.get(i + 1).get(participantTeam).add(participantTeamMember);
                            }
                            indexRow++;
                        }
                    }
                    indexCol++;
                }
            }

            participantPairing = competitionInstance.createParticipantPairing(this.numberOfTeam);
//        participantPairing.setCompetitionInstance(competitionInstance);

            for (ParticipantTeam participantTeam : participantTeamMembersPlayMapByRound.get(round).keySet()) {
//            try {
                if (participantTeam.participantTeamMembers.size() == 1) {
                    participantPairing.addParticipant(participantTeam);
                } else {
                    if (participantTeamMembersPlayMapByRound.get(round).get(participantTeam).size() == teamSize) {
                        participantPairing.addParticipant(participantTeam);
                    } else {
                        Participant participantSub = participantTeamMembersPlayMapByRound.get(round).get(participantTeam).first().getParticipant();
                        if (participantTeamMembersPlayMapByRound.get(round).get(participantTeam).size() == teamSize && participantSub instanceof ParticipantTeam) {
                            participantPairing.addParticipant(participantTeamMembersPlayMapByRound.get(round).get(participantTeam).first().getParticipant());
                        } else {
                            ParticipantTeam participantTeamNew = competitionInstance.createParticipantTeam();
                            participantTeamNew.bibNumber = participantTeam.bibNumber;
                            participantTeamNew.internationalizedLabel = participantTeam.internationalizedLabel;
//                            competitionInstance.addParticipantTeam(participantTeam);
                            for (ParticipantTeamMember participantTeamMemberToKeep : participantTeamMembersPlayMapByRound.get(round).get(participantTeam)) {
                                participantTeamNew.addParticipant(participantTeamMemberToKeep.getParticipant());
//                                competitionInstance.addParticipantTeamMember(participantTeam.localId, participantTeamMemberToKeep.participantLocalId);
                            }
                            participantPairing.addParticipant(participantTeamNew);
                        }
                    }
                }
            }
        } else {
            participantPairing = competitionInstance.createParticipantPairing(this.numberOfTeam);
        }
        for (ParticipantSeat participantSeat : participantPairing.participantSeats.toArray(new ParticipantSeat[participantPairing.participantSeats.size()])) {
            if (participantSeat.participant == null) {
                participantSeat.participant = competitionInstance.createParticipantTeamVoid();
            }
        }
        return participantPairing;
    }

    public int getRoundQuantity(CompetitionInstance competitionInstance, SortedMap<ParticipantTeam, SortedSet<ParticipantTeamMember>> participantTeamMembersMap) {
        SortedMap<ParticipantTeam, SortedSet<ParticipantTeamMember>> participantTeamMembersMapCopy = new TreeMap<>();
        for (ParticipantTeam participantTeam : participantTeamMembersMap.keySet()) {
            participantTeamMembersMapCopy.put(participantTeam, new TreeSet<>());
            participantTeamMembersMapCopy.get(participantTeam).addAll(participantTeamMembersMap.get(participantTeam));

        }
        ParticipantPairing participantPairing = null;
        ParticipantTeam participantTeam = null;
        int round = 0;
        do {
            round++;
            participantPairing = getParticipantPairing(competitionInstance, participantTeamMembersMap, round);
            boolean participantFound = false;
            for (Participant participantFromPairing : participantPairing.getParticipantsAsArray()) {
//                Participant participantFromPairing = competitionInstance.getParticipantTeamOrSingle(participantIdFromPairing);
                if (participantFromPairing instanceof ParticipantTeam) {
                    participantTeam = (ParticipantTeam) participantFromPairing;
                    for (ParticipantTeamMember participantTeamMemberFromPairing : participantTeam.participantTeamMembers) {
                        for (Participant participant : participantTeamMembersMapCopy.keySet()) {
                            for (ParticipantTeamMember participantTeamMember : participantTeamMembersMapCopy.get(participant)) {
                                if (participantTeamMember.compareTo(participantTeamMemberFromPairing) == 0) {
                                    participantTeamMembersMapCopy.get(participant).remove(participantTeamMember);
                                    participantFound = true;
                                    break;
                                }
                            }
                        }
                    }
                    if (participantFound) {
                        boolean hasChanged = true;
                        while (hasChanged) {
                            hasChanged = false;
                            for (Participant participant : participantTeamMembersMapCopy.keySet()) {
                                if (participantTeamMembersMapCopy.get(participant).isEmpty()) {
                                    participantTeamMembersMapCopy.remove(participant);
                                    hasChanged = true;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        } while (!participantTeamMembersMapCopy.isEmpty());

        return round;
    }

    public int roundUp(int numberOfParticipant) {
        numberOfParticipant = numberOfParticipant + (numberOfTeam - numberOfParticipant % numberOfTeam) % numberOfTeam;
        return numberOfParticipant;
    }

    public PlayVersusType toCaptainRegistrationMode() {
        PlayVersusType playVersusTypeCaptainMode = null;
        switch (this) {
            case TWO_VS_TWO:
            case THREE_VS_THREE:
            case FOUR_VS_FOUR:
            case FIVE_VS_FIVE:
            case SIX_VS_SIX:
                playVersusTypeCaptainMode = ONE_VS_ONE;
                break;
            case ONE_VS_ONE:
            case FREE_FOR_ALL_3_TEAMS_1_PARTICIPANT:
            case FREE_FOR_ALL_4_TEAMS_1_PARTICIPANT:
            case FREE_FOR_ALL_5_TEAMS_1_PARTICIPANT:
            case FREE_FOR_ALL_6_TEAMS_1_PARTICIPANT:
            case FREE_FOR_ALL_7_TEAMS_1_PARTICIPANT:
            case FREE_FOR_ALL_8_TEAMS_1_PARTICIPANT:
                break;

        }
        return playVersusTypeCaptainMode;
    }

    public String toDisplayString() {
        String displayString = "";
        if (numberOfTeam == 1) {
            displayString = "";
        } else {
            if (numberOfTeam <= 4) {
                for (int i = 0; i < numberOfTeam; i++) {
                    if (i > 0) {
                        displayString += "v";
                    }
                    displayString += teamSize;
                }
            } else {
                displayString += numberOfTeam + "x" + teamSize;
            }
        }
        return displayString;
    }
}
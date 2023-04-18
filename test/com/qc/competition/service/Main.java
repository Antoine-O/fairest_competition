package com.qc.competition.service;

import com.qc.competition.db.entity.competition.PlayVersusType;
import com.qc.competition.db.entity.game.MergePolicy;
import com.qc.competition.db.entity.game.ParticipantType;
import com.qc.competition.db.entity.game.ResetPolicy;
import com.qc.competition.db.entity.game.TournamentFormat;
import com.qc.competition.service.structure.*;
import com.qc.competition.service.structure.tree.CompetitionInstanceTree;
import com.qc.competition.service.template.*;
import com.qc.competition.service.template.automatic.participation.optimization.CompetitionInstanceGeneratorImpl;
import com.qc.competition.service.xml.XmlUtils;
import com.qc.competition.utils.Sets;
import com.qc.competition.utils.json.JSONUtils;
import com.qc.competition.ws.simplestructure.Duration;

import java.io.*;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

//import java.time.Duration;

/**
 * Created by Duncan on 04/10/2015.
 */
public class Main {
    public static CompetitionInstance competitionInstance = null;
    public static List<Participant> participants = null;
    public static String competitionFileName;
    public static String participantFileName;
    public static String configurationFileName;
    public static Boolean doVersioning = Boolean.TRUE;
    public static Boolean demo = Boolean.FALSE;
    public static int saveIndex = 0;
    public static String cssUrl;

    public static String homeDirectory = "D:\\tempqc\\";

    public static void main(String[] args) throws Exception {
        CompetitionComputationParam competitionComputationParam = null;
        if (args.length == 0) {
            competitionFileName = homeDirectory + "competition.xml";
            competitionInstance = readCompetitionInstance(competitionFileName);
            participantFileName = homeDirectory + "participants.csv";
//            participants = readParticipants(participantFileName);
            configurationFileName = homeDirectory + "competition_creation_param.properties";
            competitionComputationParam = readCompetitionComputationParam(configurationFileName);
            demo = true;
        } else {
            for (int i = 0; i < args.length; i++) {
                switch (args[i]) {
                    case "PARTICIPANTS": {
                        i++;
                        participantFileName = args[i];
                        break;
                    }
                    case "COMPETITION": {
                        i++;
                        competitionFileName = args[i];
                        competitionInstance = readCompetitionInstance(competitionFileName);
                        break;
                    }
                    case "CONFIGURATION": {
                        i++;
                        configurationFileName = args[i];
                        competitionComputationParam = readCompetitionComputationParam(args[i]);
                        break;
                    }
                    case "DEMO": {
                        demo = true;
                        break;
                    }
                }
            }
        }
        if (competitionInstance == null) {
            if (competitionComputationParam != null && participants != null && !participants.isEmpty()) {
                CompetitionInstanceGeneratorImpl competitionInstanceGeneratorImpl = new CompetitionInstanceGeneratorImpl();
                competitionComputationParam.numberOfParticipantCompetition = participants.size();
                competitionInstance = competitionInstanceGeneratorImpl.computeCompetitionFormatFor(competitionComputationParam);
                System.out.println(competitionInstance.getDescriptionTable());
                participants = readParticipants(competitionInstance, participantFileName);
                int rank = 1;
                for (Participant participant : participants) {
                    rank++;
                    competitionInstance.subscribe(participant, rank);
                }
                competitionInstance.initialize();
                competitionInstance.open();
                saveHistory();
//                writeCompetitionInstance(competitionInstance, competitionFileName);
//                competitionInstance = readCompetitionInstance(competitionFileName);
//                String competitionTreeFileName = competitionFileName.substring(0, competitionFileName.lastIndexOf(".")) + "_tree" + competitionFileName.substring(competitionFileName.lastIndexOf("."));
//                writeCompetitionTree(competitionInstance, competitionTreeFileName);
//                String competitionCartoucheFileName = competitionFileName.substring(0, competitionFileName.lastIndexOf(".")) + "_cartouche.html";
//                writeCompetitionCartouche(competitionInstance, competitionCartoucheFileName);
            }
        }

        System.out.println(competitionInstance.getDescriptionTable());
        menu();
    }

    private static void saveHistory() throws IOException {
        saveHistory(doVersioning);
    }

    private static void saveHistory(boolean doVersioning) throws IOException {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMDD_HH_mm_ss");
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMinimumIntegerDigits(4);
        numberFormat.setGroupingUsed(false);
        String suffix = "_" + numberFormat.format(saveIndex) + "_" + dateFormat.format(new Date());
        writeCompetitionInstance(competitionInstance, competitionFileName);
        if (doVersioning) {
            String competitionFileNameVersioned = competitionFileName.substring(0, competitionFileName.lastIndexOf(".")) + suffix + competitionFileName.substring(competitionFileName.lastIndexOf("."));
            writeCompetitionInstance(competitionInstance, competitionFileNameVersioned);
        }
        competitionInstance = readCompetitionInstance(competitionFileName);
//
//        String competitionTreeFileName = competitionFileName.substring(0, competitionFileName.lastIndexOf(".")) + "_tree" + competitionFileName.substring(competitionFileName.lastIndexOf("."));
//        writeCompetitionTree(competitionInstance, competitionTreeFileName);
//        if (saveVersionedVersion) {
//            String competitionTreeFileNameVersioned = competitionTreeFileName.substring(0, competitionTreeFileName.lastIndexOf(".")) + suffix + competitionTreeFileName.substring(competitionTreeFileName.lastIndexOf("."));
//            writeCompetitionTree(competitionInstance, competitionTreeFileNameVersioned);
//        }
//
        String competitionCartoucheFileName = competitionFileName.substring(0, competitionFileName.lastIndexOf(".")) + "_cartouche.html";
        String cssContent = Utils.getCssContent();
        writeCompetitionCartouche(competitionInstance, competitionCartoucheFileName, cssUrl, cssContent);
        if (doVersioning) {
            String competitionCartoucheFileNameVersioned = competitionCartoucheFileName.substring(0, competitionCartoucheFileName.lastIndexOf(".")) + suffix + competitionCartoucheFileName.substring(competitionCartoucheFileName.lastIndexOf("."));
            writeCompetitionCartouche(competitionInstance, competitionCartoucheFileNameVersioned, cssUrl, cssContent);
        }

    }

    private static void writeCompetitionTree(CompetitionInstance competitionInstance, String competitionTreeFileName) throws IOException {
        CompetitionInstanceTree competitionInstanceTree = competitionInstance.getCompetitionInstanceTree();
        XmlUtils<CompetitionInstanceTree> xmlUtils = new XmlUtils<>(CompetitionInstanceTree.class);
        xmlUtils.toFile(competitionInstanceTree, competitionTreeFileName);
    }

    private static void writeCompetitionCartouche(CompetitionInstance competitionInstance, String competitionCartoucheFileName, String cssUrl, String cssContent) throws IOException {
        competitionInstance.writeCartouche(competitionCartoucheFileName, cssUrl, cssContent);
    }

    private static void menu() throws IOException, CompetitionInstanceGeneratorException {
        Scanner input = new Scanner(System.in);
        String answer = "";
        while (!answer.equalsIgnoreCase("quit")) {
            int index = 0;

            index++;
            int openMatches = index;
            System.out.println(openMatches + " - Display open matchDetails list");
//            index++;
//            int openPlays = index;
//            System.out.println(index + " - Display open playDetails's list");
//            index++;
//            int openPlaySelect = index;
//            System.out.println(index + " - Display specific open playDetails");
            index++;
            int openMatchSelect = index;
            System.out.println(index + " - Display specific open matchDetails");
            index++;
            int userSelect = index;
            System.out.println(index + " - Search for user");
            int simulation = -1;
            int simulationPerWave = -1;
            if (demo && !competitionInstance.isOver()) {
                index++;
                simulation = index;
                System.out.println(index + " - LaunchSimulation");
                index++;
                simulationPerWave = index;
                System.out.println(index + " - LaunchSimulationPerWave");
            }
            int ranking = -1;
            if (competitionInstance.isOver()) {
                index++;
                ranking = index;
                System.out.println(index + " - Display Ranking");
            }

            System.out.println("or type quit");
            answer = input.nextLine();
            if (answer.trim().compareToIgnoreCase("quit") == 0) {
                input.close();
                System.exit(0);
            } else if (answer.trim().compareToIgnoreCase("" + openMatches) == 0) {
                SortedSet<CompetitionMatch> openCompetitionMatches = competitionInstance.getOpenCompetitionMatches();
                Sets.sort(openCompetitionMatches);
                for (CompetitionMatch openCompetitionMatch : openCompetitionMatches)
                    System.out.println(openCompetitionMatch.toScreenDescription());

                System.out.println(openCompetitionMatches.size() + " matchDetails(es)");
//            } else if (answer.trim().compareToIgnoreCase("" + openPlays) == 0) {
//                SortedSet<CompetitionPlay> openCompetitionPlays = competitionInstance.getOpenCompetitionPlays();
//                Sets.sort(openCompetitionPlays);
//                for (CompetitionPlay openCompetitionPlay : openCompetitionPlays)
//                    System.out.println(openCompetitionPlay.toScreenDescription());
//                System.out.println(openCompetitionPlays.size() + " playDetails(s)");
//            } else if (answer.trim().compareToIgnoreCase("" + openPlaySelect) == 0) {
//                System.out.print("playId:");
//                answer = userInput.nextLine();
//                if (!answer.trim().isEmpty()) {
//                    SortedSet<CompetitionPlay> openCompetitionPlays = competitionInstance.getOpenCompetitionPlays();
//                    CompetitionPlay openCompetitionPlaySelected = null;
//                    for (CompetitionPlay openCompetitionPlay : openCompetitionPlays) {
//                        if (openCompetitionPlay.localId.compareTo(Integer.valueOf(answer)) == 0) {
//                            openCompetitionPlaySelected = openCompetitionPlay;
//                        }
//                    }
//                    if (openCompetitionPlaySelected != null) {
//                        System.out.println(openCompetitionPlaySelected.toScreenDescription());
//                        if (openCompetitionPlaySelected.playStatus.compareTo(PlayStatus.CLOSED) != 0) {
//                            int indexParticipantChoice = 1;
//                            List<Participant> participants = openCompetitionPlaySelected.participantPairing.getParticipantsAsArray();
//                            for (Participant participant : participants) {
//                                System.out.println(indexParticipantChoice + " - " + participant.internationalizedLabel.defaultLabel + " WINS");
//                                indexParticipantChoice++;
//                            }
//                            int drawChoiceIndex = indexParticipantChoice;
//                            System.out.println(drawChoiceIndex + " - DRAW");
//                            indexParticipantChoice++;
//                            System.out.println("Other - back");
//                            answer = userInput.nextLine();
//                            if (!answer.isEmpty()) {
//                                SortedSet<ParticipantResult> participantResults = new TreeSet<>();
//                                if (Integer.parseInt(answer) <= drawChoiceIndex) {
//                                    int rankFirst = 1;
//                                    int rankOthers = 2;
//                                    if (Integer.parseInt(answer) == drawChoiceIndex) {
//                                        rankOthers = 1;
//                                    }
//                                    for (Participant participant : participants) {
//                                        ParticipantResult participantResult = ParticipantResult.createParticipantResultFor(openCompetitionPlaySelected);
//                                        participantResult.participant = participant;
//                                        if (participant.compareTo(participants.get(Integer.parseInt(answer) - 1)) == 0)
//                                            participantResult.rank = rankFirst;
//                                        else
//                                            participantResult.rank = rankOthers;
//
//                                        participantResults.add(participantResult);
//                                    }
//                                }
//                                Sets.sort(participantResults);
//                                openCompetitionPlaySelected.setParticipantResults(participantResults);
//                                saveHistory();
//                            }
//                        }
//                    }
//                }
            } else if (answer.trim().compareToIgnoreCase("" + openMatchSelect) == 0) {
                System.out.print("matchId:");
                answer = input.nextLine();
                if (!answer.trim().isEmpty()) {
                    SortedSet<CompetitionMatch> openCompetitionMatchs = competitionInstance.getOpenCompetitionMatches();
                    CompetitionMatch openCompetitionMatchSelected = null;
                    Sets.sort(openCompetitionMatchs);
                    for (CompetitionMatch openCompetitionMatch : openCompetitionMatchs) {
                        if (openCompetitionMatch.localId.compareTo(answer) == 0) {
                            openCompetitionMatchSelected = openCompetitionMatch;
                        }
                    }
                    if (openCompetitionMatchSelected != null) {
                        System.out.println(openCompetitionMatchSelected.toScreenDescription());
                        if (!openCompetitionMatchSelected.isParticipantResultsSet()) {
                            int drawChoiceIndex = 0;
                            do {
                                SortedSet<CompetitionPlay> competitionPlays = openCompetitionMatchSelected.getOpenCompetitionPlays();
                                if (competitionPlays.isEmpty())
                                    break;
                                int indexSelectedParticipant = 1;
                                CompetitionPlay openCompetitionPlaySelected = competitionPlays.first();
                                List<Participant> participants = openCompetitionPlaySelected.participantPairing.getRealParticipantsAsArray();
                                Map<Integer, Participant> choiceParticipantId = new HashMap<>();
                                for (Participant participant : participants) {
                                    System.out.println(indexSelectedParticipant + " - " + participant.internationalizedLabel.defaultLabel + " WINS");
                                    choiceParticipantId.put(indexSelectedParticipant, participant);
                                    indexSelectedParticipant++;
                                }
                                drawChoiceIndex = indexSelectedParticipant;
                                System.out.println(drawChoiceIndex + " - DRAW");
                                indexSelectedParticipant++;
                                System.out.println("Other - back");
                                answer = input.nextLine();
                                if (!answer.trim().isEmpty()) {
                                    SortedSet<ParticipantResult> participantResults = new TreeSet<>();
                                    String[] resultOrdered = answer.trim().split(",");
                                    List<Participant> remaingParticipants = new ArrayList<>();
                                    remaingParticipants.addAll(Main.participants);
                                    for (int i = 0; i < resultOrdered.length; i++) {
                                        for (Participant participant : remaingParticipants) {
                                            if (participant.compareTo(choiceParticipantId.get(Integer.valueOf(resultOrdered[i]))) == 0) {
                                                ParticipantResult participantResult = ParticipantResult.createParticipantResultFor(openCompetitionPlaySelected.getIdGenerator(), openCompetitionPlaySelected);
                                                participantResult.setParticipant(participant);
                                                participantResult.rank = i + 1;
                                                participantResult.participantScore.setParticipantScoreValue(ParticipantScorePlay.SCORE_POINTS, Math.pow(2, Main.participants.size() - participantResult.rank));
                                                participantResults.add(participantResult);
                                                remaingParticipants.remove(participant);
                                                break;
                                            }
                                        }
                                    }
                                    for (Participant participant : remaingParticipants) {
                                        ParticipantResult participantResult = ParticipantResult.createParticipantResultFor(openCompetitionPlaySelected.getIdGenerator(), openCompetitionPlaySelected);
                                        participantResult.setParticipant(participant);
                                        participantResult.rank = resultOrdered.length + 1;
                                        participantResult.participantScore.setParticipantScoreValue(ParticipantScorePlay.SCORE_POINTS, Math.pow(2, Main.participants.size() - participantResult.rank));
                                        participantResults.add(participantResult);
                                    }

                                    Sets.sort(participantResults);
                                    openCompetitionPlaySelected.setParticipantResults(participantResults);
                                    openCompetitionMatchSelected = competitionInstance.getCompetitionMatch(openCompetitionMatchSelected.localId);
                                    System.out.println(openCompetitionMatchSelected.toScreenDescription());
                                    if (competitionInstance.isOver())
                                        saveHistory();
                                    else
                                        saveHistory();
                                    openCompetitionMatchSelected = competitionInstance.getCompetitionMatch(openCompetitionMatchSelected.localId);
                                }
                            }
                            while (!answer.trim().isEmpty() && !openCompetitionMatchSelected.isParticipantResultsSet());
                        }
                    }
                }
            } else if (answer.trim().compareToIgnoreCase("" + userSelect) == 0) {
                NumberFormat numberFormat = NumberFormat.getInstance();
                numberFormat.setMinimumIntegerDigits(3);
                numberFormat.setGroupingUsed(false);
                SortedSet<CompetitionPlay> openCompetitionPlays = competitionInstance.getOpenCompetitionPlays();
                Map<String, Participant> participantMap = new HashMap<>();
                for (CompetitionPlay openCompetitionPlay : openCompetitionPlays) {
                    for (Participant participant : openCompetitionPlay.participantPairing.getRealParticipantsAsArray()) {
//                        Participant participant = competitionInstance.getParticipant(participantId);
                        participantMap.put(participant.internationalizedLabel.defaultLabel, participant);
                    }
                }
                SortedMap<String, Participant> participantSortedMap = new TreeMap<>();
                participantSortedMap.putAll(participantMap);
                for (Participant participant : participantSortedMap.values()) {
                    System.out.println(numberFormat.format(participant.localId) + "\t" + participant.internationalizedLabel.defaultLabel);
                }
                System.out.print("participant:");
                answer = input.nextLine();
                if (!answer.trim().isEmpty()) {
                    Participant selectedParticipant = null;
                    for (Participant participant : participantSortedMap.values()) {
                        if (participant.localId.compareToIgnoreCase(answer) == 0) {
                            selectedParticipant = participant;
                            break;
                        }
                    }

                    if (selectedParticipant != null) {
                        for (CompetitionPlay openCompetitionPlay : openCompetitionPlays)
                            if (openCompetitionPlay.isForParticipant(selectedParticipant))
                                System.out.println(openCompetitionPlay.toScreenDescription());
                    }
                }
                break;
            } else if (demo && answer.trim().compareToIgnoreCase("" + simulation) == 0) {
                boolean generation = false;
                competitionInstance.launchSimulation(generation);
                saveHistory();
            } else if (demo && answer.trim().compareToIgnoreCase("" + simulationPerWave) == 0) {
                boolean generation = false;
                competitionInstance.launchSimulationCurrentWave(generation);
                saveHistory();
            } else if (answer.trim().compareToIgnoreCase("" + ranking) == 0) {
                System.out.println("Final Ranking");
                saveHistory();
                StringBuilder resultTable = new StringBuilder();
                List<ParticipantResult> registererParticipantRankingFinal = competitionInstance.getRegistererParticipantRankingFinal();
                for (ParticipantResult participantResult : registererParticipantRankingFinal) {
                    resultTable.append(JSONUtils.jsonObjectToString(participantResult, true)).append(System.lineSeparator());
                }
                System.out.println(resultTable);
            }
        }
    }

    private static Participant getParticipant(String participantName) {
        for (Participant participant : participants) {
            if (participant.internationalizedLabel.defaultLabel.compareToIgnoreCase(participantName) == 0 || participant.localId.compareTo(participantName) == 0) {
                return participant;
            }
        }
        return null;
    }

    private static CompetitionComputationParam readCompetitionComputationParam(String propertiesFile) throws IOException {
        CompetitionComputationParam competitionComputationParam = CompetitionDetailsGeneratorTest.getDefaultCompetitionComputationParamStatic(null, null, ParticipantType.TEAM_ONE_PARTICIPANT, 2, PlayVersusType.ONE_VS_ONE, 1,
                Duration.ofMinutes(5), Duration.ofMinutes(15), Duration.ofMinutes(30), MergePolicy.STANDARD, ResetPolicy.NONE, false);

        Properties properties = new Properties();
        FileInputStream fileInputStream = new FileInputStream(propertiesFile);
        properties.load(fileInputStream);
        Enumeration<String> keys = (Enumeration<String>) properties.propertyNames();
        String key = null;
        while (keys.hasMoreElements()) {
            key = keys.nextElement();
            switch (key) {
                case "competitionDuration":
                    competitionComputationParam.competitionDuration = Duration.parse(properties.getProperty(key));
                    break;
                case "numberOfParticipantCompetition":
                    competitionComputationParam.numberOfParticipantCompetition = Integer.valueOf(properties.getProperty(key));
                    break;
                case "participantType":
                    competitionComputationParam.participantType = ParticipantType.valueOf(properties.getProperty(key));
                    break;
                case "playVersusType":
                    for (CompetitionCreationParamPhase competitionCreationParamPhase : competitionComputationParam.phases) {
                        competitionCreationParamPhase.playVersusType = PlayVersusType.valueOf(properties.getProperty(key));
                    }
                    break;
                case "numberOfParticipantMatch":
                    for (CompetitionCreationParamPhase competitionCreationParamPhase : competitionComputationParam.phases) {
                        competitionCreationParamPhase.numberOfParticipantMatch = Integer.valueOf(properties.getProperty(key));
                    }
                    break;
                case "averagePlayDuration":
                    for (CompetitionCreationParamPhase competitionCreationParamPhase : competitionComputationParam.phases) {
                        competitionCreationParamPhase.averagePlayDuration = Duration.parse(properties.getProperty(key)).durationValue;
                    }
                    break;
                case "minimumPlayDuration":
                    for (CompetitionCreationParamPhase competitionCreationParamPhase : competitionComputationParam.phases) {
                        competitionCreationParamPhase.minimumPlayDuration = Duration.parse(properties.getProperty(key)).durationValue;
                    }
                    break;
                case "maximumPlayDuration":
                    for (CompetitionCreationParamPhase competitionCreationParamPhase : competitionComputationParam.phases) {
                        competitionCreationParamPhase.maximumPlayDuration = Duration.parse(properties.getProperty(key)).durationValue;
                    }
                    break;
                case "maxGroupSize":
                    for (CompetitionCreationParamPhase competitionCreationParamPhase : competitionComputationParam.phases) {
                        if (competitionCreationParamPhase instanceof CompetitionCreationParamPhaseQualification)
                            ((CompetitionCreationParamPhaseQualification) competitionCreationParamPhase).groupSizeMaximum = Integer.valueOf(properties.getProperty(key));
                    }
                    break;
                case "maxFinalGroupSizeLimit":
                    for (CompetitionCreationParamPhase competitionCreationParamPhase : competitionComputationParam.phases) {
                        if (competitionCreationParamPhase instanceof CompetitionCreationParamPhaseFinal)
                            ((CompetitionCreationParamPhaseFinal) competitionCreationParamPhase).groupSizeMaximum = Integer.valueOf(properties.getProperty(key));
//                        else if(competitionCreationParamPhase instanceof CompetitionCreationParamPhaseQualification)
//                            ((CompetitionCreationParamPhaseQualification)competitionCreationParamPhase).groupSizeMaximum= Integer.valueOf(properties.getProperty(key));
                    }
                    break;
                case "maximumNumberOfParallelPlay":
                    for (CompetitionCreationParamPhase competitionCreationParamPhase : competitionComputationParam.phases) {
                        competitionCreationParamPhase.maximumNumberOfParallelPlay = Integer.valueOf(properties.getProperty(key));
                    }
                    break;
                case "finalPhaseElimination":

                    competitionComputationParam.removeFinalPhase();
                    break;
                case "minGroupSize":
                    for (CompetitionCreationParamPhase competitionCreationParamPhase : competitionComputationParam.phases) {
                        if (competitionCreationParamPhase instanceof CompetitionCreationParamPhaseQualification)
                            ((CompetitionCreationParamPhaseQualification) competitionCreationParamPhase).groupSizeMinimum = Integer.valueOf(properties.getProperty(key));
                    }
                    break;
//                case "numberOfPlayMultiplicator":
//                    competitionComputationParam.numberOfPlayMultiplicator = Integer.valueOf(properties.getProperty(key));
//                    break;
                case "maxNumberOfPlayGlobal":
                    for (CompetitionCreationParamPhase competitionCreationParamPhase : competitionComputationParam.phases) {
                        competitionCreationParamPhase.numberOfPlayMaximum = Integer.valueOf(properties.getProperty(key));
                    }
//                    competitionComputationParam.maxNumberOfPlayGlobal = Integer.valueOf(properties.getProperty(key));
                    break;
                case "minNumberOfPlayGlobal":
                    for (CompetitionCreationParamPhase competitionCreationParamPhase : competitionComputationParam.phases) {
                        competitionCreationParamPhase.numberOfPlayMinimum = Integer.valueOf(properties.getProperty(key));
                    }
                    break;
                case "sharerPercentageLimit":
                    competitionComputationParam.sharerPercentageLimit = Integer.valueOf(properties.getProperty(key));
                    break;
                case "competitionName":
                    competitionComputationParam.competitionName = properties.getProperty(key);
                    break;
                case "intermissionDuration":
                    for (CompetitionCreationParamPhase competitionCreationParamPhase : competitionComputationParam.phases) {
                        competitionCreationParamPhase.intermissionDuration = Duration.parse(properties.getProperty(key)).durationValue;
                    }
                    break;
                case "participantQualifiedPerMatch":
                    for (CompetitionCreationParamPhase competitionCreationParamPhase : competitionComputationParam.phases) {
                        competitionCreationParamPhase.participantQualifiedPerMatch = Integer.valueOf(properties.getProperty(key));
                    }
                    break;
                case "eliminationLevelMaximum":
                    for (CompetitionCreationParamPhase competitionCreationParamPhase : competitionComputationParam.phases) {
                        if (competitionCreationParamPhase instanceof CompetitionCreationParamPhaseFinal) {
                            competitionCreationParamPhase.tournamentFormatsAccepted.clear();
                            for (int i = 0; i < Integer.valueOf(properties.getProperty(key)); i++) {
                                competitionCreationParamPhase.tournamentFormatsAccepted.add(TournamentFormat.eliminationWithNumberOfBrackets(i + 1));
                            }
                        }
                    }


                    break;
                case "cssUrl":
                    cssUrl = key;
                    break;
            }


        }

        return competitionComputationParam;
    }

    private static CompetitionInstance readCompetitionInstance(String competitionInstanceFile) throws IOException {
        if (new File(competitionInstanceFile).canRead()) {
            XmlUtils<CompetitionInstance> xmlUtils = new XmlUtils<>(CompetitionInstance.class);
            return xmlUtils.fromFile(competitionInstanceFile);
        }
        return null;
    }

    private static File writeCompetitionInstance(CompetitionInstance competitionInstance, String competitionInstanceFile) throws IOException {
        XmlUtils<CompetitionInstance> xmlUtils = new XmlUtils<>(CompetitionInstance.class);
        return xmlUtils.toFile(competitionInstance, competitionInstanceFile);
    }

    private static List<Participant> readParticipants(CompetitionInstance competitionInstance, String participantFile) {
        List<Participant> participants = new ArrayList<>();
        String line;
        try {
            InputStream fis = new FileInputStream(participantFile);
            InputStreamReader isr = new InputStreamReader(fis, Charset.defaultCharset());
            BufferedReader br = new BufferedReader(isr);
            Participant participant = null;
            while ((line = br.readLine()) != null) {
                participant = createParticipantFromLine(competitionInstance, line);
                participants.add(participant);
            }
        } catch (Exception e) {

        }
        return participants;
    }

    private static Participant createParticipantFromLine(CompetitionInstance competitionInstance, String line) {
        Participant participant = competitionInstance.createParticipantSingle();
        participant.internationalizedLabel.defaultLabel = line;
        return participant;
    }
}

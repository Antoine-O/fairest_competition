package com.qc.competition.service.structure;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qc.competition.service.structure.tree.CompetitionPlayTree;
import com.qc.competition.service.structure.tree.ParticipantResultTree;
import com.qc.competition.service.template.*;
import com.qc.competition.utils.Sets;

import javax.xml.bind.annotation.*;
import java.time.ZoneId;
import java.util.*;


/**
 * Created by Duncan on 22/12/2014.
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompetitionPlay extends CompetitionObjectWithResult<CompetitionMatch, CompetitionPlay> implements Simplify<CompetitionPlay> {
    public static String CLASS = CompetitionPlay.class.getSimpleName();
    @XmlAttribute(name = "playType")
    public PlayType playType = PlayType.NORMAL;
    @XmlAttribute(name = "matchId")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("matchId")
    public CompetitionMatch competitionMatch;
    @XmlAttribute(name = "roundId")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("roundId")
    public CompetitionRound competitionRound;
    @XmlAttribute(name = "groupId")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("groupId")
    public CompetitionGroup competitionGroup;
    @XmlAttribute(name = "seedId")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("seedId")
    public CompetitionSeed competitionSeed;
    @XmlAttribute(name = "phaseId")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("phaseId")
    public CompetitionPhase competitionPhase;
    @XmlElement(name = "label")
    @JsonProperty("label")
    public InternationalizedLabel internationalizedLabel = new InternationalizedLabel();
    @XmlElement(name = "pairing")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("pairing")
    public ParticipantPairing participantPairing = null;
    @XmlElementWrapper(name = "results")
    @XmlElement(name = "result")
    @JsonProperty("results")
    public SortedSet<ParticipantResult> participantResults = new TreeSet<>();
    @XmlAttribute(name = "versus")
    @JsonProperty("versus")
    public PlayVersusType playVersusType;
    @XmlAttribute(name = "round")
    public int round;
    @XmlAttribute(name = "competitionId")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("competitionId")
    public CompetitionInstance competitionInstance;//    @XmlTransient

    public CompetitionPlay() {
        super();
    }

    private CompetitionPlay(IdGenerator idGenerator) {
        super(idGenerator);
    }
//    @JsonProperty("competitionMatchCache")
//    protected CompetitionMatch competitionMatchCache;

    public static CompetitionPlay createInstance(IdGenerator idGenerator) {
        return new CompetitionPlay(idGenerator);
    }

    public PlayType getPlayType() {
        return playType;
    }

    public void setPlayType(PlayType playType) {
        if (this.playType == null || (this.playType != null && playType != null && this.playType.compareTo(playType) != 0)) {
            setChanged();
            notifyObservers(this.localId);
        }
        this.playType = playType;

    }

    public ParticipantPairing getParticipantPairing() {
        return participantPairing;
    }

    public void setParticipantPairing(ParticipantPairing participantPairing) {
        this.setChanged();
        notifyObservers(this.localId);
        this.participantPairing = participantPairing;
        this.participantPairing.sortParticipantSeats();
    }

    @Override
    public void addParticipantResult(ParticipantResult participantResult) {
        this.participantResults.add(participantResult);
        Sets.sort(this.participantResults);
        checkAllParticipantResultsSet();
    }

//    public void fillParticipantResultWithFakeValue() {
//        this.setParticipantResults(createFakeParticipantResults());
//    }

    @Override
    public boolean allParticipantResultsSet() {
        boolean allParticipantResultsSet = true;
        List<Participant> participants = this.participantPairing.getRealParticipantsAsArray();
        for (Participant participant : participants) {
            allParticipantResultsSet = allParticipantResultsSet && isParticipantResultSet(participant);
            if (!allParticipantResultsSet)
                break;
        }
        return allParticipantResultsSet;
    }

    public void clear() {
        this.participantResults.clear();
        this.participantPairing = null;
        this.setCompetitionObjectStatus(CompetitionObjectStatus.NOT_INITIALIZED);
    }

    @Override
    protected Integer getRoundOrLane() {
        return this.round;
    }

    @Override
    public ParticipantScore createInitialParticipantScore() {
        return new ParticipantScorePlay(this);
    }

    private boolean isParticipantInvolved(Participant participant) {
        if (isParticipantPairingDefined()) {
            List<Participant> participantsInvolved = this.participantPairing.getRealParticipantsAsArray();
            if (participant != null && participant.localId != null)
                for (Participant participantInvolved : participantsInvolved)
                    if (participantInvolved.compareTo(participant) == 0)
                        return true;
        }
        return false;
    }

    public ParticipantResult getParticipantResultFor(Participant participant) {
        ParticipantResult participantResultForParticipant = null;
        for (ParticipantResult participantResult : participantResults) {
            if (participantResult.participant.compareTo(participant) == 0) {
                participantResultForParticipant = participantResult;
                break;
            }
        }
        return participantResultForParticipant;
    }

    public void initializePlay() {
        if (competitionObjectStatus.compareTo(CompetitionObjectStatus.NOT_INITIALIZED) == 0) {
            if (!isParticipantPairingDefined()) {
                this.playVersusType.fillParticipantPairing(this);
                this.setCompetitionObjectStatus(CompetitionObjectStatus.WAIT_FOR_START);
                this.playType = PlayType.NORMAL;
                setChanged();
                notifyObservers(this.localId);
            }
        }
    }

    @Override
    public CompetitionObjectWithResult getParentCompetitionObjectWithResult() {
        return this.getCompetitionMatch();
    }

    @Override
    public List<CompetitionObjectWithResult> getPreviousCompetitionObjectWithResults() {
        List<CompetitionObjectWithResult> competitionObjectWithResults = new ArrayList<>();

        for (CompetitionPlay competitionPlay : this.getCompetitionMatch().getCompetitionPlays()) {
            if (this.compareTo(competitionPlay) > 0)
                competitionObjectWithResults.add(competitionPlay);
        }
        return competitionObjectWithResults;
    }

    @Override
    public List<CompetitionObjectWithResult> getNextCompetitionObjectWithResults() {
        List<CompetitionObjectWithResult> competitionObjectWithResults = new ArrayList<>();

        for (CompetitionPlay competitionPlay : this.getCompetitionMatch().getCompetitionPlays()) {
            if (this.compareTo(competitionPlay) < 0)
                competitionObjectWithResults.add(competitionPlay);
        }
        return competitionObjectWithResults;
    }

    public List<CompetitionPlay> computeNextCompetitionPlays() {
        List<CompetitionPlay> competitionPlaysNext = new ArrayList<>();

        for (CompetitionPlay competitionPlay : this.getCompetitionMatch().getCompetitionPlays()) {
            if (this.compareTo(competitionPlay) < 0)
                competitionPlaysNext.add(competitionPlay);
        }
        return competitionPlaysNext;
    }

    public CompetitionPlay computeNextCompetitionPlay() {
        CompetitionPlay competitionPlayNext = null;

        for (CompetitionPlay competitionPlay : this.getCompetitionMatch().getCompetitionPlays()) {
            if (this.round + 1 == competitionPlay.round)
                competitionPlayNext = competitionPlay;
        }
        return competitionPlayNext;
    }

    public boolean isADraw(Participant participant) {
        ParticipantResult participantResult = getParticipantResultFor(participant);
        boolean isADraw = true;
        for (ParticipantResult participantResultOther : participantResults) {
            if (participantResultOther.participant.compareTo(participant) != 0) {
                if (participantResult.rank != participantResultOther.rank) {
                    isADraw = false;
                    break;
                }
            }
        }
        return isADraw;
    }

    public boolean isALoss(Participant participant) {
        ParticipantResult participantResult = getParticipantResultFor(participant);
        boolean isALoss = true;
        for (ParticipantResult participantResultOther : participantResults) {
            if (participantResultOther.participant.compareTo(participant) != 0) {
                if (participantResult.rank < participantResultOther.rank) {
                    isALoss = false;
                    break;
                }
            }
        }
        return isALoss;
    }

    public boolean isAWin(Participant participant) {
        ParticipantResult participantResult = getParticipantResultFor(participant);
        if (isSubPlayFromTeam()) {
            Participant participant1 = getCurrentParticipantFromTeam(participant);
            participantResult = getParticipantResultFor(participant1);
        } else {
            participantResult = getParticipantResultFor(participant);
        }
        boolean isAWin = true;
        for (ParticipantResult participantResultOther : participantResults) {
            if (participantResultOther.participant.compareTo(participant) != 0) {
                if (participantResultOther.rank <= participantResult.rank) {
                    isAWin = false;
                    break;
                }
            }
        }

        return isAWin;
    }

    private Participant getCurrentParticipantFromTeam(Participant participantTeam) {
        Participant participant = null;
        SortedSet<Participant> participants = participantTeam.toParticipantSet();
        for (Participant participantTeamMember : participants) {
            List<Participant> participantRealList = participantPairing.getRealParticipantsAsArray();
            for (Participant participantReal : participantRealList) {
                if (participantReal.compareTo(participantTeamMember) == 0) {
                    participant = participantReal;
                    break;
                }
            }
            if (participant != null)
                break;
        }
        return participant;
    }

    private boolean isSubPlayFromTeam() {
        return this.getCompetitionInstance().getCompetitionComputationParam().participantType.numberOfParticipants > playVersusType.teamSize;
    }

    @Override
    public boolean isParticipantPairingDefined() {
        return participantPairing != null && participantPairing.isFull();
    }

    public boolean isParticipantResultDefined() {
        return !participantResults.isEmpty();
    }

    @Override
    public boolean isParticipantResultSet(Participant participant) {
        boolean participantResultSet = false;
        for (ParticipantResult participantResult : this.participantResults) {
            if (participantResult.participant.compareTo(participant) == 0) {
                participantResultSet = participantResult.rank != null || !participantResult.isEmpty();
                break;
            }
        }
        return participantResultSet;
    }

    @Override
    public boolean isParticipantResultsSet() {
        boolean participantResultSet = false;
        if (this.participantPairing != null) {
            participantResultSet = true;
            for (Participant participant : this.participantPairing.getRealParticipantsAsArray()) {
                participantResultSet = isParticipantResultSet(participant);
                if (!participantResultSet)
                    break;
            }
        }
        return participantResultSet;
    }

    @Override
    public boolean isSubParticipantResultsSet() {
        return true;
    }

//    public void reset() {
//        this.clear();
//        this.participantResults.clear();
//        this.setCompetitionObjectStatus (CompetitionObjectStatus.NOT_INITIALIZED;
//        resetCache();
//    }

//    public String toDescription() {
//        StringBuilder description = new StringBuilder();
//        description.append(toString()).append(System.lineSeparator());
//        if (competitionMatch != null)
//            description.append("[competitionMatch]").append(System.lineSeparator()).append(getCompetitionMatch().toString()).append(System.lineSeparator());
//        description.append("[participantResults]").append(System.lineSeparator());
//        for (ParticipantResult participantResult : participantResults) {
//            description.append(participantResult.toString()).append(System.lineSeparator());
//        }
//        return description.toString();
//    }
//
//    public String toDescriptionTree(int level) {
//        String indentation = StringUtil.getIndentationForLevel(level);
//        StringBuilder description = new StringBuilder();
//        description.append(indentation).append(toString()).append(System.lineSeparator());
//        if (!participantResults.isEmpty()) {
//            description.append(indentation).append("[participantResults]").append(System.lineSeparator());
//            for (ParticipantResult participantResult : participantResults) {
//                description.append(participantResult.toDescriptionTree(level + 1));
//            }
//        }
//        return description.toString();
//    }
//
//    public Element toDescriptionXml(Document document) {
//        Element element = document.createElement(this.getClass().getSimpleName());
//        element.setAttribute("localId", "" + localId);
//        element.setAttribute("name", "" + internationalizedLabel.defaultLabel);
//        element.setAttribute("roundDetails", "" + round);
//        element.setAttribute("competitionObjectStatus", "" + competitionObjectStatus);
//
//        if (participantPairing != null) {
//            element.appendChild(participantPairing.toDescriptionXml(document));
//        }
//        Element elementParticipantResults = document.createElement(ParticipantResult.class.getSimpleName() + "s");
//        if (!this.participantResults.isEmpty()) {
//            for (ParticipantResult participantResult : participantResults) {
//                elementParticipantResults.appendChild(participantResult.toDescriptionXml(document));
//            }
//        }
//        element.appendChild(elementParticipantResults);
//
//
//        return element;
//    }
//
//    public String toDetailedDescription() {
//        StringBuilder description = new StringBuilder();
//        description.append(this.getCompetitionMatch().getCompetitionRound().getCompetitionGroup().getCompetitionSeed().toString()).append("\t").append(this.getCompetitionMatch().getCompetitionRound().getCompetitionGroup().toString()).append("\t").append(this.getCompetitionMatch().getCompetitionRound().toString()).append("\t").append(this.getCompetitionMatch().toString()).append("\t").append(toString());
//        if (!participantResults.isEmpty()) {
//            for (ParticipantResult participantResult : participantResults) {
//                description.append("\t").append(participantResult.getParticipant().toString()).append("\t").append(participantResult.toString()).append(participantResult.participantScore.toDetailedDescription());
//            }
//        }
//        return description.toString();
//    }
//
//    public String toSimpleDescription() {
//        StringBuilder description = new StringBuilder();
//        description.append(this.toString()).append("\t").append(this.getCompetitionMatch().toString()).append(System.lineSeparator());
//        if (!participantResults.isEmpty()) {
//            for (ParticipantResult participantResult : participantResults) {
//                description.append("\t").append(participantResult.toSimpleDescription());
//            }
//        }
//        return description.toString();
//    }
//
//    public Element toSimpleDescriptionXml(Document document, boolean withResult) {
//        Element element = document.createElement(this.getClass().getSimpleName());
//        element.setAttribute("localId", "" + localId);
//        element.setAttribute("roundDetails", "" + round);
//        element.setAttribute("competitionObjectStatus", "" + competitionObjectStatus);
//        element.setAttribute("name", "" + internationalizedLabel.defaultLabel);
//        if (!internationalizedLabel.internationalizedLabelMap.isEmpty())
//            element.appendChild(internationalizedLabel.toSimpleDescriptionXml(document));
//        if (participantPairing != null)
//            element.appendChild(participantPairing.toSimpleDescriptionXml(document));
//        if (withResult && !this.participantResults.isEmpty()) {
//            for (ParticipantResult participantResult : participantResults) {
//                element.appendChild(participantResult.toSimpleDescriptionXml(document));
//            }
//        } else {
//            for (ParticipantResult participantResult : participantResults) {
//                element.appendChild(participantResult.toSimpleDescriptionXml(document));
//            }
//        }
//
//
//        return element;
//    }

    @Override
    public String toString() {
        return "CompetitionPlay{" +
                "localId=" + localId +
                ", name='" + internationalizedLabel.defaultLabel + '\'' +
                ", status=" + competitionObjectStatus +
                (participantResults != null ? ", participantResults.size()=" + participantResults.size() : "") +
                ", round=" + round +
                (competitionMatch != null ? competitionMatch.toString() : "") +
                "}";
    }

    @Override
    public void updateResultDependencies() throws CompetitionInstanceGeneratorException {
        boolean closeOptionalPlay = true;
        if (this.competitionMatch != null) {
            this.getCompetitionMatch().checkCompetitionCompetitionObjectStatus(closeOptionalPlay);
            if (isSubParticipantResultsSet()) {
                this.getCompetitionMatch().updateResultDependencies();
            }
        }
    }

    public void initForXmlOutput() {
        for (ParticipantResult participantResult : participantResults) {
            participantResult.initForXmlOutput();
        }
        if (participantPairing != null)
            participantPairing.initForXmlOutput();
    }

//    @Override
//    public SortedSet<CompetitionPlay> getCompetitionPlays(boolean initialized, boolean open, boolean closed, Integer participantId, boolean recursive) {
//        SortedSet<CompetitionPlay> competitionPlays = new TreeSet<>();
//        if ((!initialized || this.isInitialized()) && (!open || this.isOpen()) && (!closed || this.isClosed()) && (participantId == null || isForParticipant(participantId)))
//            competitionPlays.add(this);
//        return competitionPlays;
//    }

    public ParticipantScore findParticipantScore(String localId) {
        ParticipantScore participantScore = null;
        for (ParticipantResult participantResult : participantResults) {
            participantScore = participantResult.findParticipantScore(localId);
            if (participantScore != null)
                break;
        }
        return participantScore;
    }

    @Override
    public String getLocalId() {
        return localId;
    }

    public void initFromXmlInputResult(CompetitionInstance competitionInstance) {
        this.setCompetitionInstance(competitionInstance);
        this.idGeneratorCache = competitionInstance.getIdGenerator();

        for (ParticipantResult participantResult : participantResults) {
            participantResult.initFromXmlInput(competitionInstance);
        }
//        List<ParticipantResult> participantResultsTmp = new ArrayList<ParticipantResult>();
//        participantResultsTmp.addAll(participantResults);
//        participantResults.clear();
//        participantResults.addAll(participantResultsTmp);
    }

    public String toScreenDescription() {

        StringBuilder description = new StringBuilder();

        description.append("Play(").append(this.localId).append(")\tMatch(").append(this.competitionMatch.localId).append(")");
        if (!participantPairing.isVoid()) {
            for (Participant participant : participantPairing.getRealParticipantsAsArray()) {
                if (participant instanceof ParticipantSingle || (participant instanceof ParticipantTeam && ((ParticipantTeam) participant).participantTeamMembers.size() > 1))
                    description.append("\t").append(participant.internationalizedLabel.defaultLabel).append("(").append(participant.localId).append(")");
                else if (participant instanceof ParticipantTeam && ((ParticipantTeam) participant).participantTeamMembers.size() > 0)
                    description.append("\t").append(((ParticipantTeam) participant).participantTeamMembers.first().getParticipant().internationalizedLabel.defaultLabel).append("(").append(((ParticipantTeam) participant).participantTeamMembers.first().localId).append(")");
                ParticipantResult participantResult = this.getParticipantResultFor(participant);
                if (participantResult != null)
                    description.append("\t").append(participantResult.rank);
//                ParticipantScore participantScore = this.findParticipantScore(participant.localId);
//                if (participantScore != null)
//                    description.append("\t").append(participantScore.toSimpleDescription());
//                description.append(System.lineSeparator());
            }
        }
        return description.toString();
    }

    public boolean isOverForParticipant(Participant participant) {
        boolean overForParticipant = true;
        if (isForParticipant(participant))
            overForParticipant = this.isClosed();
        return overForParticipant;
    }

    @Override
    public SortedSet<ParticipantPairing> getParticipantPairings() {
        SortedSet<ParticipantPairing> participantPairings = new TreeSet<>();
        if (isParticipantPairingDefined())
            participantPairings.add(participantPairing);
        return participantPairings;
    }

    public CompetitionPlayTree getCompetitionPlayTree() {
        CompetitionPlayTree competitionPlayTree = new CompetitionPlayTree();
        competitionPlayTree.internationalizedLabel = internationalizedLabel;
        competitionPlayTree.localId = getLocalId();
        competitionPlayTree.databaseId = databaseId;
        competitionPlayTree.round = round;
        competitionPlayTree.over = isParticipantResultDefined();
        competitionPlayTree.filled = isParticipantPairingDefined();
        competitionPlayTree.expectedDuration = this.expectedDuration;
        competitionPlayTree.expectedRelativeStartTime = this.expectedRelativeStartTime;
        competitionPlayTree.expectedRelativeEndTime = this.expectedRelativeEndTime;

        CompetitionCreationParamPhase competitionCreationParamPhase = this.getCompetitionMatch().getCompetitionRound().getCompetitionGroup().getCompetitionSeed().getCompetitionPhase().competitionCreationParamPhase;
        if (competitionCreationParamPhase == null)
            competitionCreationParamPhase = this.getCompetitionMatch().getCompetitionRound().getCompetitionGroup().getCompetitionSeed().competitionPhase.competitionCreationParamPhase;

        ScoringConfiguration scoringConfiguration = competitionCreationParamPhase.scoringConfiguration;
        Set<String> scoringConfigurationPlayElementNames = new HashSet<>();
        if (scoringConfiguration != null && scoringConfiguration.scoringConfigurationPlay != null && scoringConfiguration.scoringConfigurationPlay.scoringConfigurationPlayElements != null) {
            for (ScoringConfigurationPlayElement scoringConfigurationPlayElement :
                    scoringConfiguration.scoringConfigurationPlay.scoringConfigurationPlayElements) {
                if (scoringConfigurationPlayElement.userInput)
                    scoringConfigurationPlayElementNames.add(scoringConfigurationPlayElement.participantScoreType.name());
            }
        }
        if (!participantResults.isEmpty()) {

            for (ParticipantResult participantResult : participantResults) {
                ParticipantResultTree participantResultTree = participantResult.toParticipantResultTree(scoringConfigurationPlayElementNames);
                if (isSubPlayFromTeam()) {
                    Participant participantTeam = getTeamFromParticipant(participantResult.participant);
                    participantResultTree.participantTeam = participantTeam.toParticipantTree();
                    participantResultTree.teamLocalId = participantTeam.localId;
                }
                competitionPlayTree.participantResultTrees.add(participantResultTree);
            }
        } else if (isParticipantPairingDefined()) {
            ParticipantResult participantResult = null;
            for (Participant participant : participantPairing.getRealParticipantsAsArray()) {
                participantResult = ParticipantResult.createParticipantResultFor(getIdGenerator(), this);
                participantResult.setParticipant(participant);

                competitionPlayTree.participantResultTrees.add(participantResult.toParticipantResultTree(false, scoringConfigurationPlayElementNames));
            }
        } else {
            ParticipantResult participantResult = null;
            for (int i = 0; i < competitionCreationParamPhase.playVersusType.numberOfTeam; i++) {
                participantResult = ParticipantResult.createParticipantResultFor(getIdGenerator(), this);
                competitionPlayTree.participantResultTrees.add(participantResult.toParticipantResultTree(false, scoringConfigurationPlayElementNames));
            }

        }
        return competitionPlayTree;
    }

    private Participant getTeamFromParticipant(Participant participant) {
        return this.getCompetitionMatch().getParticipantTeamFromParticipant(participant);
    }

    public CompetitionMatch getCompetitionMatch() {
//        if (competitionMatchCache == null && competitionMatchId != null) {
//            competitionMatchCache = this.getCompetitionInstance().getCompetitionMatch(competitionMatchId);
//        }
        return competitionMatch;
    }

    @Override
    public SortedSet<Participant> getAllOpponentsFor(Participant participant) {
        SortedSet<Participant> participants = new TreeSet<>();
        if (isForParticipant(participant)) {
            participants.addAll(participantPairing.getRealParticipantsAsArray());
            for (Participant participantsElt : participants) {
                if (participantsElt.compareTo(participant) == 0) {
                    participants.remove(participantsElt);
                    break;
                }
            }
        }
        return participants;
    }

    @Override
    public SortedSet<CompetitionPlay> getSubCompetitionObjectWithResults() {
        SortedSet<CompetitionPlay> competitionPlays = new TreeSet<>();
        return competitionPlays;
    }

    @Override
    public CompetitionMatch getUpperCompetitionObjectWithResult() {
        return getCompetitionMatch();
    }

    @Override
    public void close() throws CompetitionInstanceGeneratorException {
        if (this.competitionObjectStatus.compareTo(CompetitionObjectStatus.IN_PROGRESS_CLOSING_ALLOWED) == 0) {
            forceClose();
        }
    }

    public void forceClose() throws CompetitionInstanceGeneratorException {
        super.close();
        setChanged();
        notifyObservers(this.localId);
        CompetitionPlay competitionPlay = this.computeNextCompetitionPlay();
        if (competitionPlay != null) {
            if (!competitionPlay.isOpen()) {
                competitionPlay.initializePlay();
                competitionPlay.open();
            }
        }
        this.updateResultDependencies();
    }

    @Override
    public void open() {
        if (this.isInitialized()) {
            List<Participant> participants = this.participantPairing.getRealParticipantsAsArray();
            if (this.isPreviousOverForParticipants(participants)) {
                this.openDate = GregorianCalendar.from(java.time.ZonedDateTime.now(ZoneId.of("UTC")));
                this.setCompetitionObjectStatus(CompetitionObjectStatus.IN_PROGRESS);
                this.setPlayType(PlayType.NORMAL);
                setChanged();
                notifyObservers(this.localId);
            }
        }
    }

    public void checkMergePolicy() throws CompetitionInstanceGeneratorException {

        CompetitionCreationParamPhase competitionCreationParamPhase = competitionSeed.competitionPhase.competitionCreationParamPhase;
        if (competitionCreationParamPhase == null)
            competitionCreationParamPhase = competitionSeed.competitionPhase.competitionCreationParamPhase;

        if (this.round == 1 && competitionSeed.stepType.compareTo(StepType.MERGE) == 0 && this.getCompetitionMatch().competitionPlays.size() > 1 && competitionSeed.competitionGroupResultsPrevious != null && competitionCreationParamPhase instanceof CompetitionCreationParamPhaseFinal) {
            CompetitionCreationParamPhaseFinal competitionCreationParamPhaseFinal = (CompetitionCreationParamPhaseFinal) competitionCreationParamPhase;
            if (competitionCreationParamPhaseFinal.mergePolicy.compareTo(MergePolicy.STANDARD_WITH_WINNER_ADVANTAGE) == 0) {
                List<Participant> participants = new ArrayList<>();
                for (CompetitionGroupResult competitionGroupResult : competitionSeed.competitionGroupResultsPrevious) {
                    int i = 0;
                    for (ParticipantResult participantResult : competitionGroupResult.participantResults) {
                        participants.add(participantResult.participant);
                        i++;
                        if (i >= competitionCreationParamPhase.participantQualifiedPerMatch) {
                            break;
                        }
                    }
                }
                boolean merge = true;
                for (Participant participant : participants) {
                    if (!isParticipantInvolved(participant)) {
                        merge = false;
                        break;
                    }
                }
                if (merge) {
                    SortedSet<CompetitionGroup> competitionGroups = new TreeSet<>();
                    for (CompetitionGroupResult competitionGroupResult : competitionSeed.competitionGroupResultsPrevious) {
                        competitionGroups.add(competitionGroupResult.competitionGroup);
                    }
//                    competitionGroups = Sets.sort(competitionGroups);
                    SortedSet<ParticipantResult> participantResults = new TreeSet<>();
                    for (CompetitionGroup competitionGroup : competitionGroups) {
                        int startRank = participantResults.size();
                        int i = 0;
                        for (ParticipantResult participantResult : competitionGroup.competitionGroupResult.participantResults) {
                            ParticipantResult participantResultPlay = ParticipantResult.createParticipantResultFor(getIdGenerator(), this);
                            participantResultPlay.rank = startRank + participantResult.rank;
                            participantResultPlay.participant = participantResult.participant;
                            participantResults.add(participantResult);
                            i++;
                            if (i >= competitionCreationParamPhase.participantQualifiedPerMatch) {
                                break;
                            }
                        }

                    }
                    this.setParticipantResults(participantResults);
                    if (this.competitionObjectStatus.compareTo(CompetitionObjectStatus.IN_PROGRESS_CLOSING_ALLOWED) == 0) {
                        this.close();
                    }
                }
            }

        }
    }


    public void openForced() {
        if (this.isInitialized()) {
            this.setCompetitionObjectStatus(CompetitionObjectStatus.IN_PROGRESS);
            this.setPlayType(PlayType.NORMAL);
            setChanged();
            notifyObservers(this.localId);
        }
    }


    public void createParticipantsByeResults() {

        if (this.round <= this.getCompetitionMatch().getCompetitionPlays().size() / 2 + 1) {
            for (Participant participant : participantPairing.getRealParticipantsAsArray()) {
                ParticipantResult participantResultForPlay = ParticipantResult.createParticipantResultFor(getIdGenerator(), this);
                participantResultForPlay.setParticipant(participant);
                participantResultForPlay.rank = 1;
                participantResults.add(participantResultForPlay);
            }
            Sets.sort(this.participantResults);
            ParticipantScore.fillRank(this.participantResults, ParticipantScorePlay.RANK);
            fillParticipantResultsPoints();
        }
    }

    @Override
    public void addParticipantPairing(ParticipantPairing participantPairing) {
        setParticipantPairing(participantPairing);
    }

    public void clearDatabaseId() {
        this.databaseId = null;
        if (participantResults != null)
            for (ParticipantResult participantResult : participantResults) {
                participantResult.clearDatabaseId();
            }
        if (participantPairing != null)
            participantPairing.clearDatabaseId();


    }

    @Override
    public CompetitionPlay cloneSimplified() {
        CompetitionPlay competitionPlay = null;
        try {
            competitionPlay = (CompetitionPlay) this.clone();
            competitionPlay.participantResults = new TreeSet<>();
            if (this.participantResults != null) {
                List<ParticipantResult> participantResults = new ArrayList<>();
                for (ParticipantResult participantResult : this.participantResults) {
                    participantResults.add(participantResult.cloneSimplified());
                }
                competitionPlay.participantResults = new TreeSet<>();
                competitionPlay.participantResults.addAll(participantResults);
            }
//            competitionPlay.fillPairingCache();
//            competitionPlay.fillResultCache();

        } catch (CloneNotSupportedException e) {
        }
        return competitionPlay;
    }

    public CompetitionInstance getCompetitionInstance() {
        return this.competitionInstance;
    }

    public void setCompetitionInstance(CompetitionInstance competitionInstance) {
        this.competitionInstance = competitionInstance;
    }

    @Override
    public StatisticsStructure getExpectedGlobalStep() {
        if (!expectedGlobalStepStatisticsStructureInitialized) {
            expectedGlobalStepStatisticsStructureInitialized = true;
            expectedGlobalStepStatisticsStructure.min = (long) (this.round > this.getCompetitionMatch().competitionPlays.size() / 2 + 1 ? 0 : 1);
            expectedGlobalStepStatisticsStructure.max = (long) 1;
            expectedGlobalStepStatisticsStructure.avg = expectedGlobalStepStatisticsStructure.min;
        }
        return expectedGlobalStepStatisticsStructure;
    }

    @Override
    public StatisticsStructure getExpectedGlobalDuration() {
        if (!expectedGlobalDurationStatisticsStructureInitialized) {
            expectedGlobalDurationStatisticsStructureInitialized = true;

            CompetitionCreationParamPhase competitionCreationParamPhase = this.getCompetitionMatch().getCompetitionRound().getCompetitionGroup().getCompetitionSeed().getCompetitionPhase().competitionCreationParamPhase;
            if (competitionCreationParamPhase == null)
                competitionCreationParamPhase = this.getCompetitionMatch().getCompetitionRound().getCompetitionGroup().getCompetitionSeed().competitionPhase.competitionCreationParamPhase;

            expectedGlobalDurationStatisticsStructure.min = competitionCreationParamPhase.getMinimumPlayDuration().toMinutes();
            expectedGlobalDurationStatisticsStructure.max = competitionCreationParamPhase.getMaximumPlayDuration().toMinutes();
            expectedGlobalDurationStatisticsStructure.avg = competitionCreationParamPhase.getAveragePlayDuration().toMinutes();
        }
        return expectedGlobalDurationStatisticsStructure;
    }

    @Override
    public StatisticsStructure getExpectedGlobalPlay() {
        if (!expectedGlobalPlayStatisticsStructureInitialized) {
            expectedGlobalPlayStatisticsStructureInitialized = true;
            expectedGlobalPlayStatisticsStructure.min = (long) 1;
            expectedGlobalPlayStatisticsStructure.max = (long) 1;
            expectedGlobalPlayStatisticsStructure.avg = (long) 1;
        }
        return expectedGlobalPlayStatisticsStructure;
    }

    @Override
    public StatisticsStructure getExpectedParticipantDuration() {
        if (!expectedParticipantDurationStatisticsStructureInitialized) {
            expectedParticipantDurationStatisticsStructureInitialized = true;

            CompetitionCreationParamPhase competitionCreationParamPhase = this.getCompetitionMatch().getCompetitionRound().getCompetitionGroup().getCompetitionSeed().getCompetitionPhase().competitionCreationParamPhase;
            if (competitionCreationParamPhase == null)
                competitionCreationParamPhase = this.getCompetitionMatch().getCompetitionRound().getCompetitionGroup().getCompetitionSeed().competitionPhase.competitionCreationParamPhase;

            expectedParticipantDurationStatisticsStructure.min = competitionCreationParamPhase.getMinimumPlayDuration().multipliedBy(getExpectedGlobalPlay().min).toMinutes();
            expectedParticipantDurationStatisticsStructure.max = competitionCreationParamPhase.getMaximumPlayDuration().multipliedBy(getExpectedGlobalPlay().max).toMinutes();
            expectedParticipantDurationStatisticsStructure.avg = competitionCreationParamPhase.getAveragePlayDuration().multipliedBy(getExpectedGlobalPlay().avg).toMinutes();
        }
        return expectedParticipantDurationStatisticsStructure;
    }

    @Override
    public StatisticsStructure getExpectedParticipantPlay() {
        if (!expectedParticipantPlayStatisticsStructureInitialized) {
            expectedParticipantPlayStatisticsStructureInitialized = true;
            if (isParticipantInvolved(this.getCompetitionMatch().participantPairing.getRealParticipantsAsArray().get(0)))
                expectedParticipantPlayStatisticsStructure.min = (long) 1;
            if (isParticipantInvolved(this.getCompetitionMatch().participantPairing.getRealParticipantsAsArray().get(0)))
                expectedParticipantPlayStatisticsStructure.max = (long) 1;
            if (isParticipantInvolved(this.getCompetitionMatch().participantPairing.getRealParticipantsAsArray().get(0)))
                expectedParticipantPlayStatisticsStructure.avg = (long) 1;
            if (isParticipantInvolved(this.getCompetitionMatch().participantPairing.getRealParticipantsAsArray().get(0)))
                expectedParticipantPlayStatisticsStructure.count = (long) 1;
            if (isParticipantInvolved(this.getCompetitionMatch().participantPairing.getRealParticipantsAsArray().get(0)))
                expectedParticipantPlayStatisticsStructure.sum = (long) 1;
        }
        return expectedParticipantPlayStatisticsStructure;
    }

    @Override
    public SortedSet<ParticipantResult> getParticipantResults() {
        return this.participantResults;
    }

    @Override
    public void setParticipantResults(SortedSet<ParticipantResult> participantResults) {
        super.setParticipantResults(participantResults);
        checkAllParticipantResultsSet();
    }

    private void checkAllParticipantResultsSet() {
        if (allParticipantResultsSet()) {
            ParticipantScore.fillRank(this.participantResults, ParticipantScorePlay.RANK);
            fillParticipantResultsPoints();
            this.setCompetitionObjectStatus(CompetitionObjectStatus.IN_PROGRESS_CLOSING_ALLOWED);
        }
    }

    private void fillParticipantResultsPoints() {
        Map<String, String> participantScoreOpponentsKeys = new HashMap<>();
        participantScoreOpponentsKeys.put(ParticipantScorePlay.SCORE_POINTS_OPPONENTS, ParticipantScorePlay.SCORE_POINTS);
        participantScoreOpponentsKeys.put(ParticipantScorePlay.SCORE_GOAL_OPPONENTS, ParticipantScorePlay.SCORE_GOAL);
        for (ParticipantResult participantResult : participantResults) {
//            int points = PointsCalculation.getPoints(getCompetitionMatch().matchType.compareTo(MatchType.BYE) == 0, participantResult.rank, participantResults.size(), playVersusType.numberOfTeam, round, this.getCompetitionMatch().getCompetitionPlays().size());

            for (String participantScoreOpponentsKey : participantScoreOpponentsKeys.keySet()) {
                if (participantResult.participantScore.getParticipantScoreValue(participantScoreOpponentsKey) != null) {
                    String participantScoreOpponentKey = participantScoreOpponentsKeys.get(participantScoreOpponentsKey);
                    Number scoreValueOpponents = 0;
                    for (ParticipantResult participantResultOpponent : participantResults) {
                        if (participantResultOpponent.participant.compareTo(participantResult.participant) != 0) {
                            if (participantResultOpponent.participantScore.getParticipantScoreValue(participantScoreOpponentKey) != null) {
                                scoreValueOpponents = scoreValueOpponents.intValue() + participantResultOpponent.participantScore.getParticipantScoreValue(participantScoreOpponentKey).computeNumberValue().intValue();
                            }
                        }
                    }
                    participantResult.participantScore.setParticipantScoreValue(participantScoreOpponentsKey, scoreValueOpponents);
                }
            }
        }
        participantResults = Sets.sort(participantResults);
        ParticipantScore.fillRank(participantResults, ParticipantScorePlay.RANK);
        for (ParticipantResult participantResult : participantResults) {
            if (participantResult.participantScore.getParticipantScoreValuePoints().value == null) {
                int points = PointsCalculation.getPlayPoints(participantResult.rank, participantResults);
                participantResult.participantScore.setParticipantScoreValue(ParticipantScorePlay.SCORE_POINTS, points);
            }
        }
    }

    @Override
    public void resetCache() {
        super.resetCache();
//        this.competitionMatchCache = null;
//        if (this.participantPairing != null)
//            this.participantPairing.resetCache();
//        if (this.participantResults != null) {
//            for (ParticipantResult participantResult : this.participantResults) {
//                participantResult.resetCache();
//            }
//        }
    }

    @Override
    public void reset(boolean recursive) {
        super.reset(recursive);
        this.participantPairing = null;
        resetCache();
        if (this.competitionGroup == null || this.competitionGroup.competitionGroupFormat == null || this.competitionGroup.competitionGroupFormat.compareTo(CompetitionGroupFormat.LADDER) == 0) {
            for (CompetitionPlay competitionPlay : competitionInstance.competitionPlays) {
                if (competitionPlay.compareTo(this) == 0) {
                    competitionInstance.competitionPlays.remove(competitionPlay);
                    break;
                }
            }

            if (this.competitionMatch != null)
                this.competitionMatch.competitionPlays.remove(this);
            this.competitionInstance = null;
            this.competitionPhase = null;
            this.competitionSeed = null;
            this.competitionGroup = null;
            this.competitionRound = null;
            this.competitionMatch = null;
        }
    }

//    @Override
//    public void fillCache(boolean up, boolean down) {
//        super.fillCache(up, down);
//    }

    @Override
    String getParentCompetitionObjectWithResultId() {
        return competitionMatch != null ? competitionMatch.localId : null;
    }

    public boolean isABye(Participant participant) {
        boolean bye = false;
        if (competitionPhase.competitionCreationParamPhase.playVersusType.numberOfTeam > 1) {
            bye = isParticipantInvolved(participant) && (this.playType.compareTo(PlayType.BYE) == 0 || this.playType.compareTo(PlayType.OPTIONAL) == 0 || this.playType.compareTo(PlayType.FORFEIT) == 0);
        }
        return bye;
    }

    @Override
    public void spreadCompetitionInstance(CompetitionInstance competitionInstance) {
        if (this.participantPairing != null)
            this.participantPairing.setCompetitionInstance(competitionInstance);
        super.spreadCompetitionInstance(competitionInstance);
    }

    @Override
    protected void sortParticipantPairings() {
        if (this.participantPairing != null) {
            this.participantPairing.sortParticipantSeats();
        }
    }

    @Override
    public CompetitionObjectWithResult cloneForContext() {
        CompetitionPlay competitionPlay = new CompetitionPlay();
        competitionPlay.id = this.id;
        competitionPlay.databaseId = this.databaseId;
        competitionPlay.localId = this.localId;
        return competitionPlay;
    }

    @Override
    public void clearForContext() {
        participantResults = null;
        participantPairing = null;
        String competitionMatchId = competitionMatch.id;
        competitionMatch = new CompetitionMatch();
        competitionMatch.id = competitionMatchId;
        String competitionRoundId = competitionRound.id;
        competitionRound = new CompetitionRound();
        competitionRound.id = competitionRoundId;
        String competitionGroupId = competitionGroup.id;
        competitionGroup = new CompetitionGroup();
        competitionGroup.id = competitionGroupId;
        String competitionSeedId = competitionSeed.id;
        competitionSeed = new CompetitionSeed();
        competitionSeed.id = competitionSeedId;
        String competitionPhaseId = competitionPhase.id;
        competitionPhase = new CompetitionPhase();
        competitionPhase.id = competitionPhaseId;

        String competitionInstanceId = competitionInstance.id;
        Version version = competitionInstance.version;
        competitionInstance = new CompetitionInstance();
        competitionInstance.id = competitionInstanceId;
        competitionInstance.version = version;

    }

    @Override
    public CompetitionObjectWithResult cloneForUpdateEvent() throws CloneNotSupportedException {
        CompetitionPlay competitionPlay = (CompetitionPlay) this.clone();
        String competitionMatchId = competitionMatch.id;
        competitionPlay.competitionMatch = new CompetitionMatch();
        competitionPlay.competitionMatch.id = competitionMatchId;
        String competitionRoundId = competitionRound.id;
        competitionPlay.competitionRound = new CompetitionRound();
        competitionPlay.competitionRound.id = competitionRoundId;
        String competitionGroupId = competitionGroup.id;
        competitionPlay.competitionGroup = new CompetitionGroup();
        competitionPlay.competitionGroup.id = competitionGroupId;
        String competitionSeedId = competitionSeed.id;
        competitionPlay.competitionSeed = new CompetitionSeed();
        competitionPlay.competitionSeed.id = competitionSeedId;
        String competitionPhaseId = competitionPhase.id;
        competitionPlay.competitionPhase = new CompetitionPhase();
        competitionPlay.competitionPhase.id = competitionPhaseId;
        String competitionInstanceId = competitionInstance.id;
        Version version = competitionInstance.version;
        competitionPlay.competitionInstance = new CompetitionInstance();
        competitionPlay.competitionInstance.id = competitionInstanceId;
        competitionPlay.competitionInstance.version = version;

        if (getParticipantPairings() != null && getParticipantPairings().isEmpty()) {
            competitionPlay.participantPairing = null;
        }

        return competitionPlay;

    }


    public Map<Participant, List<Participant>> getParticipantOpponentsMap() {
        Map<Participant, List<Participant>> participantListMap = new HashMap<>();
        if (this.participantPairing != null) {
            for (Participant participant : this.participantPairing.getRealParticipantsAsArray()) {
                participantListMap.put(participant, new ArrayList<>(this.participantPairing.getRealParticipantsAsArray()));
                participantListMap.get(participant).remove(participant);

            }
        }
        return participantListMap;
    }

    @Override
    public void fillExpectedRelativeTime() {
        expectedRelativeStartTime = null;
        if (!competitionMatch.isBye()) {
            expectedDuration = getCompetitionMatch().getCompetitionRound().getCompetitionGroup().getCompetitionSeed().getCompetitionPhase().competitionCreationParamPhase.getAveragePlayDuration();
            List<CompetitionPlay> competitionPlays = this.getPreviousCompetitionPlaysForParticipants(this.getParticipants());
            int computeStepSize = computeStepSize(competitionPlays);
            if (computeStepSize == 0)
                expectedRelativeStartTime = this.competitionMatch.expectedRelativeStartTime;
            else
                expectedRelativeStartTime = this.competitionMatch.expectedRelativeStartTime.plus(expectedDuration.multipliedBy(computeStepSize));

            expectedRelativeEndTime = expectedRelativeStartTime.plus(expectedDuration);
        } else {
            expectedRelativeStartTime = this.competitionMatch.expectedRelativeStartTime;
            expectedRelativeEndTime = this.competitionMatch.expectedRelativeEndTime;
        }
    }

    private int computeStepSize(List<CompetitionPlay> competitionPlays) {
        int stepSize = 0;
        for (Participant participant : getParticipants()) {
            int stepSizeForParticipant = 0;
            for (CompetitionPlay competitionPlay : competitionPlays) {
                if (competitionPlay.round < this.round && competitionPlay.getParticipants().contains(participant)) {
                    stepSizeForParticipant++;
                }
            }
            if (stepSizeForParticipant > stepSize)
                stepSize = stepSizeForParticipant;
        }

        return stepSize;
    }

    private List<CompetitionPlay> getPreviousCompetitionPlaysForParticipants(SortedSet<Participant> participants) {
        List<CompetitionPlay> competitionPlaysForParticipants = this.getCompetitionMatch().getCompetitionPlayForParticipants(participants);
        return competitionPlaysForParticipants;
    }

    @Override
    public void delete() {
        super.delete();
        this.competitionInstance.competitionPlays.remove(this);
        this.competitionMatch.competitionPlays.remove(this);

    }

    public SortedSet<CompetitionPlay> getOpenCompetitionPlays() {
        SortedSet<CompetitionPlay> competitionPlaysFound = new TreeSet<>();
        if (this.isOpen())
            competitionPlaysFound.add(this);
        return competitionPlaysFound;
    }

    @Override
    protected Iterable<? extends CompetitionPlay> getCompetitionPlays() {
        List<CompetitionPlay> competitionPlays = new ArrayList<>();
        competitionPlays.add(this);
        return competitionPlays;
    }


}

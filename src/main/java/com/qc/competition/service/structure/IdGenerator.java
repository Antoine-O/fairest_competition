package com.qc.competition.service.structure;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.*;
import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Duncan on 04/01/2015.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IdGenerator {
    private static final NumberFormat numberFormat;
    @XmlTransient
    @JsonIgnore
    private static Map<String, String> ALIAS_KEY = new ConcurrentHashMap<>();
    @XmlTransient
    @JsonIgnore
    private static String GLOBAL_COUNTER_KEY = "Global";
    @XmlTransient
    @JsonIgnore
    private static String GLOBAL_BIB_NUMBER = "Bib Number";

    static {
        numberFormat = NumberFormat.getInstance();
        numberFormat.setMinimumIntegerDigits(4);
        numberFormat.setGroupingUsed(false);
    }

    static {
        ALIAS_KEY.put(ParticipantTeam.class.getSimpleName(), Participant.class.getSimpleName());
        ALIAS_KEY.put(ParticipantTeamVoid.class.getSimpleName(), Participant.class.getSimpleName());
        ALIAS_KEY.put(ParticipantSingle.class.getSimpleName(), Participant.class.getSimpleName());
    }


    @XmlElementWrapper(name = "idGeneratorElements")
    @XmlElement(name = "idGeneratorElement")
    @JsonProperty("idGeneratorElements")
    private List<IdGeneratorElement> idGeneratorElements = new ArrayList<>();

    synchronized public void clear() {
        idGeneratorElements.clear();
    }

    synchronized public void clearParticipantCounter() {
        reset(Participant.class.getSimpleName());
//        reset(ParticipantTeam.class.getSimpleName());
        reset(ParticipantTeamMember.class.getSimpleName());
//        reset(ParticipantSingle.class.getSimpleName());
        reset(ParticipantResult.class.getSimpleName());
        reset(ParticipantScore.class.getSimpleName());
        reset(ParticipantScoreCompetition.class.getSimpleName());
        reset(ParticipantScoreGroup.class.getSimpleName());
        reset(ParticipantScorePlay.class.getSimpleName());
        reset(ParticipantScoreRound.class.getSimpleName());
        reset(ParticipantScoreSeed.class.getSimpleName());
        reset(ParticipantScorePhase.class.getSimpleName());
        reset(ParticipantScoreMatch.class.getSimpleName());
        reset(ParticipantPairing.class.getSimpleName());
    }

    synchronized public String getId() {
        String result = null;
        synchronized (this) {
            result = getId(GLOBAL_COUNTER_KEY);
        }
        return result;
    }

    synchronized public String getBibNumber() {
        return getId(GLOBAL_BIB_NUMBER);
    }

    synchronized public String getId(String key) {
        String result = null;
        synchronized (this) {
            if (ALIAS_KEY.containsKey(key))
                key = ALIAS_KEY.get(key);
            IdGeneratorElement idGeneratorElementFound = null;
            for (IdGeneratorElement idGeneratorElement : idGeneratorElements) {
                if (idGeneratorElement.key.compareTo(key) == 0) {
                    idGeneratorElementFound = idGeneratorElement;
                    break;
                }
            }
            if (idGeneratorElementFound == null) {
                idGeneratorElementFound = new IdGeneratorElement(key);
                idGeneratorElements.add(idGeneratorElementFound);

            }
            Integer id = idGeneratorElementFound.getId();
            result = numberFormat.format(id);
        }
        return result;
    }

    synchronized public void reset(String key) {
        reset(key, 0);
    }

    synchronized public void reset(String key, Integer value) {
        if (ALIAS_KEY.containsKey(key))
            key = ALIAS_KEY.get(key);
        for (IdGeneratorElement idGeneratorElement : idGeneratorElements) {
            if (idGeneratorElement.key.compareTo(key) == 0) {
                idGeneratorElement.reset(value);
                break;
            }
        }
    }

    public void resetBibNumber() {
        reset(IdGenerator.GLOBAL_BIB_NUMBER);
    }

    public void compact(String key, List<CompetitionObjectWithResult> competitionObjectWithResults) {
        Collections.sort(competitionObjectWithResults, new Comparator<CompetitionObjectWithResult>() {
            @Override
            public int compare(CompetitionObjectWithResult o1, CompetitionObjectWithResult o2) {
                return o1.localId.compareTo(o2.localId);
            }
        });
        reset(key);
        for (int i = 0; i < competitionObjectWithResults.size(); i++) {
            competitionObjectWithResults.get(i).localId = getId(key);
        }
    }
}
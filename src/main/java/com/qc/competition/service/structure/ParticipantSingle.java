package com.qc.competition.service.structure;

import javax.xml.bind.annotation.XmlType;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by Duncan on 09/01/2015.
 */
@XmlType(name = "single")
public class ParticipantSingle extends Participant {
    //   public static String CLASS = ParticipantSingle.class.getSimpleName();

    public ParticipantSingle() {
        super();
    }

//    public ParticipantSingle(Integer localId, String name, Double equalityComparison) {
//        super(localId, name, equalityComparison);
//    }
//
//
//    public ParticipantSingle(Integer localId, InternationalizedLabel internationalizedLabel, Double equalityComparison) {
//        super(localId, internationalizedLabel, equalityComparison);
//    }

    private ParticipantSingle(IdGenerator idGenerator) {
        super(idGenerator);
    }

    public static ParticipantSingle createInstance(IdGenerator idGenerator) {
        return new ParticipantSingle(idGenerator);
    }

    @Override
    public boolean isVoid() {
        return false;
    }

    @Override
    public String toSimpleDescription() {
        return super.toString();
    }

    @Override
    public SortedSet<Participant> toParticipantSet() {
        SortedSet<Participant> participants = new TreeSet<>();
        participants.add(this);
        return participants;
    }

    @Override
    public void fillCache(boolean up, boolean down) {
    }

    @Override
    public void resetCache() {
    }

    @Override
    public List<Participant> getParticipantsAsArrayForContext(List<Participant> participantArrayList) {
        boolean alreadyIn = false;
        for (Participant participant : participantArrayList) {
            if (this.compareTo(participant) == 0) {
                alreadyIn = true;
                break;
            }
        }
        if (!alreadyIn)
            participantArrayList.add(this.cloneForContext());
        return participantArrayList;
    }
}

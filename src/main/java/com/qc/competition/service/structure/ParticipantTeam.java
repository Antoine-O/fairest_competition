package com.qc.competition.service.structure;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by Duncan on 09/01/2015.
 */
@XmlType(name = "team")
public class ParticipantTeam extends Participant {
    //public static String CLASS = ParticipantTeam.class.getSimpleName();

    @XmlElementWrapper(name = "members")
    @XmlElement(name = "member")
    @JsonProperty("members")
    public SortedSet<ParticipantTeamMember> participantTeamMembers = new TreeSet<>();

    public ParticipantTeam() {
        super();
    }

//    public ParticipantTeam(Integer localId, String name, Double equalityComparison) {
//        super(localId, name, equalityComparison);
//    }
//
//    public ParticipantTeam(Integer localId, InternationalizedLabel internationalizedLabel, Double equalityComparison) {
//        super(localId, internationalizedLabel, equalityComparison);
//    }

    protected ParticipantTeam(IdGenerator idGenerator) {
        super(idGenerator);
    }

    public static ParticipantTeam createInstance(IdGenerator idGenerator) {
        return new ParticipantTeam(idGenerator);
    }

//    public ParticipantTeam clone() {
//        ParticipantTeam participantTeam = (ParticipantTeam) super.clone();
//        if (participantTeamMembers != null)
//            for (ParticipantTeamMember participantTeamMember : this.participantTeamMembers) {
//                if (participantTeamMember.participant != null) {
//                    ParticipantTeamMember participantTeamMemberNew = participantTeamMember.clone();
//                    participantTeam.participantTeamMembers.add(participantTeamMemberNew);
//                }
//            }
//        return participantTeam;
//    }


    @Override
    public boolean isVoid() {
        return false;
    }

    public String toDescription() {
        StringBuilder description = new StringBuilder();
        description.append(super.toDescription());
        if (participantTeamMembers != null && !participantTeamMembers.isEmpty()) {
            description.append("[participantTeamMember]").append(System.lineSeparator());
            for (ParticipantTeamMember participantTeamMember : participantTeamMembers) {
                description.append(participantTeamMember.toDescription());
            }

            description.append(System.lineSeparator());
        }
        return description.toString();
    }

    public Element toDescriptionXml(Document document) {
        Element element = document.createElement(this.getClass().getSimpleName());
        element.setAttribute("localId", "" + localId);
        if (internationalizedLabel != null) {
            if (internationalizedLabel.defaultLabel != null)
                element.setAttribute("name", internationalizedLabel.defaultLabel);
            if (!internationalizedLabel.internationalizedLabelMap.isEmpty()) {
                element.appendChild(internationalizedLabel.toDescriptionXml(document));
            }
        }
        if (participantTeamMembers != null && !participantTeamMembers.isEmpty()) {
            Element elementParticipantTeamMembers = document.createElement(ParticipantTeamMember.class.getSimpleName() + "s");
            for (ParticipantTeamMember participantTeamMember : participantTeamMembers) {
                elementParticipantTeamMembers.appendChild(participantTeamMember.toDescriptionXml(document));
            }
            element.appendChild(elementParticipantTeamMembers);
        }
        return element;
    }

    @Override
    public String toSimpleDescription() {
        StringBuilder description = new StringBuilder();
        description.append(super.toDescription());
        if (participantTeamMembers != null && !participantTeamMembers.isEmpty()) {
            for (ParticipantTeamMember participantTeamMember : participantTeamMembers) {
                description.append(participantTeamMember.toSimpleDescription());
            }
        }
        return description.toString();
    }

    public Element toSimpleDescriptionXml(Document document) {
        Element element = document.createElement(this.getClass().getSimpleName());
        element.setAttribute("localId", "" + localId);
        element.setAttribute("name", internationalizedLabel.defaultLabel);
        if (!internationalizedLabel.internationalizedLabelMap.isEmpty()) {
            element.appendChild(internationalizedLabel.toDescriptionXml(document));
        }
        if (!participantTeamMembers.isEmpty()) {
            Element elementParticipantTeamMembers = document.createElement(ParticipantTeamMember.class.getSimpleName() + "s");
            if (this.participantTeamMembers != null)

                for (ParticipantTeamMember participantTeamMember : participantTeamMembers) {
                    if (participantTeamMember.participant != null)
                        elementParticipantTeamMembers.appendChild(participantTeamMember.getParticipant().toSimpleDescriptionXml(document));
                }
            element.appendChild(elementParticipantTeamMembers);
        }
        return element;
    }

    public void initForXmlOutput(boolean initialOutput) {
        super.initForXmlOutput(initialOutput);
        if (this.participantTeamMembers != null)

            for (ParticipantTeamMember participantTeamMember : participantTeamMembers)
                participantTeamMember.initForXmlOutput(initialOutput);
    }

    public void initFromXmlInput(CompetitionInstance competitionInstance) {
        super.initFromXmlInput(competitionInstance);
        if (this.participantTeamMembers != null)

            for (ParticipantTeamMember participantTeamMember : participantTeamMembers)
                participantTeamMember.initFromXmlInput(competitionInstance);
    }

    public Participant getParticipant(String localId) {
        Participant participant = super.getParticipant(localId);
        if (participant == null) {
            if (this.participantTeamMembers != null)

                for (ParticipantTeamMember participantTeamMember : participantTeamMembers) {
                    participant = participantTeamMember.getParticipant(localId);
                    if (participant != null)
                        break;
                }
        }
        return participant;
    }

    @Override
    public SortedSet<Participant> toParticipantSet() {
        SortedSet<Participant> participants = new TreeSet<>();
        participants.add(this);
        if (this.participantTeamMembers != null)

            for (ParticipantTeamMember participantTeamMember : participantTeamMembers) {
                participants.addAll(participantTeamMember.getParticipant().toParticipantSet());
            }
        return participants;
    }

    @Override
    public List<Participant> getAllRealParticipantsAsArray() {
        List<Participant> participantArrayList = super.getAllRealParticipantsAsArray();
        if (this.participantTeamMembers != null)
            for (ParticipantTeamMember participantTeamMember : participantTeamMembers) {
                if (participantTeamMember.participant != null) {
                    participantArrayList.addAll(participantTeamMember.getAllRealParticipantsAsArray());
                }
            }
        return participantArrayList;
    }

    public void addParticipant(Participant participant) {
        if (this.participantTeamMembers != null)
            for (ParticipantTeamMember participantTeamMember : this.participantTeamMembers) {
                if (participantTeamMember.participant.compareTo(participant) == 0) {
                    this.participantTeamMembers.remove(participantTeamMember);
                    break;
                }
            }
        ParticipantTeamMember participantTeamMember = getCompetitionInstance().createParticipantTeamMember();
        participantTeamMember.setParticipant(participant);
        participantTeamMember.setParticipantTeam(this);
        participantTeamMember.setCompetitionInstance(getCompetitionInstance());
        participantTeamMembers.add(participantTeamMember);
    }


    @Override
    public void fillCache(boolean up, boolean down) {
//        resetCache();
//        if (down && participantTeamMembers != null)
//            for (ParticipantTeamMember participantTeamMember : participantTeamMembers) {
//                if (participantTeamMember.competitionInstance == null)
//                    participantTeamMember.competitionInstance = getCompetitionInstance();
////                participantTeamMember.fillCache(false, down);
//            }
//
    }

    //
    @Override
    public void resetCache() {
//        if (participantTeamMembers != null)
//            for (ParticipantTeamMember participantTeamMember : participantTeamMembers) {
//                participantTeamMember.resetCache();
//            }
    }


    public void spreadCompetitionInstance(CompetitionInstance competitionInstance) {
        for (ParticipantTeamMember participantTeamMember : participantTeamMembers) {
            participantTeamMember.spreadCompetitionInstance(competitionInstance);
        }
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
        List<Participant> participants = new ArrayList<>();
        if (!alreadyIn) {
            participants.add(this.cloneForContext());
            for (ParticipantTeamMember participantTeamMember : participantTeamMembers) {
                participants.addAll(participantTeamMember.participant.getParticipantsAsArrayForContext(participantArrayList));
            }
        }
        return participants;
    }

    //    public ParticipantTeam cloneForContext() {
//        ParticipantTeam participantTeam = null;
//        try {
//            participantTeam= (ParticipantTeam )super.cloneForContext();
//            participantTeam.participantTeamMembers = new TreeSet<>();
//            for (ParticipantTeamMember participantTeamMember: this.participantTeamMembers) {
//                participantTeam.participantTeamMembers.add(participantTeamMember.cloneForContext());
//
//            }
////            participant.equalityComparison = this.equalityComparison;
//        } catch (Exception e) {
////            LOGGER_clone.log(Level.SEVERE, this.toString(), e);
//            e.printStackTrace();
//        }
//        return participantTeam;
//    }
    public ParticipantTeam cloneForContext() {
        ParticipantTeam participantTeam = (ParticipantTeam) super.cloneForContext();
//        participantTeam.participantTeamMembers = null;
        if (participantTeamMembers != null)
            participantTeam.participantTeamMembers = new TreeSet<>();
        for (ParticipantTeamMember participantTeamMember : this.participantTeamMembers) {
            if (participantTeamMember.participant != null) {
                ParticipantTeamMember participantTeamMemberNew = participantTeamMember.cloneForContext();
                participantTeam.participantTeamMembers.add(participantTeamMemberNew);
            }
        }
        return participantTeam;
    }


    public void activate() {
        super.activate();
        if (this.participantTeamMembers != null) {
            for (ParticipantTeamMember participantTeamMember : this.participantTeamMembers) {
                if (participantTeamMember.participant != null) {
                    participantTeamMember.participant.activate();
                }
            }
        }
    }

    public void unactivate() {
        super.unactivate();
        if (this.participantTeamMembers != null) {
            for (ParticipantTeamMember participantTeamMember : this.participantTeamMembers) {
                if (participantTeamMember.participant != null) {
                    participantTeamMember.participant.unactivate();
                }
            }
        }
    }
}

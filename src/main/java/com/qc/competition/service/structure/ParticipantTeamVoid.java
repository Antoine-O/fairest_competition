package com.qc.competition.service.structure;

import javax.xml.bind.annotation.XmlType;

/**
 * Created by Duncan on 09/01/2015.
 */
@XmlType(name = "ParticipantTeamVoid")
public class ParticipantTeamVoid extends ParticipantTeam {
    public static String CLASS = ParticipantTeamVoid.class.getSimpleName();

    public ParticipantTeamVoid() {
        super();
    }

    private ParticipantTeamVoid(IdGenerator idGenerator) {
        super(idGenerator);
    }

    public static ParticipantTeamVoid createInstance(IdGenerator idGenerator) {
        return new ParticipantTeamVoid(idGenerator);
    }

    @Override
    public boolean isVoid() {
        return true;
    }
}

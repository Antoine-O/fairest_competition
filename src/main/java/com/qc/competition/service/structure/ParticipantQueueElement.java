package com.qc.competition.service.structure;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Duncan on 28/10/2015.
 */

@XmlAccessorType(XmlAccessType.FIELD)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ParticipantQueueElement implements Serializable {
    //    @XmlElementWrapper(name = "participants")
//    @XmlElement(name = "localId")
    @XmlList
    @XmlAttribute(name = "participantIds")
    @JsonProperty("participantIds")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    public List<Participant> participants = new ArrayList<>();

}

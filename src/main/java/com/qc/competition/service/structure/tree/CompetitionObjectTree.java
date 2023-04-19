package com.qc.competition.service.structure.tree;

import com.fasterxml.jackson.annotation.*;
import com.qc.competition.service.structure.Duration;
import com.qc.competition.service.structure.InternationalizedLabel;
import com.qc.competition.service.structure.adaptater.DurationAdapter;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Duncan on 02/10/2015.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public abstract class CompetitionObjectTree {
    @XmlAttribute(name = "localId")
    public String localId;
    @XmlAttribute(name = "id")
    @XmlID
    @JsonProperty("id")
    public String id;
    @XmlAttribute(name = "dbId")
    @JsonProperty("dbId")
    public Integer databaseId;
    //    @XmlElement
    @XmlTransient
    @JsonIgnore
    public InternationalizedLabel internationalizedLabel;
    @XmlElementWrapper(name = "results")
    @XmlElement(name = "result")
    @JsonProperty("results")
    public List<ParticipantResultTree> participantResultTrees = new ArrayList<>();

    @XmlAttribute(name = "filled")
    public Boolean filled;

    @XmlAttribute(name = "over")
    public Boolean over;

    @XmlAttribute(name = "expectedDuration")
    @XmlJavaTypeAdapter(type = Duration.class, value = DurationAdapter.class)
    public Duration expectedDuration;


    @XmlAttribute(name = "expectedRelativeEndTime")
    @XmlJavaTypeAdapter(type = Duration.class, value = DurationAdapter.class)
    public Duration expectedRelativeEndTime;
    @XmlAttribute(name = "expectedRelativeStartTime")
    @XmlJavaTypeAdapter(type = Duration.class, value = DurationAdapter.class)
    public Duration expectedRelativeStartTime;


}

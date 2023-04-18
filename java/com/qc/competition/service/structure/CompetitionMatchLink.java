package com.qc.competition.service.structure;


import com.fasterxml.jackson.annotation.*;
import com.qc.competition.utils.json.JSONObject;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@JsonIdentityInfo(generator = ObjectIdGenerators.None.class, property = "id")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, isGetterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompetitionMatchLink implements JSONObject, Cloneable, Comparable<CompetitionMatchLink> {
    @XmlAttribute(name = "previousCompetitionMatchId")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("previousCompetitionMatchId")
    public CompetitionMatch previousCompetitionMatch;
    @XmlAttribute(name = "nextCompetitionMatchId")
    @XmlIDREF
    @JsonIdentityReference(alwaysAsId = true)
    @JsonProperty("nextCompetitionMatchId")
    public CompetitionMatch nextCompetitionMatch;
    //    @XmlAttribute(name = "routeParticipantPaginationIndex")
//    @JsonProperty("routeParticipantPaginationIndex")
//    public int routeParticipantPaginationIndex;
    @XmlAttribute(name = "linkThickness")
    @JsonProperty("linkThickness")
    public Integer linkThickness;
    @XmlAttribute(name = "linkType")
    @JsonProperty("linkType")
    public MatchLinkType matchLinkType;
    @XmlAttribute(name = "nextLinkIndex")
    @JsonProperty("nextLinkIndex")
    public Integer nextLinkIndex;
    @XmlAttribute(name = "previousLinkIndex")
    @JsonProperty("previousLinkIndex")
    public Integer previousLinkIndex;
    @XmlAttribute(name = "localId")
    @JsonProperty("localId")
    public String localId;
    @XmlAttribute(name = "id")
    @XmlID
    @JsonProperty("id")
    public String id;
    @XmlAttribute(name = "dbId")
    @JsonProperty("dbId")
    public Integer databaseId = null;

    @XmlTransient
    @JsonIgnore
    protected IdGenerator idGeneratorCache;

    protected CompetitionMatchLink(IdGenerator idGenerator) {
        super();
        this.idGeneratorCache = idGenerator;
        this.localId = idGeneratorCache.getId(this.getClass().getSimpleName());
        this.id = idGeneratorCache.getId();
    }

    public CompetitionMatchLink() {
        super();

    }


    public static CompetitionMatchLink createInstance(IdGenerator idGenerator) {

        return new CompetitionMatchLink(idGenerator);
    }

    @Override
    public int compareTo(CompetitionMatchLink o) {
        int result = 0;
        if (result == 0) {
            if (this.id != null) {
                if (o.id != null) {
                    result = this.id.compareTo(o.id);
                }
            } else {
                if (o.id != null) {

                } else {

                }
            }

        } else {
            if (result == 0) {
                if (this.previousCompetitionMatch != null) {
                    if (o.previousCompetitionMatch != null) {
                        result = this.previousCompetitionMatch.compareTo(o.previousCompetitionMatch);
                    } else {
                        result = 1;
                    }
                } else {
                    if (o.previousCompetitionMatch != null) {
                        result = -1;
                    }
                }
            }

            if (result == 0) {
                if (this.nextCompetitionMatch != null) {
                    if (o.nextCompetitionMatch != null) {
                        result = this.nextCompetitionMatch.compareTo(o.nextCompetitionMatch);
                    } else {
                        result = 1;
                    }
                } else {
                    if (o.nextCompetitionMatch != null) {
                        result = -1;
                    }
                }
            }
        }
        return result;
    }
}

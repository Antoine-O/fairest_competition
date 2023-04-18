package com.qc.competition.service.xml;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAnyElement;
import java.util.List;

/**
 * Created by Duncan on 12/03/2017.
 */
class MapWrapper {

    @XmlAnyElement(lax = true)
    protected List<JAXBElement<Integer>> elements;
}
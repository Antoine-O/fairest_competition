package com.qc.competition.service.xml;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Duncan on 12/03/2017.
 */
public class MapAdapter extends XmlAdapter<MapWrapper, Map<String, Integer>> {

    @Override
    public MapWrapper marshal(Map<String, Integer> m) {
        MapWrapper wrapper = new MapWrapper();
        List<JAXBElement<Integer>> elements = new ArrayList<>();
        if (m != null && !m.isEmpty()) {
            for (Map.Entry<String, Integer> property : m.entrySet()) {
                elements.add(new JAXBElement<>(new QName(property.getKey()),
                        Integer.class, property.getValue()));
            }
        }
        wrapper.elements = elements;
        return wrapper;
    }

    @Override
    public Map<String, Integer> unmarshal(MapWrapper v) {
        Map<String, Integer> map = new HashMap<>();
        if (v != null && v.elements != null && !v.elements.isEmpty()) {
            for (Object object : v.elements) {
                org.w3c.dom.Element element = (org.w3c.dom.Element) object;
                map.put(element.getNodeName(), Integer.valueOf(element.getTextContent()));
            }
        }
        return map;
    }

}


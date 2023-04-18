package com.qc.competition.service.xml;

//import com.qc.competition.db.entity.competition.CompetitionInstanceXml;

import com.qc.competition.service.structure.XmlManagedObject;

import javax.xml.bind.*;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.*;

/**
 * Created by Duncan on 14/07/2015.
 */

public class XmlUtils<T> {
    //    private static Logger logger = Logger.getLogger(XmlUtils.class.getName());
    Class<T> tClass;

    private XmlUtils() {

    }

    public XmlUtils(Class<T> tClass) {
        super();
        this.tClass = tClass;
    }

    public T fromString(String string) {
        Reader reader = new StringReader(string);
        return fromReader(reader);
    }

    public T fromFile(String fileName) throws IOException {
        File file = new File(fileName);
        FileReader fileReader = new FileReader(file);
        T xmlObject = fromReader(fileReader);
        fileReader.close();
        return xmlObject;
    }

    public T fromReader(Reader reader) {
        T xmlObject = null;
        try {
            JAXBContext context = JAXBContext.newInstance(tClass);
            Source source = new StreamSource(reader);
            Unmarshaller un = context.createUnmarshaller();
            JAXBElement<T> o = un.unmarshal(source, tClass);
            xmlObject = o.getValue();
        } catch (JAXBException e) {
            e.printStackTrace();
//            logger.log(Level.SEVERE, "", e);
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return xmlObject;
    }


    public String toString(T xmlObject) {
        StringWriter stringWriter = new StringWriter();
        toString(xmlObject, stringWriter);
        return stringWriter.toString();
    }

    public File toFile(T xmlObject, String fileName) throws IOException {
        File file = new File(fileName);
        FileWriter fileWriter = new FileWriter(file);
        toString(xmlObject, fileWriter);
        return file;
    }

    public void toString(T xmlObject, Writer writer, Boolean jaxbFormattedOutput) {

        try {
            ((XmlManagedObject) xmlObject).initForXmlOutput();
            JAXBContext context = JAXBContext.newInstance(tClass);
            Marshaller marshaller = context.createMarshaller();
            //for pretty-print XML in JAXB
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, jaxbFormattedOutput);

            // Write to System.out for debugging
            // m.marshal(emp, System.out);

            // Write to Writer
            marshaller.marshal(xmlObject, writer);
            writer.flush();
            writer.close();
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void toFormattedString(T xmlObject, Writer writer) {
        toString(xmlObject, writer, Boolean.TRUE);
    }

    public void toString(T xmlObject, Writer writer) {
        toString(xmlObject, writer, Boolean.FALSE);
    }

//    @Deprecated
//    public CompetitionInstanceXml updateCompetitionInstanceXml(CompetitionInstanceXml competitionInstanceXml, T xmlObject) {
//        competitionInstanceXml.setXmlData(ZipUtils.compress(toString(xmlObject).getBytes()));
//        competitionInstanceXml.setCompressed(true);
//        competitionInstanceXml.setCompression(ZipUtils.COMPRESSION);
//        return competitionInstanceXml;
//
//    }
}

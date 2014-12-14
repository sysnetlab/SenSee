package sysnetlab.android.sdc.replay;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by kosta on 12/13/2014.
 */


/*
<taggingstate>
    <name>TAG_ON</name>
</taggingstate>
<tagreference name="5 steps" number="1" />
<experimenttime>
    <threadtimemillis>19480</threadtimemillis>
    <elapsedrealtime>34951226</elapsedrealtime>
</experimenttime>
*/

public class SenSeeExperimentXmlParser extends DefaultHandler {

    public void startElement(String uri, String localName,
                             String qName, Attributes attributes)
            throws SAXException {

        System.out.println("start element    : " + qName);
    }

    public void endElement(String uri, String localName, String qName)
            throws SAXException {

        System.out.println("end element      : " + qName);
    }

}

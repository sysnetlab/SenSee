package sysnetlab.android.sdc.replay;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by kosta on 12/13/2014.
 */
public class SenSeeSensingAssertionEnforcer implements SensingAssertionEnforcer {

    public SenSeeSensingAssertionEnforcer(InputStream experimentStream) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            DefaultHandler handler = new SenSeeExperimentXmlParser();
            saxParser.parse(experimentStream, handler);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }


    public void checkAssertion(long timeStamp, int state)
    {

    }

}

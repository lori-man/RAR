package com.mro.RAR.internal.parser;

import com.mro.RAR.common.ResponseMessage;
import com.mro.RAR.exception.ResponseParseException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;
import java.io.InputStream;
import java.util.HashMap;

import static com.mro.RAR.RARUtils.COMMON_RESOURCE_MANAGER;

/**
 * Implementation of <code>ResponseParser<code> with JAXB.
 */
public class JAXBResponseParser implements ResponseParser<Object> {

    private static final SAXParserFactory saxParserFactory = SAXParserFactory.newInstance("com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl",null);

    // It allows to specify the class type, if the class type is specified,
    // the contextPath will be ignored.
    private Class<?> modelClass;

    // Because JAXBContext.newInstance() is a very slow method,
    // it can improve performance a lot to cache the instances of JAXBContext
    // for used context paths or class types.
    private static HashMap<Object, JAXBContext> cachedContexts = new HashMap<Object, JAXBContext>();

    static {
        saxParserFactory.setNamespaceAware(true);
        saxParserFactory.setValidating(false);
    }

    public JAXBResponseParser(Class<?> modelClass) {
        assert (modelClass != null);
        this.modelClass = modelClass;
    }

    public Object parse(ResponseMessage var1) throws ResponseParseException {
        return null;
    }

    private Object getObject(InputStream responseContent) throws ResponseParseException {
        try {
            if (!cachedContexts.containsKey(modelClass)) {
                initJAXBContext(modelClass);
            }

            assert (cachedContexts.containsKey(modelClass));
            JAXBContext jc = cachedContexts.get(modelClass);
            Unmarshaller um = jc.createUnmarshaller();
            // It performs better to call Unmarshaller#unmarshal(Source)
            // than to call Unmarshaller#unmarshall(InputStream)
            // if XMLReader is specified in the SAXSource instance.
            return um.unmarshal(getSAXSource(responseContent));
        } catch (JAXBException e) {
            throw new ResponseParseException(
                    COMMON_RESOURCE_MANAGER.getFormattedString("FailedToParseResponse", e.getMessage()), e);
        } catch (SAXException e) {
            throw new ResponseParseException(
                    COMMON_RESOURCE_MANAGER.getFormattedString("FailedToParseResponse", e.getMessage()), e);
        } catch (ParserConfigurationException e) {
            throw new ResponseParseException(
                    COMMON_RESOURCE_MANAGER.getFormattedString("FailedToParseResponse", e.getMessage()), e);
        }
    }

    private static synchronized void initJAXBContext(Class<?> c) throws JAXBException {
        if (!cachedContexts.containsKey(c)) {
            JAXBContext jc = JAXBContext.newInstance(c);
            cachedContexts.put(c, jc);
        }
    }

    private static SAXSource getSAXSource(InputStream content) throws SAXException, ParserConfigurationException {
        SAXParser saxParser = saxParserFactory.newSAXParser();
        return new SAXSource(saxParser.getXMLReader(), new InputSource(content));
    }
}

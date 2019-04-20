package hu.fourdsoft.memorygame.error;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

/**
 * Utility methods for marshalling.
 * 
 * @author lajos
 * 
 */
@Slf4j
public class MarshallingUtil {

    /**
     * Private constructor.
     */
    protected MarshallingUtil() {
    }

    /**
     * Marshalls an object to a string xml.
     * 
     * @param object
     *            Source object.
     * @return Xml string or null.
     */
    public static String marshall(Object object) {
        try {
            return marshallUncheckedXml(object);
        } catch (JAXBException e) {
            log.warn("Cannot convert {} object to xml: {}", new Object[]{object, e.getMessage()});
        }
        return null;
    }

    /**
     * Marshalls an object to a string xml.
     * 
     * @param object
     *            Source object.
     * @return
     * @throws JAXBException
     */
    public static String marshallUncheckedXml(Object object) throws JAXBException {
        if (object == null) {
            log.warn("The object is null.");
            return null;
        }
        return marshallUncheckedXml(object, object.getClass());
    }
    
    /**
     * Marshalls an object to a string xml.
     * 
     * @param object
     * @param c
     * @return
     * @throws JAXBException
     */
    public static <T> String marshallUncheckedXml(Object object, Class<T> c) throws JAXBException {
        if (object == null || c == null) {
            log.warn("The object or type is null.");
            return null;
        }
        JAXBContext jc = JAXBContext.newInstance(c);
        Marshaller m = jc.createMarshaller();
        return marshallUncheckedXml(object, m);
    }

    /**
     * Marshalls an object to a string xml.
     * 
     * @param object
     *            Source object.
     * @param m
     *            Marshaller.
     * @return
     * @throws JAXBException
     */
    public static String marshallUncheckedXml(Object object, Marshaller m) throws JAXBException {
        if (object == null) {
            log.warn("The object is null.");
            return null;
        }
        log.debug("Marshalling object to xml.");
        // docs.oracle: Closing a StringWriter has no effect.
        StringWriter writer = new StringWriter();
        m.marshal(object, writer);
        return writer.toString();
    }
    
    /**
     * Marshalls an object to a stream.
     * 
     * @param object
     *            Source object.
     * @param s
     */
    public static void marshall(Object object, OutputStream s) {
        try {
            marshallUncheckedXml(object, s);
        } catch (JAXBException e) {
            log.warn("Cannot convert {} object to xml: {}", new Object[]{object, e.getMessage()});
        }
    }

    public static <T> void marshall(Object object, OutputStream s, Class<T> c) {
        try {
            marshallUncheckedXml(object, s, c);
        } catch (JAXBException e) {
            log.warn("Cannot convert {} object to xml: {}", new Object[]{object, e.getMessage()});
        }
    }

    /**
     * Marshalls an object to a stream.
     * 
     * @param object
     *            Source object.
     * @param s
     * @throws JAXBException
     */
    public static void marshallUncheckedXml(Object object, OutputStream s) throws JAXBException {
        if (object == null || s == null) {
            log.warn("The object or stream is null.");
            return;
        }
        marshall(object, s, object.getClass());
    }

    public static <T> void marshallUncheckedXml(Object object, OutputStream s, Class<T> c) throws JAXBException {
        if (object == null || s == null || c == null) {
            log.warn("The object, type or stream is null.");
            return;
        }
        JAXBContext jc = JAXBContext.newInstance(c);
        Marshaller m = jc.createMarshaller();
        m.marshal(object, s);
    }

    

    /**
     * Unmarshalls an object from a string.
     * 
     * @param str
     *            String source.
     * @param c
     *            Object type.
     * @return
     */
    public static <T> T unmarshall(String str, Class<T> c) {
        try {
            return unmarshallUnchecked(str, c);
        } catch (JAXBException e) {
            log.error("Error during unmarshalling XML string: " + str, e);

        } catch (Exception e) {
            log.error("Error during unmarshalling JSON string: " + str, e);
        }
        return null;
    }

    /**
     * Unmarshalls an object from a string.
     * 
     * @param str
     *            String source.
     * @param c
     *            Object type.
     * @return
     * @throws JAXBException
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    public static <T> T unmarshallUnchecked(String str, Class<T> c) throws JAXBException, JsonParseException, JsonMappingException, IOException {
        if (StringUtils.isEmpty(str) || c == null) {
            log.warn("The string source or the return type is null.");
            return null;
        }
        if (str.trim().startsWith("<")) {
            // XML
            log.debug("Unmarshalling xml.");
            return unmarshallUncheckedXml(str, c);
        }
        if (str.trim().startsWith("{")) {
            // XML
            log.debug("Unmarshalling json.");
            return unmarshallUncheckedJson(str, c);
        }
        return null;
    }

    /**
     * Unmarshalls an object from an xml string.
     * 
     * @param str
     *            String source.
     * @param c
     *            Object type.
     * @return
     * @throws JAXBException
     */
    @SuppressWarnings("unchecked")
    public static <T> T unmarshallUncheckedXml(String str, Class<T> c) throws JAXBException {
        if (StringUtils.isEmpty(str) || c == null) {
            log.warn("The string source or the return type is null.");
            return null;
        }
        JAXBContext jc = JAXBContext.newInstance(c);
        Unmarshaller um = jc.createUnmarshaller();
        StringReader reader = new StringReader(str);
        return (T) um.unmarshal(reader);
    }

    /**
     * Unmarshalls an object from a json string.
     * 
     * @param str
     *            String source.
     * @param c
     *            Object type.
     * @return
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    public static <T> T unmarshallUncheckedJson(String str, Class<T> c) throws JsonParseException, JsonMappingException, IOException {
        if (StringUtils.isEmpty(str) || c == null) {
            log.warn("The string source or the return type is null.");
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(str, c);
    }

    public static <T> void marshallTest(Class<T> c, String xml, Object object) throws JAXBException {
        try {
            Object obj = null;
            if (!StringUtils.isEmpty(xml)) {
                System.out.println(xml);
                obj = MarshallingUtil.unmarshallUncheckedXml(xml, c);
            } else {
                obj = object;
            }
            String marshalledXml = MarshallingUtil.marshall(obj);
            System.out.println(marshalledXml);
        } catch (JAXBException e) {
            e.printStackTrace();
            throw e;
        }
    }
}

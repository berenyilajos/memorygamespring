package hu.fourdsoft.memorygame.controller.rest.validator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import hu.fourdsoft.memorygame.controller.rest.ResultRestController;
import hu.fourdsoft.memorygame.error.MarshallingUtil;
import hu.fourdsoft.memorygame.error.MyErrorHandler;
import hu.fourdsoft.memorygame.error.ValidationErrorCollector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;

import java.io.InputStream;
import java.io.StringWriter;

@Slf4j
public class XSDValidator {

	public static void validateByXSD(Object xmlObject, String xsd, Resource xsdResource) throws Exception {
		StringWriter stringWriter = new StringWriter();
		MyErrorHandler eh = new MyErrorHandler();
		ValidationErrorCollector errorCollector = new ValidationErrorCollector();
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(xmlObject
					.getClass());
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
			stringWriter.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
			StreamSource src = null;
			SchemaFactory sf = null;
			InputStream stream = null;

			stream = xsdResource != null ? xsdResource.getInputStream() : null;
			if (stream == null) {
				throw new Exception("cannot find schema to validate");
			}
			src = new StreamSource(stream);
			sf = SchemaFactory
					.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
			sf.setResourceResolver(new hu.fourdsoft.memorygame.error.ResourceResolver(
					xsd, eh.getErrorBuffer()));
			Schema schema = sf.newSchema(src);
			marshaller.setEventHandler(errorCollector);
			marshaller.setSchema(schema);
			marshaller.marshal(xmlObject, stringWriter);
			if (errorCollector.getErrors().size() > 0) {
				log.warn("xml validation error(s) occured!");
				if (!log.isTraceEnabled()) {
					String xml = MarshallingUtil.marshall(xmlObject);
					log.info("xml content: {} ", new Object[]{(xml == null ? "" : xml)});
				}
				Exception ire = new Exception(
						errorCollector.getErrors().get(0).getError());
				throw ire;
			}
		} finally {

		}
	}

}

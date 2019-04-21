package hu.fourdsoft.memorygame.controller.rest.validator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import hu.fourdsoft.memorygame.error.MarshallingUtil;
import hu.fourdsoft.memorygame.error.MyErrorHandler;
import hu.fourdsoft.memorygame.error.ValidationErrorCollector;
import org.slf4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.InputStream;
import java.io.StringWriter;

public interface XSDValidator {

	ResourceLoader getResourceLoader();

	Logger getLog();

	default void validateByXSD(Object xmlObject, String xsd) throws Exception {
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

			Resource xsdResource = getResourceLoader().getResource("classpath:" + xsd);
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
				getLog().warn("xml validation error(s) occured!");
				if (!getLog().isTraceEnabled()) {
					String xml = MarshallingUtil.marshall(xmlObject);
					getLog().info("xml content: {} ", new Object[]{(xml == null ? "" : xml)});
				}
				Exception ire = new Exception(
						errorCollector.getErrors().get(0).getError());
				throw ire;
			}
		} finally {

		}
	}

}

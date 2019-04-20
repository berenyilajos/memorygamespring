package hu.fourdsoft.memorygame.error;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.transform.stream.StreamSource;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.xerces.dom.DOMInputImpl;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

/**
 * 
 * @author cstamas
 * Class for load the referenced xsd files.
 */
@Slf4j
public final class ResourceResolver implements LSResourceResolver {

    private String xsdDirPath;
    private StringBuilder errors;
    
    //jo az, h ez itt concurrentHashMap?
    private static ConcurrentHashMap<String, StreamSource> xsdCache = new ConcurrentHashMap<String, StreamSource>();

    public ResourceResolver(String xsdDirPath, StringBuilder errors) {
        this.errors = errors;
        this.xsdDirPath = new File(xsdDirPath).getParent().replace("\\", "/");
        log.debug("xsd dir path: {}", new Object[]{this.xsdDirPath});
    }
    
    public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {
        final LSInput input = new DOMInputImpl();
        log.debug("type={}, nameSpaceURI={}, publicId={}, systemId={}, baseURI={}", type, namespaceURI, publicId, systemId, baseURI);
        if( systemId == null ) {
            systemId = "";
        }
        File f = new File(xsdDirPath + '/' + systemId);
        String s;
        try { 
            /*
            //default webinf-ben keressuk, de ha metainfben van esetleg, akkor ott - csakmer'aze'
            if( f.getCanonicalPath() != null ) {
                if( f.getCanonicalPath().indexOf("META-INF") > -1 ) infDir="META-INF";
            }
            //ez windows miatt kell
            log.debug("canonical: {}", f.getCanonicalPath());
            s = f.getCanonicalPath().substring(f.getCanonicalPath().indexOf(infDir)).replace('\\', '/');
            */
            s=f.getCanonicalPath();
        } catch( Exception e ) {
            log.error("(resourceResolver) exception: ", e);
            errors.append("(resourceResolver) exception: "+ e + "\n");
            return null;
        }

        InputStream resStream = this.getClass().getClassLoader().getResourceAsStream(s);
        if( resStream == null ) {
            try {
                String normalizedPath = new URI(xsdDirPath + '/' + systemId).normalize().getPath();
                resStream = this.getClass().getClassLoader().getResourceAsStream(normalizedPath);
            } catch (URISyntaxException e) {
                log.error("(resourceResolver) exception: ", e);
                errors.append("(resourceResolver) exception: "+ e + "\n");
                return null;
            }
        }
        if( resStream == null ) {
            log.error("resStream is null ({}), try another method", new Object[]{s});
            //namespaceURI = http://common.types.hugo.hu/common
            String url = StringUtils.substringAfter(namespaceURI, "http://");
            String temp = StringUtils.substringBefore(url, "/");
            String[] words = StringUtils.split(temp, ".");
            StringBuffer packagePath = new StringBuffer();
            for (int i = 1; i < words.length; i++) {
                if (packagePath.length() > 0) {
                    packagePath.append("/");
                }
                packagePath.append(words[words.length - i]);
            }
            temp = StringUtils.substringAfter(url, "/");
            if (StringUtils.isNotBlank(temp)) {
                packagePath.append("/").append(
                        StringUtils.substringAfter(url, "/"));
            }
            resStream = this
                    .getClass()
                    .getClassLoader()
                    .getResourceAsStream(
                            packagePath.toString() + '/' + systemId);
            if (resStream == null) {
                errors.append("(resourceResolver) resource not found: " + s
                        + "\n");
                return null;
            }
        }
        ////log.debug("resStream ("+namespaceURI+") found: {}", s);
        input.setByteStream(resStream);
        input.setSystemId(systemId);
        return input;
    }
    

}

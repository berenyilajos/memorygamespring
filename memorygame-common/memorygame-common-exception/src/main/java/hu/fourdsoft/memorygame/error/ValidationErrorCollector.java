package hu.fourdsoft.memorygame.error;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;

@Slf4j
public class ValidationErrorCollector implements ValidationEventHandler {

    private ArrayList<XMLValidationError> errors = new ArrayList<XMLValidationError>();

    private String getField(String msg, String first, String last) {
        String ret = "";
        try {
            ret = msg.substring(msg.indexOf(first)+first.length(), msg.lastIndexOf(last));
        } catch( Exception e ) {
            log.warn("exc in validation string operation: ", e);
            //hogy tovabbra is hibat jelezzen
            ret=msg;
        }
        
        return ret;
    }
    @Override
    public boolean handleEvent(ValidationEvent event) {
        // record the validation error
        if (event.getSeverity() == ValidationEvent.ERROR ||
            event.getSeverity() == ValidationEvent.FATAL_ERROR) {
            
            XMLValidationError err = new XMLValidationError();
            //err.setField(event.getLocator().getObject().getClass().getName());
            String msg = event.getMessage();
            log.warn(msg);
            if (msg.startsWith("cvc-type")
                    || msg.startsWith("cvc-complex-type")
                    || msg.startsWith("cvc-attribute")) {
                if( msg.indexOf("type.2.4.d") > 0 ) {
                    String field = getField(msg, "with element '", "'. No child"); 
                    err.setField(field);
                    log.debug("setField: "+field);
                } else if( msg.indexOf("type.2.4.b") > 0 ) {
                    String field = getField(msg, "of element '", "' is not complete"); 
                    err.setField(field);
                    log.debug("setField: "+field);
                } else if( msg.indexOf("type.2.4") > 0 ) {
                    String field = getField(msg, "with element '", "'. One of"); 
                    err.setField(field);
                    log.debug("setField: "+field);
                } else if( msg.indexOf("type.3.1.3") > 0 ) {
                    String field = getField(msg, " of element '", "' is"); 
                    err.setField(field);
                    log.debug("setField: "+field);
                } else if( msg.indexOf("attribute.3") > 0 ) {
                    String field = getField(msg, " on element '", "' is"); 
                    err.setField(field);
                    log.debug("setField: "+field);
                } else {
                    String field = getField(msg, "of element '", "'");
                    err.setField(field);
                    log.debug("setField: "+field);
                }
            } else {
                log.warn(" !> ".concat(msg));
            }
            /*
            if( event.getLocator().getColumnNumber() != -1 ) {
                StringWriter errstr = new StringWriter();
                errstr.append(String.valueOf(event.getLocator().getColumnNumber())).append(":").append(String.valueOf(event.getLocator().getLineNumber())).append(" ").append(event.getMessage());
                err.setError(errstr.toString());
            } else {
                err.setError(msg);
            }
            */
            if( err.getField()!=null && err.getField().length()>0) {
                err.setError(msg.substring(msg.indexOf(":")+2));
                errors.add(err);
            } else {
                log.warn(" !!> ".concat(msg));
            }
        } else {
            log.warn(" +++ ".concat(event.getMessage()));
        }
        return true; //nem tekintjuk hibanak ebben az osztalyban
    }
    
    public ArrayList<XMLValidationError> getErrors() {
        return this.errors;
    }
}

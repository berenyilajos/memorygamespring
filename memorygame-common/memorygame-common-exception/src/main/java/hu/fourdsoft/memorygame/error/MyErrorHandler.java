package hu.fourdsoft.memorygame.error;

import java.util.ArrayList;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

/**
 * @author lajos
 *
 */
public final class MyErrorHandler implements ErrorHandler {

    private boolean valid = true;
    private StringBuilder errors = new StringBuilder("\n");
    private ArrayList<String> errorList = new ArrayList<String>();
    
    public boolean isValid() {
        return valid;
    }
    public String getError()  { return errors.toString(); }
    public StringBuilder getErrorBuffer() { return this.errors; }
    
    public void setError(String error)  {
        errors.delete(0, errors.length());
        errors.append(error); 
    }

    public void warning(SAXParseException exception) {
        String str = "warning: ".concat(exception.getMessage()).concat("  at ")
            .concat(String.valueOf(exception.getLineNumber())).concat(":").concat(String.valueOf(exception.getColumnNumber()));
        errors.append(str).append("\n");
        errorList.add(str);
    }

    public void error(SAXParseException exception) {
        valid = false;
        String str = "warning: ".concat(exception.getMessage()).concat("  at ")
            .concat(String.valueOf(exception.getLineNumber())).concat(":").concat(String.valueOf(exception.getColumnNumber()));
        errors.append(str).append("\n");
        errorList.add(str);
    }

    public void fatalError(SAXParseException exception) {
        valid = false;
        String str = "fatal error: ".concat(exception.getMessage()).concat("  at ")
            .concat(String.valueOf(exception.getLineNumber())).concat(":").concat(String.valueOf(exception.getColumnNumber()));
        errors.append(str).append("\n");
        errorList.add(str);
    }

    public ArrayList<String> getErrorList() {
        return errorList;
    }
    protected void setErrorList(ArrayList<String> errorList) {
        this.errorList = errorList;
    }

}

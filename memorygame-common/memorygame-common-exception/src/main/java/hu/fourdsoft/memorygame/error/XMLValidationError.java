package hu.fourdsoft.memorygame.error;

import java.io.Serializable;

public class XMLValidationError implements Serializable {

    private static final long serialVersionUID = -2523167059288742454L;
    private String field;
    private String error;
    
    public String getField() {
        return field;
    }
    public void setField(String field) {
        this.field = field;
    }
    public String getError() {
        return error;
    }
    public void setError(String error) {
        this.error = error;
    }
    
    
}

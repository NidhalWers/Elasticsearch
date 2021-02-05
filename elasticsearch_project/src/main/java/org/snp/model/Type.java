package main.java.org.snp.model;


public enum Type {
    STRING("java.lang.String"),
    DOUBLE("java.lang.Double"),
    INTEGER("java.lang.Integer")
    ;

    private String value;
    private Type(String value){
        this.value= value;
    }

    public String getValue(String text){
        if("string".compareToIgnoreCase(text) == 0)
            return Type.STRING.value;
        if("double".compareToIgnoreCase(text) == 0)
            return Type.DOUBLE.value;
        if("int".compareToIgnoreCase(text) == 0)
            return Type.INTEGER.value;
        return null;
    }
}

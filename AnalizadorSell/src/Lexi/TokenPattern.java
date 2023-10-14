package Lexi;

public class TokenPattern {
    private String type;
    private String regex;
    private String attribute;
    private String patron;

    public TokenPattern(String type, String regex, String attribute, String patron) {
        this.type = type;
        this.regex = regex;
        this.attribute = attribute;
        this.patron = patron;
    }

    public String getType() {
        return type;
    }

    public String getRegex() {
        return regex;
    }

    public String getAttribute() {
        return attribute;
    }

    public String getPatron() {
        return patron;
    }
}

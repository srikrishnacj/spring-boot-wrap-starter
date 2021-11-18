package in.cjcj.sboa.wrap.rest.util;


import org.apache.commons.lang3.StringUtils;

public class StringFormatter {
    private final String DECORATOR = "**********************************************************************************************************************";
    private final String HEADER_START = ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  ";
    private final String HEADER_STOP = "  <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<";

    private final StringBuilder builder = new StringBuilder();

    public StringFormatter decorator() {
        this.line(DECORATOR);
        return this;
    }

    public StringFormatter header(String msg) {
        this.line(HEADER_START + msg + HEADER_STOP);
        return this;
    }

    public StringFormatter line(int tabSize, String msg) {
        builder.append(tab(tabSize) + msg + System.lineSeparator());
        return this;
    }

    public StringFormatter line(String msg) {
        this.line(0, msg);
        return this;
    }

    public StringFormatter line() {
        this.line(0, "");
        return this;
    }

    public StringFormatter subline(String msg) {
        this.line(1, msg);
        return this;
    }

    private String tab(int size) {
        String temp = "";
        for (int ii = 0; ii < size; ii++) {
            temp = temp + "\t";
        }
        return temp;
    }

    public String value(String key, String value) {
        return key + ": " + value;
    }

    public String values(Object[] obs) {
        if (obs == null || obs.length == 0) {
            return "";
        } else if (obs.length == 1) {
            return obs[0].toString();
        } else {
            return StringUtils.join(obs, ",");
        }
    }

    @Override
    public String toString() {
        return this.builder.toString();
    }
}

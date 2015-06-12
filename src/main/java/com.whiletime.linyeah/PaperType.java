package com.whiletime.linyeah;

/**
 * Created by k on 6/12/15.
 */
public enum PaperType {

    KRAFT("ZZ0202"),
    EGGSHELL("ZZ0201"),
    COPPERPLATE("ZZ0002");

    private String field;

    PaperType(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }

}

package br.com.realtour.util;

import lombok.Getter;

@Getter
public enum States {
    AC("AC"),
    AL("AL"),
    AP("AP"),
    AM("AM"),
    BA("BA"),
    CE("CE"),
    DF("DF"),
    ES("ES"),
    GO("GO"),
    MA("MA"),
    MT("MT"),
    MS("MS"),
    MG("MG"),
    PA("PA"),
    PB("PB"),
    PR("PR"),
    PE("PE"),
    PI("PI"),
    RJ("RJ"),
    RN("RN"),
    RS("RS"),
    RO("RO"),
    RR("RR"),
    SC("SC"),
    SP("SP"),
    SE("SE"),
    TO("TO");

    private final String code;

    States(String code) {
        this.code = code;
    }

    public static boolean isValidState(String code) {
        for (States state : values()) {
            if (state.getCode().equals(code)) {
                return true;
            }
        }
        return false;
    }
}

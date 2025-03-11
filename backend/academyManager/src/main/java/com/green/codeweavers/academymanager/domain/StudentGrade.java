package com.green.codeweavers.academymanager.domain;

enum StudentGrade {
    A_PLUS("A+"), A0("A0"), B_PLUS("B+"), B0("B0"), C_PLUS("C+"), C0("C0"), D_PLUS("D+"), D0("D0"), F("F");

    private String value;

    StudentGrade(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

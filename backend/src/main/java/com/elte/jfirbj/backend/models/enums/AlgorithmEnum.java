package com.elte.jfirbj.backend.models.enums;

public enum AlgorithmEnum {
    Kyber512("Kyber512"),
    Kyber512_90s("Kyber512-90s"),
    Kyber768("Kyber768"),
    Kyber768_90s("Kyber768_90s"),
    Kyber1024("Kyber1024"),
    Kyber1024_90s("Kyber1024-90s"),
    NTRU701("NTRU701"),
    NTRU4096("NTRU4096"),
    NTRU2048v1("NTRU2048v1"),
    NTRU2048v2("NTRU2048v2");

    public final String label;

    private AlgorithmEnum(String label) {
        this.label = label;
    }

    public static boolean isLabelInEnum(String label) {
        for (AlgorithmEnum e : values()) {
            if (e.label.equals(label)) {
                return true;
            }
        }
        return false;
    }
}

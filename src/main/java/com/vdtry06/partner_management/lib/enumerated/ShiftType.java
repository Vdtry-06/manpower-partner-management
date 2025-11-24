package com.vdtry06.partner_management.lib.enumerated;

public enum ShiftType {
    DEFAULT(""),
    MORNING ("6:00-12:00"),
    AFTERNOON("12:00-18:00"),
    EVENING("18:00-22:00"),
    NIGHT("22:00-6:00");

    private final String timeRange;

    ShiftType(String timeRange) {
        this.timeRange = timeRange;
    }

    public String timeRange() {
        return timeRange;
    }
}

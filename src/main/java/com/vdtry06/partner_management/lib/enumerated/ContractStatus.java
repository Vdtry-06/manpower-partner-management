package com.vdtry06.partner_management.lib.enumerated;

public enum ContractStatus {
    DRAFT("Nháp"),
    ACTIVE("Đang thực hiện"),
    COMPLETED("Hoàn thành"),
    CANCELLED("Đã hủy");

    private final String displayName;

    ContractStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
package com.example.enums;

public enum FileUploadStatus {
    QUEUED("Queued"),
    PROCESSING("Processing"),
    FAILED("Failed"),
    SUCCESS("Success");

    private final String displayName;

    FileUploadStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
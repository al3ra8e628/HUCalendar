package com.example.al3ra8e.hucalendar.eventPackage;


public class Material {
    private int materialId ;
    private int eventId ;
    private String fileName ;
    private String materialTitle ;
    public Material() {
    }

    public Material(int materialId, int eventId, String fileName , String MterialTitle) {
        this.materialId = materialId;
        this.eventId = eventId;
        this.fileName = fileName;
        this.materialTitle = MterialTitle ;

    }

    public String getMaterialTitle() {
        return materialTitle;
    }

    public Material setMaterialTitle(String materialTitle) {
        this.materialTitle = materialTitle;
        return this ;
    }

    public int getMaterialId() {
        return materialId;
    }

    public Material setMaterialId(int materialId) {
        this.materialId = materialId;
        return this;
    }

    public int getEventId() {
        return eventId;
    }

    public Material setEventId(int eventId) {
        this.eventId = eventId;
        return this;
    }

    public String getFileName() {
        return fileName;
    }

    public Material setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }
}

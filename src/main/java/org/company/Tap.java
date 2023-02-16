package org.company;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Tap {
    private String id;
    private LocalDateTime dateTime;
    private String tapType;
    private String stopId;
    private String companyId;
    private String busId;
    private String pan;

    public Tap() {

    }
    public Tap(String id, LocalDateTime dateTime, String tapType, String stopId, String companyId, String busId, String pan) {
        this.id = id;
        this.dateTime = dateTime;
        this.tapType = tapType;
        this.stopId = stopId;
        this.companyId = companyId;
        this.busId = busId;
        this.pan = pan;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDateTimeAsString() {
        return dateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getTapType() {
        return tapType;
    }

    public void setTapType(String tapType) {
        this.tapType = tapType;
    }

    public String getStopId() {
        return stopId;
    }

    public void setStopId(String stopId) {
        this.stopId = stopId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getBusId() {
        return busId;
    }

    public void setBusId(String busId) {
        this.busId = busId;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    @Override
    public String toString() {
        return "\nTap{" +
                "id='" + id + '\'' +
                ", dateTime=" + getDateTimeAsString() +
                ", tapType='" + tapType + '\'' +
                ", stopId='" + stopId + '\'' +
                ", companyId='" + companyId + '\'' +
                ", busId='" + busId + '\'' +
                ", pan='" + pan + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tap tap = (Tap) o;
        return Objects.equals(id, tap.id) && Objects.equals(dateTime, tap.dateTime) && Objects.equals(tapType, tap.tapType) && Objects.equals(stopId, tap.stopId) && Objects.equals(companyId, tap.companyId) && Objects.equals(busId, tap.busId) && Objects.equals(pan, tap.pan);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateTime, tapType, stopId, companyId, busId, pan);
    }
}

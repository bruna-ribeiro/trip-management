package org.company;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class Trip {
    private LocalDateTime started;
    private LocalDateTime finished;
    private Long durationSecs;
    private String fromStopId;
    private String toStopId;
    private Double chargeAmount;
    private String companyId;
    private String busId;
    private String pan;
    private String status;

    public Trip(LocalDateTime started, LocalDateTime finished, String fromStopId, String toStopId, Double chargeAmount, String companyId, String busId, String pan, String status) {
        this.started = started;
        this.finished = finished;
        this.fromStopId = fromStopId;
        this.toStopId = toStopId;
        this.chargeAmount = chargeAmount;
        this.companyId = companyId;
        this.busId = busId;
        this.pan = pan;
        this.status = status;
        if (started != null && finished != null)
            this.durationSecs = ChronoUnit.SECONDS.between(started, finished);
        else this.durationSecs = (long) 0;
    }

    public LocalDateTime getStarted() {
        return started;
    }

    public String getStartedAsString() {
        if (started != null)
            return started.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        return "N/A";
    }

    public void setStarted(LocalDateTime started) {
        this.started = started;
    }

    public LocalDateTime getFinished() {
        return finished;
    }

    public String getFinishedAsString() {
        if (finished != null)
            return finished.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        return "N/A";
    }

    public void setFinished(LocalDateTime finished) {
        this.finished = finished;
    }

    public Long getDurationSecs() {
        return durationSecs;
    }

    public void setDurationSecs(Long durationSecs) {
        this.durationSecs = durationSecs;
    }

    public String getFromStopId() {
        return fromStopId;
    }

    public void setFromStopId(String fromStopId) {
        this.fromStopId = fromStopId;
    }

    public String getToStopId() {
        return toStopId;
    }

    public void setToStopId(String toStopId) {
        this.toStopId = toStopId;
    }

    public double getChargeAmount() {
        return chargeAmount;
    }

    public void setChargeAmount(double chargeAmount) {
        this.chargeAmount = chargeAmount;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "\nTrip{" +
                "started=" + getStartedAsString() +
                ", finished=" + getFinishedAsString() +
                ", durationSecs=" + durationSecs +
                ", fromStopId='" + fromStopId + '\'' +
                ", toStopId='" + toStopId + '\'' +
                ", chargeAmount=" + chargeAmount +
                ", companyId='" + companyId + '\'' +
                ", busId='" + busId + '\'' +
                ", pan='" + pan + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trip trip = (Trip) o;
        return started.equals(trip.started) && Objects.equals(finished, trip.finished) && Objects.equals(durationSecs, trip.durationSecs) && fromStopId.equals(trip.fromStopId) && Objects.equals(toStopId, trip.toStopId) && chargeAmount.equals(trip.chargeAmount) && companyId.equals(trip.companyId) && busId.equals(trip.busId) && pan.equals(trip.pan) && status.equals(trip.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(started, finished, durationSecs, fromStopId, toStopId, chargeAmount, companyId, busId, pan, status);
    }
}

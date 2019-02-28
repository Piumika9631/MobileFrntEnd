package com.example.imwageesha.made;

import java.io.Serializable;

public class Booking implements Serializable {
    private String vehicleNum;
    private String DriverName;
    private String DriverId;
    private String date;
    private String vehicleType;
    private String slotNum;
    private String slotId;
    private String keeperId;
    private String DriverEmail;
    private boolean paid;
    private String bookId;

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public String getParkName() {
        return ParkName;
    }

    public void setParkName(String parkName) {
        this.ParkName = parkName;
    }

    private String ParkName;
    private double arivalTime;
    private double depatureTime;
    private double charge;
    private double lat;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    private double lng;

    public double getCharge() {
        return charge;
    }

    public void setCharge(double charge) {
        this.charge = charge;
    }

    public Booking() {
    }

    public String getDriverEmail() {
        return DriverEmail;
    }

    public void setDriverEmail(String driverEmail) {
        DriverEmail = driverEmail;
    }

    public String getVehicleNum() {
        return vehicleNum;
    }

    public void setVehicleNum(String vehicleNum) {
        this.vehicleNum = vehicleNum;
    }

    public String getDriverName() {
        return DriverName;
    }

    public void setDriverName(String driverName) {
        DriverName = driverName;
    }

    public String getDriverId() {
        return DriverId;
    }

    public void setDriverId(String driverId) {
        DriverId = driverId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getSlotNum() {
        return slotNum;
    }

    public void setSlotNum(String slotNum) {
        this.slotNum = slotNum;
    }

    public String getSlotId() {
        return slotId;
    }

    public void setSlotId(String slotId) {
        this.slotId = slotId;
    }

    public String getKeeperId() {
        return keeperId;
    }

    public void setKeeperId(String keeperId) {
        this.keeperId = keeperId;
    }

    public double getArivalTime() {
        return arivalTime;
    }

    public void setArivalTime(double arivalTime) {
        this.arivalTime = arivalTime;
    }

    public double getDepatureTime() {
        return depatureTime;
    }

    public void setDepatureTime(double depatureTime) {
        this.depatureTime = depatureTime;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "vehicleNum='" + vehicleNum + '\'' +
                ", DriverName='" + DriverName + '\'' +
                ", DriverId='" + DriverId + '\'' +
                ", date='" + date + '\'' +
                ", vehicleType='" + vehicleType + '\'' +
                ", slotNum='" + slotNum + '\'' +
                ", slotId='" + slotId + '\'' +
                ", keeperId='" + keeperId + '\'' +
                ", DriverEmail='" + DriverEmail + '\'' +
                ", parkName='" + ParkName + '\'' +
                ", arivalTime=" + arivalTime +
                ", depatureTime=" + depatureTime +
                ", charge=" + charge +
                '}';
    }
}

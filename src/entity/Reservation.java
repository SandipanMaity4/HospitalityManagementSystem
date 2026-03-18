package entity;

import java.util.Date;

public class Reservation {
    private int reservationId;
    private int guestId;
    private int roomId;
    private Date checkInDate;
    private Date checkOutDate;
    private double totalCost;

    public Reservation() {}

    public Reservation(int reservationId, int guestId, int roomId, Date checkInDate, Date checkOutDate, double totalCost) {
        this.reservationId = reservationId;
        this.guestId = guestId;
        this.roomId = roomId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalCost = totalCost;
    }

    public Reservation(int guestId, int roomId, Date checkInDate, Date checkOutDate, double totalCost) {
        this.guestId = guestId;
        this.roomId = roomId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalCost = totalCost;
    }

    public int getReservationId() { return reservationId; }
    public void setReservationId(int reservationId) { this.reservationId = reservationId; }

    public int getGuestId() { return guestId; }
    public void setGuestId(int guestId) { this.guestId = guestId; }

    public int getRoomId() { return roomId; }
    public void setRoomId(int roomId) { this.roomId = roomId; }

    public Date getCheckInDate() { return checkInDate; }
    public void setCheckInDate(Date checkInDate) { this.checkInDate = checkInDate; }

    public Date getCheckOutDate() { return checkOutDate; }
    public void setCheckOutDate(Date checkOutDate) { this.checkOutDate = checkOutDate; }

    public double getTotalCost() { return totalCost; }
    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }

    @Override
    public String toString() {
        return "Reservation{reservationId=" + reservationId + ", guestId=" + guestId +
               ", roomId=" + roomId + ", totalCost=" + totalCost + "}";
    }
}

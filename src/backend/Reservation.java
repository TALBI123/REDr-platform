package backend;

public class Reservation {
    private String reservationId;
    private String customerName;
    private String reservationDate;

    public Reservation(String reservationId, String customerName, String reservationDate) {
        this.reservationId = reservationId;
        this.customerName = customerName;
        this.reservationDate = reservationDate;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getReservationDate() {
        return reservationDate;
    }
}

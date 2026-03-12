package model;

import java.time.LocalDate;

/**
 * Represents a Rental record in the Car Rental System.
 * Maps to the 'rentals' table in the database.
 */
public class Rental {

    private int       rentalId;
    private int       carId;
    private int       customerId;
    private LocalDate rentDate;
    private int       days;
    private double    totalPrice;
    private LocalDate returnDate; // null until the car is returned

    // ── Constructors ─────────────────────────────────────────────────────────────

    /** Used when creating a new rental (ID assigned by DB). */
    public Rental(int carId, int customerId, LocalDate rentDate, int days, double totalPrice) {
        this.carId      = carId;
        this.customerId = customerId;
        this.rentDate   = rentDate;
        this.days       = days;
        this.totalPrice = totalPrice;
        this.returnDate = null;
    }

    /** Used when fetching an existing rental from the database. */
    public Rental(int rentalId, int carId, int customerId,
                  LocalDate rentDate, int days, double totalPrice, LocalDate returnDate) {
        this.rentalId   = rentalId;
        this.carId      = carId;
        this.customerId = customerId;
        this.rentDate   = rentDate;
        this.days       = days;
        this.totalPrice = totalPrice;
        this.returnDate = returnDate;
    }

    // ── Getters ──────────────────────────────────────────────────────────────────

    public int       getRentalId()   { return rentalId; }
    public int       getCarId()      { return carId; }
    public int       getCustomerId() { return customerId; }
    public LocalDate getRentDate()   { return rentDate; }
    public int       getDays()       { return days; }
    public double    getTotalPrice() { return totalPrice; }
    public LocalDate getReturnDate() { return returnDate; }

    // ── Setters ──────────────────────────────────────────────────────────────────

    public void setRentalId(int rentalId)         { this.rentalId   = rentalId; }
    public void setCarId(int carId)               { this.carId      = carId; }
    public void setCustomerId(int customerId)     { this.customerId = customerId; }
    public void setRentDate(LocalDate rentDate)   { this.rentDate   = rentDate; }
    public void setDays(int days)                 { this.days       = days; }
    public void setTotalPrice(double totalPrice)  { this.totalPrice = totalPrice; }
    public void setReturnDate(LocalDate returnDate){ this.returnDate = returnDate; }

    // ── Utility ──────────────────────────────────────────────────────────────────

    @Override
    public String toString() {
        return String.format(
            "Rental[id=%d, carId=%d, customerId=%d, from=%s, days=%d, total=$%.2f, returned=%s]",
            rentalId, carId, customerId, rentDate, days, totalPrice,
            returnDate != null ? returnDate.toString() : "NOT RETURNED"
        );
    }
}

package model;

/**
 * Represents a Car entity in the Car Rental System.
 * Maps to the 'cars' table in the database.
 */
public class Car {

    private int carId;
    private String brand;
    private String model;
    private double rentPerDay;
    private String available; // "YES" or "NO"

    // ── Constructors ────────────────────────────────────────────────────────────

    /** Used when creating a new car (no ID yet – assigned by DB). */
    public Car(String brand, String model, double rentPerDay, String available) {
        this.brand       = brand;
        this.model       = model;
        this.rentPerDay  = rentPerDay;
        this.available   = available;
    }

    /** Used when fetching an existing car from the database. */
    public Car(int carId, String brand, String model, double rentPerDay, String available) {
        this.carId       = carId;
        this.brand       = brand;
        this.model       = model;
        this.rentPerDay  = rentPerDay;
        this.available   = available;
    }

    // ── Getters ─────────────────────────────────────────────────────────────────

    public int    getCarId()      { return carId; }
    public String getBrand()      { return brand; }
    public String getModel()      { return model; }
    public double getRentPerDay() { return rentPerDay; }
    public String getAvailable()  { return available; }

    // ── Setters ─────────────────────────────────────────────────────────────────

    public void setCarId(int carId)           { this.carId      = carId; }
    public void setBrand(String brand)         { this.brand      = brand; }
    public void setModel(String model)         { this.model      = model; }
    public void setRentPerDay(double r)        { this.rentPerDay = r; }
    public void setAvailable(String available) { this.available  = available; }

    // ── Utility ──────────────────────────────────────────────────────────────────

    @Override
    public String toString() {
        return String.format("Car[id=%d, %s %s, $%.2f/day, available=%s]",
                carId, brand, model, rentPerDay, available);
    }
}

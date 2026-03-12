package model;

/**
 * Represents a Customer entity in the Car Rental System.
 * Maps to the 'customers' table in the database.
 */
public class Customer {

    private int    customerId;
    private String name;
    private String phone;

    // ── Constructors ─────────────────────────────────────────────────────────────

    /** Used when registering a new customer (ID assigned by DB). */
    public Customer(String name, String phone) {
        this.name  = name;
        this.phone = phone;
    }

    /** Used when fetching an existing customer from the database. */
    public Customer(int customerId, String name, String phone) {
        this.customerId = customerId;
        this.name       = name;
        this.phone      = phone;
    }

    // ── Getters ──────────────────────────────────────────────────────────────────

    public int    getCustomerId() { return customerId; }
    public String getName()       { return name; }
    public String getPhone()      { return phone; }

    // ── Setters ──────────────────────────────────────────────────────────────────

    public void setCustomerId(int id)    { this.customerId = id; }
    public void setName(String name)     { this.name       = name; }
    public void setPhone(String phone)   { this.phone      = phone; }

    // ── Utility ──────────────────────────────────────────────────────────────────

    @Override
    public String toString() {
        return String.format("Customer[id=%d, name=%s, phone=%s]",
                customerId, name, phone);
    }
}

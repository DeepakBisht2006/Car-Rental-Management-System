package service;

import database.DBConnection;
import model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Service layer for all Customer-related database operations.
 */
public class CustomerService {

    // ── Register Customer ────────────────────────────────────────────────────────

    /**
     * Inserts a new customer record into the database.
     *
     * @param customer  Customer to persist (ID assigned by DB)
     * @return          true if the record was created successfully
     */
    public boolean registerCustomer(Customer customer) {
        String sql = "INSERT INTO customers (name, phone) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, customer.getName());
            ps.setString(2, customer.getPhone());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[CustomerService.registerCustomer] Error: " + e.getMessage());
            return false;
        }
    }

    // ── Get All Customers ────────────────────────────────────────────────────────

    /**
     * Retrieves all customers from the database.
     *
     * @return List of Customer objects
     */
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                customers.add(mapRow(rs));
            }

        } catch (SQLException e) {
            System.err.println("[CustomerService.getAllCustomers] Error: " + e.getMessage());
        }
        return customers;
    }

    // ── Get Customer By ID ───────────────────────────────────────────────────────

    /**
     * Fetches a single customer by primary key.
     *
     * @param customerId  Customer's primary key
     * @return            Customer object, or null if not found
     */
    public Customer getCustomerById(int customerId) {
        String sql = "SELECT * FROM customers WHERE customer_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("[CustomerService.getCustomerById] Error: " + e.getMessage());
        }
        return null;
    }

    // ── Private helper ───────────────────────────────────────────────────────────

    private Customer mapRow(ResultSet rs) throws SQLException {
        return new Customer(
            rs.getInt("customer_id"),
            rs.getString("name"),
            rs.getString("phone")
        );
    }
}

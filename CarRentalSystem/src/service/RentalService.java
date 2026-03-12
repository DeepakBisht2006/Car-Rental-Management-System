package service;

import database.DBConnection;
import model.Rental;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Service layer for all Rental-related database operations.
 * Handles renting and returning cars, plus rental history queries.
 */
public class RentalService {

    // ── Create Rental ────────────────────────────────────────────────────────────

    /**
     * Inserts a new rental record.
     * NOTE: CarService.updateAvailability() should also be called to mark the car as 'NO'.
     *
     * @param rental  Rental object (rentalId assigned by DB)
     * @return        true if record was created successfully
     */
    public boolean createRental(Rental rental) {
        String sql = "INSERT INTO rentals (car_id, customer_id, rent_date, days, total_price, return_date) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, rental.getCarId());
            ps.setInt(2, rental.getCustomerId());
            ps.setDate(3, Date.valueOf(rental.getRentDate()));
            ps.setInt(4, rental.getDays());
            ps.setDouble(5, rental.getTotalPrice());
            // return_date is NULL when first rented
            ps.setNull(6, Types.DATE);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[RentalService.createRental] Error: " + e.getMessage());
            return false;
        }
    }

    // ── Return Car ───────────────────────────────────────────────────────────────

    /**
     * Sets the return_date on an existing rental to today.
     * NOTE: CarService.updateAvailability() should also be called to re-mark the car as 'YES'.
     *
     * @param rentalId  ID of the rental to close
     * @return          true if update succeeded
     */
    public boolean returnCar(int rentalId) {
        String sql = "UPDATE rentals SET return_date = ? WHERE rental_id = ? AND return_date IS NULL";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(LocalDate.now()));
            ps.setInt(2, rentalId);

            int rows = ps.executeUpdate();
            if (rows == 0) {
                System.err.println("[RentalService.returnCar] Rental not found or already returned.");
            }
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("[RentalService.returnCar] Error: " + e.getMessage());
            return false;
        }
    }

    // ── Get Rental By ID ─────────────────────────────────────────────────────────

    /**
     * Fetches a single rental record by primary key.
     *
     * @param rentalId  Rental's primary key
     * @return          Rental object, or null if not found
     */
    public Rental getRentalById(int rentalId) {
        String sql = "SELECT * FROM rentals WHERE rental_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, rentalId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("[RentalService.getRentalById] Error: " + e.getMessage());
        }
        return null;
    }

    // ── Get All Rentals ──────────────────────────────────────────────────────────

    /**
     * Returns every rental record – useful for a full history view.
     *
     * @return List of all Rental objects
     */
    public List<Rental> getAllRentals() {
        List<Rental> rentals = new ArrayList<>();
        String sql = "SELECT * FROM rentals ORDER BY rental_id DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                rentals.add(mapRow(rs));
            }

        } catch (SQLException e) {
            System.err.println("[RentalService.getAllRentals] Error: " + e.getMessage());
        }
        return rentals;
    }

    // ── Private helper ───────────────────────────────────────────────────────────

    private Rental mapRow(ResultSet rs) throws SQLException {
        Date sqlReturnDate = rs.getDate("return_date");
        LocalDate returnDate = (sqlReturnDate != null) ? sqlReturnDate.toLocalDate() : null;

        return new Rental(
            rs.getInt("rental_id"),
            rs.getInt("car_id"),
            rs.getInt("customer_id"),
            rs.getDate("rent_date").toLocalDate(),
            rs.getInt("days"),
            rs.getDouble("total_price"),
            returnDate
        );
    }
}

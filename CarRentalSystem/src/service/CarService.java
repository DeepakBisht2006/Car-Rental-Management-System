package service;

import database.DBConnection;
import model.Car;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Service layer for all Car-related database operations.
 * Uses PreparedStatements to prevent SQL injection.
 */
public class CarService {

    // ── Add Car ──────────────────────────────────────────────────────────────────

    /**
     * Inserts a new car record into the database.
     *
     * @param car  Car object to persist (carId is assigned by the DB)
     * @return     true if insertion succeeded, false otherwise
     */
    public boolean addCar(Car car) {
        String sql = "INSERT INTO cars (brand, model, rent_per_day, available) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, car.getBrand());
            ps.setString(2, car.getModel());
            ps.setDouble(3, car.getRentPerDay());
            ps.setString(4, car.getAvailable());

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("[CarService.addCar] Error: " + e.getMessage());
            return false;
        }
    }

    // ── Get All Available Cars ───────────────────────────────────────────────────

    /**
     * Retrieves all cars marked as available ('YES').
     *
     * @return List of available Car objects
     */
    public List<Car> getAvailableCars() {
        List<Car> cars = new ArrayList<>();
        String sql = "SELECT * FROM cars WHERE available = 'YES'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                cars.add(mapRow(rs));
            }

        } catch (SQLException e) {
            System.err.println("[CarService.getAvailableCars] Error: " + e.getMessage());
        }
        return cars;
    }

    // ── Get All Cars ─────────────────────────────────────────────────────────────

    /**
     * Retrieves every car record from the database (available or not).
     *
     * @return List of all Car objects
     */
    public List<Car> getAllCars() {
        List<Car> cars = new ArrayList<>();
        String sql = "SELECT * FROM cars";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                cars.add(mapRow(rs));
            }

        } catch (SQLException e) {
            System.err.println("[CarService.getAllCars] Error: " + e.getMessage());
        }
        return cars;
    }

    // ── Update Car Availability ──────────────────────────────────────────────────

    /**
     * Updates the availability flag of a specific car.
     *
     * @param carId      ID of the car to update
     * @param available  "YES" or "NO"
     * @return           true if update succeeded
     */
    public boolean updateAvailability(int carId, String available) {
        String sql = "UPDATE cars SET available = ? WHERE car_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, available);
            ps.setInt(2, carId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("[CarService.updateAvailability] Error: " + e.getMessage());
            return false;
        }
    }

    // ── Get Car By ID ────────────────────────────────────────────────────────────

    /**
     * Fetches a single car by its primary key.
     *
     * @param carId  Car's primary key
     * @return       Car object, or null if not found
     */
    public Car getCarById(int carId) {
        String sql = "SELECT * FROM cars WHERE car_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, carId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("[CarService.getCarById] Error: " + e.getMessage());
        }
        return null;
    }

    // ── Private helper ───────────────────────────────────────────────────────────

    /** Maps the current ResultSet row to a Car object. */
    private Car mapRow(ResultSet rs) throws SQLException {
        return new Car(
            rs.getInt("car_id"),
            rs.getString("brand"),
            rs.getString("model"),
            rs.getDouble("rent_per_day"),
            rs.getString("available")
        );
    }
}

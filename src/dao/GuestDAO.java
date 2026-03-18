package dao;

import db.DatabaseConnector;
import entity.Guest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GuestDAO {

    // ─── CREATE ───────────────────────────────────────────────────────────────
    public boolean addGuest(Guest guest) {
        String sql = "INSERT INTO guests (name, email, phone) VALUES (?, ?, ?)";
        try (Connection con = DatabaseConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, guest.getName());
            ps.setString(2, guest.getEmail());
            ps.setString(3, guest.getPhone());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("addGuest error: " + e.getMessage());
            return false;
        }
    }

    // ─── READ (all) ────────────────────────────────────────────────────────────
    public List<Guest> getAllGuests() {
        List<Guest> guests = new ArrayList<>();
        String sql = "SELECT * FROM guests";
        try (Connection con = DatabaseConnector.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) guests.add(mapRow(rs));

        } catch (SQLException e) {
            System.err.println("getAllGuests error: " + e.getMessage());
        }
        return guests;
    }

    // ─── READ (by id) ──────────────────────────────────────────────────────────
    public Guest getGuestById(int guestId) {
        String sql = "SELECT * FROM guests WHERE guest_id = ?";
        try (Connection con = DatabaseConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, guestId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);

        } catch (SQLException e) {
            System.err.println("getGuestById error: " + e.getMessage());
        }
        return null;
    }

    // ─── UPDATE ───────────────────────────────────────────────────────────────
    public boolean updateGuest(Guest guest) {
        String sql = "UPDATE guests SET name=?, email=?, phone=? WHERE guest_id=?";
        try (Connection con = DatabaseConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, guest.getName());
            ps.setString(2, guest.getEmail());
            ps.setString(3, guest.getPhone());
            ps.setInt(4, guest.getGuestId());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("updateGuest error: " + e.getMessage());
            return false;
        }
    }

    // ─── DELETE ───────────────────────────────────────────────────────────────
    public boolean deleteGuest(int guestId) {
        String sql = "DELETE FROM guests WHERE guest_id = ?";
        try (Connection con = DatabaseConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, guestId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("deleteGuest error: " + e.getMessage());
            return false;
        }
    }

    // ─── Helper ───────────────────────────────────────────────────────────────
    private Guest mapRow(ResultSet rs) throws SQLException {
        return new Guest(
            rs.getInt("guest_id"),
            rs.getString("name"),
            rs.getString("email"),
            rs.getString("phone")
        );
    }
}

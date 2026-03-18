package dao;

import db.DatabaseConnector;
import entity.Reservation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {


    public boolean addReservation(Reservation res) {
        String sql = "INSERT INTO reservations (guest_id, room_id, check_in_date, check_out_date, total_cost) " +
                     "VALUES (?,?,?,?,?)";
        try (Connection con = DatabaseConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, res.getGuestId());
            ps.setInt(2, res.getRoomId());
            ps.setDate(3, new java.sql.Date(res.getCheckInDate().getTime()));
            ps.setDate(4, new java.sql.Date(res.getCheckOutDate().getTime()));
            ps.setDouble(5, res.getTotalCost());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("addReservation error: " + e.getMessage());
            return false;
        }
    }


    public List<Reservation> getAllReservations() {
        List<Reservation> list = new ArrayList<>();
        String sql = "SELECT * FROM reservations";
        try (Connection con = DatabaseConnector.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) list.add(mapRow(rs));

        } catch (SQLException e) {
            System.err.println("getAllReservations error: " + e.getMessage());
        }
        return list;
    }


    public List<Reservation> getReservationsByGuest(int guestId) {
        List<Reservation> list = new ArrayList<>();
        String sql = "SELECT * FROM reservations WHERE guest_id = ?";
        try (Connection con = DatabaseConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, guestId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));

        } catch (SQLException e) {
            System.err.println("getReservationsByGuest error: " + e.getMessage());
        }
        return list;
    }

    public boolean updateReservation(Reservation res) {
        String sql = "UPDATE reservations SET guest_id=?, room_id=?, check_in_date=?, check_out_date=?, total_cost=? " +
                     "WHERE reservation_id=?";
        try (Connection con = DatabaseConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, res.getGuestId());
            ps.setInt(2, res.getRoomId());
            ps.setDate(3, new java.sql.Date(res.getCheckInDate().getTime()));
            ps.setDate(4, new java.sql.Date(res.getCheckOutDate().getTime()));
            ps.setDouble(5, res.getTotalCost());
            ps.setInt(6, res.getReservationId());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("updateReservation error: " + e.getMessage());
            return false;
        }
    }


    public boolean deleteReservation(int reservationId) {
        String sql = "DELETE FROM reservations WHERE reservation_id = ?";
        try (Connection con = DatabaseConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, reservationId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("deleteReservation error: " + e.getMessage());
            return false;
        }
    }


    private Reservation mapRow(ResultSet rs) throws SQLException {
        return new Reservation(
            rs.getInt("reservation_id"),
            rs.getInt("guest_id"),
            rs.getInt("room_id"),
            rs.getDate("check_in_date"),
            rs.getDate("check_out_date"),
            rs.getDouble("total_cost")
        );
    }
}

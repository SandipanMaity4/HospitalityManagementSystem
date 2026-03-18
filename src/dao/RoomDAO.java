package dao;

import db.DatabaseConnector;
import entity.Room;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {
    public boolean addRoom(Room room) {
        String sql = "INSERT INTO rooms (hotel_id, room_number, type, price, status) VALUES (?,?,?,?,?)";
        try (Connection con = DatabaseConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, room.getHotelId());
            ps.setString(2, room.getRoomNumber());
            ps.setString(3, room.getType());
            ps.setDouble(4, room.getPrice());
            ps.setString(5, room.getStatus());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("addRoom error: " + e.getMessage());
            return false;
        }
    }

    public List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms";
        try (Connection con = DatabaseConnector.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) rooms.add(mapRow(rs));

        } catch (SQLException e) {
            System.err.println("getAllRooms error: " + e.getMessage());
        }
        return rooms;
    }


    public List<Room> getRoomsByHotel(int hotelId) {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms WHERE hotel_id = ?";
        try (Connection con = DatabaseConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, hotelId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) rooms.add(mapRow(rs));

        } catch (SQLException e) {
            System.err.println("getRoomsByHotel error: " + e.getMessage());
        }
        return rooms;
    }

   
    public List<Room> getAvailableRooms(Date checkIn, Date checkOut) {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms WHERE status = 'Available' AND room_id NOT IN " +
                     "(SELECT room_id FROM reservations WHERE check_in_date < ? AND check_out_date > ?)";
        try (Connection con = DatabaseConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, checkOut);
            ps.setDate(2, checkIn);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) rooms.add(mapRow(rs));

        } catch (SQLException e) {
            System.err.println("getAvailableRooms error: " + e.getMessage());
        }
        return rooms;
    }


    public boolean updateRoom(Room room) {
        String sql = "UPDATE rooms SET hotel_id=?, room_number=?, type=?, price=?, status=? WHERE room_id=?";
        try (Connection con = DatabaseConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, room.getHotelId());
            ps.setString(2, room.getRoomNumber());
            ps.setString(3, room.getType());
            ps.setDouble(4, room.getPrice());
            ps.setString(5, room.getStatus());
            ps.setInt(6, room.getRoomId());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("updateRoom error: " + e.getMessage());
            return false;
        }
    }


    public boolean deleteRoom(int roomId) {
        String sql = "DELETE FROM rooms WHERE room_id = ?";
        try (Connection con = DatabaseConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, roomId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("deleteRoom error: " + e.getMessage());
            return false;
        }
    }


    public boolean updateRoomStatus(int roomId, String status) {
        String sql = "UPDATE rooms SET status=? WHERE room_id=?";
        try (Connection con = DatabaseConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, roomId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("updateRoomStatus error: " + e.getMessage());
            return false;
        }
    }


    private Room mapRow(ResultSet rs) throws SQLException {
        return new Room(
            rs.getInt("room_id"),
            rs.getInt("hotel_id"),
            rs.getString("room_number"),
            rs.getString("type"),
            rs.getDouble("price"),
            rs.getString("status")
        );
    }
}

package dao;

import db.DatabaseConnector;
import entity.Hotel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HotelDAO {

   
    public boolean addHotel(Hotel hotel) {
        String sql = "INSERT INTO hotels (name, location, amenities) VALUES (?, ?, ?)";
        try (Connection con = DatabaseConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, hotel.getName());
            ps.setString(2, hotel.getLocation());
            ps.setString(3, hotel.getAmenities());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("addHotel error: " + e.getMessage());
            return false;
        }
    }

   
    public List<Hotel> getAllHotels() {
        List<Hotel> hotels = new ArrayList<>();
        String sql = "SELECT * FROM hotels";
        try (Connection con = DatabaseConnector.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                hotels.add(mapRow(rs));
            }
        } catch (SQLException e) {
            System.err.println("getAllHotels error: " + e.getMessage());
        }
        return hotels;
    }

   
    public Hotel getHotelById(int hotelId) {
        String sql = "SELECT * FROM hotels WHERE hotel_id = ?";
        try (Connection con = DatabaseConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, hotelId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);

        } catch (SQLException e) {
            System.err.println("getHotelById error: " + e.getMessage());
        }
        return null;
    }


    public boolean updateHotel(Hotel hotel) {
        String sql = "UPDATE hotels SET name=?, location=?, amenities=? WHERE hotel_id=?";
        try (Connection con = DatabaseConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, hotel.getName());
            ps.setString(2, hotel.getLocation());
            ps.setString(3, hotel.getAmenities());
            ps.setInt(4, hotel.getHotelId());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("updateHotel error: " + e.getMessage());
            return false;
        }
    }


    public boolean deleteHotel(int hotelId) {
        String sql = "DELETE FROM hotels WHERE hotel_id = ?";
        try (Connection con = DatabaseConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, hotelId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("deleteHotel error: " + e.getMessage());
            return false;
        }
    }

    private Hotel mapRow(ResultSet rs) throws SQLException {
        return new Hotel(
            rs.getInt("hotel_id"),
            rs.getString("name"),
            rs.getString("location"),
            rs.getString("amenities")
        );
    }
}

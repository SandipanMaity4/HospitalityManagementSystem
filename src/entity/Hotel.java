package entity;

public class Hotel {
    private int hotelId;
    private String name;
    private String location;
    private String amenities;

    public Hotel() {}

    public Hotel(int hotelId, String name, String location, String amenities) {
        this.hotelId = hotelId;
        this.name = name;
        this.location = location;
        this.amenities = amenities;
    }

    public Hotel(String name, String location, String amenities) {
        this.name = name;
        this.location = location;
        this.amenities = amenities;
    }

    public int getHotelId() { return hotelId; }
    public void setHotelId(int hotelId) { this.hotelId = hotelId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getAmenities() { return amenities; }
    public void setAmenities(String amenities) { this.amenities = amenities; }

    @Override
    public String toString() {
        return "Hotel{hotelId=" + hotelId + ", name='" + name + "', location='" + location + "'}";
    }
}

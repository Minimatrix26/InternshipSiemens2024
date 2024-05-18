import Model.Hotel;
import Model.Room;
import Utils.Utils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HotelDb {
    private Connection connection;

    public HotelDb() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:mydb.db");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTables() {
        try (PreparedStatement statement = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS hotels (id INTEGER, name TEXT, latitude REAL, longitude REAL)"
        )) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (PreparedStatement statement = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS rooms (roomNumber INTEGER, type INTEGER, price INTEGER, isAvailable BOOLEAN, hotelId INTEGER)"
        )) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertHotel(Hotel hotel) {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO hotels (id, name, latitude, longitude) VALUES (?, ?, ?, ?)"
        )) {
            statement.setLong(1, hotel.getId());
            statement.setString(2, hotel.getName());
            statement.setDouble(3, hotel.getLatitude());
            statement.setDouble(4, hotel.getLongitude());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertRoom(Room room, long hotelId) {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO rooms (roomNumber, type, price, isAvailable, hotelId) VALUES (?, ?, ?, ?, ?)"
        )) {
            statement.setInt(1, room.getRoomNumber());
            statement.setInt(2, room.getType());
            statement.setInt(3, room.getPrice());
            statement.setBoolean(4, room.isAvailable());
            statement.setLong(5, hotelId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void readJSONFile() {
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader("E:\\info Java IntelliJ Idea\\InternshipSiemens2024\\src\\file.json")) {
            // Parse JSON file
            Object obj = parser.parse(reader);
            JSONArray hotels = (JSONArray) obj;

            // Iterate over hotels array
            for (Object hotelObj : hotels) {
                JSONObject hotel = (JSONObject) hotelObj;

                // Extract hotel details
                long id = (long) hotel.get("id");
                String name = (String) hotel.get("name");
                double latitude = (double) hotel.get("latitude");
                double longitude = (double) hotel.get("longitude");

                Hotel hotelEntity = new Hotel();
                hotelEntity.setId(id);
                hotelEntity.setName(name);
                hotelEntity.setLatitude(latitude);
                hotelEntity.setLongitude(longitude);

                insertHotel(hotelEntity);

                // Extract room details
                JSONArray rooms = (JSONArray) hotel.get("rooms");
                for (Object roomObj : rooms) {
                    JSONObject room = (JSONObject) roomObj;

                    int roomNumber = ((Long) room.get("roomNumber")).intValue();
                    int type = ((Long) room.get("type")).intValue();
                    int price = ((Long) room.get("price")).intValue();
                    boolean isAvailable = (boolean) room.get("isAvailable");

                    Room roomEntity = new Room();
                    roomEntity.setRoomNumber(roomNumber);
                    roomEntity.setType(type);
                    roomEntity.setPrice(price);
                    roomEntity.setAvailable(isAvailable);

                    insertRoom(roomEntity, id);
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public List<Hotel> getAllHotels() {
        List<Hotel> hotels = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM hotels")) {
            ResultSet hotelResultSet = statement.executeQuery();
            while (hotelResultSet.next()) {
                long hotelId = hotelResultSet.getLong("id");
                String name = hotelResultSet.getString("name");
                double latitude = hotelResultSet.getDouble("latitude");
                double longitude = hotelResultSet.getDouble("longitude");

                Hotel hotel = new Hotel();
                hotel.setId(hotelId);
                hotel.setName(name);
                hotel.setLatitude(latitude);
                hotel.setLongitude(longitude);

                List<Room> rooms = new ArrayList<>();
                try (PreparedStatement roomStatement = connection.prepareStatement("SELECT * FROM rooms WHERE hotelId = ?")) {
                    roomStatement.setLong(1, hotelId);
                    ResultSet roomResultSet = roomStatement.executeQuery();
                    while (roomResultSet.next()) {
                        int roomNumber = roomResultSet.getInt("roomNumber");
                        int type = roomResultSet.getInt("type");
                        int price = roomResultSet.getInt("price");
                        boolean isAvailable = roomResultSet.getBoolean("isAvailable");

                        Room room = new Room();
                        room.setRoomNumber(roomNumber);
                        room.setType(type);
                        room.setPrice(price);
                        room.setAvailable(isAvailable);

                        rooms.add(room);
                    }
                }
                hotel.setRooms(rooms);

                hotels.add(hotel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hotels;
    }

    public ArrayList<Hotel> findNearbyHotels(double userLatitude, double userLongitude, double radius){
        ArrayList<Hotel> nearbyHotels = (ArrayList<Hotel>) getAllHotels();

        for (var hotel : nearbyHotels){
            double distance = Utils.calculateDistance(userLatitude, userLongitude, hotel.getLatitude(), hotel.getLongitude());

            if (distance <= radius)
                nearbyHotels.add(hotel);
        }

        return nearbyHotels;
    }
    
}

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.IOException;

public class ReadJsonFile {

    public static void main(String[] args) {
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

                System.out.println("Hotel ID: " + id);
                System.out.println("Name: " + name);
                System.out.println("Latitude: " + latitude);
                System.out.println("Longitude: " + longitude);

                // Extract room details
                JSONArray rooms = (JSONArray) hotel.get("rooms");
                for (Object roomObj : rooms) {
                    JSONObject room = (JSONObject) roomObj;

                    int roomNumber = ((Long) room.get("roomNumber")).intValue();
                    int type = ((Long) room.get("type")).intValue();
                    int price = ((Long) room.get("price")).intValue();
                    boolean isAvailable = (boolean) room.get("isAvailable");

                    System.out.println("Room Number: " + roomNumber);
                    System.out.println("Type: " + type);
                    System.out.println("Price: " + price);
                    System.out.println("Is Available: " + isAvailable);
                    System.out.println();
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

}

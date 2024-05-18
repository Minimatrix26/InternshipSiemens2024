import Model.Hotel;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        HotelDb hotelDb = new HotelDb();
        //hotelDb.createTables();
        //hotelDb.readJSONFile();
        //hotelDb.closeConnection();

        List<Hotel> hotelArrayList = new ArrayList<>();
        hotelArrayList = hotelDb.getAllHotels();

        for (var hotel: hotelArrayList){
            System.out.println(hotel.toString());
        }
        // debug comment
        System.out.println("Gata programul");
    }
}

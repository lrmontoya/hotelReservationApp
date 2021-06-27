import api.AdminResource;
import api.HotelResource;
import model.*;

import java.util.*;
import java.util.regex.Pattern;
import model.RoomType;


public class AdminMenu {

    private Scanner emailScan = new Scanner(System.in);
    private Collection<IRoom> roomsCollection = new HashSet<>();
    private List<IRoom> roomList = new ArrayList<>();
    private Collection<Customer> allCustomers = new HashSet<>();
    private Collection<Reservation> reservationCollection = new HashSet<>();
    public AdminResource instanceAdminResource =   AdminResource.getInstance();
    public HotelResource instanceHotelResource =   HotelResource.getInstance();

    boolean keepRunning = true;
    String optionAdminMenu;

    public void createMenu() {
        try (Scanner adminMenuScanner = new Scanner(System.in)) {

            while (keepRunning) {
                try {

                    System.out.println("\n----------------------------------------");
                    System.out.println("1. See all Customers");
                    System.out.println("2. See all Rooms");
                    System.out.println("3. See all Reservations");
                    System.out.println("4. Add a Room");
                    System.out.println("5. Back to Main Menu");
                    //System.out.println("6. Back to Main Menu");
                    System.out.println("----------------------------------------\n");
                    System.out.println("Please select a number from the menu option");


                    optionAdminMenu = adminMenuScanner.next();

                    switch (optionAdminMenu) {

                        case "1":
                            allCustomers = instanceAdminResource.getAllCustomers();
                            if (allCustomers.isEmpty()) {
                                System.out.println("There are no customers in the database");
                                continue;
                            } else {

                                for (Customer cust : allCustomers) {
                                    System.out.println(cust.toString());
                                }

                            }
                            return;
                        //keepRunning = true;

                        case "2":
                            roomsCollection.clear();
                            roomsCollection = instanceAdminResource.getAllRooms();
                            if (roomsCollection.isEmpty()) {
                                System.out.println("There are no rooms in the database");
                                continue;
                            } else {

                                for (IRoom room : roomsCollection) {
                                    System.out.println(room.toString());
                                }

                            }
                            keepRunning = true;

                        case "3":
                            instanceAdminResource.displayAllReservations();
                            continue;

                        case "4":
                            addRoom();
                            continue;
                        case "5":
                            return;
                        //case "6":
                        //    return;
                        default:
                            System.out.println("Please enter a number between 1 and 5\n");
                    }

                } catch (Exception ex) {
                    System.out.println("Error - Invalid Input\n");
                }
            }
        }

    }


    private List<IRoom> addRoom(){

        boolean keepGoing = true;
        String yesNo = null;

        //String roomRegex = "([0-9]+)";
        //Pattern pattern = Pattern.compile(roomRegex);

        while (keepGoing) {

            roomList.clear();
            IRoom room;
            //Double roomPrice = getRoomPrice();
            String roomNumber = getRoomNumber();
            Double roomPrice = getRoomPrice();
            RoomType roomType = getRoomType();
            room = new Room(roomNumber, roomPrice, roomType);
            roomList.add(room);

            try (Scanner scanAddRooms = new Scanner(System.in)) {
                System.out.println("Would you like to add another room y/n\n");
                yesNo = scanAddRooms.nextLine();

                if (yesNo.equals("y")) {
                    keepRunning = true;
                }
                if (yesNo.equals("n")) {

                    keepRunning = false;

                } else {

                    throw new IllegalArgumentException("Error, Invalid format, please enter numbers only y or n\n");

                }

            }catch(Exception ex){
                System.out.println("Error - Invalid Input\n");

            }
        }


        return roomList;

    }


    private RoomType getRoomType() {


        RoomType roomType = RoomType.SINGLE;
        String userInput = null;
        String roomTypeRegex = "([1-2])";
        Pattern pattern = Pattern.compile(roomTypeRegex);

        while (keepRunning){

            try (Scanner roomTypeScan = new Scanner(System.in)) {

                System.out.println("Enter room type: 1 for single bed, 2 for double\n");
                userInput =  roomTypeScan.nextLine();

                if (!pattern.matcher(userInput).matches()) {

                    throw new IllegalArgumentException("Error, Invalid format, please enter 1 or 2\n");

                }

            }catch(Exception ex) {
                System.out.println("Error - Invalid Input\n");
                continue;
            }
            keepRunning = false;

        }

        if (userInput.equals(1)){
            roomType = RoomType.SINGLE;
        }
        else {
            roomType = RoomType.DOUBLE;
        }

        return roomType;

    }





    public String getRoomNumber(){

        boolean keepRunning = true;
        String roomID = null;
        String roomRegex = "([0-9]+)";
        Pattern pattern = Pattern.compile(roomRegex);

        //try (Scanner roomScan = new Scanner(System.in)) {

        roomID =  validateInputNumber();

        //}
        return roomID;
    }

    private Double getRoomPrice(){

        boolean keepRunning = true;
        Double roomPriceParsed = 0.0;
        String roomPrice = null;
        String regexPrice = "([0-9]+)\\.?([0-9]+)?";
        Pattern pattern = Pattern.compile(regexPrice);

        try (Scanner priceScan = new Scanner(System.in)) {

            while (keepRunning){
                try {

                    System.out.println("Enter the price per night: ");
                    roomPriceParsed = priceScan.nextDouble();
                    if (!pattern.matcher(roomPrice).matches()){

                        throw new IllegalArgumentException("tirando exception");

                    }

                    //roomPriceParsed =  Double.parseDouble(roomPrice);

                }catch(Exception ex) {

                    System.out.println("Error - Invalid Input. Please enter a number.Example: 100 or 100.4");
                    //System.out.println("roomPrice---"+ roomPrice +"----");
                    continue;
                }
                keepRunning = false;

            }

        }
        return roomPriceParsed;
    }


    private String validateInputNumber(){

        boolean validInput = false;
        boolean roomIdExists = false;
        String userInput = null;
        String roomRegex = "([0-9]+)";
        Pattern pattern = Pattern.compile(roomRegex);

        try (Scanner numberScan = new Scanner(System.in)) {

            while (keepRunning){
                try {

                    System.out.println("What room number would you like to create");
                    userInput =  numberScan.nextLine();

                    if (!pattern.matcher(userInput).matches()) {

                        throw new IllegalArgumentException("Error, Invalid format, please enter numbers only");

                    }

                    roomIdExists =  verifyUniqueId(userInput);

                    if (roomIdExists) {

                        throw new IllegalArgumentException("Error, the room ID you entered already exists. Enter a unique room ID");

                    }

                }catch(Exception ex) {
                    System.out.println("Error - Invalid Input");
                    continue;
                }

                numberScan.close();
                keepRunning = false;

            }


        }

        return userInput;

    }


    public boolean verifyUniqueId( String userInput){


        if (instanceHotelResource.getRoom(userInput) != null) { return true; }
        return false;

    }

} // closes class AdminMenu




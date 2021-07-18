import api.AdminResource;
import api.HotelResource;
import com.sun.tools.javac.Main;
import model.*;

import java.util.*;
import java.util.regex.Pattern;
import model.RoomType;


public class AdminMenu {


    private AdminMenu adminMenu = null;
    private static Scanner emailScan = new Scanner(System.in);
    private static Collection<IRoom> roomsCollection = new HashSet<>();
    private static List<IRoom> roomList = new ArrayList<>();
    private static Collection<Customer> allCustomers = new HashSet<>();
    private static Collection<Reservation> reservationCollection = new HashSet<>();
    private static AdminResource instanceAdminResource =   AdminResource.getInstance();
    private static HotelResource instanceHotelResource =   HotelResource.getInstance();

    //variable to hold the single instance of the class
    private static AdminMenu adminMenuInstance = null;

    //private constructor to make sure there is 1 instance of the class at all times.
    private AdminMenu(){};

    // method to return the single instance of the AdminMenu class
    public static AdminMenu getInstance(){
        if (adminMenuInstance == null){
            adminMenuInstance = new AdminMenu();
        }
        return adminMenuInstance;
    }


    public static void launchAdminMenu() {

        boolean keepRunning = true;
        String optionAdminMenu;

        while (keepRunning) {
                try {

                    Scanner adminMenuScanner = new Scanner(System.in);
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
                                    System.out.println(cust);
                                }

                            }
                            break;
                        //keepRunning = true;

                        case "2":
                            //roomsCollection.clear();
                            roomsCollection = instanceAdminResource.getAllRooms();
                            if (roomsCollection.isEmpty()) {
                                System.out.println("There are no rooms in the database");
                                continue;
                            } else {

                                for (IRoom room : roomsCollection) {
                                    System.out.println(room);
                                }

                            }
                            break;

                        case "3":
                            instanceAdminResource.displayAllReservations();
                            break;

                        case "4":

                            instanceAdminResource.addRoom(addRoom());
                            break;
                        case "5":
                            //MainMenu.launchMainMenu();
                            MainMenu mainMenuInstance = MainMenu.getInstance();
                            mainMenuInstance.launchMainMenu();
                            keepRunning = false;
                            break;
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




    private static List<IRoom> addRoom(){

        boolean keepGoing = true;
        String yesNo = null;
        roomList.clear();

        while (keepGoing) {

             String ynRegex = "[ynYN]";
             Pattern pattern = Pattern.compile(ynRegex);
            IRoom room;
        //Double roomPrice = getRoomPrice();
            String roomNumber = getRoomNumber();
            Double roomPrice = getRoomPrice();
            RoomType roomType = getRoomType();
            room = new Room(roomNumber, roomPrice, roomType);
        //System.out.println("Adding room: " + room.toString()); // debug line
            roomList.add(room);

        Scanner yesNoScan = new Scanner(System.in);

        boolean askAgain = true;

        while (askAgain) {
            try {

                System.out.println("Would you like to add another room y/n");
                yesNo = yesNoScan.nextLine();

                if (!pattern.matcher(yesNo).matches()) {
                    //System.out.println();
                    askAgain = true;
                    throw new IllegalArgumentException();

                } else if (yesNo.equals("y")) {
                    askAgain = false;
                    keepGoing = true;
                } else if (yesNo.equals("n")) {
                    askAgain = false;
                    keepGoing = false;
                }

            } catch (Exception ex) {
                System.out.println("Error, Invalid format, please enter only y or n");
                keepGoing = true;
            }
          }
        }

        return roomList;

    }


    private static RoomType getRoomType() {


        boolean keepRunning = true;
        RoomType roomType = RoomType.SINGLE;
        String userInput = null;
        String roomTypeRegex = "([1-2])";
        Pattern pattern = Pattern.compile(roomTypeRegex);

        while (keepRunning){

            try  {

                Scanner roomTypeScan = new Scanner(System.in);
                System.out.println("Enter room type: 1 for single bed, 2 for double");
                userInput =  roomTypeScan.nextLine();

                if (!pattern.matcher(userInput).matches()) {

                    throw new IllegalArgumentException("Error, Invalid format, please enter 1 or 2");

                }

            }catch(Exception ex) {
                System.out.println("Error - Invalid Input");
                continue;
            }
            keepRunning = false;

        }

        if (userInput.equals("1")){
            roomType = RoomType.SINGLE;
        }
        else {
            roomType = RoomType.DOUBLE;
        }

        return roomType;

    }





    public static String getRoomNumber(){

        //boolean keepRunning = true;
        String roomID = null;
        //String roomRegex = "([0-9]+)";
        //Pattern pattern = Pattern.compile(roomRegex);

        roomID =  validateInputNumber();

        return roomID;
    }

    private static Double getRoomPrice(){

        boolean keepRunning = true;
        Double roomPriceParsed = 0.0;
        String roomPrice = null;
        String regexPrice = "([0-9]+)\\.?([0-9]+)?";
        Pattern pattern = Pattern.compile(regexPrice);

        while (keepRunning){
                try {

                    Scanner priceScan = new Scanner(System.in);
                    System.out.println("Enter the price per night: ");
                    roomPriceParsed = priceScan.nextDouble();
                    priceScan.nextLine(); // adding this line to consume the \n character
                    if (!pattern.matcher(roomPriceParsed.toString()).matches()){

                        throw new IllegalArgumentException("Error Invalid input");

                    }

                    //roomPriceParsed =  Double.parseDouble(roomPrice);

                }catch(Exception ex) {

                    System.out.println("Error - Invalid Input. Please enter a number.Example: 100 or 100.4");
                    //System.out.println("roomPrice---"+ roomPrice +"----");
                    continue;
                }
                keepRunning = false;

            }

        return roomPriceParsed;
    }


    private static String validateInputNumber(){

        boolean validInput = false;
        boolean keepRunning = true;
        boolean roomIdExists = false;
        boolean roomListContainsRoom = false;
        String userInput = null;
        String roomRegex = "([0-9]+)";
        Pattern pattern = Pattern.compile(roomRegex);



            while (keepRunning){
                try {

                    Scanner numberScan = new Scanner(System.in);
                    System.out.println("What room number would you like to create");
                    userInput =  numberScan.nextLine();

                    if (!pattern.matcher(userInput).matches()) {

                        System.out.println("Error, Invalid format, please enter numbers only");
                        throw new IllegalArgumentException();

                    }

                    roomIdExists =  verifyUniqueId(userInput);

                    //check if room exists in DB already
                    if (roomIdExists) {

                        System.out.println("Error, the room ID you entered already exists. Enter a unique room ID");
                        throw new IllegalArgumentException();

                    }
                    //validate if room exists in the list of room tha is being created at the moment

                    for (IRoom currentRoom : roomList){

                        if (currentRoom.getRoomNumber().compareTo(userInput) == 0) {

                            System.out.println("Error, the room ID you entered already exists. Enter a unique room ID");
                            throw new IllegalArgumentException();

                        }

                    }

                }catch(Exception ex) {
                    //System.out.println("Error - Invalid Input");
                    continue;
                }

                keepRunning = false;


        }

        return userInput;

    }


    public static boolean verifyUniqueId(String userInput){


        if (instanceHotelResource.getRoom(userInput) == null) { return false; }
        return true;

    }

} // closes class AdminMenu




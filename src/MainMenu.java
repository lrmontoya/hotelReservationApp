import api.AdminResource;
import api.HotelResource;
import model.Customer;
import model.IRoom;
import model.Reservation;
import model.Room;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;


public class MainMenu {


    private Scanner emailScan = new Scanner(System.in);
    private Collection<IRoom> availableRooms = new HashSet<>();
    private Collection<Customer> allCustomers = new HashSet<>();
    private Collection<Reservation> reservationCollection = new HashSet<>();
    public AdminResource instanceAdminResource =   AdminResource.getInstance();
    public HotelResource instanceHotelResource =   HotelResource.getInstance();

    public void processMainMenu() {

        boolean keepRunning = true;
        String optionMainMenu;


        try (Scanner mainMenuScanner = new Scanner(System.in)) {

            while (keepRunning) {
                try {

                    System.out.println("----------------------------------------");
                    System.out.println("1. Find and reserve a room");
                    System.out.println("2. See my reservations");
                    System.out.println("3. Create an account");
                    System.out.println("4. Admin");
                    System.out.println("5. Exit");
                    System.out.println("----------------------------------------");
                    System.out.println("Please select a number from the menu option");

                    optionMainMenu = mainMenuScanner.next();

                    switch (optionMainMenu) {

                        case "1":
                            findReserveRoom();
                            keepRunning = true;

                        case "2":

                            keepRunning = false;
                        case "3":

                            keepRunning = false;
                        case "4":
                            AdminMenu instanceAdminMenu = new AdminMenu();
                            instanceAdminMenu.createMenu();
                            keepRunning = true;
                        case "5":
                            return;
                        default:
                            System.out.println("Please enter a number between 1 and 5");
                    }

                    }catch(Exception ex)
                {
                    System.out.println("Error - Invalid Input");
                }
            }
        }
    }


    public void findReserveRoom(){

        String yesNo;
        boolean keepRunning = true;

        Date checkInDate = getDate("CheckIn");
        Date checkOutDate = getDate("CheckOut");
        availableRooms.clear();
        availableRooms = instanceHotelResource.findARoom(checkInDate, checkOutDate);

        if (availableRooms.isEmpty()){
            System.out.println("There are no rooms available within those dates.\n");
            return;
        }

        for (IRoom room: availableRooms){
            room.toString();
            }

        try (Scanner yesNoScan =  new Scanner(System.in)) {
            while (keepRunning) {

                try {
                    System.out.println("Would you like to book a room? y/n\n");

                        yesNo = yesNoScan.nextLine();

                        if (yesNo.equals("y")) {
                            //call bookRom function
                            callBookRoom(checkInDate, checkOutDate);
                            keepRunning = false;
                        }
                        if (yesNo.equals("n")) {
                            //go back to main menu
                            keepRunning = false;
                            return ;
                        } else {

                            throw new IllegalArgumentException("Error, Invalid format, please enter numbers only y or n\n");
                        }
                }catch (Exception ex){
                    System.out.println("Error - Invalid Input\n");
                }
            }

        }

        return;

    }


    public void callBookRoom(Date checkIn, Date checkOut){

        String yesNo;
        String roomNumber;
        IRoom room = null;
        boolean keepRunning = true;
        String email = null;

        try (Scanner yesNoScan = new Scanner(System.in)) {

            while (keepRunning) {

                try {
                    System.out.println("Do you have an account with us? y/n \n");
                    yesNo = yesNoScan.nextLine();
                    if (yesNo.equals("y")) {
                        email = getEmail();

                        Customer customer = instanceHotelResource.getCustomer(email);
                        // if the email is not in the DB the function in Customer Service throws an exception

                        roomNumber = getRoomNumber();
                        room = instanceHotelResource.getRoom(roomNumber);


                    }
                    instanceHotelResource.bookARoom(email, room, checkIn, checkOut);

                }catch(Exception ex){

                    System.out.println("Error - Invalid input");

                }
               }
            }
        }

     public String getRoomNumber(){

        boolean keepRunning = true;
        String roomID = null;
        String roomRegex = "([0-9]+)";
         Pattern pattern = Pattern.compile(roomRegex);

        try (Scanner roomScan = new Scanner(System.in)) {


            while (keepRunning){
                try {

                    System.out.println("What room number would you like to reserve\n");
                    roomID =  roomScan.nextLine();

                    if (!pattern.matcher(roomID).matches()) {

                        throw new IllegalArgumentException("Error, Invalid room number format\n");

                    }

                }catch(Exception ex) {
                    System.out.println("Error - Invalid Input\n");
                    continue;
                }
                keepRunning = false;

            }

        }
        return roomID;
     }


    public String getEmail( ){

         String email = null;
         String emailRegex = "^(.+)@(.+).(.+)$";
         Pattern pattern = Pattern.compile(emailRegex);
         boolean keepRunning = true;

         try (Scanner emailScan = new Scanner(System.in)) {

             while (keepRunning) {
                 try {
                     System.out.println("Enter Email format: name@domain.com\n");
                     email = emailScan.nextLine();
                     if (!pattern.matcher(email).matches()) {

                         throw new IllegalArgumentException("Error, Invalid email format\n");

                    }
                    else {keepRunning = false;}
                 } catch (Exception ex) {
                     System.out.println("Error - Invalid Input \n");
                 }
             }

             return email;

         }

    }

    public Date getDate(String dateType){


        Date validatedDate = null;
        boolean keepRunning = true;
        DateFormat dateFormat = new SimpleDateFormat("mm/dd/yyyy");
        String date;


        System.out.println("Enter " + dateType +  "date mm/dd/yyyy example 02/01/2020");


             while (keepRunning) {

                 try (Scanner scanDate = new Scanner(System.in)) {

                date = scanDate.nextLine();
                validatedDate = dateFormat.parse(date);;
                }catch (Exception ex){
                     System.out.println("Error -  Invalid Input\n");
                     continue;
                }
                keepRunning = false;
        }
        return validatedDate;
    }


}


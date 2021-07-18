import api.AdminResource;
import api.HotelResource;
import model.Customer;
import model.IRoom;
import model.Reservation;
import model.Room;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;


public class MainMenu {


    private static MainMenu instanceMainMenu = null;
    //private static Scanner emailScan = new Scanner(System.in);
    private static Collection<IRoom> availableRooms = new HashSet<>();
    private static Collection<Customer> allCustomers = new HashSet<>();
    private static Collection<Reservation> reservationCollection = new HashSet<>();
    private static AdminResource instanceAdminResource =   AdminResource.getInstance();
    private static HotelResource instanceHotelResource =   HotelResource.getInstance();


    static Scanner newMenuScanner = new Scanner(System.in);

    //private variable to hold the single instance of the class
    private static MainMenu mainMenuInstance = null;

    private MainMenu(){}

    public static MainMenu getInstance(){

        if (mainMenuInstance == null){
            mainMenuInstance = new MainMenu();
        }
        return mainMenuInstance;
    }

    public static void launchMainMenu() {

        boolean keepRunning = true;
        String optionMainMenu =  null;


        while (keepRunning) {

            //System.out.println("keep running " + keepRunning);

                try {

                    System.out.println("Welcome to the Hotel Reservation Application\n");
                    System.out.println("----------------------------------------");
                    System.out.println("1. Find and reserve a room");
                    System.out.println("2. See my reservations");
                    System.out.println("3. Create an account");
                    System.out.println("4. Admin");
                    System.out.println("5. Exit");
                    System.out.println("----------------------------------------");
                    System.out.println("Please select a number for the menu option");

                    optionMainMenu = getSwitchOption();
                    System.out.println("***** OptionMainMenu ******" + optionMainMenu );
                    // mainMenuScanner.nextLine();

                    //System.out.println("option main menu = " + optionMainMenu); //debug

                    switch (optionMainMenu) {

                        case "1":
                            findReserveRoom();
                            break;

                        case "2":
                            seeReservations();
                            break;
                        case "3":
                            //create account
                            String email = getEmail();
                            String firstName = getFirstOrLastName("First Name (first Letter must be upper case)");
                            String lastName = getFirstOrLastName("Last Name (first Letter must be upper case)");
                            instanceHotelResource.createACustomer(email, firstName, lastName);
                            break;
                        case "4":
                            //AdminMenu.launchAdminMenu();
                            AdminMenu adminMenuInstance = AdminMenu.getInstance();
                            adminMenuInstance.launchAdminMenu();
                            break;
                        case "5":
                            keepRunning = false;
                            break;
                        default:
                            System.out.println("Please enter a number between 1 and 5");
                    }

                } catch (Exception ex) {
                    System.out.println(ex.toString() + "\n");
                }

                //System.out.println("keep running antes de salir del while " + keepRunning); //debug line
            }

        return;
    }


    public static void seeReservations()
    {

        reservationCollection.clear();
        String customersEmail = getEmail();

        reservationCollection = instanceHotelResource.getCustomersReservations(customersEmail);
        if (reservationCollection.isEmpty()) {
            System.out.println("There are no reservations for this customer\n");
            return;
        }
        else {

            for (Reservation currentReservation : reservationCollection ){

                System.out.println(currentReservation);
            }
        }
        return;

    }

    public static String getSwitchOption()
        {

            String switchOption;

         //   try {
          //      System.in.available();
          //  } catch (IOException e) {
         //       e.printStackTrace();
         //       return null;
          //  }

           Scanner mainMenuScanner = new Scanner(System.in);

            //mainMenuScanner.reset();

            switchOption = newMenuScanner.nextLine();

            return switchOption;

        }

    public static void findReserveRoom(){

        String yesNo;
        boolean keepRunning = true;

        Date checkInDate = getDate("CheckIn");
        Date checkOutDate = getDate("CheckOut");
        //availableRooms.clear(); // commented since I don't think I need this
        availableRooms = instanceHotelResource.findARoom(checkInDate, checkOutDate);

        if (availableRooms.isEmpty()){
            System.out.println("There are no rooms available within those dates.\n");
            return;
        }

        for (IRoom room: availableRooms){

            System.out.println(room);

            }

        Scanner yesNoScan =  new Scanner(System.in);

        while (keepRunning) {

                try {
                    System.out.println("Would you like to book a room? y/n");

                        yesNo = yesNoScan.nextLine();

                        if (yesNo.equals("y")) {
                            //call bookRom function
                            callBookRoom(checkInDate, checkOutDate);
                            return;
                            //keepRunning = false;
                        }
                        if (yesNo.equals("n")) {
                            //go back to main menu
                            return;
                            //keepRunning = false;
                            //return ;
                        } else {

                            throw new IllegalArgumentException("Error, Invalid format, please enter numbers only \"y\" or \"n\"\n");
                        }
                }catch (Exception ex){
                    System.out.println("Error - Invalid Input\n");
                }
            }



        //MainMenu.launchMainMenu();
        return;

    }


    public static void callBookRoom(Date checkIn, Date checkOut){

        String yesNo;
        String roomNumber;
        IRoom room = null;
        boolean keepRunning = true;
        String email = null;
        String ynRegex = "[ynYN]";
        Pattern pattern = Pattern.compile(ynRegex);

            while (keepRunning) {

                try {

                    Scanner yesNoScan = new Scanner(System.in);
                    System.out.println("Do you have an account with us? y/n");
                    yesNo = yesNoScan.nextLine();


                    if (!pattern.matcher(yesNo).matches()){
                        throw new IllegalArgumentException();
                    }
                    else if (yesNo.equals("y") || yesNo.equals("Y")) {

                        email = getEmail();

                        // falta validar q el email ya exista en la DB, y quitar la validacion en el hotel resource.

                        Customer customer = instanceHotelResource.getCustomer(email);
                        if (customer.equals(null)) {
                            System.out.println("There are no customers in the DB with this email, please try again");
                            throw new IllegalArgumentException();
                        }
                        // if the email is not in the DB the function in Customer Service throws an exception

                        roomNumber = getRoomNumber();
                        room = instanceHotelResource.getRoom(roomNumber);

                        instanceHotelResource.bookARoom(email, room, checkIn, checkOut);
                        keepRunning = false;

                    }
                    else if (yesNo.equals("n")) {

                        /// ac[a probablemente se muestre un error para que se cree la cuenta y se hace el return o se le hace prompt para crear la cuenta.
                            keepRunning = false;
                        }

                    }catch(Exception ex){  System.out.println("Error - Invalid input"); }

            }
    }

     public static String getRoomNumber(){

        boolean keepRunning = true;
        String roomID = null;
        String roomRegex = "([0-9]+)";
        Pattern pattern = Pattern.compile(roomRegex);


            while (keepRunning){

                try {

                        Scanner roomScan = new Scanner(System.in);
                        System.out.println("What room number would you like to reserve");
                        roomID =  roomScan.nextLine();

                        if (!pattern.matcher(roomID).matches()) {

                            System.out.println("Error, Invalid room number format");
                            throw new IllegalArgumentException();

                        }
                        else {
                            keepRunning = false;
                        }

                    }catch(Exception ex) {  System.out.println("Error - Invalid Input"); }
            }

         return roomID;
        }




    public static String getEmail(){

         String email = null;
         String emailRegex = "^(.+)@(.+).(.+)$";
         Pattern pattern = Pattern.compile(emailRegex);
         boolean keepRunning = true;

         while (keepRunning) {
                 try {

                     Scanner emailScan = new Scanner(System.in);
                     System.out.println("Enter Email format: name@domain.com");
                     email = emailScan.nextLine();
                     if (!pattern.matcher(email).matches()) {

                         throw new IllegalArgumentException();

                    }
                    else {keepRunning = false;}
                 } catch (Exception ex) {
                     System.out.println("Error, Invalid email format");

                 }
             }

             return email;

         }

    public static String getFirstOrLastName(String nameType) {

        String firstOrLastName = null;
        String nameRegex = "[A-Z]+([ '-]*[a-zA-Z]+)*";
        Pattern pattern = Pattern.compile(nameRegex);

        boolean keepRunning = true;

            while (keepRunning) {

                try {
                    Scanner nameScan = new Scanner(System.in);
                    System.out.println(nameType);
                    firstOrLastName = nameScan.nextLine();
                    if (!pattern.matcher(firstOrLastName).matches()) {

                        throw new IllegalArgumentException( );

                    }
                    else {keepRunning = false;}
                } catch (Exception ex) {
                    System.out.println("Error - Invalid Input \n");
                }
            }

            return firstOrLastName;

        }


    public static Date getDate(String dateType){


        Date validatedDate = null;
        boolean keepRunning = true;
        DateFormat dateFormat = new SimpleDateFormat("mm/dd/yyyy");
        String date;


        while (keepRunning) {


            System.out.println("Enter " + dateType +  "date mm/dd/yyyy example 02/01/2020");

            try  {

                Scanner scanDate = new Scanner(System.in);
                date = scanDate.nextLine();
                validatedDate = dateFormat.parse(date);

            } catch (Exception ex){
                     System.out.println("Error -  Invalid Input");
                     continue;
                }
                keepRunning = false;
        }
        return validatedDate;
    }


}


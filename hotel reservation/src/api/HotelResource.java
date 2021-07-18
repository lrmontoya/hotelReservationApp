package api;

import model.Customer;
import model.IRoom;
import model.Reservation;
import service.CustomerService;
import service.ReservationService;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

public class HotelResource {

    public static HotelResource instanceHotelResource = null;
    public static ReservationService reservationService = ReservationService.getInstance();
    public static CustomerService customerService = CustomerService.getInstance();

    private HotelResource(){    }

    public Customer getCustomer(String email){

           return customerService.getCustomer(email);

    }

    public static HotelResource getInstance(){
        if (instanceHotelResource ==  null) {
            instanceHotelResource = new HotelResource();
        }
        return instanceHotelResource;
    }

    public void createACustomer (String email, String firstName, String lastName){
        //addCustomer(String email, String firstName, String lastName)

        customerService.addCustomer(email, firstName, lastName);
        return;
    }


    //************************
    public IRoom getRoom(String roomNumber){
        return reservationService.getARoom(roomNumber);
    }

    public Reservation bookARoom(String customerEmail, IRoom room, Date checkInDate, Date checkOutDate){

        Customer customer =  null;
        try {
            customer = getCustomer(customerEmail);
        } catch (IllegalArgumentException ex)
        {
            System.out.println("The email address provided is not in the database");
            System.out.println(ex.getLocalizedMessage());
        }
        return reservationService.reserveARoom(customer, room, checkInDate, checkOutDate);
    }

    public Collection<Reservation> getCustomersReservations (String customersEmail){
        Collection<Reservation> reservationsPerCust= new HashSet<>();
        //Customer customer =  null;
        Customer customer = getCustomer(customersEmail);
        try {

            if (customer.equals(null)) {

                throw new NullPointerException();
            }
        } catch (Exception ex)
        {
            System.out.println("The email address provided is not in the database.");

            return reservationsPerCust; // if cust does not exist, return null.
        }
        reservationsPerCust =  reservationService.getCustomersReservation(customer);
        return reservationsPerCust;
    }

    public Collection<IRoom> findARoom(Date checkIn, Date checkOut ){

        return reservationService.findRooms(checkIn, checkOut);
    }
}

package service;

import model.Customer;
import model.IRoom;
import model.Reservation;
import model.Room;

import java.util.*;

public class ReservationService
{

        List<Room> rooms = new ArrayList<>();
        private Map<String, IRoom> mapOfRooms = new HashMap<String, IRoom>();
        private Collection<IRoom> roomHashSet = new HashSet<>();
        private Collection<Room> reservedRooms = new HashSet<>();
        private Collection<IRoom> availableRooms = new HashSet<>();
        private Collection<Reservation> reservationsHashSet = new HashSet<>();
        private Collection<Reservation> reservationsPerCustomer = new HashSet<>();
        private Iterator<IRoom> roomIterator = roomHashSet.iterator();

        private static ReservationService reservationServiceInstance = null;

        private ReservationService(){    }

    public static ReservationService getInstance(){
            if (reservationServiceInstance ==  null) {
                reservationServiceInstance = new ReservationService();
            }
            return reservationServiceInstance;
    }

    public void addRoom(IRoom room)
    {
        //String roomNumber, Double roomPrice, RoomType roomType

           // if (mapOfRooms.containsKey(room.getRoomNumber())){ // I'm already validating this in the AdminMenu class

          //      System.out.println("Error: This room number is already included in the database");
           //     return;

           // }
            roomHashSet.add((Room) room);
            mapOfRooms.put(room.getRoomNumber(), room);


        /*
        *  try {
        if (mapOfCustomers.containsKey(email))
        {

            System.out.println("Error: This email is already included in the database");
            return;
        }

            client = new Customer (firstName, lastName, email);
            mapOfCustomers.put(email, client);
            customerHashSet.add(client);
        }catch(IllegalArgumentException ex){
            System.out.println(ex.getLocalizedMessage());
        }
        * */

    }

    public IRoom getARoom(String roomId)
    {

        return mapOfRooms.get(roomId);

    }

    public Reservation reserveARoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate){

            //Customer customer, IRoom room, Date checkInDate, Date checkOutDate

            Reservation newReservation = new Reservation (customer, room, checkInDate, checkOutDate);
            reservedRooms.add((Room) room);
            reservationsHashSet.add(newReservation);
            return newReservation;

    }

    public Collection<IRoom>  findRooms(Date checkInDate, Date checkOutDate){

            availableRooms.clear();

    for (IRoom room : roomHashSet){

        if (!reservationsHashSet.contains(room)) // if there's no any reservation for the current room yet, we add it to the list of available rooms

        {
            availableRooms.add(room);
            continue;
        }

        for (Reservation reservation : reservationsHashSet)
        {
            if(reservation.getRoom().equals(room))
            {

                 if (checkOutDate.before(reservation.getCheckOutDate()) && checkOutDate.after(reservation.getCheckInDate()))
                 { continue; }
                 if (checkInDate.before(reservation.getCheckOutDate()) && checkInDate.after(reservation.getCheckInDate()))
                 { continue; }
                 availableRooms.add(room);
            }
        }

      }

        return availableRooms;

    }

    public Collection<Reservation> getCustomersReservation(Customer customer){

        reservationsPerCustomer.clear();

        for (Reservation reservation : reservationsHashSet){

            if (reservation.getCustomer().equals(customer)){

                reservationsPerCustomer.add(reservation);
            }
        }

        return reservationsPerCustomer;

    }

    public void printAllReservations(){

        if (reservationsHashSet.isEmpty())
        {  System.out.println("There are no reservations in the database");

        }
        else {
            for (Reservation reservation : reservationsHashSet) {

                reservation.toString();
            }
        }
        return;
    }

    //create collections to store and retrieve a reservation

    //provide a static reference!!!!


    // Added this function after creating the class to meet the requirements of "AdminResource" class
    public Collection<IRoom> getAllRooms (){
            return roomHashSet;
    }
}

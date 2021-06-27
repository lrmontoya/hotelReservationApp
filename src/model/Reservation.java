package model;

import java.util.Date;

public class Reservation {

    private Customer customer;
    private IRoom room;
    private Date checkInDate;
    private Date checkOutDate;

    public Reservation(Customer customer, IRoom room, Date checkInDate, Date checkOutDate)
    {
        this.customer = customer;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public void setCustomer(Customer customer)
    {
        this.customer = customer;
    }

    public void setRoom(IRoom room)
    {
        this.room = room;
    }

    public void setCheckInDate(Date checkInDate)
    {
        this.checkInDate = checkInDate;
    }

    public void setCheckOutDate(Date checkOut)
    {
        this.checkOutDate = checkOutDate;
    }

    public Customer getCustomer(){
        return customer;
    }
    public IRoom getRoom(){
        return room;
    }
    public Date getCheckInDate(){
        return checkInDate;
    }
    public Date getCheckOutDate(){
        return checkOutDate;
    }

    @Override
    public String toString(){
        return "Customer: " + customer + " room: " + room + " CheckIn date: " + checkInDate + " Check Out date" + checkOutDate;
    }
}

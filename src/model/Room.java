package model;

public class Room implements IRoom {

    protected String roomNumber;
    protected double roomPrice;
    protected RoomType roomType;


    public Room(String roomNumber, Double roomPrice, RoomType roomType )
    {
        this.roomNumber = roomNumber;
        this.roomPrice = roomPrice;
        this.roomType = roomType;

    }

    public void setRoomNumber(String roomNumber){
        this.roomNumber = roomNumber;
    }

    public void setRoomPrice(Double roomPrice){
        this.roomPrice = roomPrice;
    }

    public void setRoomType(RoomType roomType){
        this.roomType = roomType;
    }

    @Override
    public String getRoomNumber() {
        return roomNumber;
    }

    @Override
    public Double getRoomPrice() {
        return roomPrice;
    }

    @Override
    public RoomType getRoomType() {
        return roomType;
    }

    @Override
    public boolean isFree() {
        return false;
    }

    @Override
    public String toString()
    {
        String roomType = null;

        if (this.getRoomType().equals(RoomType.SINGLE)) {

            roomType = " Single bed ";
        }
        else
        {
            roomType = " Double bed ";

        }
        return "Room number: " + roomNumber + roomType + "Room price: $" + roomPrice;

    }
}

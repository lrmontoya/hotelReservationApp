package model;

public class FreeRoom extends Room {

    public FreeRoom(String roomNumber, RoomType roomType )
    {
        super(roomNumber, 0.0, roomType);

    }

    @Override
    public String toString(){

        return "room number= " + roomNumber + " price= " + roomPrice + " Room type= " + roomType;
    }
}

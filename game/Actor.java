import java.util.ArrayList;
import java.util.List;
/**
 * Write a description of class Actor here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public abstract class Actor
{
    private static List<Actor> actors = new ArrayList<Actor>();

    private Room currentRoom;

    /**
     * Constructor for objects of class Actor
     */
    public Actor(Room initialRoom)
    {
        currentRoom = initialRoom;
        actors.add(this);
    }

    /**
     * Return the current room
     * @return currentRoom
     */
    public Room getRoom()
    {
        return currentRoom;
    }

    /**
     * Define the room in the currentRoom.
     * @param set the currentRoom to room defined in the parameter.
     */
    public void setRoom(Room NewRoom)
    {
        currentRoom = NewRoom;
    }
    
    public List<Actor> getActors()
    {
        List<Actor> newActors = new ArrayList<Actor>(actors);
        return newActors;
    }
}

import java.util.Random;
import java.util.ArrayList;
/**
 * Write a description of class Monster here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Thief extends Carrier
{
    // instance variables - replace the example below with your own
    private Random rnd;
    private ArrayList<String> directions;
    private String pointingDirection;

    /**
     * Constructor for objects of class Thief
     */
    public Thief(Room initialRoom)
    {
        // initialise instance variables
        super(initialRoom);
        rnd = new Random();
        directions = new ArrayList<String>();
        directions.add("north");
        directions.add("south");
        directions.add("east");
        directions.add("west");
        pointingDirection = "north";
    }

    /** 
     * Return index, a random number 1-6.
     * @return index, integer 1-6
     */
    public int throwDice()
    {
        int index = rnd.nextInt(6) + 1;
        return index;
    }
    
    /** 
     * If the throwDice method returns the number 3, the Thief will enter into a new random room, 
     * unless its a teleport room which the Thief will return right back to its original room after entering.
     * If the throwDice method returns the number 3, the Thief will also change his direction one of the 4 directions.
     * If the throwDice method returns greater than or equal to the number 5, the Thief picks up one object in the room and adds it to his bag.
     * If the throwDice method returns the number 1, the Thief takes an object at random out of his bag and drops it in the current room.
     * If the throwDice method returns the numbers 2 or 4, nothing happens.
     */
    public void act()
    {
        int x = throwDice();
        if(x == 3){
            Room previousRoom = getRoom();
            ArrayList<Room> newRoom = new ArrayList<Room>();
            for(String exit : directions) {
                if(getRoom().getExit(exit) != null){
                    newRoom.add(getRoom().getExit(exit));
                }
            }
            setRoom(newRoom.get(rnd.nextInt(newRoom.size())));
            if(getRoom().getExitString().equals("Exits:")){
                setRoom(previousRoom);
            }
            pointingDirection = directions.get(rnd.nextInt(4));
        }
        if(x >= 5){
            ArrayList<Item> roomItems = getRoom().getNewList();
            int z = roomItems.size();
            if(z > 0){
                int y = rnd.nextInt(z);
                takeItem(roomItems.get(y).getName());
            }
        }
        if(x == 1){
            ArrayList<Item> bagItems = getNewBag();
            int w = bagItems.size();
            if(w > 0){
                int c = rnd.nextInt(w);
                dropItem(bagItems.get(c).getName());
            }
        }

    }
    
    /** 
     * return a description that the Thief is here and which direction he will be pointing.
     * @return descrition of the Thief
     */
    public String toString()
    {
        return "There is a Thief here. The Thief is pointing " + pointingDirection;
    }
}
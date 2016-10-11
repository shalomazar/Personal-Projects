import java.util.Random;
import java.util.ArrayList;
/**
 * Write a description of class Monster here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Professor extends Actor
{
    // instance variables - replace the example below with your own
    private Random rnd;
    private ArrayList<String> directions;
    private String pointingDirection;

    /**
     * Constructor for objects of class Professor
     */
    public Professor(Room initialRoom)
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
     * If the throwDice method returns the number 6, the Professor will enter into a new random room, 
     * unless its a teleport room which the Professor will return right back to its original room after entering.
     * If the throwDice method returns the number 6, the Professor will also change his direction one of the 4 directions.
     * If the throwDice method returns the number less than or equal to 5, nothing happens.
     */
    public void act()
    {
        int x = throwDice();
        if(x == 6){
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
    }
    
    /** 
     * return a description that the proffesor is here and which direction he will be pointing.
     * @return descrition of the professor
     */
    public String toString()
    {
        return "There is a professor here. The professor is pointing " + pointingDirection;
    }
}
import java.util.HashSet;
import java.util.ArrayList;
/**
 * Write a description of class Player here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Player extends Carrier
{

    /**
     * Constructor for objects of class Player
     */
    public Player(Room initialRoom)
    {
        super(initialRoom);
    }

    /**
     *return the description of the room.
     *@return longDescription of the currentRoom 
     */
    public String look(){
        String lookDescription = getRoom().getLongDescription();
        for(Actor actor : getActors()){
            if(actor.getRoom() == this.getRoom() && actor != this){
                lookDescription += "\n" + actor.toString();
            }
        }
        return lookDescription;
    }

    /**
     * Try to in to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     * @return description of the result of the method
     */
    public String goRoom(String direction) {
        // Try to leave current room.
        Room nextRoom = getRoom().getExit(direction);
        if (nextRoom == null) {
            return "There is no door!";
        }
        else {
            setRoom(nextRoom);
            return look();
        }
    }

    

    /**
     * Return the Items in the bag and their descriptions
     * @return String item + itemDescription
     */
    public String getInventory()
    {
        if(getBag().isEmpty()) {
            return "You are not carrying anything";
        }
        String returnString = "Items in the bag:";
        for(Item eachItem : getBag()) {
            returnString += "\n" + eachItem.getName() + ", " + eachItem.getDescription();
            setTotalWeight(eachItem.getWeight() + getTotalWeight());
        }
        returnString += "\n" + "total weight:" + getTotalWeight();
        return returnString;
    }
   
    /** 
     * Return the result of a cookie being or not being eaten. If there are no items, return that you are carrying nothing.
     * @param String, cookie we are trying to eat
     * @return the result of this method
     */
    
    public String eatMagicCookie(String cookie) 
    {
        if(searchBag(cookie) == null){
            return "You are not carrying a " + cookie + ".";
        }
        else{
            if(searchBag(cookie).getName().equals("cookie")){
                removeFromBag(searchBag(cookie));
                setWeightLimit(getWeightLimit() + 50);
                return "You ate the cookie";
            }
            else{
                return "You can't eat that";
            }
        }

    }

}

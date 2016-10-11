import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
/**
 * Class Room - a room in an adventure game.
 *
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 *
 * A "Room" represents one location in the scenery of the game.  It is 
 * connected to other rooms via exits.  For each existing exit, the room 
 * stores a reference to the neighboring room.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2011.08.08
 */

public class Room 
{
    private String description;
    private HashMap<String, Room> exits;        // stores exits of this room.
    private HashSet<Item> items;

    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard".
     * @param description The room's description.
     */
    public Room(String description) 
    {
        this.description = description;
        exits = new HashMap<String, Room>();
        items = new HashSet<Item>();
    }

    /**
     * Define an exit from this room.
     * @param direction The direction of the exit.
     * @param neighbor  The room to which the exit leads.
     */
    public void setExit(String direction, Room neighbor) 
    {
        exits.put(direction, neighbor);
    }

    /**
     * @return The short description of the room
     * (the one that was defined in the constructor).
     */
    public String getShortDescription()
    {
        return description;
    }

    /**
     * Return a description of the room in the form:
     *     You are in the kitchen.
     *     Exits: north west
     * @return A long description of this room
     */
    public String getLongDescription()
    {
        return "You are " + description + ".\n" + getExitString() + "\n" + getItemString();
    }

    /**
     * Return a string describing the room's exits, for example
     * "Exits: north west".
     * @return Details of the room's exits.
     */
    public String getExitString()
    {
        String returnString = "Exits:";
        Set<String> keys = exits.keySet();
        for(String exit : keys) {
            returnString += " " + exit;
        }
        return returnString;
    }

    /**
     * Return the room that is reached if we go from this room in direction
     * "direction". If there is no room in that direction, return null.
     * @param direction The exit's direction.
     * @return The room in the given direction.
     */
    public Room getExit(String direction) 
    {
        return exits.get(direction);
    }

    /**
     * Adds an item to the HashSet
     * @param newItem Item to be added to the HashSet
     */
    public void addItem(Item newItem)
    {
        items.add(newItem);
    }

    /**
     * Return the Items in the room and their descriptions
     * @return String item + itemDescription
     */
    public String getItemString()
    {
        if(items.isEmpty()) {
            return "No items in the room";
        }
        String returnString = "Items in the room:";
        for(Item eachItem : items) {
            returnString += "\n" + eachItem.getName() + ", " + eachItem.getDescription();
        }
        return returnString;
    }

    /**
     * If Item is in the bag the method removes the item from the HashSet items
     * and returns the Item. Otherwise, the method returns null
     * @param newItem Item to be removed from the HashSet items
     * @return Item if the item specified is in the Hashset items. If not, return null.
     */
    public Item removeItem(Item endItem)
    {
        if(items.contains(endItem)){ 
            items.remove(endItem);
            return endItem;
        }
        return null;
    }

    /**
     * Returns the Item specified in the parameter if it exist in HashSet items, otherwise
     * returns null
     * @return Item
     */
    public Item searchItems(String nameItem){
        for(Item eachItem : items) {
            if(eachItem.getName().equals(nameItem)){
                return eachItem;
            }
        }
        return null;
    }
    
    public ArrayList<Item> getNewList(){
         ArrayList<Item> newList = new ArrayList<Item>(items);
         return newList;
    }
}


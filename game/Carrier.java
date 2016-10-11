import java.util.HashSet;
import java.util.ArrayList;
/**
 * Abstract class Carrier - write a description of the class here
 * 
 * @author (your name here)
 * @version (version number or date here)
 */
public abstract class Carrier extends Actor
{
    // instance variables - replace the example below with your own
    private HashSet<Item> bag;
    private double weightLimit;
    private double totalWeight;
    public Carrier(Room initialRoom)
    {
        super(initialRoom);
        weightLimit = 100;
        totalWeight = 0;
        bag = new HashSet<Item>();
    }
    
    /**
     * Adds an item to the HashSet Bag
     * @param newItem Item to be added to the HashSet Bag
     */
    public void addToBag(Item newItem)
    {
        bag.add(newItem);
    }
    
        /**
     * If Item is in the bag the method removes the item from the HashSet Bag
     * and returns the Item. Otherwise, the method returns null
     * @param newItem Item to be removed from the HashSet Bag
     * @return if Item is in the bag, return the item. Else, return null.
     */
    public Item removeFromBag(Item endItem)
    {
        if(bag.contains(endItem)){ 
            bag.remove(endItem);
            totalWeight -= endItem.getWeight();
            return endItem;
        }
        else{
            return null;
        }
    }
    
       /**
     * Returns the Item specified in the parameter if it exist in HashSet bag, otherwise
     * returns null
     * @return Item
     */
    public Item searchBag(String nameItem){
        for(Item eachItem : bag) {
            if(eachItem.getName().equals(nameItem)){
                return eachItem;
            }
        }
        return null;
    }

    /**
     * Returns the totalWeight in the bag
     * @return totalWeight
     */
    public double getTotalWeight(){
        return totalWeight;
    }
    
    /**
     * sets the totalWeight in the bag
     * @param double totalWeight
     */
    public void setTotalWeight(double weight){
        totalWeight = weight;
    }

    /**
     * Returns the weightLimit for the bag
     * @return weightLimit
     */
    public double getWeightLimit(){
        return weightLimit;
    }
    
    /**
     * sets the weightLimit for the bag
     * @param double weightLimit
     */
    public void setWeightLimit(double weight){
        weightLimit = weight;
    }

    /** 
     * Try to take an item from current room to put in the bag. If there is an item from HashSet items, enter the new
     * item, otherwise return an error message.
     * @param String, the name of the item that we are trying to put in the bag.
     * @return result of this method
     */
    public String takeItem(String item) 
    {
        if(getRoom().searchItems(item) == null){
            return "There is no " + item + " here.";
        }
        else{
            Item currentItem = getRoom().searchItems(item);
            if(totalWeight + currentItem.getWeight() > weightLimit){
                return "You are trying to carry to much!";
            }
            else{
                addToBag(currentItem);
                totalWeight += currentItem.getWeight();
                getRoom().removeItem(currentItem);
                return item + " taken.";
            }
        }
    }

    /** 
     * Try to take an item from current the bag to put in the current room. If there is an item from HashSet bag, enter the new
     * item, otherwise return an error message.
     * @param String, the name of the item that we are trying to put in the bag.
     * @return result of this method
     */
    public String dropItem(String item) 
    {
        if(searchBag(item) == null){
            return "You are not carrying a " + item + ".";
        }
        else if(searchBag(item).getName().equals("amuletOfWisdom")){
            for(Actor actor : getActors()){
                if(actor.getRoom() == getRoom() && actor != this){
                    System.out.println("Cogratulation You Have Won The Game!!!! :)");
                    return "You Have Graduated!";
                }
            }
        }
        Item currentItem = searchBag(item);
        getRoom().addItem(currentItem);
        removeFromBag(currentItem);
        return item + " dropped.";
    }
    
    /** 
     * Return the ArrayList of Items newBag which is being created from hashSet of Items bag.
     * @return ArrayList<Item> newBag
     */
    public ArrayList<Item> getNewBag(){
        ArrayList<Item> newBag = new ArrayList<Item>(bag);
        return newBag;
    }
    
    /** 
     * Return the Hashset Bag.
     * @return HashSet<Item> newBag
     */
    public HashSet<Item> getBag(){
        return bag;
    }
}

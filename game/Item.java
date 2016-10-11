/**
 * The point of the class Item, is for users to created items and be able interact with them in the zool game.
 * 
 * @author Shalom Azar 
 * @version March 28, 2016
 */
public class Item
{
    // instance variables - replace the example below with your own
    private String name;
    private String description;
    private double weight;

    /**
     * Constructor for objects of class Item
     * @param itemName The name of the Item.
     * @param itemDescription Describing the Item.
     * @param itemWeight The weight of the Item.
     */
    public Item(String itemName, String itemDescription, double itemWeight)
    {
        // initialise instance variables
        this.name = itemName;
        this.description = itemDescription;
        this.weight = itemWeight;
    }

    /**
     * @return The name of the Item.
     */
    public String getName(){
        return name;
    }

    /**
     * @return The description of the Item.
     */
    public String getDescription(){
        return description;
    }

    /**
     * @return The weight of the Item.
     */
    public double getWeight(){
        return weight;
    }

}

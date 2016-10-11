import java.util.List;
import java.util.Random;
/**
 * Write a description of class Animal here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public abstract class Animal extends Actor
{
    // Individual characteristics (instance fields).
    // Whether the animal is alive or not.
    private boolean alive;
    // The animal's position.
    private Location location;
    //The field occupied.
    private Field field;
    // The animals's age.
    private int age;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    /**
     * Constructor for objects of class Animal
     */
    public Animal(boolean randomAge, Location location, Field field)
    {
        // initialise instance variables
        age = 0;
        alive = true;
        this.field = field;
        setLocation(location);
        if(randomAge) {
            age = rand.nextInt(getMaxAge());
        }
    }

    /**
     * Place the animal at the new location in the given field.
     * @param newLocation The animals's new location.
     */
    public void setLocation(Location newLocation)
    {
        if(location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(this, newLocation);
    }

    /**
     * Return the animals's location.
     * @return The animals's location.
     */
    public Location getLocation()
    {
        return location;
    }

    /**
     * Check whether the animal is alive or not.
     * @return true if the animal is still alive.
     */
    public boolean isAlive()
    {
        return alive;
    }

    /**
     * Indicate that the animal is no longer alive.
     * It is removed from the field.
     */
    public void setDead()
    {
        alive = false;
        if(location != null) {
            field.clear(location);
            location = null;
            field = null;
        }
    }

    /**
     * 
     */
    public Field getField()
    {
        return field;
    }

    /**
     * 
     */
    public int getAge()
    {
        return age;
    }

    /**
     * 
     */
    public void setAge(int newAge)
    {
        age = newAge;
    }

    /**
     * An animal can breed if it has reached the breeding age.
     * @return true if the animal can breed
     */
    public boolean canBreed() {
        return age >= getBreedingAge(); 
    }

    /**
     * Return the breeding age of this animal.
     * @return The breeding age of this animal.
     */
    abstract protected int getBreedingAge();
    
    /**
     * Increase the age.
     * This could result in the animals's death.
     */
    public void incrementAge()
    {
        age++;
        if(age > getMaxAge()) {
            setDead();
        }
    }
    
    /**
     * Return the max age of this animal.
     * @return The max age of this animal.
     */
    abstract protected int getMaxAge();
    
    /**
     * Return the Breeding Probability of this animal.
     * @return The Breeding Probability of this animal.
     */
    abstract protected double getBreedingProbability();
    
    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    private int breed()
    {
        int births = 0;
        if(canBreed() && rand.nextDouble() <= getBreedingProbability()) {
            births = rand.nextInt(getMaxLitterSize()) + 1;
        }
        return births;
    }
    
    /**
     * Return the Breeding Probability of this animal.
     * @return The Breeding Probability of this animal.
     */
    abstract protected int getMaxLitterSize();
}

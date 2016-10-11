import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A simple model of a wolf.
 * wolfes age, move, eat foxes, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2011.07.31
 */
public class Wolf extends Animal
{
    // Characteristics shared by all wolfes (class variables).

    // The age at which a wolf can start to breed.
    private static final int BREEDING_AGE = 15;
    // The age to which a wolf can live.
    private static final int MAX_AGE = 100;
    // The likelihood of a wolf breeding.
    private static final double BREEDING_PROBABILITY = 0.1;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 2;
    // The food value of a single fox. In effect, this is the
    // number of steps a wolf can go before it has to eat again.
    private static final int FOX_FOOD_VALUE = 15;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();

    // Individual characteristics (instance fields).

    // The wolf's age.
    //private int age;
    // Whether the wolf is alive or not.
    //private boolean alive;
    // The wolf's position.
    //private Location location;
    // The field occupied.
    //private Field field;
    // The wolf's food level, which is increased by eating foxes.
    private int foodLevel;

    /**
     * Create a wolf. A wolf can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the wolf will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Wolf(boolean randomAge, Field field, Location location)
    {
        super(randomAge, location, field);
        /*
        age = 0;
        //alive = true;
        //this.field = field;
        //setLocation(location);
        if(randomAge) {
        age = rand.nextInt(MAX_AGE);
        }
        leave age at 0
         */
        foodLevel = rand.nextInt(FOX_FOOD_VALUE);
    }

    /**
     * This is what the wolf does most of the time: it hunts for
     * foxes. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param field The field currently occupied.
     * @param newWolfes A list to return newly born wolfes.
     */
    public void act(List<Actor> newWolfes)
    {
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            giveBirth(newWolfes);            
            // Move towards a source of food if found.
            Location newLocation = findFood();
            if(newLocation == null) { 
                // No food found - try to move to a free location.
                newLocation = getField().freeAdjacentLocation(getLocation());
            }
            // See if it was possible to move.
            if(newLocation != null) {
                setLocation(newLocation);
            }
            else {
                // Overcrowding.
                setDead();
            }
        }
    }

    /**
     * Check whether the wolf is alive or not.
     * @return True if the wolf is still alive.
     */
    //public boolean isAlive()
    //{
    //    return alive;
    //}

    /**
     * Indicate that the wolf is no longer alive.
     * It is removed from the field.
     */
    //private void setDead()
    //{
    //alive = false;
    //if(location != null) {
    //field.clear(location);
    //location = null;
    //field = null;
    //}
    //}

    /**
     * Return the wolf's location.
     * @return The wolf's location.
     */
    //public Location getLocation()
    //{
    //return location;
    //}

    /**
     * Place the wolf at the new location in the given field.
     * @param newLocation The wolf's new location.
     */
    //private void setLocation(Location newLocation)
    //{
    //    if(location != null) {
    //        field.clear(location);
    //    }
    //    location = newLocation;
    //    field.place(this, newLocation);
    //}

    /**
     * Increase the age. This could result in the wolf's death.
    
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }
    */

    /**
     * Make this wolf more hungry. This could result in the wolf's death.
     */
    private void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }

    /**
     * Look for foxes adjacent to the current location.
     * Only the first live foxes is eaten.
     * @return Where food was found, or null if it wasn't.
     */
    private Location findFood()
    {
        List<Location> adjacent = getField().adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = getField().getObjectAt(where);
            if(animal instanceof Fox) {
                Fox fox = (Fox) animal;
                if(fox.isAlive()) { 
                    fox.setDead();
                    foodLevel = FOX_FOOD_VALUE;
                    return where;
                }
            }
            if(animal instanceof Rabbit) {
                Rabbit rabbit = (Rabbit) animal;
                if(rabbit.isAlive()) { 
                    rabbit.setDead();
                    foodLevel = FOX_FOOD_VALUE;
                    return where;
                }
            }
        }
        return null;
    }

    /**
     * Check whether or not this wolf is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newwolfes A list to return newly born wolfes.
     */
    private void giveBirth(List<Actor> newWolfes)
    {
        // New wolfes are born into adjacent locations.
        // Get a list of adjacent free locations.
        List<Location> free = getField().getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Wolf young = new Wolf(false, getField(), loc);
            newWolfes.add(young);
        }
    }

    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    private int breed()
    {
        int births = 0;
        if(canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }

    /**
     * A wolf can breed if it has reached the breeding age.
    private boolean canBreed()
    {
    return age >= BREEDING_AGE;
    }
     */

    /**
     * Return the breeding age of this wolf.
     * @return The breeding age of this wolf.
     */
    protected int getBreedingAge()
    {
        return BREEDING_AGE;
    }

    /**
     * Return the max age of this wolf.
     * @return The max age of this wolf.
     */
    public int getMaxAge()
    {
        return MAX_AGE;
    }
    
    /**
     * Return the Breeding Probability of this wolf.
     * @return The Breeding Probability of this wolf.
     */
    public double getBreedingProbability()
    {
        return BREEDING_PROBABILITY;
    }
    
    /**
     * Return the Breeding Probability of this wolf.
     * @return The Breeding Probability of this wolf.
     */
    public int getMaxLitterSize()
    {
        return MAX_LITTER_SIZE;
    }
}

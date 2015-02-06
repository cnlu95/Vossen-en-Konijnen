package Model;
import java.util.List;
import java.util.Random;
import java.util.Iterator;

import Logic.Field;
import Logic.Location;
import Logic.Randomizer;

/**
 * A simple model of a rabbit.
 * Rabbits age, move, breed, and die.
 * 
 * @author Caroline
 * @version 1.0
 */
public class Rabbit extends Animal
{
    // Characteristics shared by all rabbits (class variables).

    // The age at which a rabbit can start to breed.
    private static int BREEDING_AGE = 1;
    // The age to which a rabbit can live.
    private static int MAX_AGE = 100;
    // The likelihood of a rabbit breeding.
    private static double BREEDING_PROBABILITY = 0.045;
    // The maximum number of births.
    private static int MAX_LITTER_SIZE = 12;
    // number of steps a rabbit can go before it has to eat again.
    private static int GRASS_FOOD_VALUE = 14;
    // A shared random number generator to control breeding.
    private static Random rand = Randomizer.getRandom();
    
    // Individual characteristics (instance fields).
    // The rabbit's food level, which is increased by eating rabbits.
    private int foodLevel;


    /**
     * Create a new rabbit. A rabbit may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the rabbit will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Rabbit(boolean randomAge, Field field, Location location)
    {            
            super(field, location);
            if(randomAge) {
                setAge(rand.nextInt(MAX_AGE));            
        }
    }
    
    /**
     * This is what the rabbit does most of the time - it runs 
     * around. Sometimes it will breed or die of old age.
     * @param newRabbits A list to return newly born rabbits.
     */
    public void act(List<Actor> newRabbits)
    {
        incrementAge();
        if(isAlive()) {
            giveBirth(newRabbits);            
            // Try to move into a free location.
            Location newLocation = getField().freeAdjacentLocation(getLocation());
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
     * Look for grass adjacent to the current location.
     * @return Where food was found, or null if it wasn't.
     */
    
    private Location findFood()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object plant = field.getObjectAt(where);
            if(plant instanceof Grass) {
                Grass grass = (Grass) plant;
                if(grass.isAlive()) { 
                    grass.setDead();
                    foodLevel = GRASS_FOOD_VALUE;;
                    return where;
                }
            }
        }
        return null;
    }
    
    /**
     * Check whether or not this rabbit is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newRabbits A list to return newly born rabbits.
     */
    private void giveBirth(List<Actor> newRabbits)
    {
        // New rabbits are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Rabbit young = new Rabbit(false, field, loc);
            newRabbits.add(young);
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
     * @return The age at which a fox starts to breed
     */    
    public static void setBreedingAge(int breeding_age)
    {
    	if (breeding_age >= 0)
    		breeding_age = breeding_age;
    }
    
    /**
     * setter voor max_age
     * @param max_age
     */
    public static void setMaxAge(int max_age)
    {
    	if (max_age >= 1)
    		max_age = max_age;
    }
    
    /**
     * setter voor breeding_probability
     * @param breeding_probability
     */
    public static void setBreedingProbability(double breeding_probability)
    {
    	if (breeding_probability >= 0)
    		breeding_probability = breeding_probability;
    }
    
    /**
     * setter voor max_litter_size
     * @param max_litter_size
     */
    public static void setMaxLitterSize(int max_litter_size)
    {
    	if (max_litter_size >= 1)
    		max_litter_size = max_litter_size;
    }  
    
    /**
     * default settings
     */
    public static void setDefault()
    {
    	BREEDING_AGE = 1;
    	MAX_AGE = 100;
    	BREEDING_PROBABILITY = 0.045;
    	MAX_LITTER_SIZE = 12;
    }
    

    /**
     * @return The age at which a rabbit starts to breed
     */    
    protected int getBreedingAge() 
    {
            return BREEDING_AGE;
    }
    
    /**
     * @return The age at which a rabbit dies.
     */       
    protected int getMaxAge()
    {
            return MAX_AGE;
    }
}

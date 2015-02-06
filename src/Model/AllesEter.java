package Model;
import java.util.List;
import java.util.Iterator;
import java.util.Random;

import Logic.Field;
import Logic.Location;
import Logic.Randomizer;

/**
 * A simple model of a alleseter
 * alleseters age, move, breed, and die.
 * 
 * @author Caroline
 * @version 1.0
 */

public class AllesEter extends Animal
{
    // Characteristics shared by all bears (static fields).
    
    // The age at which a bear can start to breed.
    private static int BREEDING_AGE = 12;
    // The age to which a bear can live.
    private static int MAX_AGE = 5000;
    // The likelihood of a bear breeding.
    private static double BREEDING_PROBABILITY = 0.001;
    // The maximum number of births.
    private static int MAX_LITTER_SIZE = 2;
    // The food value of a single rabbit or fox. In effect, this is the
    // number of steps a fox can go before it has to eat again.
    private static final int TOTAL_FOOD_VALUE = 100;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    
    // Individual characteristics (instance fields).
    // The bear's food level, which is increased by eating rabbits.
    private int foodLevel;
    
    /**
     * Create a bear. A bear can be created as a new born (age zero
     * and not hungry) or with random age.
     * @param randomAge If true, the bear will have random age and hunger level.
     */
    public AllesEter (boolean randomAge, Field field, Location location)
    {
        super(field, location);
        if(randomAge) {
            setAge(rand.nextInt(MAX_AGE));
            foodLevel = rand.nextInt(TOTAL_FOOD_VALUE);
        }
        else {
            // leave age at 0
            foodLevel = TOTAL_FOOD_VALUE;
        }
    }
    
    /**
     * This is what the bear does most of the time: it walks around.
     * In the process, it might breed, crush other animals, or die of old age.
     * @param currentField The field currently occupied.
     * @param updatedField The field to transfer to.
     * @param newAnimals A list to add newly born bears to.
     */
    public void act(List<Actor> newBears)
    {
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            giveBirth(newBears);            
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
     * Check whether or not this bear is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newBears A list to return newly born rabbits.
     */
    private void giveBirth(List<Actor> newBears)
    {
            // New bears are born into adjacent locations.
            // Get a list of adjacent free locations.
            Field field = getField();
            List<Location> free = field.getFreeAdjacentLocations(getLocation());
            int births = breed();
            for(int b = 0; b < births && free.size() > 0; b++) {
                Location loc = free.remove(0);
                Bear young = new Bear(false, field, loc);
                newBears.add(young);
            }
     }        
        
    /**
     * Make this bear more hungry. This could result in the bear's death.
     */
    private void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }
    
    /**
     * Look for rabbits and foxes adjacent to the current location.
     * Only the first live rabbit/fox is eaten.
     * @return Where food was found, or null if it wasn't.
     */
    private Location findFood()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object actor = field.getObjectAt(where);
            if(actor instanceof Fox) {
                Fox fox = (Fox) actor;
                if(fox.isAlive()) { 
                    fox.setDead();
                    foodLevel = TOTAL_FOOD_VALUE;
                    // Remove the dead actor from the field.
                    return where;
                }
            }
            if(actor instanceof Rabbit) {
                Rabbit rabbit = (Rabbit) actor;
                if(rabbit.isAlive()) { 
                    rabbit.setDead();
                    foodLevel = TOTAL_FOOD_VALUE;
                    // Remove the dead actor from the field.
                    return where;
                }
            }
            if(actor instanceof Bear) {
                Bear bear = (Bear) actor;
                if(bear.isAlive()) { 
                    bear.setDead();
                    foodLevel = TOTAL_FOOD_VALUE;
                    // Remove the dead actor from the field.
                    return where;
                }
            }
            if(actor instanceof Hunter) {
                Hunter hunter = (Hunter) actor;
                if(hunter.isAlive()) { 
                    hunter.setDead();
                    foodLevel = TOTAL_FOOD_VALUE;
                    // Remove the dead actor from the field.
                    return where;
                }
            }
            if(actor instanceof Plant) {
                Plant plant = (Plant) actor;
                if(plant.isAlive()) { 
                    plant.setDead();
                    foodLevel = TOTAL_FOOD_VALUE;
                    // Remove the dead actor from the field.
                    return where;
                }
            }
        }
        return null;
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
    	BREEDING_AGE = 12;
    	MAX_AGE = 5000;
    	BREEDING_PROBABILITY = 0.001;
    	MAX_LITTER_SIZE = 2;
    }
    
    /**
     * @return The age at which a bear starts to breed
     */    
    protected int getBreedingAge() 
    {
            return BREEDING_AGE;
    }
            
    /**
     * @return The age at which a bear dies.
     */       
    protected int getMaxAge()
    {
            return MAX_AGE;
    }
    
    /**
     * @return The likelihood of an animal breeding.
     */   
    protected double getBreedingProbability() 
    {
        return BREEDING_PROBABILITY;
    }
    
    /**
     * @return The maximum number of births.
     */
    protected int getMaxLitterSize()
    {
        return MAX_LITTER_SIZE;
    }
        
}

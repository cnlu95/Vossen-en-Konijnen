package Model;
import java.util.List;
import java.util.Iterator;
import java.util.Random;

import Logic.Field;
import Logic.Location;
import Logic.Randomizer;

/**
 * A simple model of a bear
 * Bears age, move, breed, and die.
 * 
 * @author Caroline
 * @version 1.0
 */

public class Bear extends Animal
{
    // Characteristics shared by all bears (static fields).
    
    // The age at which a bear can start to breed.
    private static final int BREEDING_AGE = 10;
    // The age to which a bear can live.
    private static final int MAX_AGE = 200;
    // The likelihood of a bear breeding.
    private static final double BREEDING_PROBABILITY = 0.04;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 2;
    // The food value of a single rabbit or fox. In effect, this is the
    // number of steps a fox can go before it has to eat again.
    private static final int RABBIT_FOOD_VALUE = 9;
    private static final int FOX_FOOD_VALUE = 2;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    
    // Individual characteristics (instance fields).
    // The fox's food level, which is increased by eating rabbits.
    private int foodLevel;
    
    /**
     * Create a bear. A beart can be created as a new born (age zero
     * and not hungry) or with random age.
     * @param randomAge If true, the elphant will have random age and hunger level.
     */
    public Bear(boolean randomAge, Field field, Location location)
    {
        super(field, location);
        if(randomAge) {
            setAge(rand.nextInt(MAX_AGE));
        }
    }
    
    /**
     * This is what the elephant does most of the time: it walks around.
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
            Object animal = field.getObjectAt(where);
            if(animal instanceof Rabbit) {
                Rabbit rabbit = (Rabbit) animal;
                if(rabbit.isAlive()) { 
                    rabbit.setDead();
                    foodLevel = RABBIT_FOOD_VALUE;
                    // Remove the dead rabbit from the field.
                    return where;
                }
            }
            if(animal instanceof Fox) {
                Fox fox = (Fox) animal;
                if(fox.isAlive()) { 
                    fox.setDead();
                    foodLevel = FOX_FOOD_VALUE;
                    // Remove the dead fox from the field.
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

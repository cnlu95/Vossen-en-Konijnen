package Model;
import java.util.List;
import java.util.Iterator;

import Logic.Field;
import Logic.Location;

/**
 * De klasse Hunter. Hunters jagen op dieren. 
 * @author Caroline
 * @version 1.0
 */
public class Hunter implements Actor
{
    // The hunter's field.
    private Field field;
    // The hunter's position in the field.
    private Location location;
    // Determine if the hunter is alive
    private boolean alive;
    
    /**
     * Constructor for objects of class Hunter
     */
    public Hunter(Field field, Location location)
    {
        this.field = field;
        this.location = location;
    }
    
    /**
     * Getter voor de locatie.
     */
    public Location getLocation() 
    {
        return location;
    }
    
    /**
     * Check whether the hunter is alive or not.
     * @return true if the hunter is still alive.
     */
    public boolean isAlive()
    {
    	return alive;
    }
    
    /**
     * This is what a Hunter does.
     * @param field The field currently occupied.
     * @param newbears A list to return Hunters.
     */
    
    public void act(List<Actor> newHunters)
    {
    	// Move towards a source of food if found.
        Location newLocation = findAnimal();
        if(newLocation == null) { 
            // No food found - try to move to a free location.
            newLocation = getField().freeAdjacentLocation(getLocation());
        }
        // See if it was possible to move.
        if(newLocation != null) {
            setLocation(newLocation);
        }
        else{
            	// Overcrowding.
            	setDead();
        	}
    }
    
    /**
     * Set the hunter's location.
     * @param row The vertical coordinate of the location.
     * @param col The horizontal coordinate of the location.
     */
    public void setLocation(int row, int col)
    {
        this.location = new Location(row, col);
    }

    /**
     * Set the hunter's location.
     * @param location The hunter's location.
     */
    public void setLocation(Location location)
    {
        this.location = location;
    }
    
    /**
     * Look for an animal adjacent to the current location.
     * Only the first live animal is shoot.
     * @return Where an animal is found, or null if it wasn't.
     */
    private Location findAnimal()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if(animal instanceof Bear) {
                Bear bear = (Bear) animal;
                if(bear.isAlive()) { 
                    bear.setDead();
                    // Remove the dead fox from the field.
                    return where;
                }
            }
            if(animal instanceof Fox) {
                Fox fox = (Fox) animal;
                if(fox.isAlive()) { 
                    fox.setDead();
                    // Remove the dead fox from the field.
                    return where;
                }
            }
            if(animal instanceof Rabbit) {
                Rabbit rabbit = (Rabbit) animal;
                if(rabbit.isAlive()) { 
                    rabbit.setDead();
                    // Remove the dead fox from the field.
                    return where;
                }
            }
        }
        return null;
    }
    
    /**
     * Indicate that the hunter is no longer alive.
     * It is removed from the field.
     */
    public void setDead()
    {
        if(location != null) {
            field.clear(location);
            location = null;
            field = null;
        }
    }
    
    /**
     * Return the hunter's field.
     * @return Field the hunter's field.
     */
    public Field getField()
    {
        return field;
    }   

}

package Model;
import java.util.List;

/**
 * De interface Actor
 * 
 * @author Caroline
 * @version 1.0
 */

public interface Actor {
	
    /**
     * Make this actor act - that is: make it do
     * whatever it wants/needs to do.
     * @param newActors A list to receive newly born animals.
     */	
	void act(List<Actor> newActors);
	
	 /**
     * Check whether the animal is alive or not.
     * @return true if the animal is still alive.
     */
	boolean isAlive();
	
}

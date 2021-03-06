package Main;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Color;

import Logic.Field;
import Logic.Location;
import Logic.Randomizer;
import Model.Actor;
import Model.Bear;
import Model.Fox;
import Model.Hunter;
import Model.Rabbit;
import Model.Grass;
import Model.AllesEter;
import View.SimulatorView;

/**
 * A simple predator-prey simulator, based on a rectangular field
 * containing rabbits and foxes.
 * 
 * @author Caroline
 * @version 1.0
 */
public class Simulator
{
    // Constants representing configuration information for the simulation.
    // The default width for the grid.
    private static final int DEFAULT_WIDTH = 120;
    // The default depth of the grid.
    private static final int DEFAULT_DEPTH = 80;
    // The probability that a fox will be created in any given grid position.
    private static double FOX_CREATION_PROBABILITY = 0.017;
    // The probability that a rabbit will be created in any given grid position.
    private static double RABBIT_CREATION_PROBABILITY = 0.09;   
    // The probability that a bear will be created in any given grid position.
    private static double BEAR_CREATION_PROBABILITY = 0.02;
    // The probability that a hunter will be created in any given grid position.
    private static double HUNTER_CREATION_PROBABILITY = 0.05;
    // The probability that grass will be created in any given grid position.
    private static double GRASS_CREATION_PROBABILITY = 0.007;  
    // The probability that alleseter will be created in any given grid position.
    private static double ALLESETER_CREATION_PROBABILITY = 0.01;

    // List of actors in the field.
    private List<Actor> actors;
    // The current state of the field.
    private Field field;
    // The current step of the simulation.
    private int step;
    // A graphical view of the simulation.
    private SimulatorView view;;
    //	animation speed of the thread
    private static int animationSpeed = 100;
    
    /**
     * Construct a simulation field with default size.
     */
    public Simulator()
    {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }
    
    /**
     * Create a simulation field with the given size.
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    public Simulator(int depth, int width)
    {
        if(width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be greater than zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }
        
        actors = new ArrayList<Actor>();
        field = new Field(depth, width);

        // Create a view of the state of each location in the field.
        view = new SimulatorView(depth, width);
        view.setColor(Rabbit.class, Color.orange);
        view.setColor(Fox.class, Color.blue);
        view.setColor(Bear.class, Color.GRAY);
        view.setColor(Hunter.class, Color.CYAN);
        view.setColor(Grass.class, Color.GREEN);
        view.setColor(AllesEter.class, Color.RED);
        
        // Setup a valid starting point.
        reset();
    }
    
    /**
     * Run the simulation from its current state for a reasonably long period,
     * (4000 steps).
     */
    public void runLongSimulation()
    {
        simulate(4000);
    }
    
    /**
     * Run the simulation from its current state for the given number of steps.
     * Stop before the given number of steps if it ceases to be viable.
     * @param numSteps The number of steps to run for.
     */
    public void simulate(int numSteps)
    {
        for(int step = 1; step <= numSteps && view.isViable(field); step++) {
            simulateOneStep();
        }
    }
    
    /**
     * Run the simulation from its current state for a single step.
     * Iterate over the whole field updating the state of each
     * fox and rabbit.
     */
    public void simulateOneStep()
    {
        step++;

        // Provide space for newborn actors.
        List<Actor> newactors = new ArrayList<Actor>();        
        // Let all actors act.
        for(Iterator<Actor> it = actors.iterator(); it.hasNext(); ) {
            Actor animal = it.next();
            animal.act(newactors);
            if(! animal.isAlive()) {
                it.remove();
            }
        }
               
        // Add the newly born foxes and rabbits to the main lists.
        actors.addAll(newactors);

        view.showStatus(step, field);
    }
        
    /**
     * Reset the simulation to a starting position.
     */
    public void reset()
    {
        step = 0;
        actors.clear();
        populate();
        
        // Show the starting state in the view.
        view.showStatus(step, field);
    }
    
    /**
     * Getter voor view
     * @return view van het type SimulatorView
     */
    public SimulatorView getSimulatorView()
    {
    	return view;
    }
    
    /**
     * setter voor bear_creation_probability
     * @param bear_creation_probability
     */
    public static void setBearCreationProbability(double bear_creation_probability)
    {
    	if (bear_creation_probability >= 0)
    		Simulator.BEAR_CREATION_PROBABILITY = bear_creation_probability;
    }   
    
    /**
     * setter voor fox_creation_probability
     * @param fox_creation_probability
     */
    public static void setFoxCreationProbability(double fox_creation_probability)
    {
    	if (fox_creation_probability >= 0)
    		Simulator.FOX_CREATION_PROBABILITY = fox_creation_probability;
    }
    
    /**
     * setter voor rabbit_creation_probability
     * @param rabbit_creation_probability
     */
    public static void setRabbitCreationProbability(double rabbit_creation_probability)
    {
    	if (rabbit_creation_probability >= 0)
    		Simulator.RABBIT_CREATION_PROBABILITY = rabbit_creation_probability;
    }
    
    /**
     * setter voor hunter_creation_probability
     * @param hunter_creation_probability
     */
    public static void setHunterCreationProbability(double hunter_creation_probability)
    {
    	if (hunter_creation_probability >= 0)
    		Simulator.HUNTER_CREATION_PROBABILITY = hunter_creation_probability;
    }
    
    /**
     * setter voor grass_creation_probability
     * @param grass_creation_probability
     */
    public static void setGrassCreationProbability(double grass_creation_probability)
    {
    	if (grass_creation_probability >= 0)
    		Simulator.GRASS_CREATION_PROBABILITY = grass_creation_probability;
    }    
    
    /**
     * setter voor alleseter_creation_probability
     * @param alleseter_creation_probability
     */
    public static void setAllesEterCreationProbability(double alleseter_creation_probability)
    {
    	if (alleseter_creation_probability >= 0)
    		Simulator.ALLESETER_CREATION_PROBABILITY = alleseter_creation_probability;
    }   
    
    /**
     * Getter voor field
     * @return field van het type Field
     */
    public Field getField()
    {
    	return field;
    }
    
    /**
     * getter voor animationSpeed()
     * @return animationSpeed of the thread
     */
    public int getAnimationSpeed()
    {
    	return animationSpeed;
    }
    
    /**
     * setter voor animationSpeed
     * @param animationSpeed
     */
    public static void setAnimationSpeed(int animationSpeed)
    {
    	if (animationSpeed >= 0 && animationSpeed <= 1000)
    		Simulator.animationSpeed = animationSpeed;
    }
    
    /**
     * default settings
     */
    public static void setDefault()
    {
    	animationSpeed = 100;
        FOX_CREATION_PROBABILITY = 0.02;
        RABBIT_CREATION_PROBABILITY = 0.09;   
        BEAR_CREATION_PROBABILITY = 0.02;
        HUNTER_CREATION_PROBABILITY = 0.05;
        GRASS_CREATION_PROBABILITY = 0.01;  
    }
        
    /**
     * Randomly populate the field with animals and hunters.
     */
    private void populate()
    {
        Random rand = Randomizer.getRandom();
        field.clear();
        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                if(rand.nextDouble() <= FOX_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Fox fox = new Fox(true, field, location);
                    actors.add(fox);
                }
                else if(rand.nextDouble() <= RABBIT_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Rabbit rabbit = new Rabbit(true, field, location);
                    actors.add(rabbit);
                }
                else if(rand.nextDouble() <= BEAR_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Bear bear = new Bear(true, field, location);
                    actors.add(bear);
                }
                else if(rand.nextDouble() <= HUNTER_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Hunter hunter = new Hunter(field, location);
                    actors.add(hunter);
                }
                else if(rand.nextDouble() <= GRASS_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Grass grass = new Grass(true, field, location);
                    actors.add(grass);
                }               
                else if(rand.nextDouble() <= ALLESETER_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    AllesEter alleseter = new AllesEter(true, field, location);
                    actors.add(alleseter);
                 //else leave the location empty.
            }
            }
        }
    }
}

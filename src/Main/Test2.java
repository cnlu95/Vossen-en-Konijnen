package Main;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *JUnit om bepaalde methodes uit de klasse Simulator te testen.
 *
 * @author Caroline
 */

public class Test2 {

    /**
     * Test if the method runLongSimulation() works.
     */
    @Test
    public void runLongSimulation() {
        System.out.println("runLongSimulation");
        Simulator sim = new Simulator();
        sim.runLongSimulation();
    }

    /**
     * Test of simulate method, of class Simulator.
     */
    @Test
    public void simulate() {
        System.out.println("simulate");
        int numSteps = 0;
        Simulator sim = new Simulator();
        sim.simulate(numSteps);
    }

    /**
     * Test of simulateOneStep method, of class Simulator.
     */
    @Test
    public void simulateOneStep() {
        System.out.println("simulateOneStep");
        Simulator sim = new Simulator();
        sim.simulateOneStep();
    }

    /**
     * Test of reset method, of class Simulator.
     */
    @Test
    public void reset() {
        System.out.println("reset");
        Simulator sim = new Simulator();
        sim.reset();
    }
}

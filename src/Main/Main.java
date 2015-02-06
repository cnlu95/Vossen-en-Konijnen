package Main;


/**
 * Main class to operate simulator.
 * 
 * @author Caroline
 * @version 1.0
 */ 
public class Main
{
    private static Simulator simulator;

    /**
     * Main methode
     */ 
    public static void main(String[] args) {
        setSimulator(new Simulator());
    }

    /**
     * Getter voor simulator.
     */ 
    public static Simulator getSimulator() {
        return simulator;
    }

    /**
     * Setter voor simulator.
     */ 
    public static void setSimulator(Simulator simulator) {
        Main.simulator = simulator;
    }
}
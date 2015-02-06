package Runner;

import Main.Main;
import Main.Simulator;
/**
 * De klasse ThreadRunner
 * 
 * @author 
 * @version 
 */
public class ThreadRunner implements Runnable
{	
	
	private int numSteps = 0;
	private boolean infinite = false;
	private boolean threadRun;
	
	
	/**
	 * leeg constructor
	 */
	public ThreadRunner() 
	{
	}
	
    /**
     * Run the simulation from its current state for a reasonably long period
     */
    public void startRun(int numSteps)
    {
    	if (numSteps == 0)
    	{
    		this.numSteps = 1;
    		infinite = true;
    	}
    	else
    	{
    		this.numSteps += numSteps;	
    	}
    	
//    	try{
    		if (!threadRun && Thread.currentThread().isAlive())
    		{
    			new Thread(this).start();
    		}
	}

    /**
     * Pauzeert de simulatie.
     */
	public void stop() 
	{
		numSteps = 0;
		threadRun = false;
		infinite = false;
	}
	
	/**
	 * Deze methode wordt alleen uitgevoerd als je de methode .start() gebruikt van de klasse Thread.
	 * Zonder de klasse (thread), wordt deze methode niet juist uitgevoerd.
	 * De methode zorgt ervoor dat de thread door het aantal numSteps heen loopt.
	 */
	@Override
	public void run() 
	{
		threadRun = true;
		Simulator simulator = Main.getSimulator();
		
		while(threadRun && numSteps > 0 && simulator.getSimulatorView().isViable(simulator.getField()))
		{
			Main.getSimulator().simulateOneStep();
			numSteps--;
			while(infinite && numSteps == 0)
			{
				numSteps++;
			}
			
			try {
				Thread.sleep(simulator.getAnimationSpeed());
			} 
			catch (Exception e) 
			{
            	System.out.println("InterruptedException");
			}
		}
		threadRun = false;
	}
}
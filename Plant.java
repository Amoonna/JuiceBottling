/**
 * 
 * @author Amoonna: Code written by Nate: Runs the plants and puts oranges
 *         through the process of becoming juice and then prints the results for
 *         the user
 */
public class Plant implements Runnable {
	// How long do we want to run the juice processing
	public static final long PROCESSING_TIME = 5 * 1000;
	// Number of plants we want to run, number can easily change to change
	// production amounts
	private static final int NUM_PLANTS = 3;

	/**
	 * Creates the plants and starts the process Number of plants is based on the
	 * static value of NUM_PLANTS
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Plant[] plants = new Plant[NUM_PLANTS];
		for (int i = 0; i < NUM_PLANTS; i++) { // sets the plant value to i and adds one each time it loops
			plants[i] = new Plant();
			plants[i].startPlant();
		}

		// Give the plants time to do work
		delay(PROCESSING_TIME, "Plant malfunction");

		// Stop the plant, and wait for it to shutdown
		for (Plant p : plants) {
			p.stopPlant();
		}
		for (Plant p : plants) {
			p.waitToStop();
		}

		// Summarize the results
		int totalProvided = 0; // Total oranges used
		int totalProcessed = 0; // Total that completed processing
		int totalBottles = 0; // Total bottles created
		int totalWasted = 0; // Oranges that did not complete processing
		for (Plant p : plants) {
			totalProvided += p.getProvidedOranges();
			totalProcessed += p.getProcessedOranges();
			totalBottles += p.getBottles();
			totalWasted += p.getWaste();
		}
		// Print to user the results from above
		System.out.println("Total provided/processed = " + totalProvided + "/" + totalProcessed);
		System.out.println("Created " + totalBottles + ", wasted " + totalWasted + " oranges");
	}

	/**
	 * Process that gives the threads time to run
	 * 
	 * @param time
	 * @param errMsg
	 */
	private static void delay(long time, String errMsg) {
		long sleepTime = Math.max(1, time);
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) { // issues error if it fails
			System.err.println(errMsg);
		}
	}

	// Determines how many oranges must complete the process to create 1 bottle
	public final int ORANGES_PER_BOTTLE = 3;

	private final Thread thread;
	private int orangesProvided; // keeps track of total provided oranges
	private int orangesProcessed; // keeps track of processed oranges
	private volatile boolean timeToWork; // when true the plant can run

	/**
	 * Sets the initial state of the plant and creates the threads Starts both
	 * values at 0
	 */
	Plant() {
		orangesProvided = 0;
		orangesProcessed = 0;
		thread = new Thread(this, "Plant");
	}

	/**
	 * This sets timeToWork as true so that the plant can begin running and starts
	 * the threads up
	 */
	public void startPlant() {
		timeToWork = true;
		thread.start();
	}

	/**
	 * Changes timeToWork to false and the plant stops no matter what state any
	 * orange is in
	 */
	public void stopPlant() {
		timeToWork = false;
	}

	/**
	 * pauses the current thread
	 */
	public void waitToStop() {
		try {
			thread.join();
		} catch (InterruptedException e) { // throws an exception when one of the plants has to stop due to an error
			System.err.println(thread.getName() + " stop malfunction");
		}
	}

	/**
	 * The plants complete work while the timeToWork is true and processing begins
	 */
	public void run() {
		System.out.print(Thread.currentThread().getName() + " Processing oranges ");
		while (timeToWork) {
			processEntireOrange(new Orange());
			orangesProvided++;
			System.out.print(".");
		}
		System.out.println("");
	}

	/**
	 * Gets the state of the orange and processes it as well as adds 1 orange to the
	 * count of total oranges
	 * 
	 * @param o
	 */
	public void processEntireOrange(Orange o) {
		while (o.getState() != Orange.State.Bottled) {
			o.runProcess();
		}
		orangesProcessed++;
	}

	/**
	 * Total number of oranges created
	 * 
	 * @return orangesProvided
	 */
	public int getProvidedOranges() {
		return orangesProvided;
	}

	/**
	 * Returns how many total oranges were processed
	 * 
	 * @return orangesProcessed
	 */
	public int getProcessedOranges() {
		return orangesProcessed;
	}

	/**
	 * Returns how many bottles of juice were created
	 * 
	 * @return orangesProcessed / ORANGES_PER_BOTTLE
	 */
	public int getBottles() {
		return orangesProcessed / ORANGES_PER_BOTTLE;
	}

	/**
	 * Returns how many oranges are wasted
	 * 
	 * @return orangesProcessed
	 */
	public int getWaste() {
		return orangesProcessed % ORANGES_PER_BOTTLE;
	}
}
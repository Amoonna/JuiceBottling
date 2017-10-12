/**
 * 
 * @author Amoonna: code written by Nate: Orange keeps track of the states the
 *         orange goes through during processing as well as sets how long it
 *         takes for each piece of the process to be completed
 */

public class Orange {
	// states the orange must go through to make the juice
	public enum State {
		Fetched(15), Peeled(38), Squeezed(29), Bottled(17), Processed(1); // States the orange can be in

		private static final int finalIndex = State.values().length - 1;

		final int timeToComplete;

		State(int timeToComplete) {
			this.timeToComplete = timeToComplete;
		}

		/**
		 * Sees what state the orange is at and finds the next state
		 * 
		 * @return State.values
		 */
		State getNext() {
			int currIndex = this.ordinal();
			// Prevents the orange from continuing should the juice be completed
			if (currIndex >= finalIndex) { // The orange is at it's final state
				throw new IllegalStateException("Already at final state");
			}
			return State.values()[currIndex + 1]; // Takes the current index of the state and adds 1 so that it can
		}
	}

	private State state;

	/**
	 * Constructor Begins the process Sets the state of an orange to Fetched
	 */
	public Orange() {
		state = State.Fetched;
		doWork();
	}

	/**
	 * Finds the state the orange is currently in
	 * 
	 * @return state
	 */
	public State getState() {
		return state;
	}

	/**
	 * Processes the orange and prevents an orange from being processed more than
	 * once
	 */
	public void runProcess() {
		// Don't attempt to process an already completed orange
		if (state == State.Processed) {
			throw new IllegalStateException("This orange has already been processed");
		}
		doWork();
		state = state.getNext();
	}

	/**
	 * Takes the set amount of time needed to complete a piece of the process
	 */
	private void doWork() {
		// Sleep for the amount of time necessary to do the work
		try {
			Thread.sleep(state.timeToComplete);
		} catch (InterruptedException e) {
			System.err.println("Incomplete orange processing, juice may be bad");
		}
	}
}

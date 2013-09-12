package osbot.api.util;

/**
 * Created by
 * User: Cory
 * Date: 11/09/13
 */
public class Timer {

	private long period, start;

	public Timer(long period) {
		this.period = period;
		start = System.currentTimeMillis();
	}

	/**
	 *
	 * Returns how much time in milliseconds has elapsed since the timer started.
	 *
	 * @return how much time in milliseconds has elapsed since the timer started.
	 */
	public long getElapsed(){
		return System.currentTimeMillis()-start;
	}

	/**
	 *
	 * Returns how much time in milliseconds is left until the timer stops.
	 *
	 * @return how much time in milliseconds is left until the timer stops.
	 */
	public long getRemaining(){
		return period-getElapsed();
	}

	/**
	 *
	 * Returns whether the current timer is still running.
	 *
	 * @return whether the current timer is still running.
	 */
	public boolean isRunning(){
		return getElapsed() <= period;
	}

	/**
	 * Resets the current timer, so it will end in the period pre-set.
	 */
	public void reset(){
		start = System.currentTimeMillis();
	}

	/**
	 *
	 * Sets the amount of time the current timer will end in.
	 *
	 * @param ms the amount of time in milliseconds the current time will end in.
	 * @return the amount of time in milliseconds the current time will end
	 */
	public long setEndIn(long ms){
		start = System.currentTimeMillis()-ms;
		return period = ms;
	}

	/**
	 * Stops the current timer.
	 */
	public void stop() {
		setEndIn(0);
	}
}

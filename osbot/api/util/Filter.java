package osbot.api.util;

/**
 * Created by
 * User: Cory
 * Date: 11/09/13
 */
public interface Filter<T> {
	/**
	 *
	 * Returns whether the filter accepts the instance passed into it.
	 *
	 * @param o the instance of what we wish to filter
	 * @return whether the filter accepts the instance passed into it.
	 */
	public boolean accept(T o);
}
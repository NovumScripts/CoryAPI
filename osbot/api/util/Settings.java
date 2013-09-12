package osbot.api.util;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by
 * User: Cory
 * Date: 11/09/13
 */
public class Settings {

	private ConcurrentHashMap<String, Object> settings = new ConcurrentHashMap<String, Object>();

	/**
	 *
	 * Adds the specified Object to the list, which will be identified by the specified key.
	 *
	 * @param key the key assigned to the object we are adding
	 * @param value the Object being stored with the specified key
	 */
	public void put(String key, Object value) {
		settings.put(key, value);
	}


	/**
	 *
	 * Returns the Object stored with the specified key
	 *
	 * @param key the key assigned to the value we wish to retrieve.
	 * @return the Object stored with the specified key
	 */
	public Object get(String key) {
		if(settings.containsKey(key)) {
			return settings.get(key);
		}
		return null;
	}

	/**
	 *
	 * Returns the Object of type T stored with the specified key
	 *
	 * @param key the key assigned to the value we wish to retrieve.
	 * @return the Object of type T stored with the specified key
	 */
	public <T extends Object> T get(String key, Class<T> type) {
		if(settings.containsKey(key))
			return type.cast(settings.get(key));
		return null;
	}

	/**
	 *
	 * Returns the String stored with the specified key
	 *
	 * @param key the key assigned to the value we wish to retrieve.
	 * @return the String stored with the specified key
	 */
	public String getString(String key) {
		if(settings.containsKey(key)) {
			return String.valueOf(get(key));
		}
		return "";
	}

	/**
	 *
	 * Returns the Integer stored with the specified key
	 *
	 * @param key the key assigned to the value we wish to retrieve.
	 * @return the Integer stored with the specified key
	 */
	public int getInt(String key) {
		if(settings.containsKey(key)) {
			String value = getString(key);
			if(value != null && value.length() > 0)
				return Integer.parseInt(value);
		}
		return -1;
	}

	/**
	 *
	 * Returns the Double stored with the specified key
	 *
	 * @param key the key assigned to the value we wish to retrieve.
	 * @return the Double stored with the specified key
	 */
	public double getDouble(String key) {
		if(settings.containsKey(key)) {
			String value = getString(key);
			if(value != null && value.length() > 0)
				return Double.parseDouble(value);
		}
		return -1;
	}


	/**
	 *
	 * Returns the Boolean stored with the specified key
	 *
	 * @param key the key assigned to the value we wish to retrieve.
	 * @return the Boolean stored with the specified key
	 */
	public boolean getBoolean(String key) {
		if(settings.containsKey(key)) {
			String value = getString(key);
			if(value != null && value.length() > 0)
				return Boolean.parseBoolean(value);
		}
		return false;
	}

	/**
	 *
	 * Returns the Long stored with the specified key
	 *
	 * @param key the key assigned to the value we wish to retrieve.
	 * @return the Long stored with the specified key
	 */
	public long getLong(String key) {
		if(settings.containsKey(key)) {
			String value = getString(key);
			if(value != null && value.length() > 0)
				return Long.parseLong(value);
		}
		return -1;
	}

	/**
	 *
	 * Returns the list of objects and keys currently stored.
	 *
	 * @return the list of objects and keys currently stored.
	 */
	public Set<Map.Entry<String, Object>> get() {
		return settings.entrySet();
	}
}
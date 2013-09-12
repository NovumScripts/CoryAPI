package osbot.api.methods.impl;

import osbot.api.methods.MethodContext;
import osbot.api.util.Filter;
import org.osbot.script.rs2.def.ObjectDefinition;
import org.osbot.script.rs2.model.RS2Object;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Cory
 * Date: 23/08/13
 * Time: 20:52
 */
public class Objects {

	private MethodContext ctx;

	public Objects(MethodContext ctx) {
		this.ctx = ctx;
	}

	/**
	 *
	 * Returns all of the Objects in the current region.
	 *
	 * @return All of the Objects in the current region.
	 */
	public RS2Object[] getAll() {
		return getAll(ALL_FILTER);
	}



	/**
	 *
	 * Returns all of the Objects which are passed through the filter.
	 *
	 * @param filter The filter you wish to apply to filter out Objects you're not looking for.
	 * @return All of the Objects which are passed through the filter.
	 */
	public RS2Object[] getAll(Filter<RS2Object> filter) {
		LinkedList<RS2Object> valid = new LinkedList<RS2Object>();
		for(RS2Object n : ctx.client.getCurrentRegion().getObjects()) {
			if(filter.accept(n))
				valid.add(n);
		}
		return valid.toArray(new RS2Object[valid.size()]);
	}

	/**
	 *
	 * Returns all of the Objects which are passed through the filter.
	 *
	 * @param filter The filter you wish to apply to filter out Objects you're not looking for.
	 * @return All of the Objects which are passed through the filter.
	 */
	public RS2Object getClosest(Filter<RS2Object> filter) {
		RS2Object closest = null;
		for (RS2Object entity : ctx.client.getCurrentRegion().getObjects()) {
			if(closest == null || ctx.distance(entity) < ctx.distance(closest))
				closest = entity;
		}
		return closest;
	}

	/**
	 *
	 * @see #getClosestWithAction(String, osbot.api.util.Filter, int)
	 */
	public RS2Object getClosestWithAction(String action) {
		return getClosestWithAction(action, ALL_FILTER, 0);
	}

	/**
	 *
	 * @see #getClosestWithAction(String, osbot.api.util.Filter, int)
	 */
	public RS2Object getClosestWithAction(String action, int distance) {
		return getClosestWithAction(action, ALL_FILTER, distance);
	}

	/**
	 *
	 * @see #getClosestWithAction(String, osbot.api.util.Filter, int)
	 */
	public RS2Object getClosestWithAction(String action, Filter<RS2Object> filter) {
		return getClosestWithAction(action, filter, 0);
	}

	/**
	 *
	 * Returns the closest Object with the specified action, which is passed through the filter.
	 *
	 * @param action The action you wish the Object to have.
	 * @param filter The filter you wish to apply to filter out Objects you're not looking for.
	 * @param distance The distance threshold that you wish to search
	 * @return The closest Object with the specified action, which is passed through the filter.
	 */
	public RS2Object getClosestWithAction(String action, Filter<RS2Object> filter, int distance) {
		List<RS2Object> allObjects = ctx.client.getCurrentRegion().getObjects();
		List<RS2Object> objects = new ArrayList<RS2Object>();
		if(allObjects != null && allObjects.size() > 0) {
			for(RS2Object object : allObjects) {
				if(!filter.accept(object) || object == null)
					continue;
				if(hasAction(object, action) && (ctx.distance(object) <= distance || distance == 0))
					objects.add(object);
			}
		}
		return (RS2Object) ctx.closest(objects);
	}

	/**
	 *
	 * @see #getClosestWithId(osbot.api.util.Filter, int, int...)
	 */
	public RS2Object getClosestWithId(int... ids) {
		return getClosestWithId(ALL_FILTER, 0, ids);
	}

	public RS2Object getClosestWithId(int distance, int... ids) {
		return getClosestWithId(ALL_FILTER, distance, ids);
	}

	/**
	 *
	 * @see #getClosestWithId(osbot.api.util.Filter, int, int...)
	 */
	public RS2Object getClosestWithId(Filter<RS2Object> filter, int... ids) {
		return getClosestWithId(filter, 0, ids);
	}

	/**
	 *
	 * Returns the closest Object with the specified id, which is passed through the filter.
	 *
	 * @param filter The filter you wish to apply to filter out Objects you're not looking for.
	 * @param distance The distance threshold that you wish to search
	 * @param ids The Object ids that you're searching for
	 * @return The closest Object with the specified id, which is passed through the filter.
	 */
	public RS2Object getClosestWithId(Filter<RS2Object> filter, int distance, int... ids) {
		List<RS2Object> allObjects = ctx.client.getCurrentRegion().getObjects();
		List<RS2Object> objects = new ArrayList<RS2Object>();
		if(allObjects != null && allObjects.size() > 0) {
			for(RS2Object object : allObjects) {
				if(!filter.accept(object) || object == null)
					continue;
				if(ctx.distance(object) <= distance || distance == 0) {
					for(int id : ids) {
						if(object.getId() == id) {
							objects.add(object);
							break;
						}
					}
				}
			}
		}
		return (RS2Object) ctx.closest(objects);
	}

	/**
	 *
	 * Checks whether the Object has the specified action
	 *
	 * @param object the Object you're checking for the action
	 * @param action The action you're searching for.
	 * @return whether the Object has the specified action
	 */
	public boolean hasAction(RS2Object object, String action) {
		ObjectDefinition i = object.getDefinition();
		if(i == null)
			return false;
		return ctx.hasAction(i.getActions(), action);
	}

	Filter<RS2Object> ALL_FILTER = new Filter<RS2Object>() {
		public boolean accept(RS2Object o) {
			return true;
		}
	};
}
package osbot.api.methods.impl;

import org.osbot.script.rs2.def.NPCDefinition;
import org.osbot.script.rs2.model.NPC;
import osbot.api.methods.MethodContext;
import osbot.api.util.Filter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
/**
 * Created by
 * User: Cory
 * Date: 11/09/13
 */
public class NPCs {

	private MethodContext ctx;

	public NPCs(MethodContext ctx) {
		this.ctx = ctx;
	}

	/**
	 *
	 * Returns all of the NPCs in the current region.
	 *
	 * @return All of the NPCs in the current region.
	 */
	public NPC[] getAll() {
		return getAll(ALL_FILTER);
	}


	/**
	 *
	 * Returns all of the NPCs which are passed through the filter.
	 *
	 * @param filter The filter you wish to apply to filter out NPCs you're not looking for.
	 * @return All of the NPCs which are passed through the filter.
	 */
	public NPC[] getAll(Filter<NPC> filter) {
		LinkedList<NPC> valid = new LinkedList<NPC>();
		for(NPC n : ctx.client.getLocalNPCs()) {
			if(filter.accept(n))
				valid.add(n);
		}
		return valid.toArray(new NPC[valid.size()]);
	}

	/**
	 *
	 * Returns the closest NPC which is passed through the filter.
	 *
	 * @param filter The filter you wish to apply to filter out NPCs you're not looking for.
	 * @return The closest NPC with the specified action, which is passed through the filter.
	 */
	public NPC getClosest(Filter<NPC> filter) {
		NPC closest = null;
		for (NPC entity : ctx.client.getLocalNPCs()) {
			if(closest == null || ctx.distance(entity) < ctx.distance(closest))
				closest = entity;
		}
		return closest;
	}

	/**
	 *
	 * @see #getClosestWithAction(String, osbot.api.util.Filter, int)
	 */
	public NPC getClosestWithAction(String action) {
		return getClosestWithAction(action, ALL_FILTER, 0);
	}

	/**
	 *
	 * @see #getClosestWithAction(String, osbot.api.util.Filter, int)
	 */
	public NPC getClosestWithAction(String action, int distance) {
		return getClosestWithAction(action, ALL_FILTER, distance);
	}

	/**
	 *
	 * @see #getClosestWithAction(String, osbot.api.util.Filter, int)
	 */
	public NPC getClosestWithAction(String action, Filter<NPC> filter) {
		return getClosestWithAction(action, filter, 0);
	}

	/**
	 *
	 * Returns the closest NPC with the specified action, which is passed through the filter.
	 *
	 * @param action The action you wish the npc to have.
	 * @param filter The filter you wish to apply to filter out NPCs you're not looking for.
	 * @param distance The distance threshold that you wish to search
	 * @return The closest NPC with the specified action, which is passed through the filter.
	 */
	public NPC getClosestWithAction(String action, Filter<NPC> filter, int distance) {
		List<NPC> allNPCs = ctx.client.getLocalNPCs();
		List<NPC> npcs = new ArrayList<NPC>();
		if(allNPCs != null && allNPCs.size() > 0) {
			for(NPC npc : allNPCs) {
				if(!filter.accept(npc) || npc == null)
					continue;
				if(hasAction(npc, action) && (ctx.distance(npc) <= distance || distance == 0))
					npcs.add(npc);
			}
		}
		return (NPC) ctx.closest(npcs);
	}

	/**
	 *
	 * @see #getClosestWithId(osbot.api.util.Filter, int, int...)
	 */
	public NPC getClosestWithId(int... ids) {
		return getClosestWithId(ALL_FILTER, 0, ids);
	}

	/**
	 *
	 * @see #getClosestWithId(osbot.api.util.Filter, int, int...)
	 */
	public NPC getClosestWithId(int distance, int... ids) {
		return getClosestWithId(ALL_FILTER, distance, ids);
	}

	/**
	 *
	 * @see #getClosestWithId(osbot.api.util.Filter, int, int...)
	 */
	public NPC getClosestWithId(Filter<NPC> filter, int... ids) {
		return getClosestWithId(filter, 0, ids);
	}

	/**
	 *
	 * Returns the closest NPC with the specified id, which is passed through the filter.
	 *
	 * @param filter The filter you wish to apply to filter out NPCs you're not looking for.
	 * @param distance The distance threshold that you wish to search
	 * @param ids The npc ids that you're searching for
	 * @return The closest NPC with the specified id, which is passed through the filter.
	 */
	public NPC getClosestWithId(Filter<NPC> filter, int distance, int... ids) {
		List<NPC> allNPCs = ctx.client.getLocalNPCs();
		List<NPC> npcs = new ArrayList<NPC>();
		if(allNPCs != null && allNPCs.size() > 0) {
			for(NPC npc : allNPCs) {
				if(!filter.accept(npc) || npc == null)
					continue;
				if(ctx.distance(npc) <= distance || distance == 0) {
					for(int id : ids) {
						if(npc.getId() == id) {
							npcs.add(npc);
							break;
						}
					}
				}
			}
		}
		return (NPC) ctx.closest(npcs);
	}

	/**
	 *
	 * Checks whether the npc has the specified action
	 *
	 * @param npc the npc you're checking for the action
	 * @param action The action you're searching for.
	 * @return whether the npc has the specified action
	 */
	public boolean hasAction(NPC npc, String action) {
		NPCDefinition i = npc.getDefinition();
		if(i == null)
			return false;
		return ctx.hasAction(i.getActions(), action);
	}


	Filter<NPC> ALL_FILTER = new Filter<NPC>() {
		public boolean accept(NPC o) {
			return true;
		}
	};
}

package osbot.api.methods.impl;

import osbot.api.methods.MethodContext;
import osbot.api.util.Timer;
import org.osbot.script.rs2.def.ObjectDefinition;
import org.osbot.script.rs2.map.Position;
import org.osbot.script.rs2.model.Entity;
import org.osbot.script.rs2.model.RS2Object;
import org.osbot.utility.PathFinder;

import java.util.LinkedList;
import java.util.List;

public class DoorHandler {

	private MethodContext ctx;

	public DoorHandler(MethodContext ctx) {
		this.ctx = ctx;
	}

	/**
	 * Gets the next obstacle that needs to be handled and then handles that obstacle, so the user can pass through it.
	 *
	 * @param e the entity you wish to reach
	 * @return whether the obstacle has been handled (1), whether the obstacle is currently being handled (0) or whether the object cannot be handled (-1).
	 */
	public int handleNextObstacle(Entity e) throws InterruptedException {
		return handleNextObstacle(e.getPosition());
	}


	/**
	 * Gets the next obstacle that needs to be handled and then handles that obstacle, so the user can pass through it.
	 *
	 * @param p the position you wish to reach
	 * @return whether the obstacle has been handled (1), whether the obstacle is currently being handled (0) or whether the object cannot be handled (-1).
	 */
	public int handleNextObstacle(Position p) throws InterruptedException {
		RS2Object obstacle = getNextObstacle(p);
		if (obstacle != null) {
			if(obstacle.interact("open")) {
				Timer timer = new Timer((ctx.distance(obstacle)*600)+2000);
				while (timer.isRunning()) {
					if(obstacle != getNextObstacle(p))
						timer.stop();
					ctx.sleep(50);
				}
				return (obstacle != getNextObstacle(p)) ? 1 : 0;
			}
			return 0;
		}
		return -1;
	}

	/**
	 * Gets the next obstacle that needs to be handled to reach the specified position.
	 *
	 * @param e the entity you wish to reach
	 * @return the next obstacle that needs to be handled to reach the specified position.
	 */
	private RS2Object getNextObstacle(Entity e) {
		return getNextObstacle(e.getPosition());
	}

	/**
	 *
	 * Gets the next obstacle that needs to be handled to reach the specified position.
	 *
	 * @param p the position you wish to reach
	 * @return the next obstacle that needs to be handled to reach the specified position.
	 */
	public RS2Object getNextObstacle(Position p) {
		List<RS2Object> obstacles = getObstacles();
		List<Position> path = generatePath(p);
		if (path == null)
			return null;
		for (RS2Object obj : obstacles) {
			for (Position pos : path)
				if (obj.getPosition().equals(pos))
					return obj;
		}
		return null;
	}

	/**
	 * Generates a list of all the objects with a "open" action, in the current region.
	 *
	 * @return the list of objects that were generated.
	 */
	public List<RS2Object> getObstacles() {
		List<RS2Object> list = new LinkedList<>();
		for (RS2Object obj : ctx.client.getCurrentRegion().getObjects()) {
			if ((obj.getType() == 0) && (obj.getDefinition() != null) && (obj.getDefinition().getActions() != null) && (obj.getDefinition().getModelIds() != null) && (obj.getDefinition().getModelIds().length < 3)) {
				boolean canOpen = false;
				for (String action : obj.getDefinition().getActions()) {
					if ((action != null) && (action.equalsIgnoreCase("open"))) {
						canOpen = true;
					}
				}
				if (canOpen)
					list.add(obj);
			}
		}
		return list;
	}

	/**
	 *
	 * Generates a path (List of nodes) to the specified position
	 *
	 * @param p the position you want to generate a path too
	 * @return the list of nodes that were generated.
	 */
	private List<Position> generatePath(Position p) {
		PathFinder pf = new PathFinder();
		int[][] flags = generateModifiedClippingData();
		List<Position> path = pf.findPath(ctx.bot, p, flags);
		if (!pf.foundPath())
			return null;
		if (path == null)
			path = new LinkedList<>();
		path.add(0, ctx.myPosition());
		return path;
	}

	/**
	 *
	 * gets and modifies the current clipping flags of each door, to allow the path generator, to generate paths through the doors, so the doors can be handled later.
	 *
	 * @return the modified clipping flags generated
	 */
	private int[][] generateModifiedClippingData() {
		int[][] origFlags = ctx.client.getClippingPlanes()[ctx.client.getPlane()].getTileFlags();
		int[][] flags = new int[origFlags.length][origFlags.length];
		for (int x = 0; x < flags.length; x++)
			System.arraycopy(origFlags[x], 0, flags[x], 0, flags.length);
		for (RS2Object obj : getObstacles()) {
			int lx = obj.getLocalX();
			int ly = obj.getLocalY();
			ObjectDefinition def = obj.getDefinition();
			if (def.isClipping1()) {
				switch (obj.getOrientation()) {
					case 0:
					case 2:
						flags[lx][ly] &= -586;
						break;
					case 1:
					case 3:
						flags[lx][ly] &= -1171;
				}

			}

			if (def.getClipping2() != 0) {
				if (0 == obj.getOrientation()) {
					flags[lx][ly] &= -129;
					flags[(lx - 1)][ly] &= -9;
				}

				if (1 == obj.getOrientation()) {
					flags[lx][ly] &= -3;
					flags[lx][(ly + 1)] &= -33;
				}

				if (2 == obj.getOrientation()) {
					flags[lx][ly] &= -9;
					flags[(lx + 1)][ly] &= -129;
				}

				if (3 == obj.getOrientation()) {
					flags[lx][ly] &= -33;
					flags[lx][(ly - 1)] &= -3;
				}

				if (def.isClipping3()) {
					if (0 == obj.getOrientation()) {
						flags[lx][ly] &= -65537;
						flags[(lx - 1)][ly] &= -4097;
					}

					if (obj.getOrientation() == 1) {
						flags[lx][ly] &= -1025;
						flags[lx][(ly + 1)] &= -16385;
					}

					if (2 == obj.getOrientation()) {
						flags[lx][ly] &= -4097;
						flags[(lx + 1)][ly] &= -65537;
					}

					if (3 == obj.getOrientation()) {
						flags[lx][ly] &= -16385;
						flags[lx][(ly - 1)] &= -1025;
					}
				}
			}
		}
		return flags;
	}
}
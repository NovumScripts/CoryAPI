package osbot.api.methods.impl;

import osbot.api.methods.MethodContext;
import osbot.api.util.ObstacleListener;
import org.osbot.script.rs2.map.Position;

import java.util.LinkedList;

/**
 * Created by
 * User: Cory
 * Date: 11/09/13
 */
public class Walking {

	private LinkedList<ObstacleListener> obstacleListeners;
	private MethodContext ctx;

	public Walking(MethodContext ctx) {
		this.ctx = ctx;
		obstacleListeners = new LinkedList<>();
	}

	/**
	 *
	 * @see #walk(boolean, int, org.osbot.script.rs2.map.Position...)
	 */
	public boolean walk(Position... path) throws InterruptedException {
		return walk(false, path);
	}

	/**
	 *
	 * @see #walk(boolean, int, org.osbot.script.rs2.map.Position[]...)
	 */
	public boolean walk(Position[]... path) throws InterruptedException {
		return walk(false, path);
	}

	/**
	 *
	 * @see #walk(boolean, int, org.osbot.script.rs2.map.Position...)
	 */
	public boolean walk(boolean reverse, Position... path) throws InterruptedException {
		return walk(reverse, 5, path) == 1;
	}

	/**
	 *
	 * @see #walk(boolean, int, org.osbot.script.rs2.map.Position[]...)
	 */
	public boolean walk(boolean reverse, Position[]... path) throws InterruptedException {
		return walk(reverse, 5, path) == 1;
	}

	/**
	 *
	 * @see #walk(boolean, int, org.osbot.script.rs2.map.Position...)
	 */
	public boolean walk(int distance, Position... path) throws InterruptedException {
		return walk(false, distance, path) == 1;
	}

	/**
	 *
	 * @see #walk(boolean, int, org.osbot.script.rs2.map.Position[]...)
	 */
	public boolean walk(int distance, Position[]... path) throws InterruptedException {
		return walk(false, distance, path) == 1;
	}

	/**
	 *
	 * Walks through an array of tiles to reach the destination
	 *
	 * @param reverse whether we're going through the tiles in reverse.
	 * @param distance the distance threshold of the destination.
	 * @param path the tiles that we wish to traverse through
	 * @return whether the tiles has been traversed (1), whether the tiles are currently being traversed (0), whether there was an error traversing a tile (-1) or whether the tiles cannot be traversed (-2).
	 * @throws InterruptedException
	 */
	public int walk(boolean reverse, int distance, Position... path) throws InterruptedException {
		if(ctx.distance(path[path.length-1]) <= distance && !reverse)
			return 1;
		if(ctx.distance(path[0]) <= distance && reverse)
			return 1;

		for(int i = reverse ? 0 : path.length-1; reverse ? i < path.length : i >= 0; i += reverse ? 1 : -1) {

			if(path[i] == null || ctx.distance(path[i]) <= distance)
				return -1;

			if((ctx.distance(path[i]) <= 18) || i == (reverse ? path.length-1 : 0)) {
				if(!ctx.canReach(path[i])) {
					if(ctx.doorHandler.handleNextObstacle(path[i]) == -1)
						return -2;
				} else
					ctx.walk(path[i]);
				return 0;
			}
		}
		return -2;
	}

	/**
	 *
	 * Walks through an array of paths to reach the destination
	 *
	 * @param reverse whether we're going through the paths in reverse.
	 * @param distance the distance threshold of the destination.
	 * @param paths the paths that we wish to traverse through
	 * @return whether the paths has been traversed (1), whether the paths are currently being traversed (0), whether there was an error traversing the path (-1) or whether the path cannot be traversed (-2).
	 * @throws InterruptedException
	 */
	public int walk(boolean reverse, int distance, Position[]... paths) throws InterruptedException {
		Position[] lastPath = paths[paths.length-1], firstPath = paths[0];
		if(ctx.distance(lastPath[lastPath.length-1]) <= distance && !reverse)
			return 1;
		if(ctx.distance(firstPath[0]) <= distance && reverse)
			return 1;

		for(int i = reverse ? 0 : paths.length-1; reverse ? i < paths.length : i >= 0; i += reverse ? 1 : -1) {
			Position[] path = paths[i];
			int status = walk(reverse, distance, path);

			if(status == -1)
				return -1;

			if(status == 0)
				return 0;

			if(status != 1 && (i == (reverse ? path.length-1 : 0)) && !executeListener())
				ctx.log("Error on path, error code: "+status);
		}

		executeListener();
		return -2;
	}

	/**
	 *
	 * Adds an #ObstacleListener to our list of listeners
	 *
	 * @param listener the ObstacleListener that you wish to add
	 */
	public void addObstacleListener(ObstacleListener listener) {
		obstacleListeners.add(listener);
	}

	/**
	 *
	 * Executes the listeners so the script can handle any tasks or obstacles it needs before we carry on with the path.
	 *
	 * @return whether or not it has executed a listener
	 * @throws InterruptedException
	 */
	private boolean executeListener() throws InterruptedException {
		boolean executed = false;
		for(ObstacleListener obstacleListener : obstacleListeners) {
			if(obstacleListener == null)
				continue;
			obstacleListener.handleObstacle();
			executed = true;
		}
		return executed;
	}
}

package osbot.api.methods.impl;

import osbot.api.methods.MethodContext;
import org.osbot.script.mouse.MouseDestination;
import org.osbot.script.mouse.RectangleDestination;
import org.osbot.script.rs2.ui.Option;

import java.awt.*;
import java.util.List;

/**
 * Created by
 * User: Cory
 * Date: 12/09/13
 */
public class Menu {

	private MethodContext ctx;

	public Menu(MethodContext ctx) {
		this.ctx = ctx;
	}

	/**
	 *
	 * Selects the action specified in the context menu in-game.
	 *
	 * @param action the action you wish the method to select.
	 * @return whether it has selected the action specified.
	 * @throws InterruptedException
	 */
	public boolean click(String action) throws InterruptedException {
		if (ctx.menu.contains(action)) {
			if (ctx.client.getMenu().get(0).action.contains(action)) {
				ctx.client.clickMouse(false);
				return true;
			} else {
				if (!ctx.client.isMenuOpen())
					ctx.client.clickMouse(true);
				if (ctx.client.isMenuOpen()) {
					int i = getIndex(action);
					MouseDestination click = new RectangleDestination(new Rectangle(ctx.client.getMenuX(), ctx.client.getMenuY() + 18 + (i * 14 + 1), ctx.client.getMenuWidth(), 14));
					ctx.client.moveMouse(click, false);
					ctx.sleep(100);
					if (click.destinationReached(ctx.client.getMousePosition())) {
						ctx.client.clickMouse(false);
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 *
	 * Gets the index of the specified action within the in-game context menu.
	 *
	 * @param action the action you wish the get the index of.
	 * @return the index of the specified action within the in-game context menu.
	 */
	public int getIndex(String action) {
		List<Option> s = ctx.client.getMenu();
		for (int i = 0; i < s.size(); i++) {
			if (s.get(i).action.equalsIgnoreCase(action)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 *
	 * Checks whether the specified action is within the in-game context menu.
	 *
	 * @param action the action you wish to check exists.
	 * @return the index of the specified action within the in-game context menu.
	 */
	public boolean contains(String action) {
		for (Option n : ctx.client.getMenu()) {
			if (n.action.equalsIgnoreCase(action))
				return true;
		}
		return false;
	}
}

package osbot.api.methods.impl;

import osbot.api.methods.MethodContext;
import org.osbot.script.rs2.ui.Inventory;

/**
 * Created by
 * User: Cory
 * Date: 11/09/13
 */
public class InventoryHandler {

	private MethodContext ctx;

	public InventoryHandler(MethodContext ctx) {
		this.ctx = ctx;
	}

	/**
	 *
	 * Drops all the items in the inventory.
	 *
	 * @throws InterruptedException
	 */
	public void dropInventory() throws InterruptedException {
		Inventory inv = ctx.client.getInventory();
		for(int i = 0; i < 28; i++) {
			if(inv.getItems()[i] == null)
				continue;
			inv.interactWithSlot(i, "Drop");
		}
	}

	/**
	 *
	 * Returns whether the inventory is full or not
	 *
	 * @return whether the inventory is full or not
	 */
	public boolean isFull() {
		return ctx.client.getInventory().isFull();
	}

	/**
	 *
	 * Returns whether the inventory is empty or not
	 *
	 * @return whether the inventory is empty or not
	 */
	public boolean isEmpty() {
		return ctx.client.getInventory().isFull();
	}

}

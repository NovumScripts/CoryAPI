package osbot.api.methods.impl;

import osbot.api.methods.MethodContext;
import osbot.api.util.Timer;
import org.osbot.script.rs2.model.Item;
import org.osbot.script.rs2.model.NPC;
import org.osbot.script.rs2.model.RS2Object;
import org.osbot.script.rs2.ui.Bank;
import org.osbot.script.rs2.ui.Inventory;
import org.osbot.script.rs2.ui.RS2InterfaceChild;

/**
 * Created by
 * User: Cory
 * Date: 11/09/13
 */
public class BankHandler {

	private MethodContext ctx;

	public BankHandler(MethodContext ctx) {
		this.ctx = ctx;
	}

	/**
	 *
	 * Returns whether the player has the specified amount of items that
	 * match the id specified.
	 *
	 * @param id the item id you wish to check.
	 * @param amount the amount of said id you are checking for.
	 * @return whether the player has the specified amount of items that match the id specified.
	 * @throws InterruptedException
	 */
	public boolean hasAmount(int id, int amount) throws InterruptedException {
		Inventory inv = ctx.client.getInventory();
		Bank bank = ctx.client.getBank();
		if(inv != null && bank != null) {
			if(amount == 0)
				return inv.getAmount(id) >= 1;
			if(amount >= 1)
				return inv.getAmount(id) == amount;
		}
		return false;
	}

	/**
	 *
	 * Withdraws or deposits the amounts required to ensure the user has the exact amount of the specified id.
	 *
	 * @param id the item id you wish to check.
	 * @param amount the amount of said id you are aiming for.
	 * @throws InterruptedException
	 */
	public void ensureUserHasAmount(int id, int amount) throws InterruptedException {
		Inventory inv = ctx.client.getInventory();
		Bank bank = ctx.client.getBank();
		if(inv != null && bank != null) {
			if(amount >= 1 && inv.getAmount(id) != amount) {
				if(inv.getAmount(id) > amount) {
					if(bank.deposit(id, (int) (inv.getAmount(id) - amount))) {
						Timer timer = new Timer(2000);
						while (timer.isRunning() && (inv.getAmount(id) > amount))
							ctx.sleep(50);
					}
				} else {
					if(!bank.contains(id)) {
						bank.close();
						ctx.stop();
						return;
					}

					if(inv.getEmptySlots() < amount-inv.getAmount(id) && amount <= 28) {
						Item itemInInv = inv.getItemForId(id);
						if(itemInInv == null || itemInInv.getAmount() <= 1)
							return;
					}

					if(bank.withdraw(id, (int) (amount-inv.getAmount(id)))) {
						Timer timer = new Timer(2000);
						while (timer.isRunning() && (inv.getAmount(id) < amount))
							ctx.sleep(50);
					}
				}
			} else
				bank.depositAll(id);
		}
	}

	/**
	 *
	 * Returns whether or not the bank screen or deposit box screen is currently open
	 *
	 * @return whether or not the bank screen or deposit box screen is currently open
	 */
	public boolean isOpen() {
		if(ctx.client.getInterface(11) != null) {
			RS2InterfaceChild c = ctx.client.getInterface(11).getChild(61);
			if(c != null && c.isVisible())
				return true;
		}
		return ctx.client.getBank().isOpen();
	}

	/**
	 *
	 * Returns whether the player is in a position that they are able to open a box or a deposit box.
	 *
	 * @return whether the player is in a position that they are able to open a box or a deposit box.
	 */
	public boolean canOpen() {
		RS2Object booth = ctx.objects.getClosestWithAction("Bank");
		if(booth != null && ctx.distance(booth) <= 5)
			return true;

		RS2Object deposit = ctx.objects.getClosestWithAction("Deposit");
		if(deposit != null && ctx.distance(deposit) <= 5)
			return true;

		NPC banker = ctx.npcs.getClosestWithAction("Bank");
		return (banker != null && ctx.distance(banker) <= 5);
	}

	/**
	 *
	 *  Opens the nearest bank or deposit box
	 *
	 * @throws InterruptedException
	 */
	public void openBank() throws InterruptedException {
		if(ctx.client.getBank().isOpen())
			return;
		RS2Object object = ctx.objects.getClosestWithAction("Bank");
		if(object != null) {
			object.interact("Bank");
		} else {
			RS2Object deposit = ctx.objects.getClosestWithAction("Deposit");
			if(deposit != null) {
				deposit.interact("Deposit");
			} else {
				NPC banker = ctx.npcs.getClosestWithAction("Bank");
				if(banker != null) {
					banker.interact("Bank");
				}
			}
		}
	}
}

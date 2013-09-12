package osbot.api.methods;

import osbot.api.methods.impl.*;
import org.osbot.script.Script;
import org.osbot.script.rs2.model.Entity;

import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Created by
 * User: Cory
 * Date: 11/09/13
 */
public class MethodContext extends Script {

	public final Walking walking = new Walking(this);
	public final DoorHandler doorHandler = new DoorHandler(this);
	public final NPCs npcs = new NPCs(this);
	public final Objects objects = new Objects(this);
	public final Skills skills = new Skills(this);
	public final BankHandler bank = new BankHandler(this);
	public final InventoryHandler inv = new InventoryHandler(this);
	public final Menu menu = new Menu(this);
	public int mouseX, mouseY, clickX, clickY;

	public String format(long milliSeconds) {
		long secs = milliSeconds/1000;
		return String.format("%02d:%02d:%02d", secs/3600, (secs%3600)/60, (secs%60));
	}

	/**
	 *
	 * Returns whether the array of actions specified contains the action specified.
	 *
	 * @param actions the array of actions we're searching through
	 * @param action the action we're searching for
	 * @return whether the array of actions specified contains the action specified.
	 */
	public boolean hasAction(String[] actions, String action) {
		if(action == null)
			return false;
		for(String a : actions) {
			if(a == null)
				continue;
			if(action.toLowerCase().equals(a.toLowerCase()))
				return true;
		}
		return false;
	}

	/**
	 *
	 * Returns the closest entity from within the list
	 *
	 * @param entities the list of entities we're finding the closest from
	 * @return the closest entity from within the list
	 */
	public Entity closest(List<? extends Entity> entities) {
		Entity closest = null;
		for (Entity entity : entities) {
			if(closest == null)
				closest = entity;
			if(distance(entity) < distance(closest))
				closest = entity;
		}
		return closest;
	}

	@Override
	public void mouseMoved(MouseEvent a) {
		if(a.getSource().getClass().getSimpleName().equalsIgnoreCase("client")) //bot input
			return;
		this.mouseX = a.getX();
		this.mouseY = a.getY();
	}

	@Override
	public void mousePressed(MouseEvent a) {
		if(a.getSource().getClass().getSimpleName().equalsIgnoreCase("client")) //bot input
			return;
		this.clickX = a.getX();
		this.clickY = a.getY();
	}
}

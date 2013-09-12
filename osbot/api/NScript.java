package osbot.api;

import osbot.api.methods.MethodContext;
import osbot.api.util.Action;

import java.awt.*;
import java.util.LinkedList;

/**
 * Created by
 * User: Cory
 * Date: 11/09/13
 */
public abstract class NScript extends MethodContext {

	private LinkedList<Action> actions = new LinkedList<>();
	private boolean isConfigured;
	public long startTime = 0;

	/**
	 * @see #start()
	 * @deprecated use {@link #start()} instead
	 */
	public void onStart() {}

	/**
	 * Gets executed at the start of the script, when
	 * the client is successfully logged in
	 */
	public void start(){};

	/**
	 * @see #loop()
	 * @deprecated use {@link #loop()} instead
	 */
	public int onLoop() throws InterruptedException {

		if(!isConfigured) {
			start();
			startTime = System.currentTimeMillis();
			isConfigured = true;
		}

		for(Action action : actions) {
			if(action.condition()) {
				sleep(action.execute());
				return loop();
			}
		}

		return loop();
	}

	/**
	 * @see org.osbot.script.Script#onLoop()
	 */
	public abstract int loop() throws InterruptedException;

	/**
	 *
	 * Adds the list of actions specified to the actions list.
	 *
	 * @param actions the list of actions you wish to add to the actions list.
	 */
	public void submit(Action... actions) {
		for (Action action : actions) {
			this.actions.add(action);
		}
	}

	/**
	 * @see org.osbot.script.Script#onMessage(org.osbot.script.rs2.ui.Message)
	 */
	public void onMessage(String msg) throws InterruptedException {
		for(Action action : actions) {
			action.onMessage(msg.toLowerCase());
		}
	}

	/**
	 * @see #paint(java.awt.Graphics2D)
	 * @deprecated use {@link #paint(java.awt.Graphics2D)} instead
	 */
	public void onPaint(Graphics g) {
		for(Action action : actions) {
			try {
				action.onPaint((Graphics2D) g.create());
			} catch (InterruptedException e) {}
		}
		paint((Graphics2D) g.create());
	}

	/**
	 * @see org.osbot.script.Script#onPaint(java.awt.Graphics)
	 */
	public void paint(Graphics2D g){}
}

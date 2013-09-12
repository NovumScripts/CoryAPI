package osbot.api.util;

import osbot.api.methods.MethodContext;

import java.awt.*;

/**
 * Created by
 * User: Cory
 * Date: 11/09/13
 */
public abstract class Action {

	protected MethodContext ctx;

	public Action(MethodContext ctx) {
		this.ctx = ctx;
	}

	/**
	 *
	 * Returns whether or not the action should be executed.
	 *
	 * @return whether or not the action should be executed.
	 * @throws InterruptedException
	 */
	public boolean condition() throws InterruptedException {
		return true;
	}

	/**
	 *
	 * The body were the action is carried out.
	 *
	 * @return the time to sleep after the method has been executed
	 * @throws InterruptedException
	 */
	public abstract int execute() throws InterruptedException;

	/**
	 *
	 * @see org.osbot.script.Script#onPaint(java.awt.Graphics)
	 */
	public void onPaint(Graphics2D g) throws InterruptedException {}

	/**
	 *
	 * @see org.osbot.script.Script#onMessage(org.osbot.script.rs2.ui.Message)
	 */
	public void onMessage(String msg) throws InterruptedException {}
}
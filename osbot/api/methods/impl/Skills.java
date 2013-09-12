package osbot.api.methods.impl;

import osbot.api.NScript;
import osbot.api.methods.MethodContext;
import org.osbot.script.rs2.skill.Skill;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: Cory
 * Date: 23/08/13
 * Time: 20:52
 */
public class Skills {

	private HashMap<Skill, int[]> trackers;
	private LinkedList<Skill> paint;
	private boolean paintGainedXP;
	private boolean initialised;
	private MethodContext ctx;

	public Skills(MethodContext ctx) {
		this.ctx = ctx;
		init();
	}

	/**
	 * Initialises resources used by the class, and stores the start xp and level for each skill, so we can track them.
	 */
	private void init() {
		trackers = new HashMap<>();
		paint = new LinkedList<>();
		paintGainedXP = true;
		if(ctx.client != null && ctx.client.getLoginState() == 30) {
			initialised = true;
			for (Skill skill : Skill.values()) {
				if (skill != null) {
					trackers.put(skill, new int[]{currentXp(skill), actualLvl(skill)});
				}
			}
		}
	}

	/**
	 *
	 * The xp in the specified skill when we started tracking it
	 *
	 * @param skill the skill we're checking at what xp we started tracking.
	 * @return xp in the specified skill when we started tracking it
	 */
	public int startXp(Skill skill) {
		if(!initialised)
			init();
		return get(skill)[0];
	}


	/**
	 *
	 * The current xp in the specified skill
	 *
	 * @param skill the skill we're checking our current xp in.
	 * @return current xp in the specified skill
	 */
	public int currentXp(Skill skill) {
		if(!initialised)
			init();
		return ctx.client.getSkills().getExperience(skill);
	}

	/**
	 *
	 * The amount of xp that we're off from our next level in the specified skill
	 *
	 * @param skill the skill we're calculating how much xp we are from the next level.
	 * @return amount of xp that we're off from our next level in the specified skill
	 */
	public int xpToLvl(Skill skill) {
		return getXPForLevel(currentLvl(skill)+1)-currentXp(skill);
	}


	/**
	 *
	 * The amount of xp gained in the specified skill
	 *
	 * @param skill the skill we're checking our xp gained in.
	 * @return amount of xp gained in the specified skill
	 */
	public int xpGained(Skill skill) {
		if(!initialised)
			init();
		return currentXp(skill)-startXp(skill);
	}


	/**
	 *
	 * Gets the average amount of xp that you're expected to gain per hour.
	 *
	 * @param skill the skill you wish to calculate the xp per hour for.
	 * @param startTime the time of which you started the script.
	 * @return the average amount of xp that you're expected to gain per hour.
	 */
	public long xpPerHour(Skill skill, long startTime) {
		return actionsPerHour(xpGained(skill), startTime);
	}

	/**
	 *
	 * The level in the specified skill when we started tracking it
	 *
	 * @param skill the skill we're checking at what level we started tracking.
	 * @return level in the specified skill when we started tracking it
	 */
	public int startLvl(Skill skill) {
		if(!initialised)
			init();
		return get(skill)[1];
	}


	/**
	 *
	 * The current level in the specified skill
	 *
	 * @param skill the skill we're checking our current level in.
	 * @return current level in the specified skill
	 */
	public int currentLvl(Skill skill) {
		if(!initialised)
			init();
		return ctx.client.getSkills().getCurrentLevel(skill);
	}

	/**
	 *
	 * The actual level in the specified skill
	 *
	 * @param skill the skill we're checking our actual level in.
	 * @return actual level in the specified skill
	 */
	public int actualLvl(Skill skill) {
		if(!initialised)
			init();
		return ctx.client.getSkills().getLevel(skill);
	}

	/**
	 *
	 * The amount of levels gained in the specified skill
	 *
	 * @param skill the skill we're checking our levels gained in.
	 * @return amount of levels gained in the specified skill
	 */
	public int lvlGained(Skill skill) {
		if(!initialised)
			init();
		return actualLvl(skill)-startLvl(skill);
	}

	/**
	 *
	 * Gets the average amount of levels that you're expected to gain per hour.
	 *
	 * @param skill the skill you wish to calculate the levels per hour for.
	 * @param startTime the time of which you started the script.
	 * @return the average amount of levels that you're expected to gain per hour.
	 */
	public long lvlPerHour(Skill skill, long startTime) {
		return actionsPerHour(lvlGained(skill), startTime);
	}

	/**
	 *
	 * Returns how many milliseconds you are away from your next level
	 *
	 * @param skill the skill you wish calculate the time till.
	 * @param startTime the time of which you started the script.
	 * @return how many milliseconds you are away from your next level;
	 */
	public long timeToLevel(Skill skill, long startTime) {
		return (long) (((double) xpToLvl(skill) * 3600000.0) / (double) xpPerHour(skill, startTime));
	}

	/**
	 *
	 * Calculates your percentage to your next level
	 *
	 * @param skill the skill you wish to retrieve the percentage for.
	 * @return your percentage to your next level
	 */
	public double percentToLvl(Skill skill) {
		double xpToLvl = (double)getXPForLevel(currentLvl(skill)+1)-(double)getXPForLevel(currentLvl(skill));
		double xpGained = (double)currentXp(skill) - (double)getXPForLevel(currentLvl(skill));
		return xpGained / xpToLvl;
	}

	/**
	 *
	 * Gets the stored information for the specified skill
	 *
	 * @param skill the skill you wish to retrieve the stores information for.
	 * @return the stored information for the specified skill
	 */
	private int[] get(Skill skill) {
		if(trackers.containsKey(skill))
			return trackers.get(skill);
		return new int[]{0, 0};
	}

	/**
	 *
	 * Calculates how many actions will be carried out on average per hour
	 *
	 * @param actions the amount of actions you have currently done
	 * @param startTime the time of which you started the script.
	 * @return how many actions will be carried out on average per hour
	 */
	private long actionsPerHour(long actions, long startTime) {
		return (long) (3600000D / (System.currentTimeMillis() - startTime) * actions);
	}

	/**
	 *
	 * Gets the xp required for the level specified.
	 *
	 * @param level the level you wish to get the exp for.
	 * @return the xp required for the level specified.
	 */
	public int getXPForLevel(int level) {
		for (int lvl = 1, output = 0, points = 0; lvl <= level; lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			if (lvl >= level)
				return output;
			output = (int) Math.floor(points / 4);
		}
		return 0;
	}

	/**
	 *
	 * Adds the specified skills to the paint list, so they will always be rendered, whether they have gained xp or not.
	 *
	 * @param skills the skills you wish to add.
	 */
	public void addToPaint(Skill... skills) {
		for (Skill skill : skills)
			paint.add(skill);
	}

	/**
	 *
	 * Sets #paintGainedXP to the specified value, which toggles whether or not it will paint all skills that you have gained xp in.
	 *
	 * @param paintGainedXP
	 */
	public void setPaintGainedXP(boolean paintGainedXP) {
		this.paintGainedXP = paintGainedXP;
	}

	/**
	 *
	 * Paints the specified skills on-screen
	 *
	 * @param g the graphics instance that will be painting the skills.
	 */
	public void paint(Graphics2D g) {
		int x = 413, y = 7, hx = 0, hy = 0;
		Skill hovered = null;

		for (Skill skill : Skill.values()) {
			if((xpGained(skill) > 0 && paintGainedXP) || paint.contains(skill)) {
				g.setColor(new Color(0,0,0,0.5f));
				g.fillRect(x, y, 100, 20);

				g.setColor(new Color(Colors.valueOf(skill.name()).color));
				g.fillRect(x+2, y+2, 96, 16);

				g.setColor(new Color(Colors.valueOf(skill.name()).trim));
				g.fillRect(x + 2, y + 2, (int) (percentToLvl(skill) * 96), 16);

				g.setColor(Color.WHITE);
				g.drawString(skill.name(), x+4, y+15);

				if(ctx.mouseX >= x && ctx.mouseX <= x + 100 && ctx.mouseY >= y && ctx.mouseY <= y + 20) {
					hovered = skill;
					hx = x;
					hy = y;
				}

				if((y += 22) >= 324) {
					x -= 104;
					y = 7;
				}
			}
		}
		if(hovered != null)
			drawHover(g, hovered, hx-25, hy);
	}

	/**
	 *
	 * Draws the hover box, for the skill that you're currently hovering.
	 *
	 * @param g the graphics instance that will be rendering the box.
	 * @param skill the skill that you're currently hovering.
	 * @param x the x position of the hover box
	 * @param y the y position of the hover box
	 */
	private void drawHover(Graphics2D g, Skill skill, int x, int y) {
		g.setColor(new Color(0,0,0,0.5f));
		g.fillRect(x, y, 150, 146);

		g.setColor(new Color(Colors.valueOf(skill.name()).color));
		g.fillRect(x + 2, y + 2, 146, 142);

		g.setColor(new Color(Colors.valueOf(skill.name()).trim));
		g.fillRect(x + 4, y + 4, 142, 23);

		g.setColor(Color.WHITE);
		g.drawString(skill.name(), x + 10, y + 20);
		g.drawString("XP Gained: "+xpGained(skill) + "", x + 10, y + 45);
		g.drawString("XP Per/Hr: "+xpPerHour(skill, ((NScript)ctx).startTime)+"", x+10, y+60);
		g.drawString("XP To LVL: "+xpToLvl(skill)+"", x+10, y+75);
		g.drawString("LVL Gained: "+lvlGained(skill)+"", x+10, y+90);
		g.drawString("LVL Per/Hr: "+lvlPerHour(skill, ((NScript)ctx).startTime)+"", x+10, y+105);
		g.drawString("% To LVL("+(currentLvl(skill)+1)+"): "+(int)(percentToLvl(skill)*100)+"", x+10, y+120);
		if(xpGained(skill) > 0)
			g.drawString("Time To LVL: "+ctx.format(timeToLevel(skill, ((NScript)ctx).startTime))+"", x+10, y+135);
		else {
			g.drawString("Time To LVL: "+ctx.format(0)+"", x+10, y+135);
		}
	}

	private enum Colors {
		AGILITY(0x1f215b, 0x6b1f1a),
		ATTACK(0x6b1f1a, 0x98931b),
		CONSTRUCTION(0x8b816f, 0x945819),
		COOKING(0x451f44, 0x5c1406),
		CRAFTING(0x6d533c, 0x6d533c),
		DEFENCE(0x6375b0, 0xc1b9b8),
		FARMING(0x214b23, 0x6c8649),
		FIREMAKING(0xa24508, 0xb29a06),
		FISHING(0x687a88, 0xa69718),
		FLETCHING(0x02272f, 0xa46108),
		HERBLORE(0x085a0d, 0x989428),
		HITPOINTS(0xb2b0a6, 0xa12111),
		HUNTER(0x454135, 0x150f0f),
		MAGIC(0x6a6964, 0x191f6d),
		MINING(0x322f1e, 0x548faf),
		PRAYER(0xbdb3b3, 0xbe8f18),
		RANGED(0x4d6317, 0x7c4728),
		RUNECRAFTING(0xa2a196, 0xcdb317),
		SLAYER(0x1f1c1c, 0x6f170d),
		SMITHING(0x494939, 0xcdb317),
		STRENGTH(0x086844, 0xa12111),
		THIEVING(0x6a3b5a, 0x383333),
		WOODCUTTING(0x826a3b, 0x2d6541);

		private Colors(int color, int trim) {
			this.color = color;
			this.trim = trim;
		}

		private int color;
		private int trim;
	}
}
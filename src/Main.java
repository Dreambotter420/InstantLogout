
import java.time.LocalDateTime;

import org.dreambot.api.Client;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.combat.Combat;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.input.mouse.MouseSettings;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.settings.PlayerSettings;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.randoms.RandomEvent;
import org.dreambot.api.randoms.RandomManager;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManager;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.interactive.Player;
import org.dreambot.api.wrappers.items.Item;
import org.dreambot.api.wrappers.widgets.WidgetChild;




@ScriptManifest(name = "PKer Instant Logout",
description = "Start script, wander in wildy, get logged out when pker is near!", 
author = "Dreambotter420", version = 0.01, category = Category.MISC)
public class Main extends AbstractScript
{
	@Override
	public void onStart()
	{
		MethodProvider.log("Starting PKer Logout Script!");
		Sleep.dt = LocalDateTime.now();
		if(!Client.isLoggedIn())
		{
			MethodProvider.sleepUntil(() -> Client.isLoggedIn(), 20000);
		}
        getRandomManager().disableSolver(RandomEvent.LOGIN);
	}
	@Override
	public void onStart(String[] i)
	{
		MethodProvider.log("OnStart Script quickstart!");
		onStart();
	}
	public static boolean threadAlive = false;
	@Override
	public int onLoop() 
	{
		if(shouldPanic())
		{
			logout();
			return Sleep.calculate(111,420);
		}
		if(!threadAlive)
		{
			PKerListener listener = new PKerListener();
			Thread t = new Thread(listener);
			t.start();
		}
		return Sleep.calculate(69,111);
	}
	/**
     * checks for any alive players within a radius of 10, returns true if so, false otherwise
     * @return
     */
    public static boolean shouldPanic()
    {
    	if(!Client.isLoggedIn() || !Combat.isInWild()) return false;
		final int maxBracket = Players.localPlayer().getLevel() + Combat.getWildernessLevel() + 1;
		final int minBracket = Players.localPlayer().getLevel() - Combat.getWildernessLevel() - 1;
		final Area squareRadius15 = new Area(
				(Players.localPlayer().getX() - 15),
				(Players.localPlayer().getY() - 15),
				(Players.localPlayer().getX() + 15),
				(Players.localPlayer().getY() + 15),Players.localPlayer().getZ());
    	for(Player p : Players.all())
		{
			if(p == null || !p.exists() || p.getName().equals(Players.localPlayer().getName()) || p.getHealthPercent() == 0) continue;
			if(squareRadius15.contains(p) && 
					p.getLevel() >= minBracket && 
					p.getLevel() <= maxBracket) 
			{
				MethodProvider.log("~~~PKer Panic!~~~");
				MethodProvider.log("Their name / cb lvl / wildy lvl :: " + p.getName() + " / " + p.getLevel() + " / " + Combat.getWildernessLevel());
				MethodProvider.log("Our cb lvl: " + Players.localPlayer().getLevel());
				MethodProvider.log(p.getTile().toString()+" <-- Their location");
				MethodProvider.log(Players.localPlayer().getTile().toString()+" <-- Our location" );
				return true;
			}
		}
    	return false;
    }
    public static boolean logout()
    {
    	if(!Client.isLoggedIn()) 
    	{
    		MethodProvider.log("Logged out!");
    		return true;
    	}
    	if(MouseSettings.getSpeed() != 100) MouseSettings.setSpeed(100);
    	if(Dialogues.areOptionsAvailable()) 
    	{
    		Walking.clickTileOnMinimap(Players.localPlayer().getTile());
    		return false;
    	}
    	if(Dialogues.canContinue()) 
    	{
    		Dialogues.continueDialogue();
    		return false;
    	}
    	if(!Tabs.isOpen(Tab.LOGOUT)) 
    	{
    		Tabs.openWithFKey(Tab.LOGOUT);
    		return false;
    	}
    	WidgetChild logoutButtonWorlds = Widgets.getWidgetChild(69, 23);
    	WidgetChild logoutButton = Widgets.getWidgetChild(182, 8);
    	if(logoutButtonWorlds != null && logoutButtonWorlds.isVisible())
    	{
    		if(logoutButtonWorlds.interact("Logout")) MethodProvider.log("Clicked logout button! (worlds list)");
    		return false;
    	} 
    	if(logoutButton != null && logoutButton.isVisible())
    	{
    		if(logoutButton.interact("Logout")) MethodProvider.log("Clicked logout button!");
    		return false;
    	}
    	MethodProvider.log("Didn't execute any logout buttons when should have!!");
    	return false;
    }
}
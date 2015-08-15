package enviromine.handlers.keybinds;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

import enviromine.core.EM_ConfigHandler;
import enviromine.core.EM_Settings;
import enviromine.core.EnviroMine;

import org.lwjgl.input.Keyboard;

public class ReloadCustomObjects
{
	public static void doReloadConfig()
	{
		Minecraft mc = Minecraft.getMinecraft();
		if((!(Minecraft.getMinecraft().isSingleplayer()) || !EnviroMine.proxy.isClient()) && Minecraft.getMinecraft().thePlayer != null)
		{
			if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
			{
				mc.thePlayer.addChatMessage(new ChatComponentText("Single player only function."));
			}
			return;
		}
		// prevents key press firing while gui screen or chat open, if that's what you want
		// if you want your key to be able to close the gui screen, handle it outside this if statement
		if(mc.currentScreen == null)
		{
			
			if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
			{
				try
				{
					mc.thePlayer.addChatMessage(new ChatComponentText("Reloading Configs..."));
					EM_Settings.armorProperties.clear();
					EM_Settings.blockProperties.clear();
					EM_Settings.itemProperties.clear();
					EM_Settings.livingProperties.clear();
					EM_Settings.stabilityTypes.clear();
					EM_Settings.biomeProperties.clear();
					EM_Settings.dimensionProperties.clear();
					EM_Settings.rotProperties.clear();
					EM_Settings.caveGenProperties.clear();
					EM_Settings.caveSpawnProperties.clear();;

					int Total = EM_ConfigHandler.initConfig();
					EnviroMine.caves.RefreshSpawnList();
					mc.thePlayer.addChatMessage(new ChatComponentText("Loaded " + Total +" objects and " + EM_Settings.stabilityTypes.size() + " stability types"));
					
				} //try
				catch(NullPointerException e)
				{
					mc.thePlayer.addChatMessage(new ChatComponentText("Failed to Load Custom Objects Files."));
				}
			}
			else
			{
				
				mc.thePlayer.addChatMessage(new ChatComponentText("Must hold left shift to reload Custom Objects"));
			}
		}
	}
}

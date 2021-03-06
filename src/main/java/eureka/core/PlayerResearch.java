package eureka.core;

import java.util.HashMap;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import net.minecraftforge.common.IExtendedEntityProperties;

import eureka.api.EurekaAPI;
import eureka.api.IEurekaInfo;
/**
 * Copyright (c) 2014-2015, AEnterprise
 * http://buildcraftadditions.wordpress.com/
 * Eureka is distributed under the terms of GNU GPL v3.0
 * Please check the contents of the license located in
 * http://buildcraftadditions.wordpress.com/wiki/licensing-stuff/
 */
public class PlayerResearch implements IExtendedEntityProperties {

	private final static String EXT_PROP_NAME = "EurekaPlayerResearch";
	private final EntityPlayer player;
	private HashMap<String, Integer> progressList = new HashMap<String, Integer>();
	private HashMap<String, Boolean> finished = new HashMap<String, Boolean>();

	public PlayerResearch(EntityPlayer player) {
		this.player = player;
	}

	public static void register(EntityPlayer player) {
		player.registerExtendedProperties(EXT_PROP_NAME, new PlayerResearch(player));
	}

	public static PlayerResearch get(EntityPlayer player) {
		return (PlayerResearch) player.getExtendedProperties(EXT_PROP_NAME);
	}

	@Override
	public void saveNBTData(NBTTagCompound compound) {
		NBTTagCompound tag = new NBTTagCompound();
		for (IEurekaInfo research : EurekaAPI.API.getAllKeys()) {
			tag.setInteger(research.getName() + "Progress", progressList.get(research.getName()));
			tag.setBoolean(research.getName() + "Finished", finished.get(research.getName()));
		}
		compound.setTag(EXT_PROP_NAME, tag);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		NBTTagCompound tag = compound.getCompoundTag(EXT_PROP_NAME);
		for (IEurekaInfo research : EurekaAPI.API.getAllKeys()) {
			progressList.put(research.getName(), tag.getInteger(research.getName() + "Progress"));
			finished.put(research.getName(), tag.getBoolean(research.getName() + "Finished"));
		}
	}

	public void loadOldResearch(NBTTagCompound tag) {
		for (IEurekaInfo research : EurekaAPI.API.getAllKeys()) {
			progressList.put(research.getName(), tag.getInteger(research.getName() + "Progress"));
			finished.put(research.getName(), tag.getBoolean(research.getName() + "Finished"));
		}
	}

	@Override
	public void init(Entity entity, World world) {
	}

	public void makeProgress(String key) {
		try {
			if (!progressList.containsKey(key))
				progressList.put(key, 0);
			int progress = progressList.get(key);
			progress++;
			if (progress >= EurekaAPI.API.getMaxProgress(key)) {
				if (finished != null && key != null && !finished.get(key))
					player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("Eureka")));
				progress = EurekaAPI.API.getMaxProgress(key);
				finished.remove(key);
				finished.put(key, true);
			}
			progressList.remove(key);
			progressList.put(key, progress);
		} catch (Throwable t) {
			//probably fake player
		}
	}

	public void revertProgress(String key) {
		try {
			if (!progressList.containsKey(key)) {
				progressList.put(key, 0);
				return;
			}
			int progress = progressList.get(key);
			progress--;
			if (progress < 0)
				return;
			progressList.remove(key);
			progressList.put(key, progress);
		} catch (Throwable t) {
			//probably fake player
		}
	}

	public boolean isFinished(String key) {
		if (player.capabilities.isCreativeMode)
			return true;
		if (!finished.containsKey(key)) {
			if (EurekaAPI.API.keyRegistered(key))
				finished.put(key, false);
			else
				return true; //no invalid key, making sure we don't block stuff we don't want to block
		}
		return finished.get(key);
	}

	public int getProgress(String key) {
		if (progressList.containsKey(key))
			return progressList.get(key);
		return 0;
	}

	public void copy(PlayerResearch research) {
		this.progressList = research.progressList;
		this.finished = research.finished;
	}

	public void overrideProgress(String key, int progress) {
		progressList.remove(key);
		progressList.put(key, progress);
	}

	public void overrideFinished(String key, boolean finished) {
		this.finished.remove(key);
		this.finished.put(key, finished);
	}
}

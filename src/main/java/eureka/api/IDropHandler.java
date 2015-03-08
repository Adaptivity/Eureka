package eureka.api;

import java.util.List;

import net.minecraft.item.ItemStack;
/**
 * Copyright (c) 2014-2015, AEnterprise
 * http://buildcraftadditions.wordpress.com/
 * Eureka is distributed under the terms of GNU GPL v3.0
 * Please check the contents of the license located in
 * http://buildcraftadditions.wordpress.com/wiki/licensing-stuff/
 */
public interface IDropHandler {

	boolean handles(ItemStack stack);

	List<ItemStack> getDrops(ItemStack stack);
}

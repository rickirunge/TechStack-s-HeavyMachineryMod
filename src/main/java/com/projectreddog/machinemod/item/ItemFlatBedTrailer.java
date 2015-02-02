package com.projectreddog.machinemod.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.projectreddog.machinemod.entity.EntityBulldozer;
import com.projectreddog.machinemod.utility.LogHelper;

public class ItemFlatBedTrailer extends ItemMachineMod {

	public ItemFlatBedTrailer() {
		super();
		this.setUnlocalizedName("flatbedtrailer");
		this.maxStackSize = 64;

	}

}
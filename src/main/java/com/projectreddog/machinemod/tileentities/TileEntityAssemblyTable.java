package com.projectreddog.machinemod.tileentities;

import java.util.ArrayList;
import java.util.List;

import com.projectreddog.machinemod.iface.ITEGuiButtonHandler;
import com.projectreddog.machinemod.iface.IWorkConsumer;
import com.projectreddog.machinemod.item.blueprint.ItemBlueprint;
import com.projectreddog.machinemod.reference.Reference;
import com.projectreddog.machinemod.utility.LogHelper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class TileEntityAssemblyTable extends TileEntity implements ITickable, ISidedInventory, IWorkConsumer, ITEGuiButtonHandler {
	protected ItemStack[] inventory;

	public final int inventorySize = 1;
	private static int[] sideSlots = new int[] { 0 };

	public int totalWorkNeededForThisTask = 0;// 1000
	public int workConsumedForThisTask = 0;// 100

	public boolean hasBuildProject = false;

	public TileEntityAssemblyTable() {
		inventory = new ItemStack[inventorySize];
		for (int i = 0; i < inventorySize; i++) {
			inventory[i] = ItemStack.EMPTY;
		}
		totalWorkNeededForThisTask = 1000;

	}

	@Override
	public void update() {
		if (!world.isRemote) {
			if (inventory[0].getItem() instanceof ItemBlueprint) {
				totalWorkNeededForThisTask = ((ItemBlueprint) inventory[0].getItem()).workRequired;
			}

			if (totalWorkNeededForThisTask == workConsumedForThisTask) {
				// TODO : Generate the output somehow!
				// LogHelper.info("Total Work Reached!");
				workConsumedForThisTask = 0;
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		// inventory
		NBTTagList tagList = compound.getTagList(Reference.MACHINE_MOD_NBT_PREFIX + "Inventory", compound.getId());
		for (int i = 0; i < tagList.tagCount(); i++) {
			NBTTagCompound tag = (NBTTagCompound) tagList.getCompoundTagAt(i);
			byte slot = tag.getByte("Slot");
			if (slot >= 0 && slot < inventory.length) {
				inventory[slot] = new ItemStack(tag);
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		// inventory
		NBTTagList itemList = new NBTTagList();
		for (int i = 0; i < inventory.length; i++) {
			ItemStack stack = inventory[i];
			if (!stack.isEmpty()) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setByte("Slot", (byte) i);
				stack.writeToNBT(tag);
				itemList.appendTag(tag);
			}
		}
		compound.setTag(Reference.MACHINE_MOD_NBT_PREFIX + "Inventory", itemList);
		return compound;

	}

	public int getField(int id) {
		switch (id) {
		case 0:
			return totalWorkNeededForThisTask;
		case 1:
			return workConsumedForThisTask;
		case 2:
			return hasBuildProject ? 1 : 0;
		default:
			break;
		}
		return 0;

	}

	public void setField(int id, int value) {
		switch (id) {
		case 0:
			totalWorkNeededForThisTask = value;
		case 1:
			workConsumedForThisTask = value;
		case 2:
			hasBuildProject = value == 0 ? false : true;
		default:
			break;
		}

	}

	public int getFieldCount() {
		return 3;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer playerIn) {
		return playerIn.getDistanceSq(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ()) < 64;
	}

	@Override
	public int getSizeInventory() {
		return inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return inventory[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amt) {
		ItemStack stack = getStackInSlot(slot);
		if (!stack.isEmpty()) {
			if (stack.getCount() <= amt) {
				setInventorySlotContents(slot, ItemStack.EMPTY);
			} else {
				stack = stack.splitStack(amt);
				if (stack.getCount() == 0) {
					setInventorySlotContents(slot, ItemStack.EMPTY);
				}

			}
		}
		return stack;
	}

	@Override
	public ItemStack removeStackFromSlot(int slot) {
		ItemStack stack = getStackInSlot(slot);
		if (!stack.isEmpty()) {
			setInventorySlotContents(slot, ItemStack.EMPTY);
		}
		return stack;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		inventory[slot] = stack;
		if (!stack.isEmpty() && stack.getCount() > getInventoryStackLimit()) {
			stack.setCount(getInventoryStackLimit());
		}

	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public void openInventory(EntityPlayer playerIn) {

	}

	@Override
	public void closeInventory(EntityPlayer playerIn) {

	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true;
	}

	@Override
	public void clear() {
		for (int i = 0; i < inventory.length; ++i) {
			inventory[i] = ItemStack.EMPTY;
		}
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public ITextComponent getDisplayName() {
		return null;
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		if (side == EnumFacing.NORTH || side == EnumFacing.SOUTH || side == EnumFacing.EAST || side == EnumFacing.WEST) {
			return sideSlots;
		}
		int[] topSlots2 = new int[] { 0 };
		return topSlots2;

	}

	@Override
	public boolean canInsertItem(int slot, ItemStack itemStackIn, EnumFacing direction) {
		if (slot < inventorySize && (direction == EnumFacing.NORTH || direction == EnumFacing.SOUTH || direction == EnumFacing.EAST || direction == EnumFacing.WEST)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, EnumFacing direction) {
		if (slot < inventorySize && (direction == EnumFacing.NORTH || direction == EnumFacing.SOUTH || direction == EnumFacing.EAST || direction == EnumFacing.WEST)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isEmpty() {
		for (int i = 0; i < inventory.length; i++) {
			if (!inventory[i].isEmpty()) {
				return false;
			}
		}

		return true;
	}

	@Override
	public int appyWork(int Amount) {

		if (amountCanConsume() >= Amount) {
			// 0 return value we can consume it all
			workConsumedForThisTask += Amount;
			return 0;
		} else {
			workConsumedForThisTask += amountCanConsume();
			return Amount - amountCanConsume();
		}

	}

	@Override
	public boolean isWorkNeeded() {
		// TODO Auto-generated method stub
		if (hasBuildProject == false) {
			// no active project so dont accept work.
			return false;
		}
		if (totalWorkNeededForThisTask > workConsumedForThisTask) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int amountCanConsume() {

		return totalWorkNeededForThisTask - workConsumedForThisTask;

	}

	public String getFriendlyOutputname() {
		if (!getStackInSlot(0).isEmpty()) {
			if (getStackInSlot(0).getItem() instanceof ItemBlueprint) {
				String outputItemName = ((ItemBlueprint) getStackInSlot(0).getItem()).outputItemName;
				Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(outputItemName));
				if (item != null) {
					String displayName = item.getItemStackDisplayName(new ItemStack(item));
					return displayName;
				} else {
					LogHelper.info("An Output of an ingredent is null Tell Tech please!" + outputItemName);
				}
			}
		}
		return null;
	}

	public List<String> getFriendlyIngredentList() {
		List<String> returnValue = new ArrayList<String>();
		if (!getStackInSlot(0).isEmpty()) {
			if (getStackInSlot(0).getItem() instanceof ItemBlueprint) {

				for (ItemBlueprint.BlueprintIngredent ingredent : ((ItemBlueprint) getStackInSlot(0).getItem()).ingredents) {
					String ingredentName = ingredent.getName();
					Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(ingredentName));
					if (item != null) {
						returnValue.add(item.getItemStackDisplayName(new ItemStack(item)) + " X " + ingredent.getCount());

					} else {
						LogHelper.info("An Ingredent is null Tell Tech please!" + ingredentName);
					}
				}
			}
		}
		return returnValue;
	}

	@Override
	public void HandleGuiButton(int buttonId, EntityPlayer player) {
		/// LogHelper.info("the button was clicked and the server knows!");
		if (!getStackInSlot(0).isEmpty()) {
			if (getStackInSlot(0).getItem() instanceof ItemBlueprint) {
				//
				workConsumedForThisTask = 0;
				hasBuildProject = true;
			}
		}
	}
}
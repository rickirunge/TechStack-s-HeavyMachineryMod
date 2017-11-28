// Date: 12/24/2014 5:11:17 PM
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX

package com.projectreddog.machinemod.model.tileentity;

import java.io.IOException;
import java.util.HashMap;

import com.projectreddog.machinemod.reference.Reference;
import com.projectreddog.machinemod.tileentities.TileEntityLiquidPipe;
import com.projectreddog.machinemod.utility.MachineModModelHelper;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.obj.OBJModel;

public class ModelLiquidPipe extends ModelBase {
	// fields

	public OBJModel objModel;
	private HashMap<String, IBakedModel> modelParts;

	public ModelLiquidPipe() {

		try {
			objModel = (OBJModel) OBJLoader.INSTANCE.loadModel(new ResourceLocation(Reference.MOD_ID.toLowerCase(), "models/pipe.obj"));
			modelParts = MachineModModelHelper.getModelsForGroups(objModel);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void renderGroupObject(String groupName) {
		MachineModModelHelper.renderBakedModel(modelParts.get(groupName));

	}

	public void render(TileEntity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		// super.render(null, f, f1, f2, f3, f4, f5);
		// myModel.renderAll();
		if (entity instanceof TileEntityLiquidPipe) {

			this.renderGroupObject("Center_Cube");
			TileEntityLiquidPipe tELP = (TileEntityLiquidPipe) entity;
			if (tELP.isConnectedNorth()) {
				this.renderGroupObject("North_Cube.006");
			}
			if (tELP.isConnectedSouth()) {
				this.renderGroupObject("South_Cube.006");
			}
			if (tELP.isConnectedEast()) {
				this.renderGroupObject("East_Cube.006");
			}
			if (tELP.isConnectedWest()) {
				this.renderGroupObject("West_Cube.006");
			}
			if (tELP.isConnectedUp()) {
				this.renderGroupObject("Up_Cube.006");
			}
			if (tELP.isConnectedDown()) {
				this.renderGroupObject("Down_Cube.006");
			}

		}

	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity e) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5, e);
	}

	public ResourceLocation getTexture() {

		return new ResourceLocation("machinemod", Reference.MODEL_PIPE_TEXTURE_LOCATION);
	}

}

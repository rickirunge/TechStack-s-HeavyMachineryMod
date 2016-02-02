// Date: 12/24/2014 5:11:17 PM
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX

package com.projectreddog.machinemod.model;

import java.io.IOException;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;

import com.projectreddog.machinemod.entity.EntityChopper;
import com.projectreddog.machinemod.reference.Reference;
import com.projectreddog.machinemod.utility.MachineModModelHelper;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.obj.OBJModel;

public class ModelChopper extends ModelTransportable {
	// fields
	public OBJModel objModel;
	private int rotorMult = 33;
	private HashMap<String, IFlexibleBakedModel> modelParts;

	public ModelChopper() {
		try {
			objModel = (OBJModel) OBJLoader.instance.loadModel(new ResourceLocation(Reference.MOD_ID.toLowerCase(), "models/chopper.obj"));
			modelParts = MachineModModelHelper.getModelsForGroups(objModel);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void renderGroupObject(String groupName) {
		MachineModModelHelper.renderBakedModel(modelParts.get(groupName));

	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		// renderGroupObject(MachineModModelHelper.ALL_PARTS);
		// will now call rendering for each individual object
		// this.renderGroupObject("Tractor_Cube.001");
		// renderGroupObject("Cylinder");
		GL11.glTranslatef(f, f1, f2);

		if (entity instanceof EntityChopper) {
			EntityChopper ec = (EntityChopper) entity;
			double dx = ec.motionX;
			double dz = ec.motionZ;
			float speed = MathHelper.sqrt_double(dx * dx + dz * dz);
			if (ec.isPlayerAccelerating) {
				GL11.glRotatef(10f, 1, 0, 0);
			}

			if (ec.isPlayerBreaking) {
				GL11.glRotatef(-10f, 1, 0, 0);
			}

			// if (speed > .1f) {
			// GL11.glRotatef(90f, 0, 0, 1);
			// }
		}
		renderGroupObject("Body_Cube");

		if (entity instanceof EntityChopper) {
			EntityChopper ec = (EntityChopper) entity;
			GL11.glRotatef(ec.Attribute2 * rotorMult, 0, 1, 0);
			renderGroupObject("MainRotor_Cube.001");
			GL11.glRotatef(ec.Attribute2 * -rotorMult, 0, 1, 0);

			GL11.glTranslatef(0f, -1.6f, 4.5f);

			GL11.glRotatef(ec.Attribute2 * rotorMult, 1, 0, 0);
			renderGroupObject("TailRotor_Cube.003");
		} else {
			renderGroupObject("MainRotor_Cube.001");

			GL11.glTranslatef(0f, -1.6f, 4.5f);

			renderGroupObject("TailRotor_Cube.003");
		}
		renderGroupObject("Cylinder");

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

		return new ResourceLocation("machinemod", Reference.MODEL_CHOPPER_TEXTURE_LOCATION);
	}

}

package net.gasdev.nomanscraft.screen.renderer;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Rect2i;
import net.minecraft.text.Text;
import team.reborn.energy.api.EnergyStorage;

import java.util.List;

/*
 *  BluSunrize
 *  Copyright (c) 2021
 *
 *  This code is licensed under "Blu's License of Common Sense" (FORGE VERSION)
 *  Details can be found in the license file in the root folder of this project
 */
public class FluidInfoArea extends InfoArea {

    private final SingleVariantStorage<FluidVariant> fluid;

    public FluidInfoArea(int xMin, int yMin)  {
        this(xMin, yMin, null,8,64);
    }

    public FluidInfoArea(int xMin, int yMin, SingleVariantStorage<FluidVariant> fluid)  {
        this(xMin, yMin, fluid,8,64);
    }

    public FluidInfoArea(int xMin, int yMin, SingleVariantStorage<FluidVariant> fluid, int width, int height)  {
        super(new Rect2i(xMin, yMin, width, height));
        this.fluid = fluid;
    }

    public List<Text> getTooltips() {
        return List.of(Text.literal((fluid.getAmount()/81)+"/"+(fluid.getCapacity()/81)+" mB"));
    }

    @Override
    public void draw(MatrixStack transform) {
        final int height = area.getHeight();
        int stored = (int)(height*(fluid.getAmount()/(float)fluid.getCapacity()));
        fillGradient(
                transform,
                area.getX(), area.getY()+(height-stored),
                area.getX() + area.getWidth(), area.getY() +area.getHeight(),
                0xff0000ff, 0xff0000a0
        );
    }
}
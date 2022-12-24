package net.gasdev.nomanscraft.screen.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.transfer.v1.client.fluid.FluidVariantRendering;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Rect2i;
import net.minecraft.fluid.Fluids;
import net.minecraft.screen.PlayerScreenHandler;
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

    /*
     * METHOD FROM https://github.com/TechReborn/TechReborn
     * UNDER MIT LICENSE: https://github.com/TechReborn/TechReborn/blob/1.19/LICENSE.md
     */
    public void drawFluid(MatrixStack matrixStack, int x, int y, int width, int height, long maxCapacity) {
        if (fluid.variant.getFluid() == Fluids.EMPTY) {
            return;
        }
        RenderSystem.setShaderTexture(0, PlayerScreenHandler.BLOCK_ATLAS_TEXTURE);
        y += height;
        final Sprite sprite = FluidVariantRendering.getSprite(fluid.variant);
        int color = FluidVariantRendering.getColor(fluid.variant);

        final int drawHeight = (int) (fluid.getAmount() / (maxCapacity * 1F) * height);
        final int iconHeight = sprite.getContents().getHeight();
        int offsetHeight = drawHeight;

        RenderSystem.setShaderColor((color >> 16 & 255) / 255.0F, (float) (color >> 8 & 255) / 255.0F, (float) (color & 255) / 255.0F, 1F);

        int iteration = 0;
        while (offsetHeight != 0) {
            final int curHeight = offsetHeight < iconHeight ? offsetHeight : iconHeight;

            DrawableHelper.drawSprite(matrixStack, x, y - offsetHeight, 0, width, curHeight, sprite);
            offsetHeight -= curHeight;
            iteration++;
            if (iteration > 50) {
                break;
            }
        }
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);

        RenderSystem.setShaderTexture(0, FluidRenderHandlerRegistry.INSTANCE.get(fluid.variant.getFluid())
                .getFluidSprites(MinecraftClient.getInstance().world, null, fluid.variant.getFluid().getDefaultState())[0].getContents().getId());
    }

    @Override
    public void draw(MatrixStack transform) {
        drawFluid(transform, area.getX(), area.getY(), area.getWidth(), area.getHeight(), fluid.getCapacity());
    }
}
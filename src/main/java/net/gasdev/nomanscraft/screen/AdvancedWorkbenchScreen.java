package net.gasdev.nomanscraft.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.gasdev.nomanscraft.NoMansCraft;
import net.gasdev.nomanscraft.screen.renderer.EnergyInfoArea;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class AdvancedWorkbenchScreen extends HandledScreen<AdvancedWorkbenchScreenHandler> {

    private static final Identifier TEXTURE = new Identifier("nomanscraft", "textures/gui/advanced_workbench.png");
    private EnergyInfoArea energyInfoArea;

    public AdvancedWorkbenchScreen(AdvancedWorkbenchScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;

        energyInfoArea = new EnergyInfoArea(((width - backgroundWidth) / 2 + 161),
                ((height - backgroundHeight) / 2 + 16), handler.blockEntity.energyStorage, 8, 54);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);

        if (handler.isCrafting()) {
            renderProgressBar(matrices, x, y);
        }
        energyInfoArea.draw(matrices);
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        renderEnergyAreaTooltip(matrices, mouseX, mouseY, x, y);
    }

    private void renderEnergyAreaTooltip(MatrixStack matrices, int mouseX, int mouseY, int x, int y) {
        if (energyInfoArea.getArea().contains(mouseX, mouseY)) {
            renderTooltip(matrices, energyInfoArea.getTooltips(), mouseX - x, mouseY - y);
        }
    }

    private void renderProgressBar(MatrixStack matrices, int x, int y) {
        float progress = handler.getProgressRatio();
        int width = (int) (progress * 33);
        drawTexture(matrices, x+85, y+35, 176, 0, width, 16);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawMouseoverTooltip(matrices, mouseX, mouseY);
    }
}

package net.gasdev.nomanscraft.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.gasdev.nomanscraft.screen.renderer.EnergyInfoArea;
import net.gasdev.nomanscraft.screen.renderer.FluidInfoArea;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ElectrolyserScreen extends HandledScreen<ElectrolyserScreenHandler> {

    private static final Identifier TEXTURE = new Identifier("nomanscraft", "textures/gui/electrolyser.png");
    private EnergyInfoArea energyInfoArea;
    private FluidInfoArea fluidInfoArea;

    public ElectrolyserScreen(ElectrolyserScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;

        energyInfoArea = new EnergyInfoArea(((width - backgroundWidth) / 2 + 161),
                ((height - backgroundHeight) / 2 + 16), handler.blockEntity.energyStorage, 8, 54);

        fluidInfoArea = new FluidInfoArea(((width - backgroundWidth) / 2 + 78),
                ((height - backgroundHeight) / 2 + 24), handler.blockEntity.fluidStorage, 19, 54);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);
        energyInfoArea.draw(matrices);
        fluidInfoArea.draw(matrices);
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        renderAreaTooltips(matrices, mouseX, mouseY, x, y);
    }

    private void renderAreaTooltips(MatrixStack matrices, int mouseX, int mouseY, int x, int y) {
        if (energyInfoArea.getArea().contains(mouseX, mouseY)) {
            renderTooltip(matrices, energyInfoArea.getTooltips(), mouseX - x, mouseY - y);
        }
        if (fluidInfoArea.getArea().contains(mouseX, mouseY)) {
            renderTooltip(matrices, fluidInfoArea.getTooltips(), mouseX - x, mouseY - y);
        }
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawMouseoverTooltip(matrices, mouseX, mouseY);
    }
}

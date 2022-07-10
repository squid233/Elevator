package io.github.squid233.elevator.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.squid233.elevator.ElevatorMod;
import io.github.squid233.elevator.block.entity.ElevatorBlockEntity;
import io.github.squid233.elevator.block.entity.ElevatorScreenHandler;
import io.github.squid233.elevator.network.NetworkHandler;
import io.github.squid233.elevator.network.client.RemoveCamoPacket;
import io.github.squid233.elevator.network.client.SetArrowPacket;
import io.github.squid233.elevator.network.client.SetDirectionalPacket;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import static io.github.squid233.elevator.block.ElevatorBlock.DIRECTIONAL;
import static io.github.squid233.elevator.block.ElevatorBlock.SHOW_ARROW;
import static io.github.squid233.elevator.network.EModNetworkingConstants.*;
import static net.minecraft.block.HorizontalFacingBlock.FACING;

/**
 * @author squid233
 * @since 0.2.0
 */
public final class ElevatorScreen extends HandledScreen<ElevatorScreenHandler> {
    private static final Identifier TEXTURE = new Identifier(ElevatorMod.MOD_ID, "textures/gui/elevator_gui.png");
    private final ElevatorBlockEntity be;
    private final Direction playerFacing;

    private FunctionalCheckbox dirButton;
    private FunctionalCheckbox hideArrowButton;
    private ButtonWidget resetCamoButton;
    private FacingControllerWrapper facingController;

    public ElevatorScreen(ElevatorScreenHandler handler, PlayerInventory inv, Text title) {
        super(handler, inv, title);
        backgroundWidth = 200;
        backgroundHeight = 100;
        be = handler.getBlockEntity();
        playerFacing = handler.getPlayerFacing();
    }

    @Override
    protected void init() {
        super.init();

        // Toggle directional button
        var dirLang = new TranslatableText("screen.elevator233.elevator.directional");
        dirButton = new FunctionalCheckbox(x + 8,
            y + 25,
            20,
            20,
            dirLang,
            be.getCachedState().get(DIRECTIONAL),
            value -> NetworkHandler.sendToServer(
                SET_DIRECTIONAL_PACKET_ID,
                new SetDirectionalPacket(value, be.getPos()),
                SetDirectionalPacket::encode
            ));
        addDrawableChild(dirButton);

        // Toggle arrow button
        var arrowLang = new TranslatableText("screen.elevator233.elevator.hide_arrow");
        hideArrowButton = new FunctionalCheckbox(x + 8,
            y + 50,
            20,
            20,
            arrowLang,
            !be.getCachedState().get(SHOW_ARROW),
            value -> NetworkHandler.sendToServer(
                SET_ARROW_PACKET_ID,
                new SetArrowPacket(!value, be.getPos()),
                SetArrowPacket::encode
            ));
        hideArrowButton.visible = be.getCachedState().get(DIRECTIONAL);
        addDrawableChild(hideArrowButton);

        // Reset camouflage button
        var resetCamoLang = new TranslatableText("screen.elevator233.elevator.reset_camo");
        resetCamoButton = new ButtonWidget(x + 8,
            y + 75,
            110,
            20,
            resetCamoLang,
            button -> NetworkHandler.sendToServer(
                REMOVE_CAMO_PACKET_ID,
                new RemoveCamoPacket(be.getPos()),
                RemoveCamoPacket::encode
            )
        );
        addDrawableChild(resetCamoButton);

        // Directional controller
        facingController = new FacingControllerWrapper(x + 120,
            y + 20,
            be.getPos(),
            playerFacing);
        facingController.getButtons().forEach(button -> {
            addDrawableChild(button);
            button.visible = be.getCachedState().get(DIRECTIONAL);
        });

        resetCamoButton.active = be.getHeldState() != null;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    protected void handledScreenTick() {
        super.handledScreenTick();
        facingController.getButtons().forEach(button -> {
            button.visible = dirButton.isChecked();
            button.active = be.getCachedState().get(FACING) != button.direction;
        });
        hideArrowButton.visible = dirButton.isChecked();
        resetCamoButton.active = be.getHeldState() != null;
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, TEXTURE);
        int relX = (width - backgroundWidth) / 2;
        int relY = (height - backgroundHeight) / 2;
        drawTexture(matrices, relX, relY, 0, 0, backgroundWidth, backgroundHeight);
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        textRenderer.draw(matrices, title, 8, 8, 0xe0e0e0);
    }
}

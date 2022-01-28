package io.github.squid233.elevator.client.gui;

import io.github.squid233.elevator.network.NetworkHandler;
import io.github.squid233.elevator.network.client.SetFacingPacket;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.awt.*;

import static io.github.squid233.elevator.network.EModNetworkingConstants.SET_FACING_PACKET_ID;

/**
 * @author squid233
 * @since 0.2.0
 */
class FacingButton extends ButtonWidget {
    Direction direction;

    FacingButton(Point slot, Direction direction, BlockPos pos) {
        super(slot.x,
            slot.y,
            20,
            20,
            Text.of(direction.getName().substring(0, 1).toUpperCase()),
            button -> NetworkHandler.sendToServer(
                SET_FACING_PACKET_ID,
                new SetFacingPacket(direction, pos),
                SetFacingPacket::encode
            )
        );
        this.direction = direction;
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (isHovered())
            fill(matrices, x, y, x + width, y + height, 0x80ffffff);
        drawCenteredText(matrices,
            MinecraftClient.getInstance().textRenderer,
            getMessage().asString(),
            x + (width >> 1),
            y + ((height - 8) >> 1),
            active ? 0xffffff : 0x00ff00);
    }
}

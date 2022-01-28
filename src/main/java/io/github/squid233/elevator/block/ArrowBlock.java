package io.github.squid233.elevator.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.Direction;

/**
 * @author squid233
 * @since 0.2.0
 */
public class ArrowBlock extends HorizontalFacingBlock {
    public ArrowBlock(Settings settings) {
        super(settings);
    }

    public static String createModel(Direction direction) {
        return "{\n" +
            "  \"textures\": {\n" +
            "    \"arrow\": \"elevator233:block/arrow\"\n" +
            "  },\n" +
            "  \"elements\": [\n" +
            "    {\n" +
            "      \"from\": [5, 16.1, 10],\n" +
            "      \"to\": [11, 16.1, 16],\n" +
            "      \"faces\": {\n" +
            "        \"up\": {\"texture\": \"#arrow\", \"rotation\": " + (((direction.getHorizontal() + 3) % 4) * 90) + ", \"uv\": [0, 0, 16, 16]}\n" +
            "      },\n" +
            "      \"__comment\": \"The arrow (default south)\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}

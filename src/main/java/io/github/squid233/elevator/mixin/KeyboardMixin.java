package io.github.squid233.elevator.mixin;

import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.KeybindsScreen;
import net.minecraft.client.gui.screen.option.NarratorOptionsScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.option.NarratorMode;
import net.minecraft.client.util.InputUtil.Key;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import static io.github.squid233.elevator.ElevatorHandler.tryTeleport;
import static net.minecraft.client.MinecraftClient.getInstance;
import static net.minecraft.client.gui.screen.Screen.hasControlDown;
import static net.minecraft.client.option.KeyBinding.onKeyPressed;
import static net.minecraft.client.option.KeyBinding.setKeyPressed;
import static net.minecraft.client.util.InputUtil.fromKeyCode;
import static net.minecraft.client.util.InputUtil.isKeyPressed;
import static net.minecraft.client.util.ScreenshotRecorder.saveScreenshot;
import static net.minecraft.util.Util.getMeasuringTimeMs;
import static org.lwjgl.glfw.GLFW.*;

/**
 * @author squid233
 * @since 0.1.0
 */
@Mixin(Keyboard.class)
public abstract class KeyboardMixin {
    private static boolean lastSneaking;

    @Final
    @Shadow
    private MinecraftClient client;
    @Shadow
    private boolean repeatEvents;
    @Shadow
    private long debugCrashStartTime;
    @Shadow
    private long debugCrashLastLogTime;
    @Shadow
    private long debugCrashElapsedTime;
    @Shadow
    private boolean switchF3State;

    @Shadow
    protected abstract boolean processF3(int key);

    /**
     * on key
     *
     * @param window    the window handle
     * @param key       the key code
     * @param scancode  the scancode
     * @param action    the action
     * @param modifiers the modifiers
     * @author squid233
     * @reason Can't locate the position
     */
    @Overwrite
    public void onKey(long window, int key, int scancode, int action, int modifiers) {
        if (window != client.getWindow().getHandle()) {
            return;
        }
        if (debugCrashStartTime > 0L) {
            if (!isKeyPressed(getInstance().getWindow().getHandle(), GLFW_KEY_C) || !isKeyPressed(getInstance().getWindow().getHandle(), GLFW_KEY_F3)) {
                debugCrashStartTime = -1L;
            }
        } else if (isKeyPressed(getInstance().getWindow().getHandle(), GLFW_KEY_C) && isKeyPressed(getInstance().getWindow().getHandle(), GLFW_KEY_F3)) {
            switchF3State = true;
            debugCrashStartTime = getMeasuringTimeMs();
            debugCrashLastLogTime = getMeasuringTimeMs();
            debugCrashElapsedTime = 0L;
        }
        Screen screen = client.currentScreen;
        if (!(action != 1 || client.currentScreen instanceof KeybindsScreen && ((KeybindsScreen) screen).lastKeyCodeUpdateTime > getMeasuringTimeMs() - 20L)) {
            if (client.options.keyFullscreen.matchesKey(key, scancode)) {
                client.getWindow().toggleFullscreen();
                client.options.fullscreen = client.getWindow().isFullscreen();
                client.options.write();
                return;
            }
            if (client.options.keyScreenshot.matchesKey(key, scancode)) {
                hasControlDown();// empty if block
                saveScreenshot(client.runDirectory, client.getFramebuffer(), message -> client.execute(() -> client.inGameHud.getChatHud().addMessage(message)));
                return;
            }
        }
        if (NarratorManager.INSTANCE.isActive()) {
            boolean bl = screen == null || !(screen.getFocused() instanceof TextFieldWidget) || !((TextFieldWidget) screen.getFocused()).isActive();
            if (action != 0 && key == GLFW_KEY_B && hasControlDown() && bl) {
                boolean bl2 = client.options.narrator == NarratorMode.OFF;
                client.options.narrator = NarratorMode.byId(client.options.narrator.getId() + 1);
                NarratorManager.INSTANCE.addToast(client.options.narrator);
                if (screen instanceof NarratorOptionsScreen) {
                    ((NarratorOptionsScreen) screen).updateNarratorButtonText();
                }
                if (bl2 && screen != null) {
                    screen.applyNarratorModeChangeDelay();
                }
            }
        }
        if (screen != null) {
            boolean[] bls = {false};
            Screen.wrapScreenError(() -> {
                if (action == 1 || action == 2 && repeatEvents) {
                    screen.applyKeyPressNarratorDelay();
                    bls[0] = screen.keyPressed(key, scancode, modifiers);
                } else if (action == 0) {
                    bls[0] = screen.keyReleased(key, scancode, modifiers);
                }
            }, "keyPressed event handler", screen.getClass().getCanonicalName());
            if (bls[0]) {
                return;
            }
        }
        if (client.currentScreen == null || client.currentScreen.passEvents) {
            Key key2 = fromKeyCode(key, scancode);
            if (action == 0) {
                setKeyPressed(key2, false);
                if (key == GLFW_KEY_F3) {
                    if (switchF3State) {
                        switchF3State = false;
                    } else {
                        client.options.debugEnabled = !client.options.debugEnabled;
                        client.options.debugProfilerEnabled = client.options.debugEnabled && Screen.hasShiftDown();
                        client.options.debugTpsEnabled = client.options.debugEnabled && Screen.hasAltDown();
                    }
                }
            } else {
                if (key == GLFW_KEY_F4 && client.gameRenderer != null) {
                    client.gameRenderer.toggleShadersEnabled();
                }
                boolean bl2 = false;
                if (client.currentScreen == null) {
                    if (key == GLFW_KEY_ESCAPE) {
                        boolean bl3 = isKeyPressed(getInstance().getWindow().getHandle(), GLFW_KEY_F3);
                        client.openPauseMenu(bl3);
                    }
                    bl2 = isKeyPressed(getInstance().getWindow().getHandle(), GLFW_KEY_F3) && processF3(key);
                    switchF3State |= bl2;
                    if (key == GLFW_KEY_F1) {
                        client.options.hudHidden = !client.options.hudHidden;
                    }
                }
                if (bl2) {
                    setKeyPressed(key2, false);
                } else {
                    setKeyPressed(key2, true);
                    onKeyPressed(key2);
                }
                if (client.options.debugProfilerEnabled && key >= GLFW_KEY_0 && key <= GLFW_KEY_9) {
                    client.handleProfilerKeyPress(key - GLFW_KEY_0);
                }
            }
            // Here; overwritten
            onInput();
        }
    }

    private void onInput() {
        var player = client.player;
        if (player == null || player.isSpectator() || !player.isAlive() || player.input == null)
            return;

        var sneaking = player.input.sneaking;
        if (lastSneaking != sneaking) {
            lastSneaking = sneaking;
            if (sneaking)
                tryTeleport(player, Direction.DOWN);
        }
    }
}

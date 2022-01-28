package io.github.squid233.elevator.client.gui;

import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.text.Text;

/**
 * @author squid233
 * @since 0.2.0
 */
class FunctionalCheckbox extends CheckboxWidget {
    private final Toggleable onPress;

    FunctionalCheckbox(int x, int y, int width, int height, Text message, boolean checked, Toggleable onPress) {
        super(x, y, width, height, message, checked);
        this.onPress = onPress;
    }

    @Override
    public void onPress() {
        super.onPress();
        onPress.onPress(isChecked());
    }

    @FunctionalInterface
    public interface Toggleable {
        void onPress(boolean value);
    }
}

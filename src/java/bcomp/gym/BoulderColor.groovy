package bcomp.gym

import java.awt.*

public enum BoulderColor {

    GREEN(Color.green), YELLOW(Color.yellow), RED(Color.red), BLUE(Color.blue), BLACK(Color.black), GRAY(Color.gray),
    WHITE(Color.white), PINK(Color.pink), ORANGE(Color.orange), BROWN(new Color(138, 69, 0)), PURPLE(new Color(130,
            0, 237)), BLUE_BLACK(Color.blue, Color.black), YELLOW_BLACK(Color.yellow, Color.black);

    public final Color primaryColor
    public final Color secondaryColor

    BoulderColor(Color primaryColor, Color secondaryColor) {
        this.primaryColor = primaryColor
        this.secondaryColor = secondaryColor
    }

    BoulderColor(Color primaryColor) {
        this.primaryColor = primaryColor
        this.secondaryColor = null
    }

    public boolean hasSecondaryColor() {
        return secondaryColor != null;
    }

}
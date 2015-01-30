package pathfinder.gui;

/* local package imports */
import pathfinder.Character;
import pathfinder.enums.TextLayout;
import pathfinder.gui.dialog.TextColumnFormat;

/* guava package imports */
import com.google.common.base.Function;

/* java package imports */
import java.awt.Color;
import java.awt.Font;

public class Resources
{
    public static final Font FONT_12 = new Font("Helvetica", Font.PLAIN, 12);
    public static final Font FONT_MONO_12 = new Font("Courier New", Font.PLAIN, 12);
    public static final TextColumnFormat COL_12 = new TextColumnFormat(FONT_12, TextLayout.CENTER_LEFT, 4, 2);
    public static final TextColumnFormat COL_MONO_12 = new TextColumnFormat(FONT_MONO_12, TextLayout.BOTTOM_CENTER, 4, 2);
    public static final Color PC_COLOR = new Color(79, 209, 226);
    public static final Color NPC_COLOR = new Color(250, 112, 112);
    public static final Color DIALOG_BACK = new Color(173, 156, 156);
    public static final Color ARROW_COLOR = Color.darkGray;
    public static final Function<Character, Color> BACK_COLOR_FUNCTION = new Function<Character, Color>()
    {
        public Color apply(Character c)
        {
            if (c.isPC())
                return PC_COLOR;
            else
                return NPC_COLOR;
        }
    };

    private Resources()
    {
    }
}

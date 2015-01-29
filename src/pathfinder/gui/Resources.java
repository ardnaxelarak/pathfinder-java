package pathfinder.gui;

/* local package imports */
import pathfinder.Character;
import pathfinder.gui.dialog.column.BasicColumn;

/* guava package imports */
import com.google.common.base.Function;

/* java package imports */
import java.awt.Color;
import java.awt.Font;

public class Resources
{
    public static final Font FONT_12 = new Font("Helvetica", Font.PLAIN, 12);
    public static final Font FONT_MONO_12 = new Font("Courier New", Font.PLAIN, 12);
    public static final Color PC_COLOR = new Color(79, 209, 226);
    public static final Color NPC_COLOR = new Color(250, 112, 112);
    public static final Color DIALOG_BACK = new Color(173, 156, 156);
    public static final Color ARROW_COLOR = Color.darkGray;
    public static final BasicColumn BORDER_5 = new BasicColumn(5);
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

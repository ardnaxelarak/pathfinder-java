package pathfinder.gui.dialog.column;

/* local package imports */
import pathfinder.gui.dialog.column.MappedTextColumn;

/* guava package imports */
import com.google.common.base.Function;
import com.google.common.base.Functions;

/* java package imports */
import java.awt.Color;
import java.awt.Font;

public class TextColumn extends MappedTextColumn<String>
{
    private static final Function<String, String> IDENT = Functions.identity();

    public TextColumn(Font font, int xGap, int yGap, Color backColor, Color foreColor)
    {
        super(font, IDENT, xGap, yGap, backColor, foreColor);
    }
}

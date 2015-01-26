package pathfinder.gui.dialog.column;

import pathfinder.gui.dialog.column.MappedTextColumn;
import pathfinder.mapping.IdentityMapper;

import java.awt.Color;
import java.awt.Font;

public class TextColumn extends MappedTextColumn<String>
{
    public TextColumn(Font font, int xGap, int yGap, Color backColor, Color foreColor)
    {
        super(font, new IdentityMapper<String>(), xGap, yGap, backColor, foreColor);
    }
}

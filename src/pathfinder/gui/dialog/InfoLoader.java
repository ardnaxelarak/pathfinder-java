package pathfinder.gui.dialog;

/* guava package imports */
import com.google.common.base.Optional;

/* java package imports */
import java.awt.Color;

public interface InfoLoader
{
    public Optional<String> getText(int row, int column);

    public Optional<Color> getBackColor(int row, int column);

    public Optional<Color> getForeColor(int row, int column);
}

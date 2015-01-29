package pathfinder.enums;

import pathfinder.enums.HorizontalLayout;
import pathfinder.enums.VerticalLayout;

public enum TextLayout
{
    TOP_LEFT        (VerticalLayout.TOP,      HorizontalLayout.LEFT),
    TOP_CENTER      (VerticalLayout.TOP,      HorizontalLayout.CENTER),
    TOP_RIGHT       (VerticalLayout.TOP,      HorizontalLayout.RIGHT),
    CENTER_LEFT     (VerticalLayout.CENTER,   HorizontalLayout.LEFT),
    CENTER_CENTER   (VerticalLayout.CENTER,   HorizontalLayout.CENTER),
    CENTER_RIGHT    (VerticalLayout.CENTER,   HorizontalLayout.RIGHT),
    BASELINE_LEFT   (VerticalLayout.BASELINE, HorizontalLayout.LEFT),
    BASELINE_CENTER (VerticalLayout.BASELINE, HorizontalLayout.CENTER),
    BASELINE_RIGHT  (VerticalLayout.BASELINE, HorizontalLayout.RIGHT),
    BOTTOM_LEFT     (VerticalLayout.BOTTOM,   HorizontalLayout.LEFT),
    BOTTOM_CENTER   (VerticalLayout.BOTTOM,   HorizontalLayout.CENTER),
    BOTTOM_RIGHT    (VerticalLayout.BOTTOM,   HorizontalLayout.RIGHT);

    private final HorizontalLayout hl;
    private final VerticalLayout vl;

    TextLayout(VerticalLayout vl, HorizontalLayout hl)
    {
        this.vl = vl;
        this.hl = hl;
    }

    public HorizontalLayout getHorizontalLayout()
    {
        return hl;
    }

    public VerticalLayout getVerticalLayout()
    {
        return vl;
    }
}

package com.bingoteamcolours;

import net.runelite.client.ui.ColorScheme;
import net.runelite.client.util.Text;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

// Source: QuestHelperPlugin
// https://github.com/Zoinkwiz/quest-helper/blob/master/src/main/java/com/questhelper/panel/DropdownRenderer.java
public final class DropdownRenderer extends DefaultListCellRenderer
{
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object o, int i, boolean isSelected, boolean b1) {
        setBackground(ColorScheme.DARK_GRAY_COLOR);
        setForeground(Color.WHITE);
        setBorder(new EmptyBorder(0, 0, 0, 0));

        if (!isSelected)
        {
            setBackground(ColorScheme.DARK_GRAY_COLOR);
            setForeground(Color.WHITE);
        }
        else
        {
            setBackground(list.getBackground());
            setForeground(ColorScheme.LIGHT_GRAY_COLOR);
        }

        setText(Text.titleCase((Enum) o));

        return this;
    }
}

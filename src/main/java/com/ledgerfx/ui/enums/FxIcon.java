package com.ledgerfx.ui.enums;

import org.kordamp.ikonli.javafx.FontIcon;

public enum FxIcon {

    LOGIN("mdi2l-login"),
    HOME("mdi2h-home"),
    ADD("mdi2c-cash-plus"),
    LIST("mdi2f-format-list-bulleted"),
    CHART("mdi2c-chart-pie"),
    SETTINGS("mdi2c-cog-outline"),
    LOGOUT("mdi2l-logout");

    private final String literal;

    FxIcon(String literal) {
        this.literal = literal;
    }

    public FontIcon icon(int size) {
        FontIcon icon = new FontIcon(literal);
        icon.setIconSize(size);
        return icon;
    }

    public FontIcon icon() {
        return icon(16);
    }
}

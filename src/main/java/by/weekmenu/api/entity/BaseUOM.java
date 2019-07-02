package by.weekmenu.api.entity;

import lombok.Getter;

@Getter
public enum BaseUOM {

    GRAMM("Грамм", "Гр");

    private final String fullName;
    private final String shortName;

    BaseUOM(String fullName, String shortName) {
        this.fullName = fullName;
        this.shortName = shortName;
    }
}

package fr.haxy972.fallenkingdom.game;

import fr.haxy972.fallenkingdom.Main;

public enum Days {

    HUNGER(2),
    PVP(3),
    ASSAULT(5),
    CHEST(2),
    PORTALS(3),
    BED(8);

    private final int day;

    Days(int day) {
        this.day = day;
    }

    public int getDay() {
        return day;
    }
}

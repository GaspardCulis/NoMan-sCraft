package net.gasdev.nomanscraft.utils;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class GasType {
    public static final GasType NONE = new GasType("none");
    public static final GasType OXYGEN = new GasType("oxygen");
    public static final GasType HYDROGEN = new GasType("hydrogen");

    private final String name;

    public GasType(String name) {
        this.name = name;
    }

    public static GasType valueOf(String gasType) {
        switch (gasType) {
            case "oxygen":
                return OXYGEN;
            case "hydrogen":
                return HYDROGEN;
            default:
                return NONE;
        }
    }

    public String getName() {
        return name;
    }

    public MutableText getDisplayName() {
        return Text.translatable("gas.nomanscraft." + name);
    }

    public static boolean isGasTypeCompatible(GasType gasType1, GasType gasType2) {
        return gasType1 == gasType2 || gasType1 == NONE || gasType2 == NONE;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof GasType && ((GasType) obj).getName().equals(name);
    }
}

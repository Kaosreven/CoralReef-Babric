package com.primetoxinz.coralreef;

import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;

public class CoralReefMod {
    public static final Namespace NAMESPACE = Namespace.of("coralreef");

    public static Identifier id(String name) {
        return NAMESPACE.id(name);
    }
}

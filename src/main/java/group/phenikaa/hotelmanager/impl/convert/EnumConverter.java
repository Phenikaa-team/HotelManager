package group.phenikaa.hotelmanager.impl.convert;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import java.util.Arrays;

public class EnumConverter extends Converter<Enum, JsonElement> {
    private final Class<? extends Enum> clazz;

    public EnumConverter(Class<? extends Enum> clazz) {
        this.clazz = clazz;
    }

    public static int currentEnum(Enum clazz) {
        for (int i = 0; i < clazz.getClass().getEnumConstants().length; ++i) {
            Enum e = clazz.getClass().getEnumConstants()[i];
            if (!e.name().equalsIgnoreCase(clazz.name())) continue;
            return i;
        }
        return -1;
    }

    public static Enum increaseEnum(Enum clazz) {
        int index = EnumConverter.currentEnum(clazz);
        for (int i = 0; i < clazz.getClass().getEnumConstants().length; ++i) {
            Enum e = clazz.getClass().getEnumConstants()[i];
            if (i != index + 1) continue;
            return e;
        }
        return clazz.getClass().getEnumConstants()[0];
    }

    public static Enum setEnumInt(Enum clazz, int id) {
        if (clazz == null) return null;
        for (int i = 0; i < clazz.getClass().getEnumConstants().length; ++i) {
            Enum e = clazz.getClass().getEnumConstants()[i];
            if (i != id) continue;
            return e;
        }
        return clazz.getClass().getEnumConstants()[0];
    }

    public static String[] getNames(Enum clazz) {
        return Arrays.stream(clazz.getClass().getEnumConstants()).map(Enum::toString).toArray(String[]::new);
    }

    public static String getProperName(Enum clazz) {
        return clazz.toString();
    }

    public JsonElement doForward(Enum anEnum) {
        return new JsonPrimitive(anEnum.name());
    }

    public Enum doBackward(JsonElement jsonElement) {
        try {
            return Enum.valueOf(this.clazz, jsonElement.getAsString());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}


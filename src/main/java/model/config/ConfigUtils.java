package model.config;

import model.enums.WeaponType;

public class ConfigUtils {
    public static String getWeaponTag(WeaponType wt)
    {
        switch (wt)
        {
            case SABRE:
                return "SABRE";
            case RAPIER:
                return "RAPIER";
            case SMALL_SWORD:
                return "SMALL_SWORD";
        }
        throw new IllegalStateException("inbalid WeaponType");
    }
}

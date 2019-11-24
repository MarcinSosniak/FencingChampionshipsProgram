package model.enums;


public enum WeaponType {
    RAPIER,
    SABRE,
    SMALL_SWORD;
    public static String str(WeaponType wt)
    {
        if( wt.equals(WeaponType.RAPIER))
            return "RAPIER";
        else if( wt.equals(WeaponType.SABRE))
            return "SABRE";
        else
            return "SMALL_SWORD";
    }
}

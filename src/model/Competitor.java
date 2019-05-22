package model;

import model.enums.WeaponType;

import java.util.HashMap;

public class Competitor{
    private String name;
    private String surname;
    private String everything_else;
    private int region;
    private HashMap<WeaponType,Integer> map;

    public Competitor()
    {
        for (WeaponType type : WeaponType.values())
            map.put(type,null);
    }

    public int getInCompetitionResult(WeaponType type)
    {
         return  map.get(type);
    }

    public void addPoints(int points, WeaponType type)
    {
        Integer val = map.get(type);
        val = val + points;
        map.replace(type,val);
    }
    /** non- null value means is added to given weapon*/
    public void addToWeapon(WeaponType type)
    {
        map.replace(type,0);
    }

    /**  check if Competitor starts in given weapon*/
    public boolean fStarts(WeaponType type)
    {
        return map.get(type)==null;
    }


}

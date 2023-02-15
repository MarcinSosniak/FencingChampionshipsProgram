package model.FightDrawing;

import model.*;
import model.KillerDrawing.KillerRandomizerStrategy;
import model.enums.WeaponType;
import util.HumanReadableFatalError;

import java.util.*;
import java.util.stream.Collectors;

public class FightDrawStrategyPicker {

    static private STRATEGY_NAMES DEFAULT_STRATEY_NAME= STRATEGY_NAMES.DEFAULT;
    private STRATEGY_NAMES strName;


    public FightDrawStrategyPicker() {
        strName=DEFAULT_STRATEY_NAME;
    }

    public FightDrawStrategyPicker(boolean fFinal)
    {
        if(fFinal) {
            strName = STRATEGY_NAMES.FINAL;
        }
        else
        {
            strName=DEFAULT_STRATEY_NAME;
        }
    }

    public FightDrawStrategyPicker(FightDrawStrategyPicker.STRATEGY_NAMES strat)
    {
        strName=strat;
    }

    public FightDrawStrategy pick(KillerRandomizerStrategy strat)
    {
        switch (strName) {
            case FINAL:
                return new FinalStrategy();
            case DEFAULT:
                return new RandomDrawStrategy(strat);
            case SPACING:
                return new SpacingStrategy(strat);
            case FIRST:
                return new FirstRoundStrategy();
            case LOCATION_GROUP:
                return new LocationGroupDrawStrategy(strat);
        }
        throw new IllegalStateException("pick failed. This means someone implemented a strategy and not added it the picker");
    }







//    public static final String[] SHOWED_STRATEGY_NAMES = {"DEFAULT"};
//    public static final String[] BACKEND_STRATEGY_NAMES = {"SEMIFINALS",};




    public enum STRATEGY_NAMES
    {
        DEFAULT,
        SPACING,
        FIRST,
        FINAL,
        LOCATION_GROUP;

        public static String toString(STRATEGY_NAMES name)
        {
            if( name == STRATEGY_NAMES.DEFAULT)
            {
                return "DEFAULT";
            }
            if( name == STRATEGY_NAMES.SPACING)
            {
                return "SPACING";
            }
            if (name == STRATEGY_NAMES.FINAL)
            {
                return "FINAL";
            }
            if (name == STRATEGY_NAMES.FIRST)
            {
                return "FIRST";
            }
            if (name == STRATEGY_NAMES.LOCATION_GROUP)
            {
                return "LOCATION_GROUP";
            }
            throw new IllegalStateException("someone did not include to String here");
        }

        public static STRATEGY_NAMES fromString(String name)
        {
            name=name.toUpperCase();
            if( name.equals(STRATEGY_NAMES.DEFAULT.name()))
            {
                return STRATEGY_NAMES.DEFAULT;
            }
            if( name.equals(STRATEGY_NAMES.SPACING.name()))
            {
                return STRATEGY_NAMES.SPACING;
            }
            if (name.equals(STRATEGY_NAMES.FIRST.name()))
            {
                return STRATEGY_NAMES.FIRST;
            }
            if (name.equals(STRATEGY_NAMES.FINAL.name()))
            {
                return STRATEGY_NAMES.FINAL;
            }
            if (name.equals(STRATEGY_NAMES.LOCATION_GROUP.name()))
            {
                return STRATEGY_NAMES.LOCATION_GROUP;
            }
            System.out.println("WARNING got STRATEGY NAME THAT IS valid defaulting to DEFAULT. got '"+name+"'");
            return STRATEGY_NAMES.DEFAULT;
        }

        public static List<STRATEGY_NAMES> listAllShowed()
        {
            return Arrays.asList(STRATEGY_NAMES.DEFAULT,STRATEGY_NAMES.SPACING, STRATEGY_NAMES.LOCATION_GROUP);
        }

        public static List<String> listAllShowedString()
        {
            return STRATEGY_NAMES.listAllShowed().stream().map(x-> STRATEGY_NAMES.toString(x)).collect(Collectors.toList());
        }

        public static List<STRATEGY_NAMES> listAll()
        {
            return Arrays.asList(STRATEGY_NAMES.DEFAULT,STRATEGY_NAMES.SPACING,STRATEGY_NAMES.FINAL, STRATEGY_NAMES.LOCATION_GROUP);
        }

        public static List<String> listAllString()
        {
            return STRATEGY_NAMES.listAll().stream().map(x-> STRATEGY_NAMES.toString(x)).collect(Collectors.toList());
        }
    }

}


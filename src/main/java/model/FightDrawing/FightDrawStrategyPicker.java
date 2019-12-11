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
        FINAL;

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
            throw new IllegalStateException("someone did not incluue to String here");
        }

        public static STRATEGY_NAMES fromString(String name)
        {
            name=name.toUpperCase();
            if( name.equals("DEFAULT"))
            {
                return STRATEGY_NAMES.DEFAULT;
            }
            if( name.equals("SPACING"))
            {
                return STRATEGY_NAMES.SPACING;
            }
            if (name.equals("FIRST"))
            {
                return STRATEGY_NAMES.FIRST;
            }
            if (name.equals("FINAL"))
            {
                return STRATEGY_NAMES.FINAL;
            }
            throw new IllegalStateException("Invalid string given '" +name+"'");
        }

        public static List<STRATEGY_NAMES> listAllShowed()
        {
            return Arrays.asList(STRATEGY_NAMES.DEFAULT,STRATEGY_NAMES.SPACING);
        }

        public static List<String> listAllShowedString()
        {
            return STRATEGY_NAMES.listAllShowed().stream().map(x-> STRATEGY_NAMES.toString(x)).collect(Collectors.toList());
        }

        public static List<STRATEGY_NAMES> listAll()
        {
            return Arrays.asList(STRATEGY_NAMES.DEFAULT,STRATEGY_NAMES.SPACING,STRATEGY_NAMES.FINAL);
        }

        public static List<String> listAllString()
        {
            return STRATEGY_NAMES.listAll().stream().map(x-> STRATEGY_NAMES.toString(x)).collect(Collectors.toList());
        }
    }

}


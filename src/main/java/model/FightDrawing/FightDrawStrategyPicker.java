package model.FightDrawing;

import model.CompetitionGroup;
import model.Fight;
import model.KillerDrawing.KillerRandomizerStrategy;
import model.Participant;
import model.Round;
import util.HumanReadableFatalError;

import java.util.*;
import java.util.stream.Collectors;

public class FightDrawStrategyPicker {

    private boolean finals_picker=false;
    private boolean fPrivate;
    private SHOWED_STRATEGY_NAMES s_name;
    private PRIVATE_STATEGY_NAMES p_name;

    public FightDrawStrategyPicker() {}
    public FightDrawStrategyPicker(boolean fFinal)
    {
        finals_picker=fFinal;
    }

    public FightDrawStrategy pick(KillerRandomizerStrategy strat)
    {
        if(fPrivate)
        {
            switch (p_name)
            {
                case FINAL:
                    return new FinalStrategy();
            }
        }
        else {
            switch (s_name)
            {
                case DEFAULT:
                    return new RandomDrawStrategy(strat);

            }
    }



    public FightDrawStrategyPicker(FightDrawStrategyPicker.SHOWED_STRATEGY_NAMES strat)
    {
        fPrivate=false;
        s_name = strat;
    }

    public FightDrawStrategyPicker(FightDrawStrategyPicker.PRIVATE_STRATEGY_NAMES strat)
    {
        fPrivate=true;
        p_name = strat;
    }



//    public static final String[] SHOWED_STRATEGY_NAMES = {"DEFAULT"};
//    public static final String[] BACKEND_STRATEGY_NAMES = {"SEMIFINALS",};




    public enum SHOWED_STRATEGY_NAMES
    {
        DEFAULT;

        public static String toString(SHOWED_STRATEGY_NAMES name)
        {
            if( name == SHOWED_STRATEGY_NAMES.DEFAULT)
            {
                return "DEFAULT";
            }
            return "";
        }

        public static List<SHOWED_STRATEGY_NAMES> listAll()
        {
            return Arrays.asList(SHOWED_STRATEGY_NAMES.DEFAULT);
        }

        public static List<String> listAllString()
        {
            return SHOWED_STRATEGY_NAMES.listAll().stream().map(x-> SHOWED_STRATEGY_NAMES.toString(x)).collect(Collectors.toList());
        }
    }

    public enum PRIVATE_STATEGY_NAMES
    {
        FINAL;

        public static String toString(PRIVATE_STATEGY_NAMES name)
        {
            if( name == PRIVATE_STATEGY_NAMES.FINAL)
            {
                return "DEFAULT";
            }
            return "";
        }

        public static List<PRIVATE_STATEGY_NAMES> listAll()
        {
            return Arrays.asList(PRIVATE_STATEGY_NAMES.FINAL);
        }

        public static List<String> listAllString()
        {
            return SHOWED_STRATEGY_NAMES.listAll().stream().map(x-> SHOWED_STRATEGY_NAMES.toString(x)).collect(Collectors.toList());
        }
    }


}

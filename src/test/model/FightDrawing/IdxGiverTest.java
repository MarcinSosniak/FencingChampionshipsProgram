package model.FightDrawing;

import model.KillerDrawing.KillerRandomizerStrategy;
import model.KillerDrawing.RandomKillerRandomizationStrategy;
import model.Participant;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class IdxGiverTest {
    @Test
    public void getIdx() throws Exception {
       SpacingStrategy sc =  new SpacingStrategy(new RandomKillerRandomizationStrategy());

       int MAX = 6;
       int DENIED =3;

       SpacingStrategy.IdxGiver idG  = sc.new IdxGiver(true,MAX,DENIED);
       List<Integer> outList=new ArrayList<>();
       Integer[] expectedList = {0,1,2,4,5,5,4,2,1,0};

       for(int i =0 ;i<(MAX-1)*2 ;i++)
       {
           Integer idx = idG.getIdx();
           outList.add(idx);
           assertEquals(outList.get(i),expectedList[i]);
       }
    }

    @Test
    public void getIdx2() throws Exception {
        SpacingStrategy sc =  new SpacingStrategy(new RandomKillerRandomizationStrategy());

        int MAX = 6;
        int DENIED =5;

        SpacingStrategy.IdxGiver idG  = sc.new IdxGiver(true,MAX,DENIED);
        List<Integer> outList=new ArrayList<>();
        Integer[] expectedList = {0,1,2,3,4,4,3,2,1,0};

        for(int i =0 ;i<(MAX-1)*2 ;i++)
        {
            Integer idx = idG.getIdx();
            outList.add(idx);
            assertEquals(outList.get(i),expectedList[i]);
        }
    }

    @Test
    public void getIdx3() throws Exception {
        SpacingStrategy sc =  new SpacingStrategy(new RandomKillerRandomizationStrategy());

        int MAX = 6;
        int DENIED =0;

        SpacingStrategy.IdxGiver idG  = sc.new IdxGiver(true,MAX,DENIED);
        List<Integer> outList=new ArrayList<>();
        Integer[] expectedList = {1,2,3,4,5,5,4,3,2,1};

        for(int i =0 ;i<(MAX-1)*2 ;i++)
        {
            Integer idx = idG.getIdx();
            outList.add(idx);
            assertEquals(outList.get(i),expectedList[i]);
        }
    }

}
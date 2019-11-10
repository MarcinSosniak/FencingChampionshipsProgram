package util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RationalNumberTest {
    @Test
    public void multiply() throws Exception {
        RationalNumber nr1 = new RationalNumber(13,7);
        RationalNumber nr2 = new RationalNumber(11,8);
        RationalNumber outcome = new RationalNumber(11*13,7*8);
        assertEquals(outcome,nr1.multiply(nr2));
    }

    @Test
    public void multiply1() throws Exception {
        RationalNumber nr1= new RationalNumber(27,13);
        int other = -2;
        RationalNumber outcome = new RationalNumber(-27*2,13);
        assertEquals(outcome,nr1.multiply(other));
    }

    @Test
    public void add() throws Exception {
        RationalNumber nr1 = new RationalNumber(13,7);
        RationalNumber nr2 = new RationalNumber(11,8);
        RationalNumber outcome = new RationalNumber(181,56);
        assertEquals(outcome,nr1.add(nr2));
    }

    @Test
    public void add_int() throws Exception {
        RationalNumber nr1= new RationalNumber(1,2);
        int other = 1;
        RationalNumber outcome = new RationalNumber(3,2);
        assertEquals(outcome,nr1.add(other));
    }

    @Test
    public void inverse() throws Exception {
        RationalNumber nr1 = new RationalNumber(7,3);
        RationalNumber outcome = new RationalNumber(-7,3);
        assertEquals(outcome,nr1.inverse());
    }

    @Test
    public void divide() throws Exception {
        RationalNumber nr1 = new RationalNumber(13,7);
        RationalNumber nr2 = new RationalNumber(11,8);
        RationalNumber outcome = new RationalNumber(104,77);
        assertEquals(outcome,nr1.divide(nr2));
    }

    @Test
    public void divide_int() throws Exception {
        RationalNumber nr1= new RationalNumber(27,13);
        int other = -2;
        RationalNumber outcome = new RationalNumber(-27,26);
        assertEquals(outcome,nr1.divide(other));
    }

    @Test
    public void substract() throws Exception {
        RationalNumber nr1 = new RationalNumber(13,7);
        RationalNumber nr2 = new RationalNumber(11,8);
        RationalNumber outcome = new RationalNumber(27,56);
        assertEquals(outcome,nr1.substract(nr2));
    }

    @Test
    public void substract_int() throws Exception {
        RationalNumber nr1= new RationalNumber(1,2);
        int other = 1;
        RationalNumber outcome = new RationalNumber(-1,2);
        assertEquals(outcome,nr1.substract(other));
    }

    @Test
    public void compare() throws Exception {
        //before
        RationalNumber s = new RationalNumber(5,3);
        RationalNumber m = new RationalNumber(7,3);
        RationalNumber l = new RationalNumber(12387,4);
        //prepare
        //test
        assertEquals(1,RationalNumber.compare(m,s));
        assertEquals(0,RationalNumber.compare(m,m));
        assertEquals(-1,RationalNumber.compare(s,m));

        assertEquals(1,RationalNumber.compare(l,s));
        assertEquals(0,RationalNumber.compare(l,l));
        assertEquals(-1,RationalNumber.compare(s,l));

        assertEquals(1,RationalNumber.compare(l,m));
        assertEquals(0,RationalNumber.compare(l,l));
        assertEquals(-1,RationalNumber.compare(m,l));
    }

    @Test
    public void greater() throws Exception {
        RationalNumber nr1 = new RationalNumber(931689,10000);
        RationalNumber nr2 = new RationalNumber(5,3);
        assertEquals(true, RationalNumber.greater(nr1,nr2));
    }

    @Test
    public void lesser() throws Exception {
        RationalNumber nr1 = new RationalNumber(2);
        RationalNumber nr2 = new RationalNumber(7,3);
        assertEquals(true, RationalNumber.lesser(nr1,nr2));
    }


    @Test
    public void test_toString1() throws Exception {
        RationalNumber number = new RationalNumber(3,3);
        assertEquals("1",number.toString());
    }

    @Test
    public void test_toString2() throws Exception {
        RationalNumber number = new RationalNumber(4,5);
        assertEquals("4/5",number.toString());
    }

    @Test
    public void test_toString3() throws Exception {
        RationalNumber number = new RationalNumber(7,3);
        assertEquals("2 1/3",number.toString());
    }

    @Test
    public void set() throws Exception {
        RationalNumber test= new RationalNumber(0);
        RationalNumber override = new RationalNumber(5,6);
        test.set(override);
        assertEquals(test,override);
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

}
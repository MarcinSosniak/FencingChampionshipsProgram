package util;

import com.sun.istack.internal.NotNull;

import java.math.BigInteger;
import java.util.Comparator;

public class RationalNumber {

    private int num;
    private int denom =1;
    public RationalNumber(int numerator, int denominator)
    {
        if(denominator==0)
            throw new IllegalArgumentException("Cannot divide by 0");
        int GCD = BigInteger.valueOf(numerator).gcd(BigInteger.valueOf(denominator)).intValue();
        this.num=numerator/GCD;
        this.denom =denominator/GCD;
    }

    public RationalNumber(int number)
    {
        num=number;
    }

    public RationalNumber multiply(RationalNumber other)
    {
        return new RationalNumber(this.num*other.num, this.denom*other.denom);
    }
    public RationalNumber multiply(int other)
    {
        return new RationalNumber(this.num*other,this.denom);
    }

    public RationalNumber add(RationalNumber other)
    {
        if(this.denom == other.denom)
        {
            return new RationalNumber(this.num + other.num,this.denom);
        }
        else
        {
            int common_denom = this.denom * other.denom;
            int left_num= this.num * other.denom;
            int right_num = other.num * this.denom;
            return new RationalNumber(left_num+right_num,common_denom);
        }
    }

    public RationalNumber add(int other)
    {
        return add( new RationalNumber(other));
    }

    private RationalNumber multiplicativeInverse()
    {
        return new RationalNumber(this.denom,this.num);
    }

    public RationalNumber inverse()
    {
        return new RationalNumber(-1* this.num,this.denom);
    }

    public RationalNumber divide(RationalNumber other) {return multiply(other.multiplicativeInverse());}
    public RationalNumber divide(int other) { return new RationalNumber(this.num,this.denom*other);}

    public RationalNumber substract(RationalNumber other) {return add(other.inverse());}
    public RationalNumber substract(int other) {return add(-1*other);}
    /** **/
    public static int compare(RationalNumber first,RationalNumber scnd)
    {
        RationalNumber left = new RationalNumber(first.num*scnd.denom, first.denom*scnd.denom);
        RationalNumber right = new RationalNumber(scnd.num*first.denom,scnd.denom*first.denom);
        int outCome = left.num - right.num;
        if(outCome >0)
            return 1;
        else if(outCome <0)
            return -1;
        return 0;
    }


    public static RNComparator getComparator() {return new RNComparator();}

    public static class RNComparator implements Comparator<RationalNumber>
    {

        @Override
        public int compare(RationalNumber o1, RationalNumber o2) {
            return RationalNumber.compare(o1,o2);
        }
    }
}

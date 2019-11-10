package util;

import java.math.BigInteger;
import java.util.Comparator;

public class RationalNumber {

    private int num;
    private int denom;
    public RationalNumber(int numerator, int denominator)
    {
        if(denominator==0)
            throw new IllegalArgumentException("Cannot divide by 0");

        boolean minus =false;
        if(numerator < 0) {
            numerator = -numerator;
            minus=true;
        }
        if(denominator < 0) {
            denominator = -denominator;
            minus = !minus;
        }
        int GCD = BigInteger.valueOf(numerator).gcd(BigInteger.valueOf(denominator)).intValue();
        this.num=numerator/GCD;
        this.denom =denominator/GCD;
        if (minus)
            this.num=-this.num;
    }

    public RationalNumber(int number)
    {
        num=number; denom=1;
    }

    public RationalNumber() {num=0; denom=1;}

    @Override
    public boolean equals(Object obj) {
        if(! (obj instanceof  RationalNumber))
            return false;
        RationalNumber other= (RationalNumber) obj;
        return num==other.num && denom==other.denom;
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
    /** implemtns '>' operator. Return 1,0,-1 **/
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

    public static boolean greater(RationalNumber left,RationalNumber right)
    {
        return compare(left,right)==1;
    }

    public static boolean lesser(RationalNumber left,RationalNumber right)
    {
        return compare(left,right)==-1;
    }


    public static Comparator<RationalNumber> getComparator() {return new RNComparator();}

    public static class RNComparator implements Comparator<RationalNumber>
    {

        @Override
        public int compare(RationalNumber o1, RationalNumber o2) {
            return RationalNumber.compare(o1,o2);
        }
    }

    @Override
    public String toString()
    {
        if(denom==1)
        {
            return String.valueOf(num);
        }
        else if (num < denom  ) // if num == denom, then denom == 1 due to reduction on creation
        {
            return String.format("%d/%d",num,denom);
        }
        else
        {
            int overflow = num / denom;
            int num_without_overflow = num - denom * overflow;
            return String.format("%d %d/%d",overflow,num_without_overflow,denom);
        }

    }

    public void set(RationalNumber other)
    {
        this.num=other.num;
        this.denom=other.denom;
    }
}

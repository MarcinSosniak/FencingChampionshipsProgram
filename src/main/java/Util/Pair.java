package Util;

public class Pair<F,S> {
    private F fst;
    private S snd;

    public Pair(F fst,S snd)
    {
        this.fst =fst;
        this.snd= snd;
    }

    public F fst()
    {
        return fst;
    }

    public S snd()
    {
        return snd;
    }

    public int hashCode() {
        int hashFirst = fst != null ? fst.hashCode() : 0;
        int hashSecond = snd != null ? snd.hashCode() : 0;

        return (hashFirst + hashSecond) * hashSecond + hashFirst;
    }

    public boolean equals(Object other) {
        if (other instanceof Pair) {
            Pair otherPair = (Pair) other;
            return
                    ((  this.fst == otherPair.fst ||
                            ( this.fst != null && otherPair.fst != null &&
                                    this.fst.equals(otherPair.fst))) &&
                            (  this.snd == otherPair.snd ||
                                    ( this.snd != null && otherPair.snd != null &&
                                            this.snd.equals(otherPair.snd))) );
        }
        return false;
    }
}

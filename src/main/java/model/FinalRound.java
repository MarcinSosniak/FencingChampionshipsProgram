package model;

public class FinalRound {

    private Fight finalFight;
    private Fight thirdPlaceFight;
    private Fight semiFinal1;
    private Fight semiFinal2;

    public boolean fFinalFightsReady() {
        if (finalFight != null && thirdPlaceFight != null) {
            return true;
        }
        return false;
    }

    public Fight getFinalFight() {
        return finalFight;
    }

    public Fight getThirdPlaceFight() {
        return thirdPlaceFight;
    }

    public Fight getSemiFinal1() {
        return semiFinal1;
    }

    public Fight getSemiFinal2() {
        return semiFinal2;
    }


}

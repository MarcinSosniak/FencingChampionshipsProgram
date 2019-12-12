package model.enums;

public enum JudgeState {
    MAIN_JUDGE,NON_JUDGE;

    @Override
    public String toString() {
        switch(this) {
            case MAIN_JUDGE: return "\u2713";     //tick
            case NON_JUDGE: return "\u2718";      //cross
        }
        return "";
    }

}

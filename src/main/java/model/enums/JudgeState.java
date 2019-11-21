package model.enums;

public enum JudgeState {
    MAIN_JUDGE,NON_JUDGE;

    @Override
    public String toString() {
        switch(this) {
            // sędzia główny --> for correct displaying
            case MAIN_JUDGE: return "s"+"\u0119"  +"dzia g" + "\u0142" + "\u00F3" + "wny";
            case NON_JUDGE: return "\u2718";
        }
        return "";
    }

}

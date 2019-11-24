package model;

import model.config.ConfigReader;
import model.exceptions.InvalidCommandException;

public final class AppMode {

    public enum APP_MODE {
        SAFE,ADMIN;
    }

    private APP_MODE mode = APP_MODE.SAFE;
    private static AppMode self;
    private String password = null;

    static void init(String password) {
        self = new AppMode();
        self.password = password;
    }

    private AppMode()
    {
        password= ConfigReader.getInstance().getStringValue("SECURITY","PASSWORD");
    }
    public void swap()
    {
        System.out.format("swapped\n");
        if (mode==APP_MODE.SAFE)
            mode=APP_MODE.ADMIN;
        else
            mode=APP_MODE.SAFE;
    }

    private static void check() {
        if (self == null) {
            System.out.println("appmode not initialized defaults to safe");
            self = new AppMode();
//            throw new InvalidCommandException();
        }
    }

    static public AppMode getMode() {
        check();
        return self;
    }

    public boolean fSafe()
    {
        return mode==APP_MODE.SAFE;
    }

    public static void setSafe() {
        check();
        self.mode = APP_MODE.SAFE;
    }

    public static void setAdmin(String password) {
        check();
        self.mode=APP_MODE.ADMIN;
    }

}

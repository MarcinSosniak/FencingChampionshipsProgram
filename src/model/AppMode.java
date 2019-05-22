package model;


import org.omg.CORBA.DynAnyPackage.Invalid;

public final class AppMode {
    public enum APP_MODE
    {
        SAFE,ADMIN;
    }

    private APP_MODE mode= AppMode.APP_MODE.SAFE;
    private static AppMode self;
    private String password = null;

    static void init(String password)
    {
        self= new AppMode();
        self.password=password;
    }

    private AppMode()
    {
        ;
    }

    private static void check()
    {
        if (self == null)
        {
            throw new InvalidCommandException();
        }
    }

    static public AppMode getMode()
    {
        check();
        return self;
    }

    public static void  setSafe()
    {
        check();
        self.mode=APP_MODE.SAFE;
    }

    public static void setAdmin(String password)
    {
        check();
        self.mode=APP_MODE.ADMIN;
    }

}

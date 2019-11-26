package model.command;

public final class ValidInvocationChecker {
    static private ValidInvocationChecker checker = new ValidInvocationChecker();

    private ValidInvocationChecker(){}

    static ValidInvocationChecker getChecker() {return checker;}
}

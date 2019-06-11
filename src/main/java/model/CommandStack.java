package model;

import model.command.Command;

import java.util.ArrayList;
import java.util.List;

public class CommandStack {
    private List<Command> stack = new ArrayList<Command>();

    public void addCommand(Command command)
    {
        command.execute();
        stack.add(command);
    }

}

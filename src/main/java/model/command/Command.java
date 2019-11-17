package model.command;

import java.io.Serializable;

public interface Command extends Serializable {
    public void execute();
    public void undo();
    public void redo();
}

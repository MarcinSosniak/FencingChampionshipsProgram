package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.command.Command;


public class CommandStack {

    private ObservableList<Command> commandStack = FXCollections.observableArrayList();
    private ObservableList<Command> undoStack = FXCollections.observableArrayList();


    public void executeCommand(Command command) {
        command.execute();
        commandStack.add(command);
        undoStack.clear();
    }

    public void redo() {
        Command command = undoStack.get(undoStack.size()-1);
        command.redo();
        undoStack.remove(undoStack.size()-1);
        commandStack.add(command);
    }

    public void undo() {
        Command command = commandStack.get(commandStack.size()-1);
        command.undo();
        commandStack.remove(commandStack.size()-1);
        undoStack.add(command);
    }

    public ObservableList<Command> getCommandStack() {
        return commandStack;
    }
}

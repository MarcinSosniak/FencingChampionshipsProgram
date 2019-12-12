package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.command.Command;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class CommandStack implements Serializable {

    private ArrayList<Command> commandStack = new ArrayList<>();
    private ArrayList<Command> undoStack = new ArrayList<>();


    public void executeCommand(Command command) {
        command.execute();
        commandStack.add(command);
        System.out.println("ex command stack:");
        undoStack.clear();
        for (int i=0; i<commandStack.size(); i++) System.out.println(commandStack.get(i));
    }

    public void redo() {
        if(undoStack.size() < 1)
            throw  new IllegalStateException("nothing to redo");
        Command command = undoStack.get(undoStack.size()-1);
        command.redo();
        undoStack.remove(undoStack.size()-1);
        commandStack.add(command);
    }

    public void undo() {
        for (int i = 0; i < commandStack.size(); i++) System.out.println(commandStack.get(i));
        System.out.println(commandStack.size() - 1);
        Command command = commandStack.get(commandStack.size() - 1);
        System.out.println("TRYING TO UNDO COMMAND: " + command);
        command.undo();
        commandStack.remove(commandStack.size() - 1);
        undoStack.add(command);
    }


    public List<Command> getCommandStack() {
        return commandStack;
    }

    public ArrayList<Command> getUndoStack() { return undoStack; }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof CommandStack) {
            if (this.commandStack.size() != ((CommandStack) obj).commandStack.size()) return false;
            for (int i=0; i<((CommandStack) obj).commandStack.size(); i++){
                if (! this.commandStack.get(i).equals(((CommandStack) obj).commandStack.get(i))) return false;
            }

            if (this.undoStack.size() != ((CommandStack) obj).undoStack.size()) return false;
            for (int i=0; i<((CommandStack) obj).undoStack.size(); i++){
                if (! this.undoStack.get(i).equals(((CommandStack) obj).undoStack.get(i))) return false;
            }
            return true;
        }
        return false;
    }

}

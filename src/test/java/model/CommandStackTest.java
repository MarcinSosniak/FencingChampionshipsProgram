package model;

import model.command.Command;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

public class CommandStackTest {

    CommandStack stack;
    private static final int MULTIPLE_TEST_SIZE=2*3*4*5*6*7;

    @Test
    public void executeCommand() throws Exception {
        Command mockedCommand = mock(Command.class);
        stack.executeCommand(mockedCommand);
        verify(mockedCommand).execute();
    }

    @Test
    public void executeCommand_multiple() throws Exception
    {

        List<Command> commandMockList = new ArrayList<>();
        for (int i=0;i<MULTIPLE_TEST_SIZE;i++)
        {
            Command mockedCommand=mock(Command.class);
            commandMockList.add(mockedCommand);
            stack.executeCommand(mockedCommand);
        }

        for(Command com : commandMockList)
        {
            verify(com).execute();
        }
    }


    @Test
    public void redo() throws Exception {
        Command mockedCommand = mock(Command.class);
        stack.executeCommand(mockedCommand);
        stack.undo();
        stack.redo();
        verify(mockedCommand).execute();
        verify(mockedCommand).undo();
        verify(mockedCommand).redo();
    }



    @Test(expected = IllegalStateException.class)
    public void redo_with_nothing_to_redo() throws Exception
    {
        Command mockedCommand = mock(Command.class);
        stack.executeCommand(mockedCommand);
        stack.redo();
    }

    @Test
    public void undo() throws Exception {
        Command mockedCommand = mock(Command.class);
        stack.executeCommand(mockedCommand);
        stack.undo();
        verify(mockedCommand).execute();
        verify(mockedCommand).undo();
    }

    @Test(expected = IllegalStateException.class)
    public void undo_with_nothing_to_undo() throws Exception
    {
        stack.undo();
    }

    @Test(expected = IllegalStateException.class)
    public  void executeAndRedoAfterUndo()
    {
        //prepare
        Command mockedCommand = mock(Command.class);
        Command mockedCommand1 = mock(Command.class);
        Command mockedCommand2 = mock(Command.class);
        try {
            stack.executeCommand(mockedCommand);
            stack.executeCommand(mockedCommand1);
            stack.undo();
            stack.executeCommand(mockedCommand2);
        }
        catch (Exception ex)
        {
            fail();
        }
        //test
        stack.redo();
    }

    @Before
    public void setUp() throws Exception {
        stack = new CommandStack();
    }

    @After
    public void tearDown() throws Exception {
        stack= null;
    }
}
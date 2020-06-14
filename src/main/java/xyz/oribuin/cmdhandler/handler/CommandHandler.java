package xyz.oribuin.cmdhandler.handler;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CommandHandler {
    private final LinkedList<Command> commands = new LinkedList<>();

    // Register Commands
    public void registerCommands(Command... commands) {
        this.commands.addAll(Arrays.asList(commands));
    }

    // Get command list
    public List<Command> getCommands() {
        return commands;
    }

    // Get command from name
    public Command getCommand(String name) {
        return getCommands().stream().filter(command -> command.getName().toLowerCase().equals(name)).findFirst().get();
    }
}

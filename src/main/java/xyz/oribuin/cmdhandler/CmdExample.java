package xyz.oribuin.cmdhandler;

import net.dv8tion.jda.api.Permission;
import xyz.oribuin.cmdhandler.handler.Command;
import xyz.oribuin.cmdhandler.handler.CommandEvent;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class CmdExample extends Command {

    public CmdExample() {
        // Define command name
        this.name = "Name";
        /*
         Define Aliases or Arguments using Arrays.asList(String...);
         Please define this.aliases in every command or it will not work
         Use Collections.emptyList() if no aliases in the command
         */
        this.aliases = Arrays.asList("alias1", "alias2");
        this.arguments = Collections.emptyList();
        // Define command description
        this.description = "Example command";
        // Define user and bot permissions
        this.botPermissions = new Permission[]{Permission.MESSAGE_MANAGE, Permission.VIEW_CHANNEL};
        this.userPermissions = new Permission[]{Permission.MESSAGE_MANAGE, Permission.VIEW_CHANNEL};
        // Boolean on command enabled or if owner only
        this.enabled = true;
        this.ownerOnly = false;
    }

    @Override
    public void executeCommand(CommandEvent event) {
        event.getChannel().sendMessage("Pinging!")
                .queue(msg -> msg.editMessage("Pong! (" + event.getJDA().getGatewayPing() + "ms)").queueAfter(3, TimeUnit.SECONDS));
    }
}

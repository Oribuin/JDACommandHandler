package xyz.oribuin.cmdhandler;

import net.dv8tion.jda.api.Permission;
import xyz.oribuin.cmdhandler.handler.Command;
import xyz.oribuin.cmdhandler.handler.CommandEvent;

import java.util.concurrent.TimeUnit;

public class CmdExample extends Command {

    public CmdExample() {
        this.name = "Name";
        this.aliases = new String[]{"Alias1", "Alias2"};
        this.arguments = new String[]{"Arg1", "Arg2"};
        this.description = "Example command";
        this.botPermissions = new Permission[]{Permission.MESSAGE_MANAGE, Permission.VIEW_CHANNEL};
        this.userPermissions = new Permission[]{Permission.MESSAGE_MANAGE, Permission.VIEW_CHANNEL};
        this.enabled = true;
        this.ownerOnly = false;
    }

    @Override
    public void executeCommand(CommandEvent event) {
        event.getChannel().sendMessage("Pinging!").queue(msg -> msg.editMessage("Pong! (" + event.getJDA().getGatewayPing() + ")").queueAfter(2, TimeUnit.SECONDS));
    }
}

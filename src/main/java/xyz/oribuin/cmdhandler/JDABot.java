package xyz.oribuin.cmdhandler;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import xyz.oribuin.cmdhandler.database.DatabaseConnector;
import xyz.oribuin.cmdhandler.database.SQLiteConnector;
import xyz.oribuin.cmdhandler.handler.Command;
import xyz.oribuin.cmdhandler.handler.CommandExecutor;
import xyz.oribuin.cmdhandler.handler.CommandHandler;
import xyz.oribuin.cmdhandler.managers.GuildSettingsManager;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;

public class JDABot extends ListenerAdapter {

    public static String DEFAULT_PREFIX = "!";
    public static String OWNER_ID = "345406020450779149";
    private GuildSettingsManager guildSettingsManager;
    private DatabaseConnector connector;
    private CommandHandler commandHandler;

    private static void main(String[] args) {
        try {
            new JDABot();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    private void registerCommands() {
        this.commandHandler.registerCommands(
                new CmdExample()
        );
    }

    private JDABot() throws LoginException {
        File file = new File("data", "jdabot.db");
        try {
            if (!file.exists()) {
                file.createNewFile();

                System.out.println();
            }

            // Register SQL COnnector
            this.connector = new SQLiteConnector(file);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        this.guildSettingsManager = new GuildSettingsManager(this);
        this.commandHandler = new CommandHandler();

        this.registerCommands();
        this.enable();

        JDA jda = JDABuilder.createDefault("BOT-TOKEN").addEventListeners(new CommandExecutor(this, commandHandler)).build();

        // Startup Message
        System.out.println("*=* Loading Bot Commands *=*");
        int i = 0;
        for (Command command : this.getCommandHandler().getCommands()) {
            System.out.println("Loaded Command: " + command.getName() + " | (" + ++i + "/" + this.getCommandHandler().getCommands().size() + ")");
        }

        System.out.println("*=* Loaded Up " + jda.getSelfUser().getName() + " with " + this.getCommandHandler().getCommands().size() + " Command(s) *=*");
    }

    private void enable() {
        this.guildSettingsManager.enable();
    }

    public DatabaseConnector getConnector() {
        return connector;
    }

    public GuildSettingsManager getGuildSettingsManager() {
        return guildSettingsManager;
    }

    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

}

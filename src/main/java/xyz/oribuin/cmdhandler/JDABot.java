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

    private static JDABot instance;
    public static String DEFAULT_PREFIX = "!";
    public static String OWNER_ID = "345406020450779149";
    private final GuildSettingsManager guildSettingsManager;
    private final CommandHandler commandHandler;
    private DatabaseConnector connector;

    private final EventWaiter waiter = new EventWaiter();

    public static void main(String[] args) {
        try {
            new JDABot();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    // Register all the commands
    private void registerCommands() {
        this.commandHandler.registerCommands(
                new CmdExample(waiter)
        );
    }

    // Define bot stuff
    private JDABot() throws LoginException {
        instance = this;

        // Check if the file exists, if not, create the file

        // INFO: Define the File Path and name
        File file = new File("data", "jdabot.db");
        try {

            if (!file.getParentFile().exists())
                file.mkdir();

            if (!file.exists()) {
                file.createNewFile();

                System.out.println("Created file at " + file.getAbsolutePath());
            }

            // Register SQL Connector
            this.connector = new SQLiteConnector(file);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // Register GuildSettingsManager and CommandHandler
        this.guildSettingsManager = new GuildSettingsManager(this);
        this.commandHandler = new CommandHandler();

        this.registerCommands();
        this.enable();

        // Login the bot
        JDA jda = JDABuilder.createDefault("BOT-TOKEN").addEventListeners(waiter, new CommandExecutor(this, commandHandler), new GeneralEvents(this),  this).build();

        // Startup Bot
        System.out.println("*=* Loading Bot Commands *=*");
        int i = 0;

        for (Command command : this.getCommandHandler().getCommands())
            if (command.getAliases() == null)
                throw new NullPointerException("Command aliases is null");
            else
                System.out.println("Loaded Command: " + command.getName() + " | (" + ++i + "/" + this.getCommandHandler().getCommands().size() + ")");

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

    public static JDABot getInstance() {
        return instance;
    }
}

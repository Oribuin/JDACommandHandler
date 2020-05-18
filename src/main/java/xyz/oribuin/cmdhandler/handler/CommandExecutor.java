package xyz.oribuin.cmdhandler.handler;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.PermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import xyz.oribuin.cmdhandler.JDABot;
import xyz.oribuin.cmdhandler.managers.GuildSettings;

import java.awt.*;
import java.util.Arrays;

public class CommandExecutor extends ListenerAdapter {

    private final JDABot bot;
    private final CommandHandler commandHandler;

    public CommandExecutor(JDABot bot, CommandHandler commandHandler) {
        this.bot = bot;
        this.commandHandler = commandHandler;
    }


    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        GuildSettings guildSettings = bot.getGuildSettingsManager().getGuildSettings(event.getGuild());

        for (Command cmd : commandHandler.getCommands()) {

            try {
                if (!event.getMessage().getContentRaw().toLowerCase().startsWith(guildSettings.getPrefix().toLowerCase() + cmd.getName().toLowerCase()))
                    continue;


                if (cmd.isOwnerOnly() && !event.getAuthor().getId().equals(JDABot.OWNER_ID)) {
                    event.getChannel().sendMessage(event.getAuthor().getAsMention() + ", Sorry! You don't have permission to use this command.").queue();
                    return;
                }

                if (!cmd.isEnabled())
                    return;

                if (event.getAuthor().isBot())
                    return;

                if (cmd.getBotPermissions() != null && !event.getGuild().getSelfMember().getPermissions().containsAll(Arrays.asList(cmd.getBotPermissions()))) {
                    EmbedBuilder botEmbed = new EmbedBuilder()
                            .setAuthor("Missing Permissions!")
                            .setColor(Color.decode("#33539e"))
                            .setFooter("Created by Oribuin", "https://imgur.com/ssJcsZg.png")
                            .setDescription("I am missing the permissions to execute this command.");

                    event.getChannel().sendMessage(botEmbed.build()).queue();
                    return;
                }

                if (cmd.getUserPermissions() != null && event.getMember() != null && !event.getMember().getPermissions().containsAll(Arrays.asList(cmd.getUserPermissions()))) {
                    EmbedBuilder userEmbed = new EmbedBuilder()
                            .setAuthor("Missing Permissions!")
                            .setColor(Color.decode("#33539e"))
                            .setFooter("Created by Oribuin", "https://imgur.com/ssJcsZg.png")
                            .setDescription("You are missing the permissions to execute this command.");

                    event.getChannel().sendMessage(userEmbed.build()).queue();
                    return;
                }

                cmd.executeCommand(new CommandEvent(bot, event));
            } catch (PermissionException ex) {
                System.out.println("Error Running Command: " + cmd.getName() +
                        "Guild: " + event.getGuild().getName() +
                        "Author: " + event.getAuthor().getAsTag() +
                        "Permission: " + ex.getPermission());
            }
        }
    }
}

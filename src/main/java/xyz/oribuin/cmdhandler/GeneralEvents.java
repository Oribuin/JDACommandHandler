package xyz.oribuin.cmdhandler;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GeneralEvents extends ListenerAdapter {

    private final JDABot bot;

    public GeneralEvents(JDABot bot) {
        this.bot = bot;
    }

    public void onReady(ReadyEvent event) {
        // Load all the guilds on startup
        for (Guild guild : event.getJDA().getGuilds()) {
            bot.getGuildSettingsManager().loadGuildSettings(guild);
        }
    }

    // Create guild inside SQLite Database and load settings
    public void onGuildJoin(GuildJoinEvent event) {
        bot.getGuildSettingsManager().createGuild(event.getGuild(), JDABot.DEFAULT_PREFIX);
        bot.getGuildSettingsManager().loadGuildSettings(event.getGuild());
    }

    // Remove guild from SQLite Database
    public void onGuildLeave(GuildLeaveEvent event) {
        bot.getGuildSettingsManager().removeGuild(event.getGuild());
    }
}

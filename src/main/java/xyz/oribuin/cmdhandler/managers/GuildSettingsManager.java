package xyz.oribuin.cmdhandler.managers;

import net.dv8tion.jda.api.entities.Guild;
import xyz.oribuin.cmdhandler.JDABot;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class GuildSettingsManager extends Manager {

    private Map<String, GuildSettings> guildSettings;

    public GuildSettingsManager(JDABot bot) {
        super(bot);
        this.guildSettings = new HashMap<>();
    }

    @Override
    public void enable() {
        this.createTable();
    }
    public void createTable() {
    // Create the Guild Settings table if it doesn't exist
         this.bot.getConnector().connect(connection -> {
            try (PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS guild_settings (guild_id LONG, prefix TXT, PRIMARY KEY(guild_id))")) {
                statement.executeUpdate();
            }
        });
    }

    public void loadGuildSettings(Guild guild) {
        if (this.guildSettings.containsKey(guild.getId()))
            return;

        this.bot.getConnector().connect(connection -> {
            GuildSettings settings = GuildSettings.getDefault();

            this.guildSettings.put(guild.getId(), settings);

            String commandPrefix = "SELECT prefix FROM guild_settings WHERE guild_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(commandPrefix)) {
                statement.setLong(1, guild.getIdLong());
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next())
                    settings.setPrefix(resultSet.getString(1));
            }
        });
    }


    public void createGuild(Guild guild, String prefix) {
        bot.getConnector().connect(connection -> {
            String createGuild = "INSERT INTO guild_settings (guild_id, prefix) VALUES (?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(createGuild)) {
                statement.setLong(1, guild.getIdLong());
                statement.setString(2, prefix);
                statement.executeUpdate();
            }
        });
    }

    public void removeGuild(Guild guild) {
        bot.getConnector().connect(connection -> {
            String deleteGuild = "REMOVE FROM guild_settings WHERE guild_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(deleteGuild)) {
                statement.setLong(1, guild.getIdLong());
                statement.executeUpdate();
            }
        });
    }

}

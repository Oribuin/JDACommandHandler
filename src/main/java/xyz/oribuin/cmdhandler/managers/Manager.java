package xyz.oribuin.cmdhandler.managers;

import xyz.oribuin.cmdhandler.JDABot;

public abstract class Manager {

    protected final JDABot bot;

    public Manager(JDABot bot) {
        this.bot = bot;
    }

    public abstract void enable();
}

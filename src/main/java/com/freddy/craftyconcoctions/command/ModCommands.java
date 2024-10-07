package com.freddy.craftyconcoctions.command;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class ModCommands
{
    public static void register()
    {
        CommandRegistrationCallback.EVENT.register(PotionCommand::handlePotionCommand);
    }
}

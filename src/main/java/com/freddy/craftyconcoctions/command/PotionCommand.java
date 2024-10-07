package com.freddy.craftyconcoctions.command;

import com.freddy.craftyconcoctions.block.witch_cauldron.ResultCalculator;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import java.util.ArrayList;
import java.util.List;

public class PotionCommand
{
    @SuppressWarnings("unused")
    public static void handlePotionCommand(CommandDispatcher<ServerCommandSource> dispatcher,
                                           CommandRegistryAccess commandRegistryAccess,
                                           CommandManager.RegistrationEnvironment registrationEnvironment)
    {
        dispatcher.register(CommandManager.literal("potion")
                .requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.argument("target", EntityArgumentType.player())
                        .then(CommandManager.argument("item1", IdentifierArgumentType.identifier())
                                .suggests((context, builder) -> CommandSource.suggestIdentifiers(Registries.ITEM.getIds(), builder))
                                .executes(context -> executeCreatePotion(context, 1))
                                .then(CommandManager.argument("item2", IdentifierArgumentType.identifier())
                                        .suggests((context, builder) -> CommandSource.suggestIdentifiers(Registries.ITEM.getIds(), builder))
                                        .executes(context -> executeCreatePotion(context, 2))
                                        .then(CommandManager.argument("item3", IdentifierArgumentType.identifier())
                                                .suggests((context, builder) -> CommandSource.suggestIdentifiers(Registries.ITEM.getIds(), builder))
                                                .executes(context -> executeCreatePotion(context, 3))
                                                .then(CommandManager.argument("item4", IdentifierArgumentType.identifier())
                                                        .suggests((context, builder) -> CommandSource.suggestIdentifiers(Registries.ITEM.getIds(), builder))
                                                        .executes(context -> executeCreatePotion(context, 4))
                                                )
                                        )
                                )
                        )
                )
        );
    }

    private static int executeCreatePotion(CommandContext<ServerCommandSource> context, int itemAmount) throws CommandSyntaxException
    {
        List<Item> list = new ArrayList<>();
        if (itemAmount >= 1)
            list.add(Registries.ITEM.get(IdentifierArgumentType.getIdentifier(context, "item1")));
        if (itemAmount >= 2)
            list.add(Registries.ITEM.get(IdentifierArgumentType.getIdentifier(context, "item2")));
        if (itemAmount >= 3)
            list.add(Registries.ITEM.get(IdentifierArgumentType.getIdentifier(context, "item3")));
        if (itemAmount >= 4)
            list.add(Registries.ITEM.get(IdentifierArgumentType.getIdentifier(context, "item4")));
        ResultCalculator.ResultCalculatorOutput output = ResultCalculator.getResult(list);
        EntityArgumentType.getPlayer(context, "target").giveItemStack(output.getOutput());
        return 1;
    }
}

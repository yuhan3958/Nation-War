package com.nationwar.nation.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.nationwar.core.server.claim.ClaimPolicyService;
import com.nationwar.nation.common.Nation;
import com.nationwar.nation.common.event.NationUpdateEvent;
import com.nationwar.nation.server.claim.ClaimManager;
import com.nationwar.nation.server.manager.NationManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.common.MinecraftForge;

import java.util.Optional;

public class NationCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, NationManager nationManager, ClaimManager claimManager, ClaimPolicyService claimPolicyService) {
        dispatcher.register(Commands.literal("nation")
                .then(Commands.literal("create")
                        .then(Commands.argument("name", StringArgumentType.word())
                                .executes(context -> createNation(context.getSource(), StringArgumentType.getString(context, "name"), nationManager))))
                .then(Commands.literal("claim")
                        .executes(context -> claimChunk(context.getSource(), nationManager, claimManager, claimPolicyService)))
                .then(Commands.literal("setting")
                        .then(Commands.literal("color")
                                .then(Commands.argument("hex", StringArgumentType.word())
                                        .executes(context -> setColor(context.getSource(), StringArgumentType.getString(context, "hex"), nationManager)))))
        );
    }

    private static int createNation(CommandSourceStack source, String name, NationManager nationManager) {
        ServerPlayer player = source.getPlayer();
        if (player == null) {
            source.sendFailure(Component.literal("This command can only be run by a player."));
            return 0;
        }

        if (nationManager.isPlayerInNation(player.getUUID())) {
            source.sendFailure(Component.literal("You are already in a nation."));
            return 0;
        }

        if (nationManager.getNationByName(name).isPresent()) {
            source.sendFailure(Component.literal("A nation with this name already exists."));
            return 0;
        }

        nationManager.createNation(name, player.getUUID());
        source.sendSuccess(() -> Component.literal("Nation '" + name + "' created!"), true);
        return 1;
    }

    private static int claimChunk(CommandSourceStack source, NationManager nationManager, ClaimManager claimManager, ClaimPolicyService claimPolicyService) {
        ServerPlayer player = source.getPlayer();
        if (player == null) {
            source.sendFailure(Component.literal("This command can only be run by a player."));
            return 0;
        }

        Optional<Nation> playerNationOpt = nationManager.getNationOfPlayer(player.getUUID());
        if (playerNationOpt.isEmpty()) {
            source.sendFailure(Component.literal("You are not in a nation."));
            return 0;
        }
        Nation playerNation = playerNationOpt.get();

        ChunkPos chunkPos = new ChunkPos(player.blockPosition());
        if (!claimPolicyService.isClaimable(player.level().dimension())) {
            source.sendFailure(Component.literal("You cannot claim land in this dimension."));
            return 0;
        }

        if (claimManager.getNationAt(player.level().dimension(), chunkPos).isPresent()) {
            source.sendFailure(Component.literal("This chunk is already claimed."));
            return 0;
        }

        if (claimManager.claimChunk(playerNation, player.level().dimension(), chunkPos)) {
            source.sendSuccess(() -> Component.literal("Chunk (" + chunkPos.x + ", " + chunkPos.z + ") claimed!"), true);
            return 1;
        } else {
            source.sendFailure(Component.literal("Failed to claim chunk. It might have been claimed by someone else just now."));
            return 0;
        }
    }

    private static int setColor(CommandSourceStack source, String hex, NationManager nationManager) {
        ServerPlayer player = source.getPlayer();
        if (player == null) {
            source.sendFailure(Component.literal("This command can only be run by a player."));
            return 0;
        }

        Optional<Nation> playerNationOpt = nationManager.getNationOfPlayer(player.getUUID());
        if (playerNationOpt.isEmpty()) {
            source.sendFailure(Component.literal("You are not in a nation."));
            return 0;
        }
        Nation playerNation = playerNationOpt.get();

        try {
            // Basic validation for hex color string
            if (hex.startsWith("#")) {
                hex = hex.substring(1);
            }
            int color = Integer.parseInt(hex, 16);
            playerNation.setColor(color);
            MinecraftForge.EVENT_BUS.post(new NationUpdateEvent(playerNation));
            source.sendSuccess(() -> Component.literal("Nation color changed to #" + hex), true);
            return 1;
        } catch (NumberFormatException e) {
            source.sendFailure(Component.literal("Invalid hex color format. Use RRGGBB, e.g., FF0000 for red."));
            return 0;
        }
    }
}

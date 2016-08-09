/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.drools.minecraft.adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerOpenContainerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.drools.game.capture.flag.cmds.CommandRegistry;
import org.drools.game.capture.flag.cmds.EnterZoneCommand;
import org.drools.game.capture.flag.cmds.ExitZoneCommand;
import org.drools.game.capture.flag.cmds.PickFlagCommand;
import org.drools.game.core.api.GameSession;
import org.drools.game.core.*;
import org.drools.game.core.api.GameConfiguration;
import org.drools.game.capture.flag.model.Chest;
import org.drools.game.capture.flag.model.Flag;
import org.drools.game.capture.flag.model.Location;
import org.drools.game.capture.flag.model.NamedLocation;
import org.drools.game.capture.flag.model.Zone;
import org.drools.game.capture.flag.model.Team;
import org.drools.game.core.api.Command;
import org.drools.game.core.api.Context;
import org.drools.game.core.api.PlayerConfiguration;
import org.drools.game.model.api.Player;
import org.drools.game.model.impl.base.BasePlayerImpl;
import org.drools.minecraft.adapter.cmds.ChangeScoreCommand;
import org.drools.minecraft.adapter.cmds.ClearPlayerInventoryLogicalCommand;
import org.kie.api.runtime.rule.FactHandle;

public class NewAdapter
{

    private static final NewAdapter INSTANCE = new NewAdapter();

    private int throttle = 0;
    private final int maxThrottle = 20;
    private boolean hasSetUpWorld;

    private GameSession game;

    public NewAdapter()
    {
        game = new GameSessionImpl();
        game.setExecutor(new CommandExecutorImpl());
        game.setMessageService(new GameMessageServiceImpl());
        game.setCallbackService(new GameCallbackServiceImpl());
        CommandRegistry.set("TELEPORT_CALLBACK", "org.drools.minecraft.adapter.cmds.TeleportPlayerCommand");
        CommandRegistry.set("CLEAR_INVENTORY_CALLBACK", "org.drools.minecraft.adapter.cmds.ClearPlayerInventoryCommand");
        CommandRegistry.set("NOTIFY_VIA_CHAT_CALLBACK", "org.drools.minecraft.adapter.cmds.NotifyViaChatCommand");
        CommandRegistry.set("NOTIFY_ALL_VIA_CHAT_CALLBACK", "org.drools.minecraft.adapter.cmds.NotifyAllViaChatCommand");
        CommandRegistry.set("RESET_FLAG_CALLBACK", "org.drools.minecraft.adapter.cmds.ResetFlagCommand");
        CommandRegistry.set("SET_PLAYER_HEALTH_CALLBACK", "org.drools.minecraft.adapter.cmds.SetPlayerHealthCommand");
        CommandRegistry.set("SET_PLAYER_PARAM_CALLBACK", "org.drools.minecraft.adapter.cmds.SetPlayerParamCommand");
        CommandRegistry.set("CLEAR_PLAYER_INVENTORY_LOGICAL_CALLBACK", "org.drools.minecraft.adapter.cmds.ClearPlayerInventoryLogicalCommand");
        CommandRegistry.set("CHANGE_SCORE_CALLBACK", "org.drools.minecraft.adapter.cmds.ChangeScoreCommand");
        bootstrapWorld();
        hasSetUpWorld = false;
    }

    private void bootstrapWorld()
    {
        List initFacts = new ArrayList();
        Chest chest = new Chest("Flag Chest", new Location(182, 94, -276));
        initFacts.add(chest);
        Team redTeam = new Team("red");
        initFacts.add(redTeam);
        Zone scoreZoneRed = new Zone("red", 155, 94, -280, 151, 99, -272);
        initFacts.add(scoreZoneRed);
        NamedLocation redSpawn = new NamedLocation("red", 153, 98, -275);
        initFacts.add(redSpawn);
        Team blueTeam = new Team("blue");
        initFacts.add(blueTeam);
        Zone scoreZoneBlue = new Zone("blue", 209, 94, -272, 213, 99, -280);
        initFacts.add(scoreZoneBlue);
        NamedLocation blueSpawn = new NamedLocation("blue", 211, 97, -275);
        initFacts.add(blueSpawn);
        Flag flag = new Flag("Flag", "banner");
        initFacts.add(flag);
        Zone chasm = new Zone("Chasm", 141, 80, -310, 260, 62, -199);
        initFacts.add(chasm);
        GameConfiguration config = new BaseGameConfigurationImpl(initFacts, "");
        game.bootstrap(config);
    }

    public static NewAdapter getInstance()
    {
        return INSTANCE;
    }

    private void update(World world) throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        if (!hasSetUpWorld)
        {
            if (world.isAreaLoaded(ChangeScoreCommand.blueScorePos, ChangeScoreCommand.redScorePos.add(0, 23, 40)))
            {
                for (int i = 0; i < 20; i++)
                {
                    for (int a = 0; a < 3 + i; a++)
                    {
                        world.setBlockToAir(ChangeScoreCommand.blueScorePos.add(0, a, i * 2));
                        world.setBlockToAir(ChangeScoreCommand.redScorePos.add(0, a, i * 2));

                        world.setBlockToAir(ChangeScoreCommand.redScorePos.add(0, a, i * 2));

                    }
                }
                hasSetUpWorld = true;
            }
        }

        for (String player : game.getPlayers())
        {
            EntityPlayer playerEntity = world.getPlayerEntityByName(player);

            Location location = new Location(playerEntity.getPosition().getX(),
                    playerEntity.getPosition().getY(),
                    playerEntity.getPosition().getZ());

            Collection<Zone> zones = game.getGameObjects(Zone.class);
            for (Zone zone : zones)
            {
                if (UtilMathHelper.playerWithinZone(location, zone))
                {
                    game.execute(new EnterZoneCommand(game.getPlayerByName(player), zone));
                } else if (zone.getPlayersInZone().contains(player))
                {
                    game.execute(new ExitZoneCommand(game.getPlayerByName(player), zone));
                }
            }

            game.execute(new ClearPlayerInventoryLogicalCommand(game.getPlayerByName(player)));
            if (UtilMathHelper.playerPickedTheFlag(playerEntity))
            {
                Collection<Flag> flags = game.getGameObjects(Flag.class);
                game.execute(new PickFlagCommand(game.getPlayerByName(player), flags.iterator().next()));
            }
            dealWithCallbacks(world);
        }

    }

    /**
     * Execute any updates that occur when the game ticks.
     *
     * @param event
     */
    @SubscribeEvent
    public void onServerTick(TickEvent.WorldTickEvent event) throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        if (!event.world.isRemote)
        {
            if (event.phase == TickEvent.WorldTickEvent.Phase.START)
            {
                return;
            }

            throttle++;
            if (throttle >= maxThrottle)
            {
                throttle = 0;

                //for simplicity's sake, this locks the adapter into only working
                //in the default dimension. Rules will not work in the nether or end.
                //We should change this at some point.
                if (event.world.provider.getDimension() == 0)
                {
                    update(event.world);
                }
            }
        }
    }

    /**
     * Set up player session, inventory, etc.
     *
     * @param event
     */
    @SubscribeEvent
    public void onPlayerJoin(EntityJoinWorldEvent event)
    {
        if (!event.getWorld().isRemote)
        {
            if (event.getEntity() instanceof EntityPlayer)
            {
                PlayerConfiguration playerConfig = new BasePlayerConfigurationImpl(null);
                String name = event.getEntity().getDisplayName().getUnformattedText();
                Player player = new BasePlayerImpl(name);
                game.join(player, playerConfig);
            }
        }
    }

    /**
     * When the player dies, remove him from any occupied rooms.
     *
     * @param event
     */
    @SubscribeEvent
    public void onPlayerDie(LivingDeathEvent event)
    {
        if (!event.getEntity().worldObj.isRemote)
        {
            if (event.getEntity() instanceof EntityPlayer)
            {
                String name = event.getEntity().getDisplayName().getUnformattedText();
                game.drop(game.getPlayerByName(name));
            }
        }
    }

    private void dealWithCallbacks(World world)
    {
        Context callbackCtx = new ContextImpl();
        callbackCtx.getData().put("world", world);
        Queue<Command> callbacks = game.getCallbacks();
        while (callbacks.peek() != null)
        {
            Command cmd = callbacks.poll();
            cmd.execute(callbackCtx);
        }
    }

}

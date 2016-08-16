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

package org.drools.minecraft.adapter.cmds;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.drools.game.core.api.BaseCommand;
import org.drools.game.core.api.Context;
import org.drools.game.model.api.Player;

/**
 * command that clears the player inventory in minecraft. This is 
 * used to ensure that the player
 * @author salaboy
 */
public class ClearPlayerInventoryCommand extends BaseCommand<Void> {

    /**
     * 
     * @param player 
     */
    public ClearPlayerInventoryCommand( Player player ) {
        super( player );
    }

    @Override
    public Void execute( Context ctx ) {
        System.out.println( " Clearing player " + getPlayer().getName() + " inventory!");
        World world = ( World ) ctx.getData().get( "world" );
        EntityPlayer playerEntity = world.getPlayerEntityByName( getPlayer().getName() );
        playerEntity.inventory.clear();
        return null;
    }

}

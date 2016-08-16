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
 * Sets the player health.
 * @author salaboy
 */
public class SetPlayerHealthCommand extends BaseCommand<Void> {

    private Integer health;

    public SetPlayerHealthCommand( Player player, Integer health ) {
        super( player );
        this.health = health;
    }

    @Override
    public Void execute( Context ctx ) {
        System.out.println( "Setting player health for: " + getPlayer().getName() + " To:" + health );
        World world = ( World ) ctx.getData().get( "world" );
        EntityPlayer playerEntity = world.getPlayerEntityByName( getPlayer().getName() );
        playerEntity.setHealth(health);
        return null;
    }

    public Integer getHealth() {
        return health;
    }

    public void setHealth( Integer health ) {
        this.health = health;
    }

}

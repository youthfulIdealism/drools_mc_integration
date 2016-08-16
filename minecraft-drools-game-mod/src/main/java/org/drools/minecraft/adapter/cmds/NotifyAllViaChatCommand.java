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
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import org.drools.game.core.api.BaseCommand;
import org.drools.game.core.api.Context;
import org.drools.game.model.api.Player;
/**
 * Command that sends a chat message to all players within Minecraft.
 * @author Samuel
 */
public class NotifyAllViaChatCommand extends BaseCommand<Void> {

    private String message;

    public NotifyAllViaChatCommand( Player player, String message ) {
        super( player );
        this.message = message;
    }

    @Override
    public Void execute( Context ctx ) {
        System.out.println( "Notifying via chat to all players Message: " + message );
        World world = ( World ) ctx.getData().get( "world" );
        for(EntityPlayer playerEntity : world.playerEntities)
        {
            playerEntity.addChatMessage(new TextComponentString(message));
        }
        return null;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage( String message ) {
        this.message = message;
    }

}

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

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.drools.game.capture.flag.model.Team;
import org.drools.game.core.api.BaseCommand;
import org.drools.game.core.api.Context;
import org.drools.game.model.api.Player;
import org.drools.minecraft.adapter.UtilMathHelper;

/**
 * Command that changes the scoreboard within minecraft to correctly represent the score.
 * @author Samuel
 */
public class ChangeScoreCommand extends BaseCommand<Void> {
    
    private Team team;
    public static final BlockPos redScorePos = new BlockPos(155, 110, -280);
    public static final BlockPos blueScorePos = new BlockPos( 209, 110, -272);
    
    /**
     * 
     * @param player
     * @param team 
     */
    public ChangeScoreCommand( Player player, Team team ) {
        super( player );
        this.team = team;
    }

    /**
     * gets the team subjected to a score change
     * @return 
     */
    public Team getTeam()
    {
        return team;
    }

    /**
     * sets the team subjected to  a score change
     * @param team 
     */
    public void setTeam(Team team)
    {
        this.team = team;
    }
    
    @Override
    public Void execute( Context ctx ) {
        World world = ( World ) ctx.getData().get( "world" );
        BlockPos startingPos = null;
        IBlockState blockstate = null;
        
        if(team.getName().equals("red"))
        {
            startingPos = redScorePos;
            blockstate = Blocks.REDSTONE_BLOCK.getDefaultState();
            
        }else if(team.getName().equals("blue"))
        {
            startingPos = blueScorePos;
            blockstate = Blocks.LAPIS_BLOCK.getDefaultState();
        }
        
        UtilMathHelper.displayScore(world, startingPos, blockstate, team.getPoints());
        
        return null;
    }
}
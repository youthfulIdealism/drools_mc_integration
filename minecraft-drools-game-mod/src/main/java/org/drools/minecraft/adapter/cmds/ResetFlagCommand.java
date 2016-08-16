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
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.minecraftforge.fml.common.registry.GameRegistry;
import org.drools.game.capture.flag.model.Chest;
import org.drools.game.capture.flag.model.Flag;
import org.drools.game.capture.flag.model.Location;
import org.drools.game.core.api.BaseCommand;
import org.drools.game.core.api.Context;
import org.drools.game.model.api.Player;

/**
 * Command that puts thee flag back into the starting chest
 * @author salaboy
 */
public class ResetFlagCommand extends BaseCommand<Void> {

    private Chest chest;
    private Flag flag;

    public ResetFlagCommand( Player player, Chest chest, Flag flag ) {
        super( player );
        this.chest = chest;
        this.flag = flag;
    }

    //TODO: Find out how much of this behemoth is necessary.
    @Override
    public Void execute( Context ctx ) {

        World world = ( World ) ctx.getData().get( "world" );
        Location location = chest.getLocation();
        BlockPos pos = new BlockPos( location.getX(), location.getY(), location.getZ() );
        world.setBlockState( pos, net.minecraft.init.Blocks.CHEST.getDefaultState(), 2 );
        TileEntity chestEntity = world.getTileEntity( pos );

        //generate the item and place it in the chest
        net.minecraft.item.Item minecraftItem = GameRegistry.findItem( "examplemod", flag.getType().replace( "item.", "" ).replace( "block.", "" ) );
        if ( minecraftItem == null ) {
            minecraftItem = GameRegistry.findItem( "minecraft", flag.getType().replace( "item.", "" ).replace( "block.", "" ) );
        }
        if ( minecraftItem == null ) {
            //TODO: find a way to handle any additional mods
        }
        if ( minecraftItem != null ) {
            ItemStack stack = new ItemStack( minecraftItem );
            if ( chestEntity instanceof TileEntityChest ) {
                for ( int i = 0; i < ( ( TileEntityChest ) chestEntity ).getSizeInventory(); i++ ) {
                    if ( ( ( TileEntityChest ) chestEntity ).getStackInSlot( i ) == null ) {
                        ( ( TileEntityChest ) chestEntity ).setInventorySlotContents( i, stack.setStackDisplayName( flag.getName() ) );
                        break;
                    }
                }
            } else {
                System.out.println( "Error placing item in chest: chest not found in world." );
            }
        } else {
            System.out.println( "Error placing item in chest: item " + flag.getType() + " not found." );
        }
        
        
        //Remove the flag from the player's inventory
        EntityPlayer playerEntity = world.getPlayerEntityByName( getPlayer().getName() );
        for ( int i = 0; i < playerEntity.inventory.mainInventory.length; i++ ) {
            ItemStack stack = playerEntity.inventory.mainInventory[i];
            if ( stack != null ) {
                System.out.println( "Item: " + stack.getUnlocalizedName() + " -  " + stack.getDisplayName() );
                if ( stack.getDisplayName().equals( "Flag" ) ) {
                    playerEntity.inventory.mainInventory[i] = null;
                }
            }
        }
        
        return null;
    }

    public Chest getChest() {
        return chest;
    }

    public void setChest( Chest chest ) {
        this.chest = chest;
    }

}

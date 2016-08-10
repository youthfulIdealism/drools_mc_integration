/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drools.minecraft.adapter;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.drools.game.capture.flag.model.Location;
import org.drools.game.capture.flag.model.Zone;

/**
 *
 * @author Samuel
 */
public class UtilMathHelper {
    //Flag that determines whether the game uses tickmarks or numerals to display the score over the map.
    public static final boolean useTickMarks = false;
    
    public static final boolean[] numeral0 = new boolean[]
                                           { true, true, true,
                                             true, false, true,
                                             true, false, true,
                                             true, false, true,
                                             true, true, true};
    public static final boolean[] numeral1 = new boolean[]
                                           { false, false, true,
                                             false, false, true,
                                             false, false, true,
                                             false, false, true,
                                             false, false, true};
    public static final boolean[] numeral2 = new boolean[]
                                           { true, true, true,
                                             false, false, true,
                                             true, true, true,
                                             true, false, false,
                                             true, true, true};
    public static final boolean[] numeral3 = new boolean[]
                                           { true, true, true,
                                             false, false, true,
                                             true, true, true,
                                             false, false, true,
                                             true, true, true};
    public static final boolean[] numeral4 = new boolean[]
                                           { true, false, true,
                                             true, false, true,
                                             true, true, true,
                                             false, false, true,
                                             false, false, true};
    public static final boolean[] numeral5 = new boolean[]
                                           { true, true, true,
                                             true, false, false,
                                             true, true, true,
                                             false, false, true,
                                             true, true, true};
    public static final boolean[] numeral6 = new boolean[]
                                           { true, true, true,
                                             true, false, false,
                                             true, true, true,
                                             true, false, true,
                                             true, true, true};
    public static final boolean[] numeral7 = new boolean[]
                                           { true, true, true,
                                             false, false, true,
                                             false, false, true,
                                             false, false, true,
                                             false, false, true};
    public static final boolean[] numeral8 = new boolean[]
                                           { true, true, true,
                                             true, false, true,
                                             true, true, true,
                                             true, false, true,
                                             true, true, true};
    public static final boolean[] numeral9 = new boolean[]
                                           { true, true, true,
                                             true, false, true,
                                             true, true, true,
                                             false, false, true,
                                             false, false, true};

    /**
     * helper method. Determines if a player is mathematically
     * within a zone.
     *
     * @param player
     * @param zone
     *
     * @return
     */
    public static boolean playerWithinZone( Location playerLoc, Zone zone ) {
        Location roomLowerLoc = zone.getLowerBound();
        Location roomUpperLoc = zone.getUpperBound();
        boolean xWithin = within( playerLoc.getX(), roomLowerLoc.getX(), roomUpperLoc.getX() );
        boolean yWithin = within( playerLoc.getY(), roomLowerLoc.getY(), roomUpperLoc.getY() );
        boolean zWithin = within( playerLoc.getZ(), roomLowerLoc.getZ(), roomUpperLoc.getZ() );
        return xWithin && yWithin && zWithin;
    }

    /**
     * helper method. Determines if a number is within bounds.
     *
     * @param number
     * @param first
     * @param second
     *
     * @return
     */
    private static boolean within( int number, int first, int second ) {
        int min = Math.min( first, second );
        int max = Math.max( first, second );
        return number >= min && number <= max;
    }

    public static boolean playerPickedTheFlag( EntityPlayer player ) {

        for ( int i = 0; i < player.inventory.mainInventory.length; i++ ) {
            ItemStack stack = player.inventory.mainInventory[i];
            if ( stack != null ) {
                System.out.println( "Item: " + stack.getUnlocalizedName() + " -  " + stack.getDisplayName() );
                if ( stack.getDisplayName().equals( "Flag" ) ) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static void displayScore(World world, BlockPos startingPos, IBlockState block, int score)
    {
        IBlockState airState = Blocks.AIR.getDefaultState();
        
        //if we are using tickmarks,
        if(useTickMarks)
        {
            //draw the latest tick mark.
            for(int i = 0; i < 3 + score; i++)
            {
                world.setBlockState(startingPos.add(0, i, score * 2), block);
            }
        }else
        {
            //if we are using numerals,
            
            //convert the score to a string,
            char[] scoreNumerals = (score + "").toCharArray();
            
            //determine the needed digitmap,
            for(int i = scoreNumerals.length - 1; i >= 0; i--)
            {
                char digit = scoreNumerals[i];
                boolean[] digitmap = null;
                if(digit == '0')
                {
                    digitmap = numeral0;
                }else if(digit == '1')
                {
                    digitmap = numeral1;
                }else if(digit == '2')
                {
                    digitmap = numeral2;
                }else if(digit == '3')
                {
                    digitmap = numeral3;
                }else if(digit == '4')
                {
                    digitmap = numeral4;
                }else if(digit == '5')
                {
                    digitmap = numeral5;
                }else if(digit == '6')
                {
                    digitmap = numeral6;
                }else if(digit == '7')
                {
                    digitmap = numeral7;
                }else if(digit == '8')
                {
                    digitmap = numeral8;
                }else if(digit == '9')
                {
                    digitmap = numeral9;
                }
                
                //and plunk it into the world.
                for(int p = 0; p < digitmap.length; p++)
                {
                    IBlockState chosen = null;
                    if(digitmap[p])
                    {
                        chosen = block;
                    }else
                    {
                        chosen = airState;
                    }
                    world.setBlockState(startingPos.add(0, -(p / 3), (scoreNumerals.length - i) * 4 + (3 - p % 3)), chosen);
                }
            }
            
            
            
        }
    }

}

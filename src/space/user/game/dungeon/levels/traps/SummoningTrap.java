/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package space.user.game.dungeon.levels.traps;

import java.util.ArrayList;

import space.user.game.dungeon.Dungeon;
import space.user.game.dungeon.actors.Actor;
import space.user.game.dungeon.actors.Char;
import space.user.game.dungeon.actors.mobs.Bestiary;
import space.user.game.dungeon.actors.mobs.Mob;
import space.user.game.dungeon.items.wands.WandOfBlink;
import space.user.game.dungeon.levels.Level;
import space.user.game.dungeon.scenes.GameScene;
import space.user.game.utils.Random;

public class SummoningTrap {

	private static final float DELAY = 2f;
	
	private static final Mob DUMMY = new Mob() {};
	
	// 0x770088
	
	public static void trigger( int pos, Char c ) {
		
		if (Dungeon.bossLevel()) {
			return;
		}
		
		if (c != null) {
			Actor.occupyCell( c );
		}
		
		int nMobs = 1;
		if (Random.Int( 2 ) == 0) {
			nMobs++;
			if (Random.Int( 2 ) == 0) {
				nMobs++;
			}
		}
		
		ArrayList<Integer> candidates = new ArrayList<Integer>();
		
		for (int i=0; i < Level.NEIGHBOURS8.length; i++) {
			int p = pos + Level.NEIGHBOURS8[i];
			if (Actor.findChar( p ) == null && (Level.passable[p] || Level.avoid[p])) {
				candidates.add( p );
			}
		}
		
		ArrayList<Integer> respawnPoints = new ArrayList<Integer>();
		
		while (nMobs > 0 && candidates.size() > 0) {
			int index = Random.index( candidates );
			
			DUMMY.pos = candidates.get( index );
			Actor.occupyCell( DUMMY );
			
			respawnPoints.add( candidates.remove( index ) );
			nMobs--;
		}
		
		for (Integer point : respawnPoints) {
			Mob mob = Bestiary.mob( Dungeon.depth );
			mob.state = mob.WANDERING;
			GameScene.add( mob, DELAY );
			WandOfBlink.appear( mob, point );
		}
	}
}

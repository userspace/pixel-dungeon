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
package space.user.game.dungeon.levels.painters;

import space.user.game.dungeon.actors.blobs.Foliage;
import space.user.game.dungeon.items.Honeypot;
import space.user.game.dungeon.levels.Level;
import space.user.game.dungeon.levels.Room;
import space.user.game.dungeon.levels.Terrain;
import space.user.game.dungeon.plants.Sungrass;
import space.user.game.utils.Random;

public class GardenPainter extends Painter {

	public static void paint( Level level, Room room ) {
		
		fill( level, room, Terrain.WALL );
		fill( level, room, 1, Terrain.HIGH_GRASS );
		fill( level, room, 2, Terrain.GRASS );
		
		room.entrance().set( Room.Door.Type.REGULAR );
		
		if (Random.Int( 2 ) == 0) {
			level.drop( new Honeypot(), room.random() );
		} else {
			int bushes = (Random.Int( 5 ) == 0 ? 2 : 1);
			for (int i=0; i < bushes; i++) {
				int pos = room.random();
				set( level, pos, Terrain.GRASS );
				level.plant( new Sungrass.Seed(), pos );
			}
		}
		
		Foliage light = (Foliage)level.blobs.get( Foliage.class );
		if (light == null) {
			light = new Foliage();
		}
		for (int i=room.top + 1; i < room.bottom; i++) {
			for (int j=room.left + 1; j < room.right; j++) {
				light.seed( j + Level.WIDTH * i, 1 );
			}
		}
		level.blobs.put( Foliage.class, light );
	}
}

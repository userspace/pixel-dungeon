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
package space.user.game.dungeon.items.rings;

import space.user.game.dungeon.Dungeon;
import space.user.game.dungeon.actors.hero.Hero;

public class RingOfDetection extends Ring {

	{
		name = "Ring of Detection";
	}
	
	@Override
	public boolean doEquip( Hero hero ) {
		if (super.doEquip( hero )) {
			Dungeon.hero.search( false );
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	protected RingBuff buff( ) {
		return new Detection();
	}
	
	@Override
	public String desc() {
		return isKnown() ?
			"Wearing this ring will allow the wearer to notice hidden secrets - " +
			"traps and secret doors - without taking time to search. Degraded rings of detection " +
			"will dull your senses, making it harder to notice secrets even when actively searching for them." :
			super.desc();
	}
	
	public class Detection extends RingBuff {
	}
}

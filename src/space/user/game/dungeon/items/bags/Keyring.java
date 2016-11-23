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
package space.user.game.dungeon.items.bags;

import space.user.game.dungeon.items.Item;
import space.user.game.dungeon.items.keys.Key;
import space.user.game.dungeon.sprites.ItemSpriteSheet;

public class Keyring extends Bag {

	{
		name = "key ring";
		image = ItemSpriteSheet.KEYRING;
		
		size = 12;
	}
	
	@Override
	public boolean grab( Item item ) {
		return item instanceof Key;
	}
	
	@Override
	public int price() {
		return 50;
	}
	
	@Override
	public String info() {
		return
			"This is a copper key ring, that lets you keep all your keys " +
			"separately from the rest of your belongings.";
	}
}

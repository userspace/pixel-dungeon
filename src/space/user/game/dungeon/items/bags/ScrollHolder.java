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
import space.user.game.dungeon.items.scrolls.Scroll;
import space.user.game.dungeon.sprites.ItemSpriteSheet;

public class ScrollHolder extends Bag {

	{
		name = "scroll holder";
		image = ItemSpriteSheet.HOLDER;
		
		size = 12;
	}
	
	@Override
	public boolean grab( Item item ) {
		return item instanceof Scroll;
	}
	
	@Override
	public int price() {
		return 50;
	}
	
	@Override
	public String info() {
		return
			"You can place any number of scrolls into this tubular container. " +
			"It saves room in your backpack and protects scrolls from fire.";
	}
}
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
package space.user.game.dungeon.items.food;

import space.user.game.dungeon.actors.buffs.Hunger;
import space.user.game.dungeon.sprites.ItemSpriteSheet;

public class ChargrilledMeat extends Food {

	{
		name = "chargrilled meat";
		image = ItemSpriteSheet.STEAK;
		energy = Hunger.STARVING - Hunger.HUNGRY;
	}
	
	@Override
	public String info() {
		return "It looks like a decent steak.";
	}
	
	@Override
	public int price() {
		return 5 * quantity;
	}
	
	public static Food cook( MysteryMeat ingredient ) {
		ChargrilledMeat result = new ChargrilledMeat();
		result.quantity = ingredient.quantity();
		return result;
	}
}

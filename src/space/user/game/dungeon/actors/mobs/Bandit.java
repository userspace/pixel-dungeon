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
package space.user.game.dungeon.actors.mobs;

import space.user.game.dungeon.Badges;
import space.user.game.dungeon.Dungeon;
import space.user.game.dungeon.actors.buffs.Blindness;
import space.user.game.dungeon.actors.buffs.Buff;
import space.user.game.dungeon.actors.hero.Hero;
import space.user.game.dungeon.items.Item;
import space.user.game.dungeon.sprites.BanditSprite;
import space.user.game.utils.Random;

public class Bandit extends Thief {
	
	public Item item;
	
	{
		name = "crazy bandit";
		spriteClass = BanditSprite.class;
	}
	
	@Override
	protected boolean steal( Hero hero ) {
		if (super.steal( hero )) {
			
			Buff.prolong( hero, Blindness.class, Random.Int( 5, 12 ) );
			Dungeon.observe();
			
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public void die( Object cause ) {
		super.die( cause );
		Badges.validateRare( this );
	}
}

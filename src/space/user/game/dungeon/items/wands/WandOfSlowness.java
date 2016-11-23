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
package space.user.game.dungeon.items.wands;

import space.user.game.noosa.audio.Sample;
import space.user.game.dungeon.Assets;
import space.user.game.dungeon.actors.Actor;
import space.user.game.dungeon.actors.Char;
import space.user.game.dungeon.actors.buffs.Buff;
import space.user.game.dungeon.actors.buffs.Slow;
import space.user.game.dungeon.effects.MagicMissile;
import space.user.game.dungeon.utils.GLog;
import space.user.game.utils.Callback;

public class WandOfSlowness extends Wand {

	{
		name = "Wand of Slowness";
	}

	@Override
	protected void onZap( int cell ) {
		Char ch = Actor.findChar( cell );
		if (ch != null) {
			
			Buff.affect( ch, Slow.class, Slow.duration( ch ) / 3 + power() );

		} else {
			
			GLog.i( "nothing happened" );
			
		}
	}
	
	protected void fx( int cell, Callback callback ) {
		MagicMissile.slowness( curUser.sprite.parent, curUser.pos, cell, callback );
		Sample.INSTANCE.play( Assets.SND_ZAP );
	}
	
	@Override
	public String desc() {
		return 
			"This wand will cause a creature to move and attack " +
			"at half its ordinary speed until the effect ends";
	}
}

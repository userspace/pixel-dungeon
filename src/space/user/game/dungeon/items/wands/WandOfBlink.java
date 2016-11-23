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
import space.user.game.noosa.tweeners.AlphaTweener;
import space.user.game.dungeon.Assets;
import space.user.game.dungeon.Dungeon;
import space.user.game.dungeon.actors.Actor;
import space.user.game.dungeon.actors.Char;
import space.user.game.dungeon.effects.MagicMissile;
import space.user.game.dungeon.effects.Speck;
import space.user.game.dungeon.mechanics.Ballistica;
import space.user.game.utils.Callback;

public class WandOfBlink extends Wand {

	{
		name = "Wand of Blink";
	}
	
	@Override
	protected void onZap( int cell ) {

		int level = power();
		
		if (Ballistica.distance > level + 4) {
			cell = Ballistica.trace[level + 3];
		} else if (Actor.findChar( cell ) != null && Ballistica.distance > 1) {
			cell = Ballistica.trace[Ballistica.distance - 2];
		}
		
		curUser.sprite.visible = true;
		appear( Dungeon.hero, cell );
		Dungeon.observe();
	}
	
	@Override
	protected void fx( int cell, Callback callback ) {
		MagicMissile.whiteLight( curUser.sprite.parent, curUser.pos, cell, callback );
		Sample.INSTANCE.play( Assets.SND_ZAP );
		curUser.sprite.visible = false;
	}
	
	public static void appear( Char ch, int pos ) {
		
		ch.sprite.interruptMotion();
		
		ch.move( pos );
		ch.sprite.place( pos );
		
		if (ch.invisible == 0) {
			ch.sprite.alpha( 0 );
			ch.sprite.parent.add( new AlphaTweener( ch.sprite, 1, 0.4f ) );
		}
		
		ch.sprite.emitter().start( Speck.factory( Speck.LIGHT ), 0.2f, 3 );
		Sample.INSTANCE.play( Assets.SND_TELEPORT );
	}
	
	@Override
	public String desc() {
		return
			"This wand will allow you to teleport in the chosen direction. " +
			"Creatures and inanimate obstructions will block the teleportation.";
	}
}

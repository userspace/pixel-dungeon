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
import space.user.game.dungeon.Dungeon;
import space.user.game.dungeon.ResultDescriptions;
import space.user.game.dungeon.actors.Actor;
import space.user.game.dungeon.actors.Char;
import space.user.game.dungeon.actors.blobs.Blob;
import space.user.game.dungeon.actors.blobs.Fire;
import space.user.game.dungeon.actors.buffs.Buff;
import space.user.game.dungeon.actors.buffs.Burning;
import space.user.game.dungeon.effects.MagicMissile;
import space.user.game.dungeon.effects.particles.FlameParticle;
import space.user.game.dungeon.levels.Level;
import space.user.game.dungeon.mechanics.Ballistica;
import space.user.game.dungeon.scenes.GameScene;
import space.user.game.dungeon.utils.GLog;
import space.user.game.dungeon.utils.Utils;
import space.user.game.utils.Callback;
import space.user.game.utils.Random;

public class WandOfFirebolt extends Wand {

	{
		name = "Wand of Firebolt";
	}
	
	@Override
	protected void onZap( int cell ) {

		int level = power();
		
		for (int i=1; i < Ballistica.distance - 1; i++) {
			int c = Ballistica.trace[i];
			if (Level.flamable[c]) {
				GameScene.add( Blob.seed( c, 1, Fire.class ) );
			}
		}
		
		GameScene.add( Blob.seed( cell, 1, Fire.class ) );
					
		Char ch = Actor.findChar( cell );
		if (ch != null) {	
			
			ch.damage( Random.Int( 1, 8 + level * level ), this );
			Buff.affect( ch, Burning.class ).reignite( ch );

			ch.sprite.emitter().burst( FlameParticle.FACTORY, 5 );
			
			if (ch == curUser && !ch.isAlive()) {
				Dungeon.fail( Utils.format( ResultDescriptions.WAND, name, Dungeon.depth ) );
				GLog.n( "You killed yourself with your own Wand of Firebolt..." );
			}
		}
	}
	
	protected void fx( int cell, Callback callback ) {
		MagicMissile.fire( curUser.sprite.parent, curUser.pos, cell, callback );
		Sample.INSTANCE.play( Assets.SND_ZAP );
	}
	
	@Override
	public String desc() {
		return
			"This wand unleashes bursts of magical fire. It will ignite " +
			"flammable terrain, and will damage and burn a creature it hits.";
	}
}
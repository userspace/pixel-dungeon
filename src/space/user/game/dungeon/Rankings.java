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
package space.user.game.dungeon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import space.user.game.noosa.Game;
import space.user.game.dungeon.utils.Utils;
import space.user.game.utils.Bundlable;
import space.user.game.utils.Bundle;
import space.user.game.utils.SystemTime;

public enum Rankings {
	
	INSTANCE;
	
	public static final int TABLE_SIZE	= 6;
	
	public static final String RANKINGS_FILE = "RANKINGS";
	public static final String DETAILS_FILE = "RANKING_DETAIL_%d";
	
	public ArrayList<RankingsRecord> records;
	public int lastRecord;
	public int totalNumber;
	public int wonNumber;
	
	public void submit( boolean win ) {
		
		load();
		
		RankingsRecord rec = new RankingsRecord();
		
		rec.info	= Dungeon.resultDescription;
		rec.win		= win;
		rec.heroClass	= Dungeon.hero.heroClass;
		rec.armorTier	= Dungeon.hero.tier();
		rec.score	= score( win );
		
		String gameFile = Utils.format( DETAILS_FILE, SystemTime.now );
		try {
			Dungeon.saveGame( gameFile );
			rec.gameFile = gameFile;
		} catch (IOException e) {
			rec.gameFile = "";
		}
		
		records.add( rec );
		
		Collections.sort( records, scoreComparator );
		
		lastRecord = records.indexOf( rec );
		int size = records.size();
		if (size > TABLE_SIZE) {
			
			RankingsRecord removedGame;
			if (lastRecord == size - 1) {
				removedGame = records.remove( size - 2 );
				lastRecord--;
			} else {
				removedGame = records.remove( size - 1 );
			}
			
			if (removedGame.gameFile.length() > 0) {
				Game.instance.deleteFile( removedGame.gameFile );
			}
		}
		
		totalNumber++;
		if (win) {
			wonNumber++;
		}
		
		Badges.validateGamesPlayed();
		
		save();
	}
	
	private int score( boolean win ) {
		return (Statistics.goldCollected + Dungeon.hero.lvl * Statistics.deepestFloor * 100) * (win ? 2 : 1);
	}
	
	private static final String RECORDS	= "records";
	private static final String LATEST	= "latest";
	private static final String TOTAL	= "total";
	private static final String WON		= "won";
	
	public void save() {
		Bundle bundle = Bundle.read(RANKINGS_FILE);
		bundle.put( RECORDS, records );
		bundle.put( LATEST, lastRecord );
		bundle.put( TOTAL, totalNumber );
		bundle.put( WON, wonNumber );
		
		try {
			bundle.write();
		} catch (Exception e) {
		}
	}
	
	public void load() {
		
		if (records != null) {
			return;
		}
		
		records = new ArrayList<RankingsRecord>();
		
		try {
			Bundle bundle = Bundle.read( RANKINGS_FILE );

			for (Bundlable record : bundle.getCollection( RECORDS )) {
				records.add( (RankingsRecord)record );
			}			
			lastRecord = bundle.getInt( LATEST );
			
			totalNumber = bundle.getInt( TOTAL );
			if (totalNumber == 0) {
				totalNumber = records.size();
			}
			
			wonNumber = bundle.getInt( WON );
			if (wonNumber == 0) {
				for (RankingsRecord rec : records) {
					if (rec.win) {
						wonNumber++;
					}
				}
			}
			
		} catch (Exception e) {
		}
	}
	
	private static final Comparator<RankingsRecord> scoreComparator = new Comparator<RankingsRecord>() {
		@Override
		public int compare(RankingsRecord lhs, RankingsRecord rhs ) {
			return (int)Math.signum( rhs.score - lhs.score );
		}
	};
}

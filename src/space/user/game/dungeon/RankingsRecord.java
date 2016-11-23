package space.user.game.dungeon;

import space.user.game.dungeon.actors.hero.HeroClass;
import space.user.game.utils.Bundlable;
import space.user.game.utils.Bundle;

/**
 * Created by sebasjm on 31/10/16.
 */

public class RankingsRecord implements Bundlable {

    public static final String REASON	= "reason";
    public static final String WIN		= "win";
    public static final String SCORE	= "score";
    public static final String TIER	= "tier";
    public static final String GAME	= "gameFile";

    public String info;
    public boolean win;

    public HeroClass heroClass;
    public int armorTier;

    public int score;

    public String gameFile;

    @Override
    public void restoreFromBundle( Bundle bundle ) {

        info	= bundle.getString( REASON );
        win		= bundle.getBoolean( WIN );
        score	= bundle.getInt( SCORE );

        heroClass	= HeroClass.restoreInBundle( bundle );
        armorTier	= bundle.getInt( TIER );

        gameFile	= bundle.getString( GAME );
    }

    @Override
    public void storeInBundle( Bundle bundle ) {

        bundle.put( REASON, info );
        bundle.put( WIN, win );
        bundle.put( SCORE, score );

        heroClass.storeInBundle( bundle );
        bundle.put( TIER, armorTier );

        bundle.put( GAME, gameFile );
    }
}
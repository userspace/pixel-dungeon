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

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.auth0.android.Auth0;
import com.auth0.android.lock.AuthenticationCallback;
import com.auth0.android.lock.Lock;
import com.auth0.android.lock.LockCallback;
import com.auth0.android.lock.utils.LockException;
import com.auth0.android.result.Credentials;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.parse.Parse;
import com.parse.http.ParseHttpRequest;
import com.parse.http.ParseHttpResponse;
import com.parse.http.ParseNetworkInterceptor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.microedition.khronos.opengles.GL10;

import io.keen.client.android.AndroidKeenClientBuilder;
import io.keen.client.java.KeenClient;
import io.keen.client.java.KeenProject;
import space.user.game.noosa.Game;
import space.user.game.noosa.audio.Music;
import space.user.game.noosa.audio.Sample;
import space.user.game.dungeon.scenes.GameScene;
import space.user.game.dungeon.scenes.PixelScene;
import space.user.game.dungeon.scenes.TitleScene;


public class PixelDungeon extends Game {

	public PixelDungeon() {
		super( TitleScene.class );

		space.user.game.utils.Bundle.addAlias(
			space.user.game.dungeon.items.scrolls.ScrollOfUpgrade.class,
			"com.watabou.pixeldungeon.items.scrolls.ScrollOfEnhancement" );
		space.user.game.utils.Bundle.addAlias(
			space.user.game.dungeon.actors.blobs.WaterOfHealth.class,
			"com.watabou.pixeldungeon.actors.blobs.Light" );
		space.user.game.utils.Bundle.addAlias(
			space.user.game.dungeon.items.rings.RingOfMending.class,
			"com.watabou.pixeldungeon.items.rings.RingOfRejuvenation" );
		space.user.game.utils.Bundle.addAlias(
			space.user.game.dungeon.items.wands.WandOfReach.class,
			"com.watabou.pixeldungeon.items.wands.WandOfTelekenesis" );
		space.user.game.utils.Bundle.addAlias(
			space.user.game.dungeon.actors.blobs.Foliage.class,
			"com.watabou.pixeldungeon.actors.blobs.Blooming" );
		space.user.game.utils.Bundle.addAlias(
			space.user.game.dungeon.actors.buffs.Shadows.class,
			"com.watabou.pixeldungeon.actors.buffs.Rejuvenation" );
		space.user.game.utils.Bundle.addAlias(
			space.user.game.dungeon.items.scrolls.ScrollOfPsionicBlast.class,
			"com.watabou.pixeldungeon.items.scrolls.ScrollOfNuclearBlast" );
		space.user.game.utils.Bundle.addAlias(
			space.user.game.dungeon.actors.hero.Hero.class,
			"com.watabou.pixeldungeon.actors.Hero" );
		space.user.game.utils.Bundle.addAlias(
			space.user.game.dungeon.actors.mobs.npcs.Shopkeeper.class,
			"com.watabou.pixeldungeon.actors.mobs.Shopkeeper" );
		// 1.6.1
		space.user.game.utils.Bundle.addAlias(
			space.user.game.dungeon.items.quest.DriedRose.class,
			"com.watabou.pixeldungeon.items.DriedRose" );
		space.user.game.utils.Bundle.addAlias(
			space.user.game.dungeon.actors.mobs.npcs.MirrorImage.class,
			"com.watabou.pixeldungeon.items.scrolls.ScrollOfMirrorImage$MirrorImage" );
		// 1.6.4
		space.user.game.utils.Bundle.addAlias(
			space.user.game.dungeon.items.rings.RingOfElements.class,
			"com.watabou.pixeldungeon.items.rings.RingOfCleansing" );
		space.user.game.utils.Bundle.addAlias(
			space.user.game.dungeon.items.rings.RingOfElements.class,
			"com.watabou.pixeldungeon.items.rings.RingOfResistance" );
		space.user.game.utils.Bundle.addAlias(
			space.user.game.dungeon.items.weapon.missiles.Boomerang.class,
			"com.watabou.pixeldungeon.items.weapon.missiles.RangersBoomerang" );
		space.user.game.utils.Bundle.addAlias(
			space.user.game.dungeon.items.rings.RingOfPower.class,
			"com.watabou.pixeldungeon.items.rings.RingOfEnergy" );
		// 1.7.2
		space.user.game.utils.Bundle.addAlias(
			space.user.game.dungeon.plants.Dreamweed.class,
			"com.watabou.pixeldungeon.plants.Blindweed" );
		space.user.game.utils.Bundle.addAlias(
			space.user.game.dungeon.plants.Dreamweed.Seed.class,
			"com.watabou.pixeldungeon.plants.Blindweed$Seed" );
		// 1.7.4
		space.user.game.utils.Bundle.addAlias(
			space.user.game.dungeon.items.weapon.enchantments.Shock.class,
			"com.watabou.pixeldungeon.items.weapon.enchantments.Piercing" );
		space.user.game.utils.Bundle.addAlias(
			space.user.game.dungeon.items.weapon.enchantments.Shock.class,
			"com.watabou.pixeldungeon.items.weapon.enchantments.Swing" );
		space.user.game.utils.Bundle.addAlias(
			space.user.game.dungeon.items.scrolls.ScrollOfEnchantment.class,
			"com.watabou.pixeldungeon.items.scrolls.ScrollOfWeaponUpgrade" );
		// 1.7.5
		space.user.game.utils.Bundle.addAlias(
			space.user.game.dungeon.items.scrolls.ScrollOfEnchantment.class,
			"com.watabou.pixeldungeon.items.Stylus" );
		// 1.8.0
		space.user.game.utils.Bundle.addAlias(
			space.user.game.dungeon.actors.mobs.FetidRat.class,
			"com.watabou.pixeldungeon.actors.mobs.npcs.Ghost$FetidRat" );
		space.user.game.utils.Bundle.addAlias(
			space.user.game.dungeon.plants.Rotberry.class,
			"com.watabou.pixeldungeon.actors.mobs.npcs.Wandmaker$Rotberry" );
		space.user.game.utils.Bundle.addAlias(
			space.user.game.dungeon.plants.Rotberry.Seed.class,
			"com.watabou.pixeldungeon.actors.mobs.npcs.Wandmaker$Rotberry$Seed" );
		// 1.9.0
		space.user.game.utils.Bundle.addAlias(
			space.user.game.dungeon.items.wands.WandOfReach.class,
			"com.watabou.pixeldungeon.items.wands.WandOfTelekinesis" );
	}

    public static void initialize_parse() {
        Parse.initialize(new Parse.Configuration.Builder(instance.getApplicationContext())
            .applicationId("userspace")
            .addNetworkInterceptor(new ParseNetworkInterceptor() {
                @Override
                public ParseHttpResponse intercept(Chain chain) throws IOException {
                    ParseHttpRequest newRequest = new ParseHttpRequest.Builder(chain.getRequest())
                        .addHeader("Authorization", "Bearer " + PixelDungeon.instance.token)
                        .build();

					Log.e(TAG,"");
					Log.e(TAG, "Request To Parse Server: "  + newRequest.getMethod().toString() + " " + newRequest.getUrl());
                    for(Map.Entry<String,String> entry : newRequest.getAllHeaders().entrySet()) {
                        Log.e(TAG, entry.getKey() + ": " + entry.getValue());
                    }

                    if (newRequest.getBody() != null){
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        newRequest.getBody().writeTo(out);
                        Log.e(TAG, out.toString("UTF-8"));
                    }
					Log.e(TAG,"-------------------------");

                    ParseHttpResponse response = chain.proceed(newRequest);
					Log.e(TAG,"");
                    Log.e(TAG, "Response From ParseServer:" + response.getStatusCode() );
                    for(Map.Entry<String,String> entry :  response.getAllHeaders().entrySet()) {
                        Log.e(TAG, entry.getKey() + ": " + entry.getValue());
                    }

                    String content = convertStreamToString(response.getContent());
                    Log.e(TAG, content);
                    InputStream stream = new ByteArrayInputStream(content.getBytes("UTF-8"));
					Log.e(TAG,"-------------------------");

                    return new ParseHttpResponse.Builder(response)
                        .setContent(stream)
                        .build();
                }
            })
            .server("http://user.space/main")
            .build()
        );
    }

    public static void initialize_keen() {
        Map<String, Object> global = new HashMap<>();
        Map<String, Object> event = new HashMap<>();

        String profile = instance.token.split("\\.")[1];
        byte[] decode = Base64.decode(profile, Base64.DEFAULT);
        JsonElement parse = new JsonParser().parse(new String(decode));

        JsonObject jsonObject = parse.getAsJsonObject();
        for (Map.Entry<String, JsonElement> e : jsonObject.entrySet() ) {
            event.put(e.getKey(), e.getValue().getAsString());
        }
        String sub = jsonObject.get("sub").getAsString();
        if (sub != null) {
            String[] split = sub.split("\\|");
            if (split.length > 0) global.put("validator", split[0]);
            if (split.length > 1) global.put("userid", split[1]);
        }
        global.put("sub", sub);
        KeenClient.client().setGlobalProperties(global);

        KeenClient.client().addEventAsync("loggedIn", event);

    }

	private LockCallback callback = new AuthenticationCallback() {
        @Override
        public void onAuthentication(final Credentials credentials) {
			token = credentials.getIdToken();

            initialize_keen();

            SharedPreferences.Editor editor = instance.getPreferences(0).edit();
            editor.putString("last_token", token);
            editor.commit();
            Log.d(TAG,"onAuth " + credentials.getAccessToken() + " d " + credentials.getIdToken() );
		}



        @Override
        public void onCanceled() {
			PixelDungeon.instance.finish();
			Log.d(TAG,"Cancel");
        }

        @Override
        public void onError(LockException error) {
			PixelDungeon.instance.finish();
			Log.d(TAG,"Error");
        }
	};

    private static final String TAG = "ASDASDASDASD PD";
	public String token;
    private static Lock lock;

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public static void printStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        Log.e(TAG, sw.toString());
    }

    public static PixelDungeon instance;

    @Override
	protected void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
        instance = this;

		updateImmersiveMode();

		DisplayMetrics metrics = new DisplayMetrics();
		instance.getWindowManager().getDefaultDisplay().getMetrics( metrics );
		boolean landscape = metrics.widthPixels > metrics.heightPixels;

		if (Preferences.INSTANCE.getBoolean( Preferences.KEY_LANDSCAPE, false ) != landscape) {
			landscape( !landscape );
		}

		Music.INSTANCE.enable( music() );
		Sample.INSTANCE.enable( soundFx() );

		Sample.INSTANCE.load(
			Assets.SND_CLICK,
			Assets.SND_BADGE,
			Assets.SND_GOLD,

			Assets.SND_DESCEND,
			Assets.SND_STEP,
			Assets.SND_WATER,
			Assets.SND_OPEN,
			Assets.SND_UNLOCK,
			Assets.SND_ITEM,
			Assets.SND_DEWDROP,
			Assets.SND_HIT,
			Assets.SND_MISS,
			Assets.SND_EAT,
			Assets.SND_READ,
			Assets.SND_LULLABY,
			Assets.SND_DRINK,
			Assets.SND_SHATTER,
			Assets.SND_ZAP,
			Assets.SND_LIGHTNING,
			Assets.SND_LEVELUP,
			Assets.SND_DEATH,
			Assets.SND_CHALLENGE,
			Assets.SND_CURSED,
			Assets.SND_EVOKE,
			Assets.SND_TRAP,
			Assets.SND_TOMB,
			Assets.SND_ALERT,
			Assets.SND_MELD,
			Assets.SND_BOSS,
			Assets.SND_BLAST,
			Assets.SND_PLANT,
			Assets.SND_RAY,
			Assets.SND_BEACON,
			Assets.SND_TELEPORT,
			Assets.SND_CHARMS,
			Assets.SND_MASTERY,
			Assets.SND_PUFF,
			Assets.SND_ROCKS,
			Assets.SND_BURNING,
			Assets.SND_FALLING,
			Assets.SND_GHOST,
			Assets.SND_SECRET,
			Assets.SND_BONES,
			Assets.SND_BEE,
			Assets.SND_DEGRADE,
			Assets.SND_MIMIC );

        initialize_auth0();
        initialize_parse();

        KeenClient client = new AndroidKeenClientBuilder(this).build();
        KeenClient.initialize(client);
        client.setDefaultProject(new KeenProject(getString(R.string.KEEN_PROJECT_ID) , getString(R.string.KEEN_WRITE_KEY), ""));

        String last_token = getPreferences(0).getString("last_token",null);
        if (last_token == null) {
            login();
        } else {
            token = last_token;
            initialize_keen();
        }


    }

    public static void login() {
        PixelDungeon.instance.startActivity(lock.newIntent(PixelDungeon.instance));
    }

    public static void initialize_auth0() {
        Auth0 auth0 = new Auth0(
            PixelDungeon.instance.getString(R.string.auth0_client_id),
            PixelDungeon.instance.getString(R.string.auth0_domain)
        );

        lock = Lock.newBuilder(auth0, PixelDungeon.instance.callback).build(PixelDungeon.instance);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        lock.onDestroy(this);
        lock = null;
    }

    @Override
	public void onWindowFocusChanged( boolean hasFocus ) {

		super.onWindowFocusChanged( hasFocus );

		if (hasFocus) {
			updateImmersiveMode();
		}
	}

	public static void switchNoFade( Class<? extends PixelScene> c ) {
		PixelScene.noFade = true;
		switchScene( c );
	}

	/*
	 * ---> Prefernces
	 */

	public static void landscape( boolean value ) {
		Game.instance.setRequestedOrientation( value ?
			ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE :
			ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );
		Preferences.INSTANCE.put( Preferences.KEY_LANDSCAPE, value );
	}

	public static boolean landscape() {
		return width > height;
	}

	// *** IMMERSIVE MODE ****

	private static boolean immersiveModeChanged = false;

	@SuppressLint("NewApi")
	public static void immerse( boolean value ) {
		Preferences.INSTANCE.put( Preferences.KEY_IMMERSIVE, value );

		instance.runOnUiThread( new Runnable() {
			@Override
			public void run() {
				updateImmersiveMode();
				immersiveModeChanged = true;
			}
		} );
	}

	@Override
	public void onSurfaceChanged( GL10 gl, int width, int height ) {
		super.onSurfaceChanged( gl, width, height );

		if (immersiveModeChanged) {
			requestedReset = true;
			immersiveModeChanged = false;
		}
	}

	@SuppressLint("NewApi")
	public static void updateImmersiveMode() {
		if (android.os.Build.VERSION.SDK_INT >= 19) {
			try {
				// Sometime NullPointerException happens here
				instance.getWindow().getDecorView().setSystemUiVisibility(
					immersed() ?
					View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
					View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
					View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
					View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
					View.SYSTEM_UI_FLAG_FULLSCREEN |
					View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
					:
					0 );
			} catch (Exception e) {
				reportException( e );
			}
		}
	}

	public static boolean immersed() {
		return Preferences.INSTANCE.getBoolean( Preferences.KEY_IMMERSIVE, false );
	}

	// *****************************

	public static void scaleUp( boolean value ) {
		Preferences.INSTANCE.put( Preferences.KEY_SCALE_UP, value );
		switchScene( TitleScene.class );
	}

	public static boolean scaleUp() {
		return Preferences.INSTANCE.getBoolean( Preferences.KEY_SCALE_UP, true );
	}

	public static void zoom( int value ) {
		Preferences.INSTANCE.put( Preferences.KEY_ZOOM, value );
	}

	public static int zoom() {
		return Preferences.INSTANCE.getInt( Preferences.KEY_ZOOM, 0 );
	}

	public static void music( boolean value ) {
		Music.INSTANCE.enable( value );
		Preferences.INSTANCE.put( Preferences.KEY_MUSIC, value );
	}

	public static boolean music() {
		return Preferences.INSTANCE.getBoolean( Preferences.KEY_MUSIC, true );
	}

	public static void soundFx( boolean value ) {
		Sample.INSTANCE.enable( value );
		Preferences.INSTANCE.put( Preferences.KEY_SOUND_FX, value );
	}

	public static boolean soundFx() {
		return Preferences.INSTANCE.getBoolean( Preferences.KEY_SOUND_FX, true );
	}

	public static void brightness( boolean value ) {
		Preferences.INSTANCE.put( Preferences.KEY_BRIGHTNESS, value );
		if (scene() instanceof GameScene) {
			((GameScene)scene()).brightness( value );
		}
	}

	public static boolean brightness() {
		return Preferences.INSTANCE.getBoolean( Preferences.KEY_BRIGHTNESS, false );
	}

	public static void donated( String value ) {
		Preferences.INSTANCE.put( Preferences.KEY_DONATED, value );
	}

	public static String donated() {
		return Preferences.INSTANCE.getString( Preferences.KEY_DONATED, "" );
	}

	public static void lastClass( int value ) {
		Preferences.INSTANCE.put( Preferences.KEY_LAST_CLASS, value );
	}

	public static int lastClass() {
		return Preferences.INSTANCE.getInt( Preferences.KEY_LAST_CLASS, 0 );
	}

	public static void challenges( int value ) {
		Preferences.INSTANCE.put( Preferences.KEY_CHALLENGES, value );
	}

	public static int challenges() {
		return Preferences.INSTANCE.getInt( Preferences.KEY_CHALLENGES, 0 );
	}

	public static void intro( boolean value ) {
		Preferences.INSTANCE.put( Preferences.KEY_INTRO, value );
	}

	public static boolean intro() {
		return Preferences.INSTANCE.getBoolean( Preferences.KEY_INTRO, true );
	}
	
	/*
	 * <--- Preferences
	 */

	public static void reportException( Throwable tr ) {
		Log.e( "PD", Log.getStackTraceString( tr ) );
	}
}
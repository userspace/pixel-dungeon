/*
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

package space.user.game.utils;

import android.os.Handler;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import space.user.game.dungeon.PixelDungeon;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Bundle {

	private static final String CLASS_NAME = "field__className";
	
	private static HashMap<String,String> aliases = new HashMap<String, String>();
	
	private ParseObject data;
	private boolean dirty = false;

    public boolean hasChange() {
        return dirty;
    }

	public String toString() {
		return data.toString();
	}
	
	private Bundle(ParseObject data ) {
		this.data = data;
	}
	
	public boolean isNull() {
		return data == null;
	}
	
	public boolean contains( String key ) {
		return data.containsKey( key );
	}
	
	public boolean getBoolean( String key ) {
		return data.getBoolean( key );
	}
	
	public int getInt( String key ) {
		return data.getInt( key );
	}
	
	public float getFloat( String key ) {
		return (float)data.getDouble( key );
	}
	
	public String getString( String key ) {
		return data.getString( key );
	}
	
	public Bundle getBundle( String key ) {
        try {
            ParseObject parseObject = data.getParseObject(key);
            return new Bundle( parseObject == null ? null : parseObject.fetchIfNeeded() );
        } catch (ParseException e) {
            PixelDungeon.printStackTrace(e);
            return null;
        }
    }
	
	private Bundlable get() {
		if (data == null) return null;
		try {
			String clName = getString( CLASS_NAME );
			if (aliases.containsKey( clName )) {
				clName = aliases.get( clName );
			}
			
			Class<?> cl = Class.forName( clName );
			if (cl != null) {
				Bundlable object = (Bundlable)cl.newInstance();
				object.restoreFromBundle( this );
				return object;
			} else {
				return null;
			}
		} catch (Exception e) {
            PixelDungeon.printStackTrace(e);
			return null;
		}	
	}
	
	public Bundlable get( String key ) {
		return getBundle( key ).get();	
	}
	
	public <E extends Enum<E>> E getEnum( String key, Class<E> enumClass ) {
//		try {
			return Enum.valueOf( enumClass, data.getString( key ) );
//		} catch (JSONException e) {
//			return enumClass.getEnumConstants()[0];
//		}
	}
	
	public int[] getIntArray( String key ) {
		try {
			JSONArray array = data.getJSONArray( key );
			int length = array.length();
			int[] result = new int[length];
			for (int i=0; i < length; i++) {
				result[i] = array.getInt( i );
			}
			return result;
		} catch (JSONException e) {
            PixelDungeon.printStackTrace(e);
			return null;
		}
	}
	
	public boolean[] getBooleanArray( String key ) {
		try {
			JSONArray array = data.getJSONArray( key );
			int length = array.length();
			boolean[] result = new boolean[length];
			for (int i=0; i < length; i++) {
				result[i] = array.getBoolean( i );
			}
			return result;
		} catch (JSONException e) {
            PixelDungeon.printStackTrace(e);
			return null;
		}
	}
	
	public String[] getStringArray( String key ) {
		try {
			JSONArray array = data.getJSONArray( key );
			int length = array.length();
			String[] result = new String[length];
			for (int i=0; i < length; i++) {
				result[i] = array.getString( i );
			}
			return result;
		} catch (JSONException e) {
            PixelDungeon.printStackTrace(e);
			return null;
		}
	}
	
	public Collection<Bundlable> getCollection( String key ) {
		
		ArrayList<Bundlable> list = new ArrayList<Bundlable>();
		
		try {
			List<ParseObject> array = data.getList( key );
			if (array != null) for (int i=0; i < array.size(); i++) {
				list.add( new Bundle( array.get( i ).fetchIfNeeded() ).get() );
			}
		} catch (Exception e) {
            PixelDungeon.printStackTrace(e);

		}
		
		return list;
	}
	
	public void put( String key, boolean valuep ) {
        Boolean value = valuep;
        Object old = data.get(key);
        if (old == null && value == null) return;
        if (value == null || !value.equals(old)) {
            data.put( key, value );
            dirty = true;
        }
	}
	
	public void put( String key, int valuep ) {
        Integer value = valuep;
        Object old = data.get(key);
        if (old == null && value == null) return;
        if (value == null || !value.equals(old)) {
            data.put( key, value );
            dirty = true;
        }
	}
	
	public void put( String key, float valuep ) {
        Float value = valuep;
        Object old = data.get(key);
        if (old == null && value == null) return;
        if (value == null || !value.equals(old)) {
            data.put( key, value );
            dirty = true;
        }
	}
	
	public void put( String key, String value ) {
        Object old = data.get(key);
        if (old == null && value == null) return;
        if (value == null || !value.equals(old)) {
            data.put( key, value );
            dirty = true;
        }
	}
	
	public void put( String key, Bundle bundle ) {
        Object old = data.get(key);
        ParseObject data = bundle == null ? null : bundle.data;
        if (old == null && data == null) return;
        if (data == null || !data.equals(old)) {
            this.data.put( key, data);
            dirty = true;
        }
	}
	
	public void put( String key, Bundlable object ) {
        if (data.get(key) != null && object == null) {
            data.put(key, object);
            dirty = true;
        } else
		if (object != null) {
            Bundle bundle = Bundle.read(object.getClass().getName().replace(".","_").replace("$","__").replace("space_user_game_dungeon_",""));
            bundle.put( CLASS_NAME, object.getClass().getName() );
            object.storeInBundle( bundle );
            if (bundle.hasChange()) {
                data.put( key, bundle.data );
                dirty = true;
            }
		}
	}
	
	public void put( String key, Enum<?> value ) {
        Object old = data.get(key);
        if (old == null && value == null) return;
        if (value == null || !value.name().equals(old)) {
            data.put( key, value.name() );
            dirty = true;
        }
	}
	
	public void put( String key, int[] array ) {
		try {
			JSONArray value = array == null ? null : new JSONArray();
            JSONArray old = data.getJSONArray(key);
            if (array == null && old == null) return;
            boolean somethingChanged = old == null || array == null || old.length() != array.length;
            if (array != null) for (int i=0; i < array.length; i++) {
				value.put( i, array[i] );
                somethingChanged = somethingChanged || array[i] != old.getInt(i);
			}
            if (somethingChanged) {
                data.put( key, value );
                dirty = true;
            }
		} catch (JSONException e) {
            PixelDungeon.printStackTrace(e);

		}
	}
	
	public void put( String key, boolean[] array ) {
		try {
			JSONArray value = array == null ? null : new JSONArray();
            JSONArray old = data.getJSONArray(key);
            if (array == null && old == null) return;
            boolean somethingChanged = old == null || array == null || old.length() != array.length;
            if (array != null) for (int i=0; i < array.length; i++) {
				value.put( i, array[i] );
                somethingChanged = somethingChanged || array[i] != old.getBoolean(i);
			}
            if (somethingChanged) {
                data.put( key, value );
                dirty = true;
            }
		} catch (JSONException e) {
            PixelDungeon.printStackTrace(e);
		}
	}
	
	public void put( String key, String[] array ) {
		try {
			JSONArray value = array == null ? null : new JSONArray();
            JSONArray old = data.getJSONArray(key);
            if (array == null && old == null) return;
            boolean somethingChanged = old == null || array == null || old.length() != array.length;
			if (array != null) for (int i=0; i < array.length; i++) {
				value.put( i, array[i] );
                somethingChanged = somethingChanged || !array[i].equals(old.get(i));
			}
            if (somethingChanged) {
                data.put( key, value );
                dirty = true;
            }
		} catch (JSONException e) {
            PixelDungeon.printStackTrace(e);

		}
	}
	
	public void put( String key, Collection<? extends Bundlable> collection ) {
		JSONArray array = collection == null ? null : new JSONArray();
        JSONArray old = data.getJSONArray(key);
        if (array == null && old == null) return;
        boolean somethingChanged = old == null || array == null || old.length() != collection.size();
		if (array != null) for (Bundlable object : collection) {
            Bundle bundle = Bundle.read(object.getClass().getName().replace(".","_").replace("$","__").replace("space_user_game_dungeon_",""));
			bundle.put( CLASS_NAME, object.getClass().getName() );
			object.storeInBundle( bundle );
            somethingChanged = somethingChanged || bundle.hasChange();
			array.put( bundle.data );
		}
        if (somethingChanged) {
			data.put( key, array );
            dirty = true;
        }
	}

    private static final ConcurrentHashMap<String,ParseObject> cache = new ConcurrentHashMap<String,ParseObject>();

	public static Bundle read( String className ) {
        if (cache.containsKey(className)) return new Bundle(cache.get(className));
        ParseObject data;
		try {
            data = new ParseQuery(className).orderByDescending("UpdatedAt").getFirst().fetchIfNeeded();
		} catch (Exception e) {
            PixelDungeon.printStackTrace(e);
            data = new ParseObject(className);
		}
        cache.put(className, data);
        return new Bundle( data );
	}


	public boolean write( ) {
//        try {
        data.saveEventually(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				dirty = false;
			}
		});
//            data.saveEventually();
//        ParseObject.saveAllInBackground()
        return true;
//        } catch (ParseException e) {
//            PixelDungeon.printStackTrace(e);
//            return false;
//        }
    }
	
	public static void addAlias( Class<?> cl, String alias ) {
		aliases.put( alias, cl.getName() );
	}
}

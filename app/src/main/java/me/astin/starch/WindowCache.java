package me.astin.starch;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import me.astin.starch.StarchWindow;
import me.astin.starch.ui.Window;


public class WindowCache {
    public Map<Integer, Window> sWindows;

    public WindowCache() {
        sWindows = new HashMap<Integer, Window>();
    }

    public void putCache(int id, Window window) {
        sWindows.put(id, window);
    }
    
    public Window getCache(int id) {
        return sWindows.get(id);
    }
    
    public boolean isCached(int id) {
        return getCache(id) != null;
    }
    
    public void removeCache(int id) {
        sWindows.remove(id);
    }
	
	public Set<Integer> getCachedIds() {
		return sWindows.keySet();
	}
	
	public int size() {
		return sWindows.size();
	}
}

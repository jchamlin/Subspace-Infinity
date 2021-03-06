/*
 * Copyright (c) 2018, Asser Fahrenholz
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package infinity.systems;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections4.map.ListOrderedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.asset.AssetManager;
import com.jme3.system.JmeSystem;

import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import com.simsilica.es.EntitySet;
import com.simsilica.mathd.Vec3d;
import com.simsilica.sim.AbstractGameSystem;
import com.simsilica.sim.SimTime;

import infinity.es.ArenaId;
import infinity.map.LevelLoader;
import infinity.sim.ArenaManager;
import infinity.sim.CoreGameConstants;

/**
 * State to keep track of different arenas. Arenas are composed of a tileset and
 * a ruleset and a location (since areanas are 1024x1024. This state keeps track
 * of where the next arena can be loaded and associates rulesets to each loaded
 * arena
 *
 * @author Asser
 */
public class ArenaSystem extends AbstractGameSystem implements ArenaManager {

    private EntityData ed;
    private EntitySet arenaEntities;
    // private EntitySet staticBodyPositions;
    private final java.util.Map<Vec3d, EntityId> index = new ConcurrentHashMap<>();
    // private AssetManager am;
    static Logger log = LoggerFactory.getLogger(ArenaSystem.class);
    // private SimTime time;

    private final HashMap<String, EntityId> currentOpenArenas = new HashMap<>();

    // private final boolean createdDefaultArena = false;

    private final ListOrderedMap<String, String> arenas = new ListOrderedMap<>();
    // private MapSystem mapSystem;
    // private int xCoord, zCoord;

    @Override
    protected void initialize() {

        ed = getSystem(EntityData.class);

        arenaEntities = ed.getEntities(ArenaId.class); // This filters all entities that are in arenas

        final AssetManager assetManager = JmeSystem.newAssetManager(
                Thread.currentThread().getContextClassLoader().getResource("com/jme3/asset/Desktop.cfg"));

        assetManager.registerLoader(LevelLoader.class, "lvl");
        assetManager.registerLoader(LevelLoader.class, "lvz");

        // staticBodyPositions = ed.getEntities(BodyPosition.class);

        // mapSystem = getSystem(MapSystem.class, true);
    }

    public EntityId getEntityId(final Vec3d coord) {
        return index.get(coord);
    }

    @Override
    protected void terminate() {
        // Release the entity set we grabbed previously
        arenaEntities.release();
        arenaEntities = null;
    }

    @Override
    public void update(final SimTime tpf) {
        return;
    }

    @Override
    public void start() {
        return;
    }

    @Override
    public void stop() {
        return;
    }

    @Override
    public String[] getActiveArenas() {
        return (String[]) currentOpenArenas.keySet().toArray();
    }

    @SuppressWarnings("unused")
    private void closeArena(final String arenaId) {
        ed.removeEntity(currentOpenArenas.get(arenaId));
        currentOpenArenas.remove(arenaId);
    }

    @Override
    public String getDefaultArenaId() {
        return CoreGameConstants.DEFAULTARENAID;
    }

    public void loadArena(final String name, final boolean forceLoad) {
        if (!arenas.containsKey(name)) {
            // find next coordinate pair to load map on

        }

        if (arenas.containsKey(name) && forceLoad) {
            // TODO implement me
        }
    }

    @SuppressWarnings("unused")
    private static class Vector2i {
        int x, z;

        Vector2i(final int x, final int z) {
            this.x = x;
            this.z = z;
        }

        @Override
        public int hashCode() {
            final int hash = 7;
            return hash;
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Vector2i other = (Vector2i) obj;
            if (x != other.x) {
                return false;
            }
            if (z != other.z) {
                return false;
            }
            return true;
        }

    }
}

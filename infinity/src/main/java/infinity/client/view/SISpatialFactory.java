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
package infinity.client.view;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.light.AmbientLight;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.control.LightControl;
import com.jme3.scene.shape.Quad;
import com.jme3.system.Timer;
import com.jme3.texture.Image;
import com.jme3.texture.Texture2D;
import com.jme3.texture.plugins.AWTLoader;
import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import com.simsilica.ext.mphys.Mass;
import com.simsilica.ext.mphys.ShapeInfo;
import com.simsilica.mblock.phys.CellArrayPart;
import com.simsilica.mblock.phys.Group;
import com.simsilica.mblock.phys.MBlockShape;
import com.simsilica.mblock.phys.Part;
import infinity.client.ConnectionState;
import infinity.es.TileType;
import infinity.es.TileTypes;
import infinity.es.ship.weapons.BombLevelEnum;
import infinity.sim.CoreViewConstants;
import infinity.es.ShapeNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Asser
 */
public class SISpatialFactory {

    static Logger log = LoggerFactory.getLogger(SISpatialFactory.class);
    private AssetManager assets;
    private EntityData ed;

    private EffectFactory ef;
    private Timer timer;
    private Node rootNode;

    SISpatialFactory(EntityData ed, SimpleApplication app) {
        this.ed = ed;
        this.assets = app.getAssetManager();
        this.timer = app.getTimer();
        this.rootNode = app.getRootNode();
    }

    Spatial createModel(EntityId id, MBlockShape shape, ShapeInfo shapeInfo, Mass mass) {
        //TODO: Use the shape ?
        String shapeName = shapeInfo.getShapeName(ed);

        if (shapeName == null || shapeName == "") {
            throw new NullPointerException("Model shapeInfo name cannot be null or empty");
        }

        Spatial s = this.createModel(id, shapeName);

        return s;
    }

    public Spatial createModel(EntityId eId, String shapeName) {

        switch (shapeName) {
            //case "thrust":
            //    return createParticleEmitter(eId, shapeName);
            case ShapeNames.BULLETL1:
                return createBullet(eId, 1);
            case ShapeNames.BOMBL1:
                //Create bomb
                return createBomb(eId, BombLevelEnum.BOMB_1);
            case ShapeNames.BOMBL2:
                //Create bomb
                return createBomb(eId, BombLevelEnum.BOMB_2);
            case ShapeNames.BOMBL3:
                //Create bomb
                return createBomb(eId, BombLevelEnum.BOMB_3);
            case ShapeNames.BOMBL4:
                //Create bomb
                return createBomb(eId, BombLevelEnum.BOMB_4);
            case ShapeNames.THOR:
                //Create bomb
                return createBomb(eId, BombLevelEnum.THOR);
            case ShapeNames.BURST:
                //Create bomb
                return createBurst(eId);
            case ShapeNames.EXPLOSION:
                //Create explosion
                return createExplosion(eId);
            case ShapeNames.PRIZE:
                //Create bounty
                return createBounty(eId);
            case ShapeNames.ARENA:
                return createArena(eId);
            /*
            case ViewTypes.MAPTILE:
                return createMapTile(eId);*/
            case ShapeNames.EXPLOSION2:
                return createExplosion2(eId);
            case ShapeNames.OVER5:
                return createOver5(eId);
            case ShapeNames.WORMHOLE:
                return createWormhole(eId);
            case ShapeNames.OVER1:
                return createOver1(eId);
            case ShapeNames.WARP:
                return createWarp(eId);
            case ShapeNames.REPEL:
                return createRepel(eId);
            case ShapeNames.OVER2:
                return createOver2(eId);
            case ShapeNames.SHIP_WARBIRD:
                return createShip(31);
            case ShapeNames.SHIP_JAVELIN:
                return createShip(27);
            case ShapeNames.SHIP_SPIDER:
                return createShip(23);
            case ShapeNames.SHIP_LEVI:
                return createShip(19);
            case ShapeNames.SHIP_TERRIER:
                return createShip(15);
            case ShapeNames.SHIP_WEASEL:
                return createShip(11);
            case ShapeNames.SHIP_LANCASTER:
                return createShip(7);
            case ShapeNames.SHIP_SHARK:
                return createShip(3);
            case ShapeNames.FLAG_OURS:
                return createFlag(0);
            case ShapeNames.FLAG_THEIRS:
                return createFlag(1);
            /*case "mob":
                return createMob();
            case "tower":
                return createTower();
            case "base":
                return createBase();*/
            default:
                throw new UnsupportedOperationException("Unknown shapeinfo name:" + shapeName);

        }
    }

    private Spatial createBase() {
        Quad quad = new Quad(CoreViewConstants.BASESIZE, CoreViewConstants.BASESIZE);
        //<-- Move into the material?
        float halfSize = CoreViewConstants.BASESIZE * 0.5f;
        quad.setBuffer(VertexBuffer.Type.Position, 3, this.getArray(halfSize));
        //-->
        quad.updateBound();
        Geometry geom = new Geometry("Base", quad);
        Material mat = assets.loadMaterial("Materials/BaseMaterialLight.j3m");
        //mat.setColor("Color", ColorRGBA.Yellow);

        geom.setMaterial(mat);
        geom.setQueueBucket(RenderQueue.Bucket.Transparent);
        return geom;
    }

    private Spatial createMob() {
        Quad quad = new Quad(CoreViewConstants.MOBSIZE, CoreViewConstants.MOBSIZE);
        //<-- Move into the material?
        float halfSize = CoreViewConstants.MOBSIZE * 0.5f;
        quad.setBuffer(VertexBuffer.Type.Position, 3, this.getArray(halfSize));
        //-->
        quad.updateBound();
        Geometry geom = new Geometry("Mob", quad);
        Material mat = assets.loadMaterial("Materials/MobMaterialLight.j3m");
        //mat.setColor("Color", ColorRGBA.Yellow);

        geom.setMaterial(mat);
        geom.setQueueBucket(RenderQueue.Bucket.Transparent);
        return geom;
    }

    private Spatial createTower() {
        Quad quad = new Quad(CoreViewConstants.TOWERSIZE, CoreViewConstants.TOWERSIZE);
        //<-- Move into the material?
        float halfSize = CoreViewConstants.TOWERSIZE * 0.5f;
        quad.setBuffer(VertexBuffer.Type.Position, 3, this.getArray(halfSize));
        //-->
        quad.updateBound();
        Geometry geom = new Geometry("Tower", quad);
        Material mat = assets.loadMaterial("Materials/TowerMaterialLight.j3m");
        //mat.setColor("Color", ColorRGBA.Yellow);

        geom.setMaterial(mat);
        geom.setQueueBucket(RenderQueue.Bucket.Transparent);
        return geom;
    }

    private Spatial createFlag(int flag) {
        Quad quad = new Quad(CoreViewConstants.FLAGSIZE, CoreViewConstants.FLAGSIZE);
        //<-- Move into the material?
        float halfSize = CoreViewConstants.FLAGSIZE * 0.5f;
        quad.setBuffer(VertexBuffer.Type.Position, 3, this.getArray(halfSize));
        //-->
        quad.updateBound();
        Geometry geom = new Geometry("Flag", quad);
        Material mat = assets.loadMaterial("Materials/FlagMaterialLight.j3m");

        geom.setMaterial(mat);
        //mat.setInt("numTilesOffsetY", flag);
        setFlagMaterialVariables(geom, flag);

        geom.setMaterial(mat);
        geom.setQueueBucket(RenderQueue.Bucket.Transparent);

        return geom;
    }

    public void setFlagMaterialVariables(Spatial s, int flag) {
        Geometry geom;
        if (s instanceof Geometry) {
            geom = (Geometry) s;
        } else {
            geom = (Geometry) ((Node) s).getChild("Flag");
        }

        Material mat = geom.getMaterial();
        mat.setInt("numTilesOffsetY", flag);
        geom.setMaterial(mat);
    }

    private Spatial createShip(int ship) {
        Quad quad = new Quad(CoreViewConstants.SHIPSIZE, CoreViewConstants.SHIPSIZE);
        //<-- Move into the material?
        float halfSize = CoreViewConstants.SHIPSIZE * 0.5f;
        quad.setBuffer(VertexBuffer.Type.Position, 3, this.getArray(halfSize));
        //-->
        quad.updateBound();
        Geometry geom = new Geometry("Ship", quad);
        Material mat = assets.loadMaterial("Materials/ShipMaterialLight.j3m");
        //Material mat = assets.loadMaterial("Materials/ShipMaterialUnshaded.j3m");

        geom.setMaterial(mat);
        setShipMaterialVariables(geom, ship);
        //mat.setInt("numTilesOffsetY", ship);

        geom.setQueueBucket(RenderQueue.Bucket.Transparent);

        PointLight myLight = new PointLight();
        myLight.setColor(ColorRGBA.White);
        myLight.setRadius(20);
        myLight.setPosition(geom.getLocalTranslation().add(0, 0, 2));
        rootNode.addLight(myLight);
        LightControl lightControl = new LightControl(myLight);
        geom.addControl(lightControl);

        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(1.3f));
        rootNode.addLight(al);

        return geom;
    }

    public void setShipMaterialVariables(Spatial s, int ship) {
        Geometry geom;
        if (s instanceof Geometry) {
            geom = (Geometry) s; //From createShip
        } else {
            geom = (Geometry) ((Node) s).getChild("Ship"); //From ModelViewState
        }
        Material mat = geom.getMaterial();
        mat.setInt("numTilesOffsetY", ship);
        geom.setMaterial(mat);
        log.info("Setting geometry material on spatial:" + s + "; ship:" + ship);
    }

    private Spatial createParticleEmitter(EntityId eId, String shapeName) {
        Spatial result = null;
        ParticleEmitter particleEmitter = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 30); //will be overriden in switch

        switch (shapeName) {
            //Create a thrust particle emitter
            case "thrust":
                result = createThrustEmitter(particleEmitter, eId);
        }
        return result;
    }

    private Spatial createThrustEmitter(ParticleEmitter thrustEffect, EntityId eId) {

        Material smokeMat = new Material(assets, "Common/MatDefs/Misc/Particle.j3md");
        smokeMat.setTexture("Texture", assets.loadTexture("Effects/Smoke/Smoke.png"));

        thrustEffect = new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 250);
        thrustEffect.setGravity(0, 0, 0);
        thrustEffect.setMaterial(smokeMat);
        thrustEffect.setImagesX(15);
        thrustEffect.setImagesY(1); // 2x
        thrustEffect.setEndColor(ColorRGBA.Black);
        thrustEffect.setStartColor(ColorRGBA.Orange);
        thrustEffect.setStartSize(0.1f);
        thrustEffect.setEndSize(0f);
        thrustEffect.setHighLife(0.25f); //Fits the decay
        thrustEffect.setLowLife(0.1f);
        thrustEffect.setNumParticles(1);

        return thrustEffect;
    }

    private Spatial createBomb(EntityId eId, BombLevelEnum level) {
        Quad quad = new Quad(CoreViewConstants.BOMBSIZE, CoreViewConstants.BOMBSIZE);
        //<-- Move into the material?
        float halfSize = CoreViewConstants.BOMBSIZE * 0.5f;
        quad.setBuffer(VertexBuffer.Type.Position, 3, this.getArray(halfSize));
        //-->
        quad.updateBound();
        Geometry geom = new Geometry("Bomb", quad);
        Material mat = assets.loadMaterial("Materials/BombMaterialLight.j3m");
        mat.setInt("numTilesOffsetY", level.viewOffset);
        geom.setMaterial(mat);
        geom.setQueueBucket(RenderQueue.Bucket.Transparent);
        return geom;
    }

    private Spatial createBullet(EntityId eId, int offSet) {
        Quad quad = new Quad(CoreViewConstants.BULLETSIZE, CoreViewConstants.BULLETSIZE);
        //<-- Move into the material?
        float halfSize = CoreViewConstants.BULLETSIZE * 0.5f;
        quad.setBuffer(VertexBuffer.Type.Position, 3, this.getArray(halfSize));
        //-->
        quad.updateBound();
        Geometry geom = new Geometry("Bullet", quad);
        Material mat = assets.loadMaterial("Materials/BulletMaterialUnshaded.j3m");
        mat.setInt("numTilesOffsetY", offSet);
        geom.setMaterial(mat);
        geom.setQueueBucket(RenderQueue.Bucket.Transparent);
        return geom;
    }

    private Spatial createExplosion(EntityId eId) {

        return ef.createExplosion();

//        /** Explosion effect. Uses Texture from jme3-test-data library! */ 
//        Material debrisMat = new Material(assets, "Common/MatDefs/Misc/Particle.j3md");
//        debrisMat.setTexture("Texture", assets.loadTexture("Effects/Explosion/Debris.png"));
//        
//        ParticleEmitter debrisEffect = new ParticleEmitter("Debris", ParticleMesh.Type.Triangle, 10);
//        debrisEffect.setMaterial(debrisMat);
//        debrisEffect.setImagesX(3); debrisEffect.setImagesY(3); // 3x3 texture animation
//        debrisEffect.setRotateSpeed(4);
//        debrisEffect.setSelectRandomImage(true);
//        debrisEffect.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 4, 0));
//        debrisEffect.setStartColor(new ColorRGBA(1f, 1f, 1f, 1f));
//        debrisEffect.setGravity(0f,6f,0f);
//        debrisEffect.getParticleInfluencer().setVelocityVariation(.60f);
//        //debrisEffect.setNumParticles(1);
//        return debrisEffect;
    }

    private Spatial createBounty(EntityId eId) {
        Quad quad = new Quad(CoreViewConstants.PRIZESIZE, CoreViewConstants.PRIZESIZE);
        //<-- Move into the material?
        float halfSize = CoreViewConstants.PRIZESIZE * 0.5f;
        quad.setBuffer(VertexBuffer.Type.Position, 3, this.getArray(halfSize));
        //-->
        quad.updateBound();
        Geometry geom = new Geometry("Bounty", quad);
        Material mat = assets.loadMaterial("Materials/BountyMaterialLight.j3m");
        mat.setFloat("StartTime", timer.getTimeInSeconds());
        geom.setMaterial(mat);
        geom.setQueueBucket(RenderQueue.Bucket.Transparent);
        return geom;
    }

    private Spatial createArena(EntityId eId) {
        Quad quad = new Quad(CoreViewConstants.ARENASIZE, CoreViewConstants.ARENASIZE);
        //<-- Move into the material?
        float halfSize = CoreViewConstants.ARENASIZE * 0.5f;
        quad.setBuffer(VertexBuffer.Type.Position, 3, this.getArray(halfSize));
        //-->
        quad.updateBound();
        Geometry geom = new Geometry("Arena", quad);
        geom.setCullHint(Spatial.CullHint.Always);
        Material mat = new Material(assets, "Common/MatDefs/Misc/Unshaded.j3md");

        geom.setMaterial(mat);

        return geom;
    }

    /*
    private Spatial createMapTile(EntityId eId) {
        //SquareShape s = e.get(SquareShape.class);

        Quad quad = new Quad(CoreViewConstants.MAPTILESIZE, CoreViewConstants.MAPTILESIZE);
        //<-- Move into the material?

        float halfSize = CoreViewConstants.MAPTILESIZE * 0.5f;
        quad.setBuffer(VertexBuffer.Type.Position, 3, this.getArray(halfSize));

        //-->
        quad.updateBound();
        Geometry geom = new Geometry("MapTile", quad);

        if (state.getType(eId).getTypeName(ed).equals(TileTypes.LEGACY)) {
            Material mat = new Material(assets, "MatDefs/BlackTransparentShader.j3md");

            //mat.setColor("Color", ColorRGBA.Yellow);
            Image image = clientMapState.getImage(eId);

            if (image == null) {
                image = clientMapState.forceLoadImage(eId);
            }

            Texture2D tex2D = new Texture2D(image);
            mat.setTexture("ColorMap", tex2D);

            geom.setMaterial(mat);
        } else {
            TileType tileType = state.getType(eId);
            
            //TODO: Do some caching and re-use materials/shaders
            Material mat = assets.loadMaterial("Materials/WangBlobLight.j3m"); //tileType.getTileSet()            
                        
            mat.setTransparent(false);
            
            
            geom.setMaterial(mat);
            geom.setQueueBucket(RenderQueue.Bucket.Opaque);
            this.updateWangBlobTile(geom, tileType);
        }

        return geom;
    }
     */
    private Spatial createExplosion2(EntityId eId) {
        Quad quad = new Quad(CoreViewConstants.EXPLOSION2SIZE, CoreViewConstants.EXPLOSION2SIZE);
        //<-- Move into the material?
        float halfSize = CoreViewConstants.EXPLOSION2SIZE * 0.5f;
        quad.setBuffer(VertexBuffer.Type.Position, 3, this.getArray(halfSize));
        //-->
        quad.updateBound();
        Geometry geom = new Geometry("Bomb", quad);
        Material mat = assets.loadMaterial("Materials/Explode2MaterialLight.j3m");
        mat.setFloat("StartTime", timer.getTimeInSeconds());

        geom.setMaterial(mat);
        geom.setQueueBucket(RenderQueue.Bucket.Transparent);
        return geom;
    }

    private Spatial createOver5(EntityId eId) {

        Quad quadOver5 = new Quad(CoreViewConstants.OVER5SIZE, CoreViewConstants.OVER5SIZE);
        //<-- Move into the material?
        float halfSizeOver5 = CoreViewConstants.OVER5SIZE * 0.5f;
        quadOver5.setBuffer(VertexBuffer.Type.Position, 3, this.getArray(halfSizeOver5));
        //-->
        quadOver5.updateBound();
        Geometry geomOver5 = new Geometry("Wormhole", quadOver5);
        Material matOver5 = assets.loadMaterial("Materials/Over5MaterialLight.j3m");
        matOver5.setFloat("StartTime", timer.getTimeInSeconds());
        geomOver5.setMaterial(matOver5);
        geomOver5.setQueueBucket(RenderQueue.Bucket.Transparent);
        return geomOver5;

    }

    private Spatial createWormhole(EntityId eId) {
        Quad quad = new Quad(CoreViewConstants.WORMHOLESIZE, CoreViewConstants.WORMHOLESIZE);
        //<-- Move into the material?
        float halfSize = CoreViewConstants.WORMHOLESIZE * 0.5f;
        quad.setBuffer(VertexBuffer.Type.Position, 3, this.getArray(halfSize));
        //-->
        quad.updateBound();
        Geometry geom = new Geometry("Wormhole", quad);
        Material mat = assets.loadMaterial("Materials/WormholeMaterialLight.j3m");
        mat.setFloat("StartTime", timer.getTimeInSeconds());
        geom.setMaterial(mat);
        geom.setQueueBucket(RenderQueue.Bucket.Transparent);
        return geom;
    }

    private Spatial createOver1(EntityId eId) {
        Quad quad = new Quad(CoreViewConstants.OVER1SIZE, CoreViewConstants.OVER1SIZE);
        //<-- Move into the material?
        float halfSize = CoreViewConstants.OVER1SIZE * 0.5f;
        quad.setBuffer(VertexBuffer.Type.Position, 3, this.getArray(halfSize));
        //-->
        quad.updateBound();
        Geometry geom = new Geometry("Over1", quad);
        Material mat = assets.loadMaterial("Materials/Over1MaterialLight.j3m");
        mat.setFloat("StartTime", timer.getTimeInSeconds());
        geom.setMaterial(mat);
        geom.setQueueBucket(RenderQueue.Bucket.Transparent);
        return geom;
    }

    private Spatial createOver2(EntityId eId) {
        Quad quad = new Quad(CoreViewConstants.OVER2SIZE, CoreViewConstants.OVER2SIZE);
        //<-- Move into the material?
        float halfSize = CoreViewConstants.OVER2SIZE * 0.5f;
        quad.setBuffer(VertexBuffer.Type.Position, 3, this.getArray(halfSize));
        //-->
        quad.updateBound();
        Geometry geom = new Geometry("Over2", quad);
        Material mat = assets.loadMaterial("Materials/Over2MaterialLight.j3m");
        mat.setFloat("StartTime", timer.getTimeInSeconds());
        geom.setMaterial(mat);
        geom.setQueueBucket(RenderQueue.Bucket.Transparent);
        return geom;
    }

    private Spatial createWarp(EntityId eId) {
        Quad quad = new Quad(CoreViewConstants.WARPSIZE, CoreViewConstants.WARPSIZE);
        //<-- Move into the material?
        float halfSize = CoreViewConstants.WARPSIZE * 0.5f;
        quad.setBuffer(VertexBuffer.Type.Position, 3, this.getArray(halfSize));
        //-->
        quad.updateBound();
        Geometry geom = new Geometry("Warp", quad);
        Material mat = assets.loadMaterial("Materials/WarpMaterialLight.j3m");
        mat.setFloat("StartTime", timer.getTimeInSeconds());
        geom.setMaterial(mat);
        geom.setQueueBucket(RenderQueue.Bucket.Transparent);
        return geom;
    }

    private Spatial createRepel(EntityId eId) {
        Quad quad = new Quad(CoreViewConstants.REPELSIZE, CoreViewConstants.REPELSIZE);
        //<-- Move into the material?
        float halfSize = CoreViewConstants.REPELSIZE * 0.5f;
        quad.setBuffer(VertexBuffer.Type.Position, 3, this.getArray(halfSize));
        //-->
        quad.updateBound();
        Geometry geom = new Geometry("Repel", quad);
        Material mat = assets.loadMaterial("Materials/RepelMaterialLight.j3m");
        mat.setFloat("StartTime", timer.getTimeInSeconds());
        geom.setMaterial(mat);
        geom.setQueueBucket(RenderQueue.Bucket.Transparent);
        return geom;
    }

    /*
    public void updateWangBlobTile(Spatial s, TileType tileType) {
        Geometry geom;
        if (s instanceof Geometry) {
            geom = (Geometry) s;
        } else {
            geom = (Geometry) ((Node) s).getChild("MapTile"); //From ModelViewState
        }
        Material mat = geom.getMaterial();
        //Offset tile
        mat.setInt("numTilesOffsetX", clientMapState.getWangBlobTileNumber(tileType.getTileIndex()));

        //Rotate tile
        Quaternion rot = new Quaternion();
        float rotations = clientMapState.getWangBlobRotations(tileType.getTileIndex());
        float ninety_degrees_to_radians = FastMath.PI / 2;

        rot.fromAngleAxis(-ninety_degrees_to_radians * rotations, Vector3f.UNIT_Z);
        //Reset rotation
        geom.setLocalRotation(new Quaternion());
        //Set correct rotation
        geom.rotate(rot);

        //log.info("Coords: "+s.getLocalTranslation() +" rotated: "+geom.getLocalRotation());
    }
     */
    private Spatial createBurst(EntityId eId) {
        Quad quad = new Quad(CoreViewConstants.BURSTSIZE, CoreViewConstants.BURSTSIZE);
        //<-- Move into the material?
        float halfSize = CoreViewConstants.BURSTSIZE * 0.5f;
        quad.setBuffer(VertexBuffer.Type.Position, 3, this.getArray(halfSize));
        //-->
        quad.updateBound();
        Geometry geom = new Geometry("Burst", quad);
        Material mat = assets.loadMaterial("Materials/BurstMaterialLight.j3m");
        mat.setFloat("StartTime", timer.getTimeInSeconds());
        geom.setMaterial(mat);
        geom.setQueueBucket(RenderQueue.Bucket.Transparent);
        return geom;
    }

    private float[] getArray(float halfSize) {
        float[] res = new float[]{
            halfSize, 0, -halfSize,
            -halfSize, 0, -halfSize,
            -halfSize, 0, halfSize,
            halfSize, 0, halfSize
        };
        return res;
    }
}

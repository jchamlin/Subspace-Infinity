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
package infinity.client;

import infinity.client.view.ModelViewState;
import com.jme3.app.Application;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Spatial;
import com.simsilica.es.EntityId;
import com.jme3.app.state.BaseAppState;
import infinity.util.MathUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A state to manage in-game camera
 *
 * @author Asser
 */
public class RadarCameraState extends BaseAppState {

    static Logger log = LoggerFactory.getLogger(RadarCameraState.class);

    private MovingAverage movingAverage = new MovingAverage(10);
    public static final float DISTANCETOPLANE = 300;
    private Camera radarViewPortCamera;
    private final EntityId localPlayerShip;

    private ModelViewState models;
    private Spatial playerShip;

    private Vector3f oldCamPos, newCamPos;

    public RadarCameraState(EntityId shipId) {
        this.localPlayerShip = shipId;

        log.debug("Constructed CameraState");
    }

    @Override
    protected void initialize(Application app) {
        this.radarViewPortCamera = new Camera();

        this.radarViewPortCamera.setLocation(new Vector3f(0, 0, DISTANCETOPLANE));
        this.radarViewPortCamera.lookAt(new Vector3f(0, 0, 0), Vector3f.UNIT_Z); //Set camera to look at the origin

        this.models = getState(ModelViewState.class);
        this.playerShip = models.getPlayerSpatial();
    }

    @Override
    protected void cleanup(Application app) {
    }

    @Override
    public void update(float tpf) {

        if (playerShip != null) {
            radarViewPortCamera.setLocation(playerShip.getWorldTranslation().add(0, 0, DISTANCETOPLANE));  //Set camera position above spatial - Z is up
            radarViewPortCamera.lookAt(playerShip.getWorldTranslation(), Vector3f.UNIT_Z); //Set camera to look at the spatial
        } else //Probably a crude way to do it - should be handled properly
        {
            this.playerShip = models.getPlayerSpatial();
        }

        //oldCamPos = newCamPos.clone();
    }

    public void setPlayerShip(Spatial s) {
        this.playerShip = s;
    }

    @Override
    public String getId() {
        return "RadarCameraState";
    }

    @Override
    protected void onEnable() {

    }

    @Override
    protected void onDisable() {

    }
}

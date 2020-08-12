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
package infinity.sim;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.math.Quaternion;

import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import com.simsilica.es.base.DefaultWatchedEntity;
import com.simsilica.mathd.Vec3d;
import com.simsilica.mphys.ControlDriver;
import com.simsilica.mphys.RigidBody;

import infinity.es.input.MovementInput;
import infinity.es.ship.Energy;
import infinity.es.ship.Rotation;
import infinity.es.ship.Speed;
import infinity.es.ship.Thrust;
import infinity.systems.SettingsSystem;

/**
 * Uses rotation and a 3-axis thrust vector to supply specific velocity to a
 * body. We ignore the normal physics acceleration for now and just set the
 * velocity directly based on our accelerated thrust values.
 *
 * @author Paul Speed
 */
public class PlayerDriver implements ControlDriver, Driver {

    static Logger log = LoggerFactory.getLogger(PlayerDriver.class);
    // The entity that is controlling this driver
    private DefaultWatchedEntity shipEntity;

    // Keep track of what the player has provided.
    private volatile Quaternion orientation = new Quaternion();
    private volatile MovementInput movementForces = new MovementInput(new Vec3d());

    private double pickup = 3;

    // Local reference to the body that we want to update
    private RigidBody body;
    private Vec3d velocity = new Vec3d();
    private final EntityData ed;
    private final SettingsSystem settings;

    public PlayerDriver(EntityId shipEntityId, EntityData ed, SettingsSystem settings) {
        // Watch all the relevant movement components of the ship
        Class[] types = { Energy.class, Rotation.class, Speed.class, Thrust.class };
        shipEntity = new DefaultWatchedEntity(ed, shipEntityId, types);
        this.settings = settings;
        this.ed = ed;
    }

    @Override
    public void applyMovementState(MovementInput input) {
        movementForces = input;
    }

    private double applyThrust(double v, double thrust, double tpf) {
        if (thrust > 0) {
            // Accelerate
            v = Math.min(thrust, v + pickup * tpf);
        } else if (thrust < 0) {
            // Decelerate
            v = Math.max(thrust, v - pickup * tpf);
        } else {
            if (v > 0) {
                // Fall to zero
                v = Math.max(0, v - pickup * tpf);
            } else {
                // Rist to zero
                v = Math.min(0, v + pickup * tpf);
            }
        }
        return v;
    }

    @Override
    public void update(long frameTime, double step) {
        // Drivable bodies should not fall asleep, keep them awake at all times
        body.wakeUp(true);

        shipEntity.applyChanges();

        // x-axis is side-to-side
        // Grab local versions of the player settings in case another
        // thread sets them while we are calculating.
        // Quaternion quat = orientation;
        Vec3d vec = movementForces.getMove();

        // x is rotate - we dont need to clamp that
        // velocity.x = applyThrust(velocity.x, vec.x, step);
        // z is forward
        velocity.z = applyThrust(velocity.z, vec.z, step);

        // Rotate the ship according to left and right (should stop rotating right away
        // when not pressing the keys
        // Rotate around the y-axis (y is upwards)
        body.setRotationalVelocity(0, vec.x, 0);

        // Set a clamped velocity on the forward axis rotated by the bodies current
        // rotation
        Vec3d newLinearVelocity = body.orientation.mult(velocity);
        // body.setLinearVelocity(newLinearVelocity);

        body.addForce(newLinearVelocity.mult(20));
        // log.info("Player (body) velocity (length of linvel):
        // "+body.getLinearVelocity().length());
    }

    @Override
    public void initialize(RigidBody body) {
        this.body = body;
    }

    @Override
    public void terminate(RigidBody body) {
        this.body = null;
    }

    public void fireGuns() {

    }
}

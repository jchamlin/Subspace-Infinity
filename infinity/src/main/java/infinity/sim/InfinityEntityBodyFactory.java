/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package infinity.sim;

import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import com.simsilica.ext.mphys.EntityBodyFactory;
import com.simsilica.ext.mphys.Gravity;
import com.simsilica.ext.mphys.Mass;
import com.simsilica.ext.mphys.ShapeFactory;
import com.simsilica.ext.mphys.ShapeInfo;
import com.simsilica.ext.mphys.SpawnPosition;
import com.simsilica.mathd.Vec3d;
import com.simsilica.mphys.AbstractShape;
import com.simsilica.mphys.RigidBody;
import com.simsilica.mphys.StaticBody;

/**
 *
 * @author AFahrenholz
 * @param <S>
 */
public class InfinityEntityBodyFactory extends EntityBodyFactory<AbstractShape> {

    EntityData ed;

    public InfinityEntityBodyFactory(EntityData ed, Vec3d defaultGravity, ShapeFactory<AbstractShape> shapeFactory) {
        super(ed, defaultGravity, shapeFactory);
        this.ed = ed;
    }

    @Override
    protected StaticBody<EntityId, AbstractShape> createStaticBody(EntityId id, SpawnPosition pos, ShapeInfo info, Mass mass) {
        StaticBody<EntityId, AbstractShape> result = super.createStaticBody(id, pos, info, mass);
        //Do whatever to the static body

        return result;//To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected RigidBody<EntityId, AbstractShape> createRigidBody(EntityId id, SpawnPosition pos, ShapeInfo info, Mass mass, Gravity gravity) {
        RigidBody<EntityId, AbstractShape> result = super.createRigidBody(id, pos, info, mass, gravity); //To change body of generated methods, choose Tools | Templates.

        //Do whatever we want to the body depending on the ShapeInfo
        switch (info.getShapeName(ed)) {
            case "bullet_l1":
                //Set the dampening/restitution
                result.setLinearDamping(0);
                break;
            case "warbird":

                break;
            default:
                throw new AssertionError();
        }

        return result;
    }

}

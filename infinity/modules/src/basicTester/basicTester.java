/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basicTester;

import java.util.HashSet;
import java.util.regex.Pattern;

import com.simsilica.es.EntityData;
import com.simsilica.es.EntityId;
import com.simsilica.mathd.Vec3d;
import com.simsilica.sim.SimTime;

import infinity.sim.AccessLevel;
import infinity.sim.AccountManager;
import infinity.sim.AdaptiveLoader;
import infinity.sim.ArenaManager;
import infinity.sim.BaseGameModule;
import infinity.sim.ChatHostedPoster;
import infinity.sim.CommandConsumer;
import infinity.sim.GameEntities;
import infinity.sim.PhysicsManager;
import infinity.sim.TimeManager;

/**
 *
 * @author AFahrenholz
 */
public class basicTester extends BaseGameModule {

    private final Pattern basicCommand = Pattern.compile("\\~basictest\\s(\\w+)");
    private EntityData ed;
    private final HashSet<EntityId> createdEntities = new HashSet<>();

    public basicTester(final ChatHostedPoster chp, final AccountManager am, final AdaptiveLoader loader,
            final ArenaManager arenas, final TimeManager time, final PhysicsManager physics) {
        super(chp, am, loader, arenas, time, physics);
    }

    @Override
    protected void initialize() {
        ed = getSystem(EntityData.class, true);

        // Test the smallest asteroids
        createdEntities.add(GameEntities.createOver1(ed, EntityId.NULL_ID, getPhysicsManager().getPhysics(),
                getTimeManager().getTime(), new Vec3d(10, 0, 10), 1));
        createdEntities.add(GameEntities.createOver1(ed, EntityId.NULL_ID, getPhysicsManager().getPhysics(),
                getTimeManager().getTime(), new Vec3d(10, 0, -10), 10));
        createdEntities.add(GameEntities.createOver1(ed, EntityId.NULL_ID, getPhysicsManager().getPhysics(),
                getTimeManager().getTime(), new Vec3d(-10, 0, 10), 100));
        createdEntities.add(GameEntities.createOver1(ed, EntityId.NULL_ID, getPhysicsManager().getPhysics(),
                getTimeManager().getTime(), new Vec3d(-10, 0, -10), 1000));

        // Test the medium asteroids
        createdEntities.add(GameEntities.createOver2(ed, EntityId.NULL_ID, getPhysicsManager().getPhysics(),
                getTimeManager().getTime(), new Vec3d(20, 0, 20)));
        createdEntities.add(GameEntities.createOver2(ed, EntityId.NULL_ID, getPhysicsManager().getPhysics(),
                getTimeManager().getTime(), new Vec3d(20, 0, -20)));
        createdEntities.add(GameEntities.createOver2(ed, EntityId.NULL_ID, getPhysicsManager().getPhysics(),
                getTimeManager().getTime(), new Vec3d(-20, 0, 20)));
        createdEntities.add(GameEntities.createOver2(ed, EntityId.NULL_ID, getPhysicsManager().getPhysics(),
                getTimeManager().getTime(), new Vec3d(-20, 0, -20)));
    }

    @Override
    protected void terminate() {
        createdEntities.forEach((id) -> {
            ed.removeEntity(id);
        });
    }

    @Override
    public void stop() {
        super.stop(); // To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(final SimTime time) {
        super.update(time); // To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void start() {
        super.start();

        // EventBus.addListener(this, ShipEvent.shipDestroyed, ShipEvent.shipSpawned);
        getChp().registerPatternBiConsumer(basicCommand,
                "The command to make this basic tester do stuff is ~basic <command>, where <command> is the command you want to execute",
                new CommandConsumer(AccessLevel.PLAYER_LEVEL, (id, s) -> messageHandler(id, s)));
    }

    private CommandConsumer messageHandler(@SuppressWarnings("unused") final EntityId id,
            @SuppressWarnings("unused") final String s) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
                                                                       // Tools | Templates.
    }

}

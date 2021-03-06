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
package infinity.net;

import com.google.common.base.MoreObjects;

import com.jme3.network.HostedConnection;

import com.simsilica.es.EntityId;
import com.simsilica.event.EventType;

/**
 * Events that are send to the event bus for different account state related
 * events. These are server-side only events and are available to the other
 * hosted services and possible the game systems in some rarer cases.
 *
 * @author Paul Speed
 */
public class AccountEvent {

    /**
     * Signals that a player has successfully logged in.
     */
    public static EventType<AccountEvent> playerLoggedOn = EventType.create("PlayerLoggedOn", AccountEvent.class);

    /**
     * Signals that a player has logged out.
     */
    public static EventType<AccountEvent> playerLoggedOff = EventType.create("PlayerLoggedOff", AccountEvent.class);

    private final HostedConnection conn;
    private final String playerName;
    private final EntityId playerEntity;

    public AccountEvent(final HostedConnection conn, final String playerName, final EntityId playerEntity) {
        this.conn = conn;
        this.playerName = playerName;
        this.playerEntity = playerEntity;
    }

    public HostedConnection getConnection() {
        return conn;
    }

    public String getPlayerName() {
        return playerName;
    }

    public EntityId getPlayerEntity() {
        return playerEntity;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(getClass().getSimpleName()).add("conn", conn).add("playerName", playerName)
                .add("playerEntity", playerEntity).toString();
    }
}

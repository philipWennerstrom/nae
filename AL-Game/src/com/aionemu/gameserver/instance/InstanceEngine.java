package com.aionemu.gameserver.instance;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.scripting.classlistener.AggregatedClassListener;
import com.aionemu.commons.scripting.classlistener.OnClassLoadUnloadListener;
import com.aionemu.commons.scripting.classlistener.ScheduledTaskClassListener;
import com.aionemu.commons.scripting.scriptmanager.ScriptManager;
import com.aionemu.gameserver.GameServerError;
import com.aionemu.gameserver.instance.handlers.EventID;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.GameEngine;
import com.aionemu.gameserver.world.WorldMapInstance;

/**
 * @author ATracer
 */
public class InstanceEngine implements GameEngine {

    private static final Logger log = LoggerFactory.getLogger(InstanceEngine.class);
    private static ScriptManager scriptManager = new ScriptManager();
    public static final File INSTANCE_DESCRIPTOR_FILE = new File("./data/scripts/system/instancehandlers.xml");
    public static final InstanceHandler DUMMY_INSTANCE_HANDLER = new GeneralInstanceHandler();
    private Map<Integer, Class<? extends InstanceHandler>> handlers = new HashMap<Integer, Class<? extends InstanceHandler>>();    
    // new
    private Map<Integer, Class<? extends InstanceHandler>> eventHandlers = new HashMap<Integer, Class<? extends InstanceHandler>>();

    @Override
    public void load(CountDownLatch progressLatch) {
        log.info("Instance engine load started");
        scriptManager = new ScriptManager();

        AggregatedClassListener acl = new AggregatedClassListener();
        acl.addClassListener(new OnClassLoadUnloadListener());
        acl.addClassListener(new ScheduledTaskClassListener());
        acl.addClassListener(new InstanceHandlerClassListener());
        scriptManager.setGlobalClassListener(acl);

        try {
            scriptManager.load(INSTANCE_DESCRIPTOR_FILE);
            log.info("Loaded " + handlers.size() + " instance handlers.");
        } catch (Exception e) {
            throw new GameServerError("Can't initialize instance handlers.", e);
        } finally {
            if (progressLatch != null) {
                progressLatch.countDown();
            }
        }
    }

    @Override
    public void shutdown() {
        log.info("Instance engine shutdown started");
        scriptManager.shutdown();
        scriptManager = null;
        handlers.clear();        
        eventHandlers.clear(); // new
        log.info("Instance engine shutdown complete");
    }

    public InstanceHandler getNewInstanceHandler(int worldId) {
        Class<? extends InstanceHandler> instanceClass = handlers.get(worldId);
        InstanceHandler instanceHandler = null;
        if (instanceClass != null) {
            try {
                instanceHandler = instanceClass.newInstance();
            } catch (Exception ex) {
                log.warn("Can't instantiate instance handler " + worldId, ex);
            }
        }
        if (instanceHandler == null) {
            instanceHandler = DUMMY_INSTANCE_HANDLER;
        }
        return instanceHandler;
    }

    // new
    public InstanceHandler getNewEventInstanceHandler(int handlerId) {
        Class<? extends InstanceHandler> instanceClass = this.eventHandlers.get(handlerId);
        InstanceHandler instanceHandler = null;
        if (instanceClass != null) {
            try {
                instanceHandler = instanceClass.newInstance();
            } catch (Exception ex) {
                log.warn("Can't instantiate event instance handler " + handlerId, ex);
            }
        }
        if (instanceHandler == null) {
            instanceHandler = DUMMY_INSTANCE_HANDLER;
        }
        return instanceHandler;
    }

    /**
     * @param handler
     */
    final void addInstanceHandlerClass(Class<? extends InstanceHandler> handler) {
        InstanceID idAnnotation = handler.getAnnotation(InstanceID.class);
        if (idAnnotation != null) {
            handlers.put(idAnnotation.value(), handler);
        } else { // new
            EventID eA = handler.getAnnotation(EventID.class);
            if (eA != null) {
                this.eventHandlers.put(eA.eventId(), handler);
            }
        }
    }

    /**
     * @param instance
     */
    public void onInstanceCreate(WorldMapInstance instance) {
        instance.getInstanceHandler().onInstanceCreate(instance);
    }

    public static final InstanceEngine getInstance() {
        return SingletonHolder.instance;
    }

    @SuppressWarnings("synthetic-access")
    private static class SingletonHolder {

        protected static final InstanceEngine instance = new InstanceEngine();
    }
}
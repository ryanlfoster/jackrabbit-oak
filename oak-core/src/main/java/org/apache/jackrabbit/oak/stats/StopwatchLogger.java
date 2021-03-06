/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.jackrabbit.oak.stats;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.ScheduledExecutorService;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class to be used for tracking of timing within methods. It makes use of the
 * {@link Clock.Fast} for speeding up the operation.
 */
public class StopwatchLogger implements Closeable {
    private static final Logger LOG = LoggerFactory.getLogger(StopwatchLogger.class);

    private final ScheduledExecutorService executor = newSingleThreadScheduledExecutor();
    private final String clazz;
    
    private Clock clock;
    private Logger customLog;
    
    /**
     * Create a class with the provided class.
     * 
     * @param claz
     */
    public StopwatchLogger(@Nonnull final String clazz) {
        this(null, checkNotNull(clazz));
    }

    /**
     * instantiate a class with the provided class
     * 
     * @param clazz
     */
    public StopwatchLogger(@Nonnull final Class<?> clazz) {
        this(checkNotNull(clazz).getName().toString());
    }

    /**
     * Instantiate a class with the provided class and custom logger. The provided logger, if not
     * null, will be then used for tracking down times
     * 
     * @param customLog
     * @param clazz
     */
    public StopwatchLogger(@Nullable final Logger customLog, @Nonnull final Class<?> clazz) {
        this(customLog, checkNotNull(clazz).getName().toString());
    }

    /**
     * Instantiate a class with the provided class and custom logger. The provided logger, if not
     * null, will be then used for tracking down times
     *
     * @param customLog
     * @param clazz
     */
    public StopwatchLogger(@Nullable final Logger customLog, @Nonnull final String clazz) {
        this.clazz = checkNotNull(clazz);
        this.customLog = customLog;
    }

    /**
     * starts the clock
     */
    public void start() {
        clock = new Clock.Fast(executor);
    }
    
    /**
     * track of an intermediate time without stopping the ticking.
     * 
     * @param message
     */
    public void split(@Nullable final String message) {
        track(customLog, clock, clazz, message);
    }
    
    /**
     * track the time and stop the clock.
     * 
     * @param message
     */
    public void stop(@Nullable final String message) {
        track(customLog, clock, clazz, message);
        clock = null;
    }

    /**
     * convenience method for tracking the messages
     * 
     * @param customLog a potential custom logger. If null the static instance will be used
     * @param clock the clock used for tracking.
     * @param clazz the class to be used during the tracking of times
     * @param message a custom message for the tracking.
     */
    private static void track(@Nullable final Logger customLog,
                              @Nullable final Clock clock,
                              @Nonnull final String clazz,
                              @Nullable final String message) {
        
        Logger l = (customLog == null) ? LOG :  customLog;
        
        if (clock == null) {
            l.debug("{} - clock has not been started yet.", clazz);
        } else {
            l.debug(
                "{} - {} {}",
                new Object[] { checkNotNull(clazz), message == null ? "" : message,
                              clock.getTimeMonotonic() });
        }
    }

    @Override
    public void close() throws IOException {
        try {
            executor.shutdownNow();            
        } catch (Throwable t) {
            LOG.error("Error while shutting down the scheduler.", t);
        }
    }
}

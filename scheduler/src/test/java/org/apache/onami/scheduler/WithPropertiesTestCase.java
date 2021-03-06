package org.apache.onami.scheduler;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import static org.junit.Assert.assertEquals;

import java.util.Properties;

import javax.inject.Inject;

import org.apache.onami.test.OnamiRunner;
import org.apache.onami.test.annotation.GuiceProvidedModules;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.Scheduler;

import com.google.inject.Module;

@RunWith( OnamiRunner.class )
public class WithPropertiesTestCase
{
    private static final String INSTANCE_NAME = "Guartz";

    @GuiceProvidedModules
    public static Module createTestModule()
    {
        return new QuartzModule()
        {

            @Override
            protected void schedule()
            {
                Properties properties = new Properties()
                {
                    private static final long serialVersionUID = 1L;

                    {
                        put( "org.quartz.scheduler.instanceName", INSTANCE_NAME );
                        put( "org.quartz.threadPool.class", "org.quartz.simpl.ZeroSizeThreadPool" );
                    }
                };
                configureScheduler().withProperties( properties );
            }

        };
    }

    @Inject
    private Scheduler scheduler;

    @After
    public void tearDown()
        throws Exception
    {
        scheduler.shutdown();
    }

    @Test
    public void testPropertiesConfiguredInstanceName()
        throws Exception
    {
        assertEquals( scheduler.getSchedulerName(), INSTANCE_NAME );
    }

}

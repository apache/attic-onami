package org.apache.onami.configuration.mixed;

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

import static com.google.inject.name.Names.bindProperties;
import static org.apache.onami.configuration.Rocoto.expandVariables;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Properties;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.onami.configuration.ConfigurationModule;
import org.apache.onami.configuration.converters.FileConverter;
import org.apache.onami.test.OnamiRunner;
import org.apache.onami.test.annotation.GuiceProvidedModules;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.AbstractModule;
import com.google.inject.Module;

@RunWith( OnamiRunner.class )
public final class ConfigurationConverterTestCase
{

    @GuiceProvidedModules
    public static Module createTestModule()
    {
        return expandVariables( new AbstractModule()
        {

            @Override
            protected void configure()
            {
                Properties properties = new Properties();
                properties.put( "test.suites", "${user.dir}/src/test/resources/testng.xml" );

                bindProperties( binder(), properties );
            }

        }, new ConfigurationModule()
        {

            @Override
            protected void bindConfigurations()
            {
                bindSystemProperties();
            }

        }, new FileConverter() );
    }

    @Inject
    @Named( "test.suites" )
    private File testSuites;

    public void setTestSuites( File testSuites )
    {
        this.testSuites = testSuites;
    }

    @Test
    public void verifyFileExists()
    {
        assertTrue( testSuites.exists() );
    }

}

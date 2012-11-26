package org.apache.onami.test.handler;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.onami.test.annotation.MockFramework;
import org.apache.onami.test.annotation.MockType;
import org.apache.onami.test.reflection.ClassHandler;
import org.apache.onami.test.reflection.ClassVisitor;
import org.apache.onami.test.reflection.HandleException;

/**
 * Handler class to handle all {@link MockFramework} annotations.
 *
 * @see ClassVisitor
 * @see MockFramework
 */
public final class MockFrameworkHandler
    implements ClassHandler<MockFramework>
{

    final static private Logger logger = Logger.getLogger( MockFrameworkHandler.class.getName() );

    private MockType mockType;

    /**
     * @return the mockType
     */
    public MockType getMockType()
    {
        return mockType;
    }

    /**
     * {@inheritDoc}
     */
    public void handle( MockFramework annotation, Class<?> element )
        throws HandleException
    {
        if ( mockType != null && mockType != annotation.value() )
        {
            throw new HandleException( "Inconsistent mock framework found. " + "Mock framework already set [set: "
                + mockType + " now found: " + annotation.value() + "]" );
        }

        if ( logger.isLoggable( Level.FINER ) )
        {
            logger.finer( "  Found MockFramework: " + annotation.value() );
        }

        mockType = annotation.value();
    }

}
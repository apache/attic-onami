package org.apache.onami.test;

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

import org.apache.onami.test.annotation.Mock;
import org.apache.onami.test.data.Service;
import org.easymock.EasyMock;

abstract public class AbstractMockTestCase
    extends AbstractEmptyTestCase
{

    // Create and inject a Provided EasyMock
    @Mock( providedBy = "getMock" )
    protected Service providedMock;

    public static Service getMock()
    {
        // Create the mock object and inject the dependency via Google-guice into HelloWorld
        return EasyMock.createNiceMock( Service.class );
    }

}

package org.apache.onami.configuration;

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

import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 */
public final class MyBatisConfiguration
{

    @Inject
    @Named( "ibatis.environment.id" )
    private String environmentId;

    @Inject
    @Named( "ibatis.configuration.lazyLoadingEnabled" )
    private boolean lazyLoadingEnabled;

    public String getEnvironmentId()
    {
        return environmentId;
    }

    public void setEnvironmentId( String environmentId )
    {
        this.environmentId = environmentId;
    }

    public boolean isLazyLoadingEnabled()
    {
        return lazyLoadingEnabled;
    }

    public void setLazyLoadingEnabled( boolean lazyLoadingEnabled )
    {
        this.lazyLoadingEnabled = lazyLoadingEnabled;
    }

}

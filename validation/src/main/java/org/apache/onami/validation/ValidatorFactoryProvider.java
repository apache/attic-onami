package org.apache.onami.validation;

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
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.validation.ValidatorFactory;
import javax.validation.spi.ConfigurationState;

import org.apache.bval.jsr303.ApacheValidationProvider;

/**
 * Validator Factory guice provider implementation.
 */
@Singleton
final class ValidatorFactoryProvider
    implements Provider<ValidatorFactory>
{

    private final ConfigurationState configurationState;

    @Inject
    public ValidatorFactoryProvider( ConfigurationState configurationState )
    {
        this.configurationState = configurationState;
    }

    /**
     * {@inheritDoc}
     */
    public ValidatorFactory get()
    {
        return new ApacheValidationProvider().buildValidatorFactory( configurationState );
    }

}

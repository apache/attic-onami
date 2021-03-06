package org.apache.onami.logging.juli;

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

import static com.google.inject.matcher.Matchers.any;

import java.util.logging.Logger;

import org.apache.onami.logging.core.AbstractLoggingModule;

import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matcher;

/**
 * {@code java.util.logging.Logger} logger module implementation.
 */
public final class JuliLoggingModule extends AbstractLoggingModule<Logger> {

    /**
     * Creates a new {@code java.util.logging.Logger} injection module that matches any class.
     *
     * @since 3.1
     */
    public JuliLoggingModule()
    {
        super( any(), JuliLoggerInjector.class );
    }

    /**
     * Creates a new {@code java.util.logging.Logger} injection module.
     *
     * @param matcher types matcher for whom the Logger injection has to be
     *        performed.
     */
    public JuliLoggingModule( Matcher<? super TypeLiteral<?>> matcher )
    {
        super( matcher, JuliLoggerInjector.class );
    }

}

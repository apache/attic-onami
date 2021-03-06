package org.apache.onami.autobind.annotations.features;

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

import static java.lang.String.format;
import static java.util.logging.Level.FINE;
import static org.apache.onami.autobind.annotations.To.Type.IMPLEMENTATION;
import static org.apache.onami.autobind.install.BindingStage.BINDING;
import static org.apache.onami.autobind.install.BindingStage.IGNORE;

import java.lang.annotation.Annotation;
import java.util.Map;

import javax.inject.Singleton;

import org.apache.onami.autobind.annotations.Bind;
import org.apache.onami.autobind.install.BindingStage;

@Singleton
public class ImplementationBindingFeature
    extends AutoBindingFeature
{

    @Override
    public BindingStage accept( Class<Object> annotatedClass, Map<String, Annotation> annotations )
    {
        if ( annotations.containsKey( Bind.class.getName() ) )
        {
            Bind annotation = (Bind) annotations.get( Bind.class.getName() );
            if ( !annotation.multiple() && ( IMPLEMENTATION == annotation.to().value() ) )
            {
                return BINDING;
            }
        }
        return IGNORE;
    }

    @Override
    public void process( final Class<Object> annotatedClass, final Map<String, Annotation> annotations )
    {
        final boolean asSingleton =
            ( annotations.containsKey( com.google.inject.Singleton.class.getName() ) || annotations.containsKey( javax.inject.Singleton.class.getName() ) );

        if ( _logger.isLoggable( FINE ) )
        {
            _logger.fine( format( "Binding Class %s. Singleton? %s ", annotatedClass, asSingleton ) );
        }

        bind( annotatedClass, null, ( asSingleton ? Singleton.class : null ) );
    }

}

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
import static java.util.logging.Level.INFO;
import static java.util.logging.Logger.getLogger;
import static org.apache.onami.autobind.install.BindingStage.IGNORE;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.logging.Logger;

import javax.inject.Singleton;

import org.apache.onami.autobind.annotations.GuiceModule;
import org.apache.onami.autobind.install.BindingStage;
import org.apache.onami.autobind.install.bindjob.BindingJob;
import org.apache.onami.autobind.install.bindjob.ModuleBindingJob;
import org.apache.onami.autobind.scanner.features.BindingScannerFeature;

import com.google.inject.Module;

@Singleton
public class ModuleBindingFeature
    extends BindingScannerFeature
{

    private final Logger _logger = getLogger( getClass().getName() );

    @Override
    public BindingStage accept( Class<Object> annotatedClass, Map<String, Annotation> annotations )
    {
        if ( annotations.containsKey( GuiceModule.class.getName() ) )
        {
            GuiceModule module = (GuiceModule) annotations.get( GuiceModule.class.getName() );
            return module.stage();
        }
        return IGNORE;
    }

    @Override
    public void process( final Class<Object> annotatedClass, Map<String, Annotation> annotations )
    {
        BindingJob job = new ModuleBindingJob( annotatedClass.getName() );
        if ( !tracer.contains( job ) )
        {
            if ( _logger.isLoggable( INFO ) )
            {
                _logger.info( "Installing Module: " + annotatedClass.getName() );
            }
            synchronized ( _binder )
            {
                _binder.install( (Module) injector.getInstance( annotatedClass ) );
            }
        }
        else
        {
            String message = format( "Ignoring BindingJob \"%s\", because it was already bound.", job );

            if ( _logger.isLoggable( FINE ) )
            {
                _logger.log( FINE, message, new Exception( message ) );
            }
            else if ( _logger.isLoggable( INFO ) )
            {
                _logger.log( INFO, message );
            }
        }
    }

}

package org.apache.onami.test.handler;

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

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.onami.test.annotation.GuiceProvidedModules;
import org.apache.onami.test.reflection.HandleException;
import org.apache.onami.test.reflection.MethodHandler;

import com.google.inject.Module;
import com.google.inject.TypeLiteral;
import com.google.inject.internal.MoreTypes;

/**
 * Handler class to handle all {@link GuiceProvidedModules} annotations.
 *
 * @see org.apache.onami.test.reflection.ClassVisitor
 * @see GuiceProvidedModules
 */
public final class GuiceProvidedModuleHandler
    implements MethodHandler<GuiceProvidedModules>
{

    private static Logger logger = Logger.getLogger( GuiceProvidedModuleHandler.class.getName() );

    private final List<Module> modules = new ArrayList<Module>();

    /**
     * @return the guiceProviderModuleRegistry
     */
    public List<Module> getModules()
    {
        return modules;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings( "unchecked" )
    public void handle( GuiceProvidedModules annotation, Method method )
        throws HandleException
    {
        final Class<?> returnType = method.getReturnType();

        if ( logger.isLoggable( Level.FINER ) )
        {
            logger.finer( String.format( "  Found %s annotated method, checking if return type '%s' is one of %s ( %s | Iterable<%s> | %s[] )",
                                         GuiceProvidedModules.class.getSimpleName(),
                                         returnType.getName(),
                                         Module.class.getName(),
                                         Module.class.getName(),
                                         Module.class.getName() ) );
        }

        if ( !Modifier.isPublic( method.getModifiers() ) || !Modifier.isStatic( method.getModifiers() ) )
        {
            throw new HandleException( "Impossible to invoke method: " + method + ", it has to be static and public" );
        }

        final Class<?> type = method.getDeclaringClass();

        try
        {
            if ( Module.class.isAssignableFrom( returnType ) )
            {
                modules.add( (Module) method.invoke( type ) );
            }
            else if ( MoreTypes.getRawType( new TypeLiteral<Iterable<Module>>()
            {
            }.getType() ).isAssignableFrom( returnType ) )
            {
                addModules( (Iterable<Module>) method.invoke( type ) );
            }
            else if ( MoreTypes.getRawType( new TypeLiteral<Module[]>()
            {
            }.getType() ).isAssignableFrom( returnType ) )
            {
                addModules( (Module[]) method.invoke( type ) );
            }
            else
            {
                throw new ClassCastException("Incompatible return type: '" + returnType.getName() + "' of method '"  
                        + method.getName() + "'. \nThe return type must be one of " 
                        + " ('" 
                        + Module.class.getName() 
                        + "' | '" 
                        + "Iterable<" + Module.class.getName() + ">" 
                        + "' | '" 
                        + Module.class.getName() + "[]" +
                        "' )" );
            }
            
            if ( logger.isLoggable( Level.FINER ) )
            {
                logger.finer( "  Invoked method: " + method.toGenericString() );
            }
        }
        catch ( Exception e )
        {
            throw new HandleException( e );
        }
    }

    private void addModules( Iterable<Module> modules )
    {
        for ( Module module : modules )
        {
            this.modules.add( module );
        }
    }

    private void addModules( Module... modules )
    {
        for ( Module module : modules )
        {
            this.modules.add( module );
        }
    }
}

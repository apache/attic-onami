package org.apache.onami.configuration.variables;

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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @since 6.0
 */
public final class VariablesMap implements Map<String, String>
{

    /** Parser to use for variables resolving */
    private final Parser parser;

    private final Map<String, Resolver> resolvers = new HashMap<String, Resolver>();

    private final Map<String, String> data = new HashMap<String, String>();

    public VariablesMap()
    {
        this( new AntStyleParser() );
    }

    public VariablesMap( Parser parser )
    {
        if ( parser == null )
        {
            throw new IllegalArgumentException( "Parser used to resolve input variables cannot be null." );
        }
        this.parser = parser;
    }

    public void clear()
    {
        resolvers.clear();
        data.clear();
    }

    public boolean containsKey( Object key )
    {
        return data.containsKey( key );
    }

    public boolean containsValue( Object value )
    {
        return data.containsValue( value );
    }

    public Set<Entry<String, String>> entrySet()
    {
        return data.entrySet();
    }

    public String get( Object key )
    {
        return data.get( key );
    }

    public boolean isEmpty()
    {
        return data.isEmpty();
    }

    public Set<String> keySet()
    {
        return data.keySet();
    }

    public String put( String key, String value )
    {
        putValue( key, value );
        resolveVariables();
        return data.get( key );
    }

    public void putAll( Map<? extends String, ? extends String> t )
    {
        for ( Entry<? extends String, ? extends String> entry : t.entrySet() )
        {
            putValue( entry.getKey(), entry.getValue() );
        }
        resolveVariables();
    }

    public void putAll( Properties properties )
    {
        for ( Entry<Object, Object> entry : properties.entrySet() )
        {
            putValue( entry.getKey().toString(), entry.getValue().toString() );
        }
        resolveVariables();
    }

    private void putValue( String key, String value )
    {
        data.put( key, value );

        Resolver resolver = parser.parse( value );
        if ( resolver.needsResolving() )
        {
            resolvers.put( key, resolver );
        }
        else
        {
            if ( resolvers.containsKey( key ) )
            {
                resolvers.remove( key );
            }
        }
    }

    private void resolveVariables()
    {
        for ( Entry<String, Resolver> entry : resolvers.entrySet() )
        {
            data.put( entry.getKey(), entry.getValue().resolve( data ) );
        }
    }

    public String remove( Object key )
    {
        String value = null;
        if ( containsKey( key ) )
        {
            value = data.remove( key );
            resolvers.remove( key );
            resolveVariables();
        }
        return value;
    }

    public int size()
    {
        return data.size();
    }

    public Collection<String> values()
    {
        return data.values();
    }

    @Override
    public String toString()
    {
        return data.toString();
    }

}

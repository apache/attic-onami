package org.apache.onami.autobind.configuration;

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
import static java.util.logging.Level.WARNING;
import static java.util.logging.Logger.getLogger;

import java.net.URL;
import java.util.Properties;
import java.util.logging.Logger;

import javax.inject.Provider;

import org.apache.onami.configuration.PropertiesURLReader;

public class PropertiesProvider
    implements Provider<Properties>
{

    private final Logger _logger = getLogger( getClass().getName() );

    private final URL url;

    private final boolean isXML;

    public PropertiesProvider( URL url, boolean isXML )
    {
        super();
        this.url = url;
        this.isXML = isXML;
    }

    @Override
    public Properties get()
    {
        try
        {
            _logger.info( format( "Doing lazy Loading for Configuration %s", url ) );
            PropertiesURLReader reader = new PropertiesURLReader( url );
            if(isXML){
            	reader.inXMLFormat();
            }
			return reader.readConfiguration();
        }
        catch ( Exception e )
        {
            _logger.log( WARNING, format( "Configuration in %s couldn't be read.", url), e );
            return new Properties();
        }
    }

}

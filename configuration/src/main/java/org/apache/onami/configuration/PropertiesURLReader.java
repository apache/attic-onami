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

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

import org.apache.onami.configuration.binder.XMLPropertiesFormatBindingBuilder;

/**
 * {@link Properties} reader implementation able to read configuration files from classpath, file system or URLs.
 *
 * This reader implementation support both {@code .properties} and {@code .xml} properties format.
 */
public final class PropertiesURLReader
    implements XMLPropertiesFormatBindingBuilder
{

    /**
     * The URL has to be open.
     */
    private final URL url;

    /**
     * Flag to mark properties are in XML format.
     */
    private boolean isXML = false;

    /**
     * Create a new {@code .properties} reader of a resource located in the given URL.
     *
     * @param url the URL that locates the configuration resource.
     * @param isXML to mark if the properties file is in XML format or not.
     */
    public PropertiesURLReader( URL url )
    {
        if ( url == null)
        {
            throw new IllegalArgumentException( "'url' argument can't be null" );
        }

        this.url = url;
    }

    /**
     * {@inheritDoc}
     */
    public void inXMLFormat()
    {
        isXML = true;
    }

    /**
     *
     * @return
     */
    public URL getUrl()
    {
        return url;
    }

    /**
     *
     *
     * @return
     * @throws Exception
     */
    public final Properties readConfiguration()
        throws Exception
    {
        URLConnection connection = null;
        InputStream input = null;
        try
        {
            connection = url.openConnection();
            connection.setUseCaches( false );
            input = connection.getInputStream();

            Properties properties = new Properties();
            if ( isXML )
            {
                properties.loadFromXML( input );
            }
            else
            {
                properties.load( input );
            }

            return properties;
        }
        finally
        {
            if ( connection != null && ( connection instanceof HttpURLConnection ) )
            {
                ( (HttpURLConnection) connection ).disconnect();
            }
            if ( input != null )
            {
                try
                {
                    input.close();
                }
                catch ( IOException e )
                {
                    // close quietly
                }
            }
        }
    }

}

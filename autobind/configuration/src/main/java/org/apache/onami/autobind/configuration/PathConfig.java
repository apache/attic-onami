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

import static org.apache.onami.autobind.configuration.PathConfig.PathType.CLASSPATH;

import java.lang.annotation.Documented;

@Documented
public @interface PathConfig
{

    String value();

    PathType type() default CLASSPATH;

    public static enum PathType
    {

        /**
         * Use the Classloader to fetch the Configuration
         */
        CLASSPATH,

        /**
         * Tries to load the Configuration from a File. Can be absolute or relative. Relative to the the Path of
         * ClassLoader.getResources("/").
         */
        FILE,

        /**
         * Can be each Kind of URL. file:/, classpath:/, http://
         */
        URL

    }

}

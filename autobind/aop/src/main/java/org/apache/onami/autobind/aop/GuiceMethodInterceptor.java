package org.apache.onami.autobind.aop;

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

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.aopalliance.intercept.MethodInterceptor;

import com.google.inject.matcher.Matcher;

/**
 * If you don't want to use the {@link Invoke}, {@link MethodMatcher} and
 * {@link ClassMatcher} Annotation, your {@link MethodInterceptor} could inherit
 * of this class.
 */
public abstract class GuiceMethodInterceptor
    implements MethodInterceptor
{

    public static final Type CLASS_MATCHER_TYPE;

    public static final Type METHOD_MATCHER_TYPE;

    static
    {
        try
        {
            CLASS_MATCHER_TYPE =
                GuiceMethodInterceptor.class.getMethod( "getClassMatcher" ).getGenericReturnType();
            METHOD_MATCHER_TYPE =
                GuiceMethodInterceptor.class.getMethod( "getMethodMatcher" ).getGenericReturnType();
        }
        catch ( Exception e )
        {
            throw new AssertionError( e );
        }
    }

    public abstract Matcher<? super Class<?>> getClassMatcher();

    public abstract Matcher<? super Method> getMethodMatcher();

}

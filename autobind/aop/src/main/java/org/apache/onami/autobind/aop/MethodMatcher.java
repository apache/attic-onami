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

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;

/**
 * This Annotation marks a Method, which returns an Object of Type
 * {@link Matcher}. This Matcher is used by Guice, to decide if a
 * {@link MethodInterceptor} should be invoked for that {@link Method}.
 *
 * <pre>@MethodMatcher
 * public Matcher<? super Method> getMethodMatcher()
 * {
 *     return Matchers.annotatedWith( Intercept.class );
 * }
 */
@Documented
@Retention( RUNTIME )
@Target( { METHOD } )
public @interface MethodMatcher
{
}

package org.apache.onami.autobind.scanner.features;

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

import static com.google.inject.multibindings.Multibinder.newSetBinder;

import java.lang.annotation.Annotation;

import org.apache.onami.autobind.scanner.ScannerModule;

import com.google.inject.binder.ScopedBindingBuilder;
import com.google.inject.multibindings.Multibinder;

/**
 * Default Implementation for Annotation Listeners, which should stay informed
 * abbout found annotated classes. Due the fact, that we need the Binder of the
 * Child Injector, it will be set at runtime by the {@link ScannerModule}.
 */
public abstract class MultiBindingScannerFeature
    extends BindingScannerFeature
{

    @Override
    protected <T, V extends T> void bindInstance( V impl, Class<T> interf, Annotation annotation,
                                                  Class<? extends Annotation> scope )
    {
        Multibinder<T> builder;
        synchronized ( _binder )
        {
            if ( annotation != null )
            {
                builder = newSetBinder( _binder, interf, annotation );
            }
            else
            {
                builder = newSetBinder( _binder, interf );
            }

            builder.addBinding().toInstance( impl );
        }
    }

    @Override
    protected void bindConstant( String value, Annotation annotation )
    {
        Multibinder<String> builder;
        synchronized ( _binder )
        {
            builder = newSetBinder( _binder, String.class, annotation );
            builder.addBinding().toInstance( value );
        }
    }

    @Override
    protected <T, V extends T> void bind( Class<V> impl, Class<T> interf, Annotation annotation,
                                          Class<? extends Annotation> scope )
    {
        Multibinder<T> builder;
        synchronized ( _binder )
        {
            if ( annotation != null )
            {
                builder = newSetBinder( _binder, interf, annotation );
            }
            else
            {
                builder = newSetBinder( _binder, interf );
            }

            ScopedBindingBuilder scopedBindingBuilder = builder.addBinding().to( impl );
            if ( scope != null )
            {
                scopedBindingBuilder.in( scope );
            }
        }
    }

}

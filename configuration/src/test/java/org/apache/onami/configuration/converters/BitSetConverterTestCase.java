package org.apache.onami.configuration.converters;

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

import static com.google.inject.name.Names.named;
import static com.google.inject.util.Modules.combine;

import java.util.BitSet;

import org.apache.onami.test.OnamiRunner;
import org.apache.onami.test.annotation.GuiceProvidedModules;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Module;
import com.google.inject.name.Named;

/**
 *
 */
@RunWith( OnamiRunner.class )
public final class BitSetConverterTestCase
    extends AbstractTestCase<BitSet>
{

    @GuiceProvidedModules
    public static Module createTestModule()
    {
        return combine( new BitSetConverter(), new AbstractModule()
        {
            protected void configure()
            {
                bindConstant().annotatedWith( named( "bitset" ) ).to( "a, 123, ~" );
            };
        } );
    }

    @Override
    @Inject
    public void setConvertedField( @Named( "bitset" ) BitSet convertedField )
    {
        super.setConvertedField( convertedField );
    }

    @Test
    public void bitset()
    {
        BitSet expected = new BitSet();
        expected.set( 'a' );
        expected.set( 123 );
        expected.set( '~' );
        verifyConversion( expected );
    }

}

package org.apache.onami.autobind.configuration.example.commons.plist;

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

import org.apache.onami.autobind.annotations.GuiceModule;
import org.apache.onami.autobind.scanner.asm.ASMClasspathScanner;

import com.google.inject.AbstractModule;


/**
 * This is a GuiceModule, which bind the {@link ExampleImpl} to the
 * {@link Example} interface and it will be recognized by the
 * {@link ASMClasspathScanner}, due the fact that it is annotated with the
 * {@link GuiceModule}.
 * 
 * @author Daniel Manzke
 * 
 */
@GuiceModule
public class ExampleModule extends AbstractModule {
	@Override
	protected void configure() {
		bind(Example.class).to(ExampleImpl.class);
	}
}

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

import static com.google.inject.TypeLiteral.get;
import static java.lang.System.getProperty;
import static java.util.Collections.addAll;
import static java.util.logging.Level.FINE;
import static java.util.logging.Level.INFO;
import static java.util.logging.Logger.getLogger;
import static org.apache.onami.autobind.jsr330.Names.named;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.onami.autobind.annotations.features.AutoBindingFeature;
import org.apache.onami.autobind.annotations.features.ImplementationBindingFeature;
import org.apache.onami.autobind.annotations.features.ModuleBindingFeature;
import org.apache.onami.autobind.annotations.features.MultiBindingFeature;
import org.apache.onami.autobind.install.BindingTracer;
import org.apache.onami.autobind.install.InstallationContext;
import org.apache.onami.autobind.install.bindjob.BindingJob;
import org.apache.onami.autobind.scanner.ClasspathScanner;
import org.apache.onami.autobind.scanner.PackageFilter;
import org.apache.onami.autobind.scanner.ScannerModule;
import org.apache.onami.autobind.scanner.features.ScannerFeature;
import org.apache.onami.configuration.PropertiesURLReader;

import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.multibindings.Multibinder;

/**
 * The StartupModule is used for creating an initial Injector, which binds and
 * instantiates the Scanning module. Due the fact that we have multiple Scanner
 * Implementations, you have to pass the Class for the Scanner and the Packages
 * which should be scanned. You can override the bindAnnotationListeners-Method,
 * to add your own {@link ScannerFeature}.
 */
public abstract class StartupModule extends AbstractModule {

	protected final Logger _logger = getLogger(getClass().getName());

	protected PackageFilter[] _packages;

	protected Class<? extends ClasspathScanner> _scanner;

	protected List<Class<? extends ScannerFeature>> _features = new ArrayList<Class<? extends ScannerFeature>>();

	protected boolean bindSystemProperties;

	protected boolean bindEnvironment;

	protected boolean bindStartupConfiguration = true;

	protected boolean verbose = getProperty("org.apache.onami.autobind.verbose") != null;

	public StartupModule(Class<? extends ClasspathScanner> scanner,
			PackageFilter... filter) {
		_packages = (filter == null ? new PackageFilter[0] : filter);
		_scanner = scanner;
	}

	@Override
	public void configure() {
		List<PackageFilter> packages = new ArrayList<PackageFilter>();
		addAll(packages, _packages);
		addAll(packages, bindPackages());

		_packages = packages.toArray(new PackageFilter[packages.size()]);
		Module scannerModule = new AbstractModule() {
			@Override
			protected void configure() {
				Binder binder = binder();
				if (_logger.isLoggable(FINE)) {
					_logger.fine("Binding ClasspathScanner to "
							+ _scanner.getName());
					for (PackageFilter p : _packages) {
						_logger.fine("Using Package " + p + " for scanning.");
					}
				}

				binder.bind(InstallationContext.class).asEagerSingleton();
				binder.bind(ClasspathScanner.class).to(_scanner);
				binder.bind(get(PackageFilter[].class))
						.annotatedWith(named("packages")).toInstance(_packages);
				Set<URL> classpath = findClassPaths();
				binder.bind(get(URL[].class))
						.annotatedWith(named("classpath"))
						.toInstance(
								classpath.toArray(new URL[classpath.size()]));

				bindFeatures(binder);
			}
		};

		ConfigurationModule configurationModule = new ConfigurationModule();
		if (bindSystemProperties) {
			configurationModule.bindSystemProperties();
		}
		if (bindEnvironment) {
			configurationModule.bindEnvironmentVariables();
		}

		if (bindStartupConfiguration) {
			URL startup = getClass().getResource("/conf/startup.xml");
			if (startup != null) {
				try {
					URI startupURI = startup.toURI();
					_logger.log(INFO, "Startup Config is used from Path: "
							+ startupURI);
					PropertiesURLReader reader = new PropertiesURLReader(
							startup);
					reader.inXMLFormat();
					configurationModule.addConfigurationReader(reader);
				} catch (URISyntaxException e) {
					_logger.log(INFO,
							"Startup Config couldn't be found in Classpath.", e);
				}
			} else {
				_logger.log(INFO,
						"Startup Config couldn't be found, so it is not used.");
			}
		}

		Injector internal = Guice.createInjector(scannerModule,
				configurationModule);
		binder().install(internal.getInstance(ScannerModule.class));
		binder().install(configurationModule);

		if (verbose) {
			BindingTracer tracer = internal.getInstance(BindingTracer.class);

			StringBuilder builder = new StringBuilder(
					"Following Binding were processed.\n");
			for (BindingJob job : tracer) {
				builder.append(job.toString()).append("\n");
			}
			_logger.info(builder.toString());
		}
	}

	protected abstract Multibinder<ScannerFeature> bindFeatures(Binder binder);

	protected PackageFilter[] bindPackages() {
		return new PackageFilter[0];
	}

	protected Set<URL> findClassPaths() {
		Set<URL> urlSet = new HashSet<URL>();

		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		while (loader != null) {
			if (loader instanceof URLClassLoader) {
				URL[] urls = ((URLClassLoader) loader).getURLs();
				Collections.addAll(urlSet, urls);
			}
			loader = loader.getParent();
		}

		String classpath = System.getProperty("java.class.path");
		if (classpath != null && classpath.length() > 0) {
			try {
				URL resource = StartupModule.class.getResource("/");

				if (resource == null) {
					String className = StartupModule.class.getName().replace(
							'.', '/')
							+ ".class";
					resource = StartupModule.class.getResource(className);

					if (resource != null) {
						String url = resource.toExternalForm();
						url = url.substring(0,
								url.length() - className.length());
						resource = new URL(url);
					}
				}

				if (resource != null) {
					classpath = classpath + File.pathSeparator
							+ new File(resource.toURI()).getAbsolutePath();
				}
			} catch (URISyntaxException e) {
				// FIXME ignore for now
			} catch (MalformedURLException e) {
				// FIXME ignore for now
			}

			for (String path : classpath.split(File.pathSeparator)) {
				File file = new File(path);
				try {
					if (file.exists()) {
						urlSet.add(file.toURI().toURL());
					}
				} catch (MalformedURLException e) {
					_logger.log(INFO,
							"Found invalid URL in Classpath: " + path, e);
				}
			}
		}

		return urlSet;
	}

	public StartupModule addFeature(Class<? extends ScannerFeature> listener) {
		_features.add(listener);
		return this;
	}

	public StartupModule bindSystemProperties() {
		bindSystemProperties = true;
		return this;
	}

	public StartupModule disableStartupConfiguration() {
		bindStartupConfiguration = false;
		return this;
	}

	public StartupModule bindEnvironment() {
		bindEnvironment = true;
		return this;
	}

	public void verbose() {
		verbose = true;
	}

	public static StartupModule create(
			Class<? extends ClasspathScanner> scanner, PackageFilter... filter) {
		return new DefaultStartupModule(scanner, filter);
	}

	public static class DefaultStartupModule extends StartupModule {
		public DefaultStartupModule(Class<? extends ClasspathScanner> scanner,
				PackageFilter... filter) {
			super(scanner, filter);
		}

		@Override
		protected Multibinder<ScannerFeature> bindFeatures(Binder binder) {
			Multibinder<ScannerFeature> listeners = Multibinder.newSetBinder(
					binder, ScannerFeature.class);
			listeners.addBinding().to(AutoBindingFeature.class);
			listeners.addBinding().to(ImplementationBindingFeature.class);
			listeners.addBinding().to(MultiBindingFeature.class);
			listeners.addBinding().to(ModuleBindingFeature.class);

			for (Class<? extends ScannerFeature> listener : _features) {
				listeners.addBinding().to(listener);
			}

			return listeners;
		}

		@Override
		protected PackageFilter[] bindPackages() {
			try {
				return new PackageFilter[] { PackageFilter
						.create(Thread
								.currentThread()
								.getContextClassLoader()
								.loadClass(
										"org.apache.onami.autobind.configuration.ConfigurationModule"),
								false) };
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return new PackageFilter[0];
			}
		}
	}

}

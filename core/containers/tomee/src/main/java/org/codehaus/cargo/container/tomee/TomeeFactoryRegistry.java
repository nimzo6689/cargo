/*
 * ========================================================================
 *
 * Codehaus CARGO, copyright 2004-2011 Vincent Massol, 2012-2016 Ali Tokmen.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ========================================================================
 */
package org.codehaus.cargo.container.tomee;

import org.codehaus.cargo.container.ContainerType;
import org.codehaus.cargo.container.configuration.ConfigurationType;
import org.codehaus.cargo.container.deployable.DeployableType;
import org.codehaus.cargo.container.deployer.DeployerType;
import org.codehaus.cargo.container.packager.PackagerType;
import org.codehaus.cargo.container.tomcat.TomcatDirectoryPackager;
import org.codehaus.cargo.container.tomcat.TomcatWAR;
import org.codehaus.cargo.container.tomcat.internal.TomcatExistingLocalConfigurationCapability;
import org.codehaus.cargo.container.tomcat.internal.TomcatRuntimeConfigurationCapability;
import org.codehaus.cargo.container.tomee.internal.TomeeContainerCapability;
import org.codehaus.cargo.container.tomee.internal.TomeeStandaloneLocalConfigurationCapability;
import org.codehaus.cargo.generic.AbstractFactoryRegistry;
import org.codehaus.cargo.generic.ContainerCapabilityFactory;
import org.codehaus.cargo.generic.ContainerFactory;
import org.codehaus.cargo.generic.configuration.ConfigurationCapabilityFactory;
import org.codehaus.cargo.generic.configuration.ConfigurationFactory;
import org.codehaus.cargo.generic.deployable.DeployableFactory;
import org.codehaus.cargo.generic.deployer.DeployerFactory;
import org.codehaus.cargo.generic.packager.PackagerFactory;

/**
 * Registers Tomee support into default factories.
 */
public class TomeeFactoryRegistry extends AbstractFactoryRegistry
{

    /**
     * Register deployable factory.
     * 
     * @param deployableFactory Factory on which to register.
     */
    @Override
    protected void register(DeployableFactory deployableFactory)
    {
        deployableFactory.registerDeployable("tomee1x", DeployableType.WAR, TomcatWAR.class);
        deployableFactory.registerDeployable("tomee7x", DeployableType.WAR, TomcatWAR.class);
    }

    /**
     * Register configuration capabilities.
     * 
     * @param configurationCapabilityFactory Factory on which to register.
     */
    @Override
    protected void register(ConfigurationCapabilityFactory configurationCapabilityFactory)
    {
        configurationCapabilityFactory.registerConfigurationCapability("tomee1x",
            ContainerType.INSTALLED, ConfigurationType.STANDALONE,
            TomeeStandaloneLocalConfigurationCapability.class);
        configurationCapabilityFactory.registerConfigurationCapability("tomee1x",
            ContainerType.INSTALLED, ConfigurationType.EXISTING,
            TomcatExistingLocalConfigurationCapability.class);
        configurationCapabilityFactory.registerConfigurationCapability("tomee1x",
            ContainerType.REMOTE, ConfigurationType.RUNTIME,
            TomcatRuntimeConfigurationCapability.class);

        configurationCapabilityFactory.registerConfigurationCapability("tomee7x",
            ContainerType.INSTALLED, ConfigurationType.STANDALONE,
            TomeeStandaloneLocalConfigurationCapability.class);
        configurationCapabilityFactory.registerConfigurationCapability("tomee7x",
            ContainerType.INSTALLED, ConfigurationType.EXISTING,
            TomcatExistingLocalConfigurationCapability.class);
        configurationCapabilityFactory.registerConfigurationCapability("tomee7x",
            ContainerType.REMOTE, ConfigurationType.RUNTIME,
            TomcatRuntimeConfigurationCapability.class);
    }

    /**
     * Register configuration factories.
     * 
     * @param configurationFactory Factory on which to register.
     */
    @Override
    protected void register(ConfigurationFactory configurationFactory)
    {
        configurationFactory.registerConfiguration("tomee1x",
            ContainerType.INSTALLED, ConfigurationType.STANDALONE,
            Tomee1xStandaloneLocalConfiguration.class);
        configurationFactory.registerConfiguration("tomee1x",
            ContainerType.INSTALLED, ConfigurationType.EXISTING,
            Tomee1xExistingLocalConfiguration.class);
        configurationFactory.registerConfiguration("tomee1x",
            ContainerType.REMOTE, ConfigurationType.RUNTIME,
            TomeeRuntimeConfiguration.class);

        configurationFactory.registerConfiguration("tomee7x",
            ContainerType.INSTALLED, ConfigurationType.STANDALONE,
            Tomee7xStandaloneLocalConfiguration.class);
        configurationFactory.registerConfiguration("tomee7x",
            ContainerType.INSTALLED, ConfigurationType.EXISTING,
            Tomee7xExistingLocalConfiguration.class);
        configurationFactory.registerConfiguration("tomee7x",
            ContainerType.REMOTE, ConfigurationType.RUNTIME,
            TomeeRuntimeConfiguration.class);
    }

    /**
     * Register deployer.
     * 
     * @param deployerFactory Factory on which to register.
     */
    @Override
    protected void register(DeployerFactory deployerFactory)
    {
        deployerFactory.registerDeployer("tomee1x", DeployerType.INSTALLED,
            TomeeCopyingInstalledLocalDeployer.class);
        deployerFactory.registerDeployer("tomee1x", DeployerType.REMOTE,
            Tomee1xRemoteDeployer.class);

        deployerFactory.registerDeployer("tomee7x", DeployerType.INSTALLED,
            TomeeCopyingInstalledLocalDeployer.class);
        deployerFactory.registerDeployer("tomee7x", DeployerType.REMOTE,
            Tomee7xRemoteDeployer.class);
    }

    /**
     * Register packager.
     * 
     * @param packagerFactory Factory on which to register.
     */
    @Override
    protected void register(PackagerFactory packagerFactory)
    {
        packagerFactory.registerPackager("tomee1x", PackagerType.DIRECTORY,
            TomcatDirectoryPackager.class);
        packagerFactory.registerPackager("tomee7x", PackagerType.DIRECTORY,
            TomcatDirectoryPackager.class);
    }

    /**
     * Register container capabilities.
     * 
     * @param containerCapabilityFactory Factory on which to register.
     */
    @Override
    protected void register(ContainerCapabilityFactory containerCapabilityFactory)
    {
        containerCapabilityFactory.registerContainerCapability("tomee1x",
            TomeeContainerCapability.class);
        containerCapabilityFactory.registerContainerCapability("tomee7x",
            TomeeContainerCapability.class);
    }

    /**
     * Register container.
     * 
     * @param containerFactory Factory on which to register.
     */
    @Override
    protected void register(ContainerFactory containerFactory)
    {
        containerFactory.registerContainer("tomee1x", ContainerType.INSTALLED,
            Tomee1xInstalledLocalContainer.class);
        containerFactory.registerContainer("tomee1x", ContainerType.REMOTE,
            Tomee1xRemoteContainer.class);

        containerFactory.registerContainer("tomee7x", ContainerType.INSTALLED,
            Tomee7xInstalledLocalContainer.class);
        containerFactory.registerContainer("tomee7x", ContainerType.REMOTE,
            Tomee7xRemoteContainer.class);
    }
}

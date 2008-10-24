/* 
 * ========================================================================
 * 
 * Copyright 2004-2008 Vincent Massol.
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
package org.codehaus.cargo.container.tomcat;

import java.io.File;
import java.util.Iterator;
import java.util.Set;

import org.apache.tools.ant.taskdefs.Copy;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.FilterChain;
import org.codehaus.cargo.container.LocalContainer;
import org.codehaus.cargo.container.InstalledLocalContainer;
import org.codehaus.cargo.container.EmbeddedLocalContainer;
import org.codehaus.cargo.container.property.DatasourcePropertySet;
import org.codehaus.cargo.container.property.DataSource;
import org.codehaus.cargo.container.resource.Resource;
import org.codehaus.cargo.container.tomcat.internal.AbstractCatalinaStandaloneLocalConfiguration;

/**
 * Catalina standalone {@link org.codehaus.cargo.container.spi.configuration.ContainerConfiguration}
 * implementation.
 *
 * @version $Id$
 */
public class Tomcat6xStandaloneLocalConfiguration
    extends AbstractCatalinaStandaloneLocalConfiguration
{
    /**
     * {@inheritDoc}
     * @see AbstractCatalinaStandaloneLocalConfiguration#AbstractCatalinaStandaloneLocalConfiguration(String)
     */
    public Tomcat6xStandaloneLocalConfiguration(String dir)
    {
        super(dir);
        setProperty(TomcatPropertySet.CONNECTOR_EMPTY_SESSION_PATH, "true");
    }

    /**
     * {@inheritDoc}
     * @see AbstractCatalinaStandaloneLocalConfiguration#setupManager(org.codehaus.cargo.container.LocalContainer)
     */
    protected void setupManager(LocalContainer container)
    {
        if (container instanceof EmbeddedLocalContainer)
        {
            // when running in the embedded mode, there's no need
            // of any manager application.
        }
        else
        {
            Copy copy = (Copy) getAntUtils().createAntTask("copy");

            FileSet fileSet = new FileSet();
            fileSet.setDir(new File(((InstalledLocalContainer) container).getHome()));
            fileSet.createInclude().setName("conf/Catalina/localhost/manager.xml");
            fileSet.createInclude().setName("server/lib/catalina.jar");
            fileSet.createInclude().setName("server/webapps/manager/**");
            copy.addFileset(fileSet);

            copy.setTodir(new File(getHome()));

            copy.execute();
        }
    }

    /**
     * @return the XML to be put into the <code>server.xml</code> file
     */
    protected String createDatasourceTokenValue()
    {
        getLogger().debug("Tomcat 6x createDatasourceTokenValue", this.getClass().getName());

        final String dataSourceProperty = getPropertyValue(DatasourcePropertySet.DATASOURCE);
        getLogger().debug("Datasource property value [" + dataSourceProperty + "]",
            this.getClass().getName());

        if (dataSourceProperty == null)
        {
            // have to return a non-empty string, as Ant's token stuff doesn't work otherwise
            return " ";
        }
        else
        {
            DataSource ds = new DataSource(dataSourceProperty);
            return
                "<Resource name='" + ds.getJndiLocation() + "'\n"
                    + "    auth='Container'\n"
                    + "    type='" + ds.getDataSourceType() + "'\n"
                    + "    username='" + ds.getUsername() + "'\n"
                    + "    password='" + ds.getPassword() + "'\n"
                    + "    driverClassName='" + ds.getDriverClass() + "'\n"
                    + "    url='" + ds.getUrl() + "'\n"
                    + "/>\n"
                    // As we are using a database - we will likely need a transaction factory too.
                    + "<Resource jotm.timeout='60' " 
                    + "    factory='org.objectweb.jotm.UserTransactionFactory' "
                    + "    name='UserTransaction' "
                    + "    type='javax.transaction.UserTransaction' "
                    + "    auth='Container'>\n"
                    + "</Resource>";
        }
    }


    /**
     * @return the XML to be put into the server.xml file.
     */
    protected String createResourceTokenValue()
    {
        getLogger().debug("Tomcat 6x createResourceTokenValue", this.getClass().getName());

        String out = "";
        Iterator it = getResources().iterator();

        while (it.hasNext())
        {
            Resource r = (Resource) it.next();
            out = out + "<Resource name=\"" + r.getName() + "\"\n" + "          type=\""
                    + r.getType() + "\"\n";
            Set parameterNames = r.getParameterNames();
            Iterator pit = parameterNames.iterator();
            while (pit.hasNext())
            {
                String pName = (String) pit.next();
                out = out + "          " + pName + "=\"" + r.getParameter(pName) + "\"\n";
            }
            out = out + "/>\n";
        }
        return out;
    }

    
    /**
     * Configure the emptySessionPath property token on the filter chain for the
     * server.xml configuration file.
     *
     * {@inheritDoc}
     * @see AbstractCatalinaStandaloneLocalConfiguration#createTomcatFilterChain()
     */
    protected FilterChain createTomcatFilterChain()
    {
        FilterChain filterChain = super.createTomcatFilterChain();

        getAntUtils().addTokenToFilterChain(filterChain, "catalina.connector.emptySessionPath",
            getPropertyValue(TomcatPropertySet.CONNECTOR_EMPTY_SESSION_PATH));

        return filterChain;
    }

    /**
     * {@inheritDoc}
     * @see Object#toString()
     */
    public String toString()
    {
        return "Tomcat 6.x Standalone Configuration";
    }
}

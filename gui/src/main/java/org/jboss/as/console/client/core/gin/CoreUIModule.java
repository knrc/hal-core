
/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @author tags. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */

package org.jboss.as.console.client.core.gin;

import com.google.gwt.event.shared.SimpleEventBus;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.RootPresenter;
import com.gwtplatform.mvp.client.annotations.GaAccount;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import com.gwtplatform.mvp.client.googleanalytics.GoogleAnalytics;
import com.gwtplatform.mvp.client.proxy.Gatekeeper;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.TokenFormatter;
import org.jboss.as.console.client.administration.AdministrationPresenter;
import org.jboss.as.console.client.administration.AdministrationView;
import org.jboss.as.console.client.administration.audit.AuditLogPresenter;
import org.jboss.as.console.client.administration.audit.AuditLogView;
import org.jboss.as.console.client.administration.role.RoleAssignementView;
import org.jboss.as.console.client.administration.role.RoleAssignmentPresenter;
import org.jboss.as.console.client.analytics.AnalyticsProvider;
import org.jboss.as.console.client.analytics.NavigationTracker;
import org.jboss.as.console.client.auth.CurrentUser;
import org.jboss.as.console.client.auth.SignInPagePresenter;
import org.jboss.as.console.client.auth.SignInPageView;
import org.jboss.as.console.client.core.ApplicationProperties;
import org.jboss.as.console.client.core.BootstrapContext;
import org.jboss.as.console.client.core.DefaultPlaceManager;
import org.jboss.as.console.client.core.Footer;
import org.jboss.as.console.client.core.Header;
import org.jboss.as.console.client.core.MainLayoutPresenter;
import org.jboss.as.console.client.core.MainLayoutViewImpl;
import org.jboss.as.console.client.core.NewTokenFormatter;
import org.jboss.as.console.client.core.message.MessageBar;
import org.jboss.as.console.client.core.message.MessageCenter;
import org.jboss.as.console.client.core.message.MessageCenterImpl;
import org.jboss.as.console.client.core.message.MessageCenterView;
import org.jboss.as.console.client.core.settings.ModelVersions;
import org.jboss.as.console.client.core.settings.SettingsPresenter;
import org.jboss.as.console.client.core.settings.SettingsPresenterViewImpl;
import org.jboss.as.console.client.core.settings.SettingsPresenterWidget;
import org.jboss.as.console.client.core.settings.SettingsView;
import org.jboss.as.console.client.domain.groups.ServerGroupPresenter;
import org.jboss.as.console.client.domain.groups.ServerGroupView;
import org.jboss.as.console.client.domain.groups.deployment.DomainDeploymentPresenter;
import org.jboss.as.console.client.domain.groups.deployment.DomainDeploymentView;
import org.jboss.as.console.client.domain.hosts.HostMgmtPresenter;
import org.jboss.as.console.client.domain.hosts.HostMgmtView;
import org.jboss.as.console.client.domain.hosts.HostVMMetricPresenter;
import org.jboss.as.console.client.domain.hosts.HostVMMetricView;
import org.jboss.as.console.client.domain.hosts.ServerConfigPresenter;
import org.jboss.as.console.client.domain.hosts.ServerConfigView;
import org.jboss.as.console.client.domain.hosts.general.HostInterfacesPresenter;
import org.jboss.as.console.client.domain.hosts.general.HostInterfacesView;
import org.jboss.as.console.client.domain.hosts.general.HostJVMPresenter;
import org.jboss.as.console.client.domain.hosts.general.HostJVMView;
import org.jboss.as.console.client.domain.hosts.general.HostPropertiesPresenter;
import org.jboss.as.console.client.domain.hosts.general.HostPropertiesView;
import org.jboss.as.console.client.domain.model.HostInformationStore;
import org.jboss.as.console.client.domain.model.ProfileStore;
import org.jboss.as.console.client.domain.model.ServerGroupStore;
import org.jboss.as.console.client.domain.model.impl.HostInfoStoreImpl;
import org.jboss.as.console.client.domain.model.impl.ProfileStoreImpl;
import org.jboss.as.console.client.domain.model.impl.ServerGroupStoreImpl;
import org.jboss.as.console.client.domain.profiles.CurrentProfileSelection;
import org.jboss.as.console.client.domain.profiles.ProfileMgmtPresenter;
import org.jboss.as.console.client.domain.profiles.ProfileMgmtView;
import org.jboss.as.console.client.domain.runtime.DomainRuntimePresenter;
import org.jboss.as.console.client.domain.runtime.DomainRuntimeView;
import org.jboss.as.console.client.domain.topology.TopologyPresenter;
import org.jboss.as.console.client.domain.topology.TopologyView;
import org.jboss.as.console.client.plugins.AccessControlRegistry;
import org.jboss.as.console.client.plugins.AccessControlRegistryImpl;
import org.jboss.as.console.client.plugins.RuntimeExtensionRegistry;
import org.jboss.as.console.client.plugins.RuntimeLHSItemExtensionRegistryImpl;
import org.jboss.as.console.client.plugins.SubsystemRegistry;
import org.jboss.as.console.client.plugins.SubsystemRegistryImpl;
import org.jboss.as.console.client.rbac.RBACGatekeeper;
import org.jboss.as.console.client.rbac.SecurityFramework;
import org.jboss.as.console.client.rbac.SecurityFrameworkImpl;
import org.jboss.as.console.client.rbac.UnauthorisedPresenter;
import org.jboss.as.console.client.rbac.UnauthorisedView;
import org.jboss.as.console.client.shared.deployment.DeploymentStore;
import org.jboss.as.console.client.shared.expr.DefaultExpressionResolver;
import org.jboss.as.console.client.shared.expr.ExpressionResolver;
import org.jboss.as.console.client.shared.general.InterfacePresenter;
import org.jboss.as.console.client.shared.general.InterfaceView;
import org.jboss.as.console.client.shared.general.PathManagementPresenter;
import org.jboss.as.console.client.shared.general.PathManagementView;
import org.jboss.as.console.client.shared.general.PropertiesPresenter;
import org.jboss.as.console.client.shared.general.PropertiesView;
import org.jboss.as.console.client.shared.general.SocketBindingPresenter;
import org.jboss.as.console.client.shared.general.SocketBindingView;
import org.jboss.as.console.client.shared.help.HelpSystem;
import org.jboss.as.console.client.shared.model.SubsystemStore;
import org.jboss.as.console.client.shared.model.SubsystemStoreImpl;
import org.jboss.as.console.client.shared.runtime.RuntimeBaseAddress;
import org.jboss.as.console.client.shared.runtime.ds.DataSourceMetricPresenter;
import org.jboss.as.console.client.shared.runtime.ds.DataSourceMetricView;
import org.jboss.as.console.client.shared.runtime.env.EnvironmentPresenter;
import org.jboss.as.console.client.shared.runtime.env.EnvironmentView;
import org.jboss.as.console.client.shared.runtime.jms.JMSMetricPresenter;
import org.jboss.as.console.client.shared.runtime.jms.JMSMetricView;
import org.jboss.as.console.client.shared.runtime.jpa.JPAMetricPresenter;
import org.jboss.as.console.client.shared.runtime.jpa.JPAMetricsView;
import org.jboss.as.console.client.shared.runtime.naming.JndiPresenter;
import org.jboss.as.console.client.shared.runtime.naming.JndiView;
import org.jboss.as.console.client.shared.runtime.tx.TXLogPresenter;
import org.jboss.as.console.client.shared.runtime.tx.TXLogView;
import org.jboss.as.console.client.shared.runtime.tx.TXMetricPresenter;
import org.jboss.as.console.client.shared.runtime.tx.TXMetricViewImpl;
import org.jboss.as.console.client.shared.runtime.web.WebMetricPresenter;
import org.jboss.as.console.client.shared.runtime.web.WebMetricView;
import org.jboss.as.console.client.shared.runtime.ws.WebServiceRuntimePresenter;
import org.jboss.as.console.client.shared.runtime.ws.WebServiceRuntimeView;
import org.jboss.as.console.client.shared.state.DomainEntityManager;
import org.jboss.as.console.client.shared.state.ReloadState;
import org.jboss.as.console.client.shared.state.ResponseProcessorFactory;
import org.jboss.as.console.client.shared.subsys.Baseadress;
import org.jboss.as.console.client.shared.subsys.configadmin.ConfigAdminPresenter;
import org.jboss.as.console.client.shared.subsys.configadmin.ConfigAdminView;
import org.jboss.as.console.client.shared.subsys.deploymentscanner.ScannerPresenter;
import org.jboss.as.console.client.shared.subsys.deploymentscanner.ScannerView;
import org.jboss.as.console.client.shared.subsys.ejb3.EEPresenter;
import org.jboss.as.console.client.shared.subsys.ejb3.EESubsystemView;
import org.jboss.as.console.client.shared.subsys.ejb3.EJB3Presenter;
import org.jboss.as.console.client.shared.subsys.ejb3.EJB3View;
import org.jboss.as.console.client.shared.subsys.infinispan.CacheContainerPresenter;
import org.jboss.as.console.client.shared.subsys.infinispan.CacheContainerView;
import org.jboss.as.console.client.shared.subsys.infinispan.DistributedCachePresenter;
import org.jboss.as.console.client.shared.subsys.infinispan.DistributedCacheView;
import org.jboss.as.console.client.shared.subsys.infinispan.InvalidationCachePresenter;
import org.jboss.as.console.client.shared.subsys.infinispan.InvalidationCacheView;
import org.jboss.as.console.client.shared.subsys.infinispan.LocalCachePresenter;
import org.jboss.as.console.client.shared.subsys.infinispan.LocalCacheView;
import org.jboss.as.console.client.shared.subsys.infinispan.ReplicatedCachePresenter;
import org.jboss.as.console.client.shared.subsys.infinispan.ReplicatedCacheView;
import org.jboss.as.console.client.shared.subsys.infinispan.model.CacheContainerStore;
import org.jboss.as.console.client.shared.subsys.infinispan.model.CacheContainerStoreImpl;
import org.jboss.as.console.client.shared.subsys.infinispan.model.LocalCacheStore;
import org.jboss.as.console.client.shared.subsys.infinispan.model.LocalCacheStoreImpl;
import org.jboss.as.console.client.shared.subsys.jacorb.JacOrbPresenter;
import org.jboss.as.console.client.shared.subsys.jacorb.JacOrbView;
import org.jboss.as.console.client.shared.subsys.jca.DataSourcePresenter;
import org.jboss.as.console.client.shared.subsys.jca.DatasourceView;
import org.jboss.as.console.client.shared.subsys.jca.JcaPresenter;
import org.jboss.as.console.client.shared.subsys.jca.JcaSubsystemView;
import org.jboss.as.console.client.shared.subsys.jca.ResourceAdapterPresenter;
import org.jboss.as.console.client.shared.subsys.jca.ResourceAdapterView;
import org.jboss.as.console.client.shared.subsys.jca.model.DataSourceStore;
import org.jboss.as.console.client.shared.subsys.jca.model.DataSourceStoreImpl;
import org.jboss.as.console.client.shared.subsys.jca.model.DomainDriverStrategy;
import org.jboss.as.console.client.shared.subsys.jca.model.StandaloneDriverStrategy;
import org.jboss.as.console.client.shared.subsys.jgroups.JGroupsPresenter;
import org.jboss.as.console.client.shared.subsys.jgroups.JGroupsSubsystemView;
import org.jboss.as.console.client.shared.subsys.jmx.JMXPresenter;
import org.jboss.as.console.client.shared.subsys.jmx.JMXSubsystemView;
import org.jboss.as.console.client.shared.subsys.jpa.JpaPresenter;
import org.jboss.as.console.client.shared.subsys.jpa.JpaView;
import org.jboss.as.console.client.shared.subsys.logging.HandlerListManager;
import org.jboss.as.console.client.shared.subsys.logging.LoggingPresenter;
import org.jboss.as.console.client.shared.subsys.logging.LoggingView;
import org.jboss.as.console.client.shared.subsys.mail.MailPresenter;
import org.jboss.as.console.client.shared.subsys.mail.MailSubsystemView;
import org.jboss.as.console.client.shared.subsys.messaging.MsgDestinationsPresenter;
import org.jboss.as.console.client.shared.subsys.messaging.MsgDestinationsView;
import org.jboss.as.console.client.shared.subsys.messaging.cluster.MsgClusteringPresenter;
import org.jboss.as.console.client.shared.subsys.messaging.cluster.MsgClusteringView;
import org.jboss.as.console.client.shared.subsys.messaging.connections.MsgConnectionsPresenter;
import org.jboss.as.console.client.shared.subsys.messaging.connections.MsgConnectionsView;
import org.jboss.as.console.client.shared.subsys.modcluster.ModclusterPresenter;
import org.jboss.as.console.client.shared.subsys.modcluster.ModclusterView;
import org.jboss.as.console.client.shared.subsys.osgi.config.OSGiConfigurationPresenter;
import org.jboss.as.console.client.shared.subsys.osgi.config.OSGiSubsystemView;
import org.jboss.as.console.client.shared.subsys.osgi.runtime.OSGiRuntimePresenter;
import org.jboss.as.console.client.shared.subsys.osgi.runtime.OSGiRuntimeView;
import org.jboss.as.console.client.shared.subsys.security.SecurityDomainsPresenter;
import org.jboss.as.console.client.shared.subsys.security.SecurityDomainsView;
import org.jboss.as.console.client.shared.subsys.security.SecuritySubsystemPresenter;
import org.jboss.as.console.client.shared.subsys.security.SecuritySubsystemView;
import org.jboss.as.console.client.shared.subsys.threads.ThreadsPresenter;
import org.jboss.as.console.client.shared.subsys.threads.ThreadsView;
import org.jboss.as.console.client.shared.subsys.web.WebPresenter;
import org.jboss.as.console.client.shared.subsys.web.WebSubsystemView;
import org.jboss.as.console.client.shared.subsys.ws.DomainEndpointStrategy;
import org.jboss.as.console.client.shared.subsys.ws.EndpointRegistry;
import org.jboss.as.console.client.shared.subsys.ws.StandaloneEndpointStrategy;
import org.jboss.as.console.client.shared.subsys.ws.WebServicePresenter;
import org.jboss.as.console.client.shared.subsys.ws.WebServiceView;
import org.jboss.as.console.client.standalone.ServerMgmtApplicationPresenter;
import org.jboss.as.console.client.standalone.ServerMgmtApplicationView;
import org.jboss.as.console.client.standalone.StandaloneServerPresenter;
import org.jboss.as.console.client.standalone.StandaloneServerView;
import org.jboss.as.console.client.standalone.deployment.StandaloneDeploymentPresenter;
import org.jboss.as.console.client.standalone.deployment.StandaloneDeploymentView;
import org.jboss.as.console.client.standalone.runtime.StandaloneRuntimePresenter;
import org.jboss.as.console.client.standalone.runtime.StandaloneRuntimeView;
import org.jboss.as.console.client.standalone.runtime.VMMetricsPresenter;
import org.jboss.as.console.client.standalone.runtime.VMMetricsView;
import org.jboss.as.console.client.tools.BrowserPresenter;
import org.jboss.as.console.client.tools.BrowserView;
import org.jboss.as.console.client.tools.ToolsPresenter;
import org.jboss.as.console.client.tools.ToolsView;
import org.jboss.as.console.client.tools.modelling.workbench.ApplicationPresenter;
import org.jboss.as.console.client.tools.modelling.workbench.ApplicationView;
import org.jboss.as.console.client.tools.modelling.workbench.FooterPresenter;
import org.jboss.as.console.client.tools.modelling.workbench.FooterView;
import org.jboss.as.console.client.tools.modelling.workbench.HeaderPresenter;
import org.jboss.as.console.client.tools.modelling.workbench.HeaderView;
import org.jboss.as.console.client.tools.modelling.workbench.context.ContextPresenter;
import org.jboss.as.console.client.tools.modelling.workbench.context.ContextView;
import org.jboss.as.console.client.tools.modelling.workbench.preview.PreviewPresenter;
import org.jboss.as.console.client.tools.modelling.workbench.preview.PreviewView;
import org.jboss.as.console.client.tools.modelling.workbench.repository.RepositoryPresenter;
import org.jboss.as.console.client.tools.modelling.workbench.repository.RepositoryView;
import org.jboss.as.console.client.tools.modelling.workbench.repository.SampleRepository;
import org.jboss.as.console.client.widgets.forms.ApplicationMetaData;
import org.jboss.as.console.spi.GinExtensionBinding;
import org.jboss.dmr.client.dispatch.DispatchAsync;
import org.jboss.dmr.client.dispatch.HandlerMapping;
import org.jboss.dmr.client.dispatch.impl.DMRHandler;
import org.jboss.dmr.client.dispatch.impl.DispatchAsyncImpl;
import org.jboss.dmr.client.dispatch.impl.HandlerRegistry;

/**
 * Provides the bindings for the core UI widgets.
 *
 * @author Heiko Braun
 * @date 1/31/11
 */
@GinExtensionBinding
public class CoreUIModule extends AbstractPresenterModule {

    protected void configure() {

        // SPI first
        bind(SubsystemRegistry.class).to(SubsystemRegistryImpl.class).in(Singleton.class);
        bind(RuntimeExtensionRegistry.class).to(RuntimeLHSItemExtensionRegistryImpl.class).in(Singleton.class);

        // static injections
        requestStaticInjection(RuntimeBaseAddress.class);
        requestStaticInjection(Baseadress.class);
        requestStaticInjection(ResponseProcessorFactory.class);


        // main layout
        bind(Header.class).in(Singleton.class);
        bind(Footer.class).in(Singleton.class);

        // supporting components
        bind(MessageBar.class).in(Singleton.class);
        bind(MessageCenter.class).to(MessageCenterImpl.class).in(Singleton.class);
        bind(MessageCenterView.class).in(Singleton.class);

        bind(HelpSystem.class).in(Singleton.class);

        bind(ExpressionResolver.class).to(DefaultExpressionResolver.class).in(Singleton.class);
        bind(Baseadress.class).in(Singleton.class);
        bind(RuntimeBaseAddress.class).in(Singleton.class);

        // mobile:
        // bindConstant().annotatedWith(GaAccount.class).to("UA-36590267-1");

        bindConstant().annotatedWith(GaAccount.class).to("UA-35829315-1");

        bind(GoogleAnalytics.class).toProvider(AnalyticsProvider.class).in(Singleton.class);
        bind(NavigationTracker.class).asEagerSingleton();

        bind(ModelVersions.class).in(Singleton.class);

        // ----------------------------------------------------------------------

        //TODO: bind(EventBus.class).to(DebugEventBus.class).in(Singleton.class);
        bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);

        bind(PlaceManager.class).to(DefaultPlaceManager.class).in(Singleton.class);

        // see http://code.google.com/p/gwt-platform/issues/detail?id=381
        //bind(TokenFormatter.class).to(ParameterTokenFormatter.class).in(Singleton.class);
        bind(TokenFormatter.class).to(NewTokenFormatter.class).in(Singleton.class);

        bind(RootPresenter.class).asEagerSingleton();
        //bind(ProxyFailureHandler.class).to(DefaultProxyFailureHandler.class).in(Singleton.class);

        bind(Gatekeeper.class).to(RBACGatekeeper.class).in(Singleton.class);

        bind(CurrentUser.class).in(Singleton.class);
        bind(BootstrapContext.class).in(Singleton.class);
        bind(ApplicationProperties.class).to(BootstrapContext.class).in(Singleton.class);
        bind(ApplicationMetaData.class).in(Singleton.class);

        bind(DomainEntityManager.class).in(Singleton.class);

        // sign in
        bindPresenter(SignInPagePresenter.class, SignInPagePresenter.MyView.class,
                SignInPageView.class, SignInPagePresenter.MyProxy.class);

        // main layout
        bindPresenter(MainLayoutPresenter.class,
                MainLayoutPresenter.MainLayoutView.class,
                MainLayoutViewImpl.class,
                MainLayoutPresenter.MainLayoutProxy.class);

        // tools
        bindPresenter(ToolsPresenter.class,
                ToolsPresenter.MyView.class,
                ToolsView.class,
                ToolsPresenter.MyProxy.class);

        bindPresenterWidget(BrowserPresenter.class,
                BrowserPresenter.MyView.class,
                BrowserView.class
        );

        /*bindPresenterWidget(DebugPresenter.class,
                DebugPresenter.MyView.class,
                DebugPresenterView.class
        ); */

        bindPresenter(SettingsPresenter.class,
                SettingsPresenter.MyView.class,
                SettingsPresenterViewImpl.class,
                SettingsPresenter.MyProxy.class);

        bindPresenterWidget(SettingsPresenterWidget.class,
                SettingsPresenterWidget.MyView.class,
                SettingsView.class
        );

        // ----------------------------------------------------------------------

        bind(DispatchAsync.class).to(DispatchAsyncImpl.class).in(Singleton.class);
        bind(HandlerMapping.class).to(HandlerRegistry.class).in(Singleton.class);
        bind(DMRHandler.class).in(Singleton.class);

        // ----------------------------------------------------------------------

        // server management application

        bindPresenter(ServerMgmtApplicationPresenter.class,
                ServerMgmtApplicationPresenter.ServerManagementView.class,
                ServerMgmtApplicationView.class,
                ServerMgmtApplicationPresenter.ServerManagementProxy.class);

        bindPresenter(StandaloneDeploymentPresenter.class,
                StandaloneDeploymentPresenter.MyView.class,
                StandaloneDeploymentView.class,
                StandaloneDeploymentPresenter.MyProxy.class);

        bind(DeploymentStore.class).in(Singleton.class);

        // ------------------------------------------------
        // domain management application

        bindPresenter(InterfacePresenter.class,
                InterfacePresenter.MyView.class,
                InterfaceView.class,
                InterfacePresenter.MyProxy.class);

        bindPresenter(PropertiesPresenter.class,
                PropertiesPresenter.MyView.class,
                PropertiesView.class,
                PropertiesPresenter.MyProxy.class);

        bindPresenter(HostPropertiesPresenter.class,
                HostPropertiesPresenter.MyView.class,
                HostPropertiesView.class,
                HostPropertiesPresenter.MyProxy.class);

        bindPresenter(HostInterfacesPresenter.class,
                HostInterfacesPresenter.MyView.class,
                HostInterfacesView.class,
                HostInterfacesPresenter.MyProxy.class);

        bindPresenter(HostJVMPresenter.class,
                HostJVMPresenter.MyView.class,
                HostJVMView.class,
                HostJVMPresenter.MyProxy.class);

        // profile management application
        bindPresenter(ProfileMgmtPresenter.class,
                ProfileMgmtPresenter.MyView.class,
                ProfileMgmtView.class,
                ProfileMgmtPresenter.MyProxy.class);


        bindPresenter(TopologyPresenter.class,
                TopologyPresenter.MyView.class,
                TopologyView.class,
                TopologyPresenter.MyProxy.class);

        bind(CurrentProfileSelection.class).in(Singleton.class);
        bind(ReloadState.class).in(Singleton.class);

        // domain/server-group
        bindPresenter(ServerGroupPresenter.class,
                ServerGroupPresenter.MyView.class,
                ServerGroupView.class,
                ServerGroupPresenter.MyProxy.class);

        bind(ProfileStore.class).to(ProfileStoreImpl.class).in(Singleton.class);
        bind(SubsystemStore.class).to(SubsystemStoreImpl.class).in(Singleton.class);
        bind(ServerGroupStore.class).to(ServerGroupStoreImpl.class).in(Singleton.class);
        bind(HostInformationStore.class).to(HostInfoStoreImpl.class).in(Singleton.class);

        // domain/domain-deployments
        bindPresenter(DomainDeploymentPresenter.class,
                DomainDeploymentPresenter.MyView.class,
                DomainDeploymentView.class,
                DomainDeploymentPresenter.MyProxy.class);


        bindPresenter(HostMgmtPresenter.class,
                HostMgmtPresenter.MyView.class,
                HostMgmtView.class,
                HostMgmtPresenter.MyProxy.class);

        bindPresenter(ServerConfigPresenter.class,
                ServerConfigPresenter.MyView.class,
                ServerConfigView.class,
                ServerConfigPresenter.MyProxy.class);



        // -------

        bindPresenter(DataSourcePresenter.class,
                DataSourcePresenter.MyView.class,
                DatasourceView.class,
                DataSourcePresenter.MyProxy.class);

        bind(DataSourceStore.class).to(DataSourceStoreImpl.class).in(Singleton.class);
        bind(DomainDriverStrategy.class).in(Singleton.class);
        bind(StandaloneDriverStrategy.class).in(Singleton.class);

        bindPresenter(EJB3Presenter.class,
                EJB3Presenter.MyView.class,
                EJB3View.class,
                EJB3Presenter.MyProxy.class);

        bindPresenter(MsgDestinationsPresenter.class,
                MsgDestinationsPresenter.MyView.class,
                MsgDestinationsView.class,
                MsgDestinationsPresenter.MyProxy.class);

        bindPresenter(MsgConnectionsPresenter.class,
                MsgConnectionsPresenter.MyView.class,
                MsgConnectionsView.class,
                MsgConnectionsPresenter.MyProxy.class);

        bindPresenter(MsgClusteringPresenter.class,
                MsgClusteringPresenter.MyView.class,
                MsgClusteringView.class,
                MsgClusteringPresenter.MyProxy.class);

        bindPresenter(LoggingPresenter.class,
                LoggingPresenter.MyView.class,
                LoggingView.class,
                LoggingPresenter.MyProxy.class);

        bind(HandlerListManager.class).in(Singleton.class);

        bindPresenter(ScannerPresenter.class,
                ScannerPresenter.MyView.class,
                ScannerView.class,
                ScannerPresenter.MyProxy.class);

        bindPresenter(ThreadsPresenter.class,
                ThreadsPresenter.MyView.class,
                ThreadsView.class,
                ThreadsPresenter.MyProxy.class);

        bindPresenter(ConfigAdminPresenter.class,
                ConfigAdminPresenter.MyView.class,
                ConfigAdminView.class,
                ConfigAdminPresenter.MyProxy.class);

        bindPresenter(OSGiConfigurationPresenter.class,
                OSGiConfigurationPresenter.MyView.class,
                OSGiSubsystemView.class,
                OSGiConfigurationPresenter.MyProxy.class);

        bindPresenter(OSGiRuntimePresenter.class,
                OSGiRuntimePresenter.MyView.class,
                OSGiRuntimeView.class,
                OSGiRuntimePresenter.MyProxy.class);

        // Infinispan
        bindPresenter(CacheContainerPresenter.class,
                CacheContainerPresenter.MyView.class,
                CacheContainerView.class,
                CacheContainerPresenter.MyProxy.class);
        bindPresenter(LocalCachePresenter.class,
                LocalCachePresenter.MyView.class,
                LocalCacheView.class,
                LocalCachePresenter.MyProxy.class);
        bindPresenter(InvalidationCachePresenter.class,
                InvalidationCachePresenter.MyView.class,
                InvalidationCacheView.class,
                InvalidationCachePresenter.MyProxy.class);
        bindPresenter(ReplicatedCachePresenter.class,
                ReplicatedCachePresenter.MyView.class,
                ReplicatedCacheView.class,
                ReplicatedCachePresenter.MyProxy.class);
        bindPresenter(DistributedCachePresenter.class,
                DistributedCachePresenter.MyView.class,
                DistributedCacheView.class,
                DistributedCachePresenter.MyProxy.class);


        bindPresenter(SocketBindingPresenter.class,
                SocketBindingPresenter.MyView.class,
                SocketBindingView.class,
                SocketBindingPresenter.MyProxy.class);

        bindPresenter(WebPresenter.class,
                WebPresenter.MyView.class,
                WebSubsystemView.class,
                WebPresenter.MyProxy.class);

        bindPresenter(StandaloneServerPresenter.class,
                StandaloneServerPresenter.MyView.class,
                StandaloneServerView.class,
                StandaloneServerPresenter.MyProxy.class);

        bindPresenter(WebServicePresenter.class,
                WebServicePresenter.MyView.class,
                WebServiceView.class,
                WebServicePresenter.MyProxy.class);

        bindPresenter(WebServiceRuntimePresenter.class,
                WebServiceRuntimePresenter.MyView.class,
                WebServiceRuntimeView.class,
                WebServiceRuntimePresenter.MyProxy.class);

        bind(CacheContainerStore.class).to(CacheContainerStoreImpl.class).in(Singleton.class);
        bind(LocalCacheStore.class).to(LocalCacheStoreImpl.class).in(Singleton.class);
        bind(EndpointRegistry.class).in(Singleton.class);
        bind(DomainEndpointStrategy.class).in(Singleton.class);
        bind(StandaloneEndpointStrategy.class).in(Singleton.class);

        bindPresenter(ResourceAdapterPresenter.class,
                ResourceAdapterPresenter.MyView.class,
                ResourceAdapterView.class,
                ResourceAdapterPresenter.MyProxy.class);

        bindPresenter(JndiPresenter.class,
                JndiPresenter.MyView.class,
                JndiView.class,
                JndiPresenter.MyProxy.class);

        bindPresenter(VMMetricsPresenter.class,
                VMMetricsPresenter.MyView.class,
                VMMetricsView.class,
                VMMetricsPresenter.MyProxy.class);

        bindPresenter(HostVMMetricPresenter.class,
                HostVMMetricPresenter.MyView.class,
                HostVMMetricView.class,
                HostVMMetricPresenter.MyProxy.class);

        bindPresenter(SecuritySubsystemPresenter.class,
                SecuritySubsystemPresenter.MyView.class,
                SecuritySubsystemView.class,
                SecuritySubsystemPresenter.MyProxy.class);

        bindPresenter(SecurityDomainsPresenter.class,
                SecurityDomainsPresenter.MyView.class,
                SecurityDomainsView.class,
                SecurityDomainsPresenter.MyProxy.class);

        bindPresenter(StandaloneRuntimePresenter.class,
                StandaloneRuntimePresenter.MyView.class,
                StandaloneRuntimeView.class,
                StandaloneRuntimePresenter.MyProxy.class);

        bindPresenter(DomainRuntimePresenter.class,
                DomainRuntimePresenter.MyView.class,
                DomainRuntimeView.class,
                DomainRuntimePresenter.MyProxy.class);

        bindPresenter(TXMetricPresenter.class,
                TXMetricPresenter.MyView.class,
                TXMetricViewImpl.class,
                TXMetricPresenter.MyProxy.class);

        bindPresenter(TXLogPresenter.class,
                TXLogPresenter.MyView.class,
                TXLogView.class,
                TXLogPresenter.MyProxy.class);

        bindPresenter(JpaPresenter.class,
                JpaPresenter.MyView.class,
                JpaView.class,
                JpaPresenter.MyProxy.class);

        bindPresenter(JacOrbPresenter.class,
                JacOrbPresenter.MyView.class,
                JacOrbView.class,
                JacOrbPresenter.MyProxy.class);

        bindPresenter(MailPresenter.class,
                MailPresenter.MyView.class,
                MailSubsystemView.class,
                MailPresenter.MyProxy.class);

        bindPresenter(ModclusterPresenter.class,
                ModclusterPresenter.MyView.class,
                ModclusterView.class,
                ModclusterPresenter.MyProxy.class);

        bindPresenter(JMXPresenter.class,
                JMXPresenter.MyView.class,
                JMXSubsystemView.class,
                JMXPresenter.MyProxy.class);

        bindPresenter(EEPresenter.class,
                EEPresenter.MyView.class,
                EESubsystemView.class,
                EEPresenter.MyProxy.class);

        bindPresenter(JcaPresenter.class,
                JcaPresenter.MyView.class,
                JcaSubsystemView.class,
                JcaPresenter.MyProxy.class);

        bindPresenter(WebMetricPresenter.class,
                WebMetricPresenter.MyView.class,
                WebMetricView.class,
                WebMetricPresenter.MyProxy.class);

        bindPresenter(JMSMetricPresenter.class,
                JMSMetricPresenter.MyView.class,
                JMSMetricView.class,
                JMSMetricPresenter.MyProxy.class);

        bindPresenter(DataSourceMetricPresenter.class,
                DataSourceMetricPresenter.MyView.class,
                DataSourceMetricView.class,
                DataSourceMetricPresenter.MyProxy.class);

        bindPresenter(JPAMetricPresenter.class,
                JPAMetricPresenter.MyView.class,
                JPAMetricsView.class,
                JPAMetricPresenter.MyProxy.class);

        bindPresenter(JGroupsPresenter.class,
                JGroupsPresenter.MyView.class,
                JGroupsSubsystemView.class,
                JGroupsPresenter.MyProxy.class);

        bindPresenter(PathManagementPresenter.class,
                PathManagementPresenter.MyView.class,
                PathManagementView.class,
                PathManagementPresenter.MyProxy.class);

        bindPresenter(EnvironmentPresenter.class,
                EnvironmentPresenter.MyView.class,
                EnvironmentView.class,
                EnvironmentPresenter.MyProxy.class);

        // Administration
        bindPresenter(AdministrationPresenter.class,
                AdministrationPresenter.MyView.class,
                AdministrationView.class,
                AdministrationPresenter.MyProxy.class);
        bindPresenter(RoleAssignmentPresenter.class,
                RoleAssignmentPresenter.MyView.class,
                RoleAssignementView.class,
                RoleAssignmentPresenter.MyProxy.class);
        bindPresenter(AuditLogPresenter.class,
                AuditLogPresenter.MyView.class,
                AuditLogView.class,
                AuditLogPresenter.MyProxy.class);

        // mbui workbench
        bindPresenter(ApplicationPresenter.class, ApplicationPresenter.MyView.class, ApplicationView.class,
                ApplicationPresenter.MyProxy.class);
        bindPresenterWidget(ContextPresenter.class, ContextPresenter.MyView.class, ContextView.class);
        bindPresenterWidget(FooterPresenter.class, FooterPresenter.MyView.class, FooterView.class);
        bindPresenterWidget(HeaderPresenter.class, HeaderPresenter.MyView.class, HeaderView.class);
        bindPresenter(PreviewPresenter.class, PreviewPresenter.MyView.class, PreviewView.class,
                PreviewPresenter.MyProxy.class);
        bindPresenterWidget(RepositoryPresenter.class, RepositoryPresenter.MyView.class, RepositoryView.class);

        // Application
        bind(SampleRepository.class).in(Singleton.class);

        bind(AccessControlRegistry.class).to(AccessControlRegistryImpl.class).in(Singleton.class);

        bind(SecurityFramework.class).to(SecurityFrameworkImpl.class).in(Singleton.class);
        bindPresenterWidget(UnauthorisedPresenter.class, UnauthorisedPresenter.MyView.class, UnauthorisedView.class);
    }
}


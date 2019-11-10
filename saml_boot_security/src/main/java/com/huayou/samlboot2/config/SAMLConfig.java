package com.huayou.samlboot2.config;

import com.google.common.collect.ImmutableMap;
import com.huayou.samlboot2.spring.SAMLResource;
import com.huayou.samlboot2.spring.security.SSOUserDetailsService;
import com.huayou.samlboot2.unuseful.KeystoreFactory;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.velocity.app.VelocityEngine;
import org.opensaml.saml2.metadata.provider.MetadataProvider;
import org.opensaml.saml2.metadata.provider.MetadataProviderException;
import org.opensaml.saml2.metadata.provider.ResourceBackedMetadataProvider;
import org.opensaml.xml.parse.StaticBasicParserPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.saml.SAMLAuthenticationProvider;
import org.springframework.security.saml.SAMLBootstrap;
import org.springframework.security.saml.SAMLDiscovery;
import org.springframework.security.saml.SAMLEntryPoint;
import org.springframework.security.saml.SAMLLogoutFilter;
import org.springframework.security.saml.SAMLLogoutProcessingFilter;
import org.springframework.security.saml.SAMLProcessingFilter;
import org.springframework.security.saml.SAMLWebSSOHoKProcessingFilter;
import org.springframework.security.saml.context.SAMLContextProviderImpl;
import org.springframework.security.saml.key.JKSKeyManager;
import org.springframework.security.saml.key.KeyManager;
import org.springframework.security.saml.log.SAMLDefaultLogger;
import org.springframework.security.saml.metadata.CachingMetadataManager;
import org.springframework.security.saml.metadata.ExtendedMetadata;
import org.springframework.security.saml.metadata.ExtendedMetadataDelegate;
import org.springframework.security.saml.metadata.MetadataDisplayFilter;
import org.springframework.security.saml.metadata.MetadataGenerator;
import org.springframework.security.saml.metadata.MetadataGeneratorFilter;
import org.springframework.security.saml.metadata.MetadataManager;
import org.springframework.security.saml.parser.ParserPoolHolder;
import org.springframework.security.saml.processor.HTTPArtifactBinding;
import org.springframework.security.saml.processor.HTTPPAOS11Binding;
import org.springframework.security.saml.processor.HTTPPostBinding;
import org.springframework.security.saml.processor.HTTPRedirectDeflateBinding;
import org.springframework.security.saml.processor.HTTPSOAP11Binding;
import org.springframework.security.saml.processor.SAMLBinding;
import org.springframework.security.saml.processor.SAMLProcessorImpl;
import org.springframework.security.saml.trust.httpclient.TLSProtocolConfigurer;
import org.springframework.security.saml.util.VelocityFactory;
import org.springframework.security.saml.websso.ArtifactResolutionProfileImpl;
import org.springframework.security.saml.websso.SingleLogoutProfile;
import org.springframework.security.saml.websso.SingleLogoutProfileImpl;
import org.springframework.security.saml.websso.WebSSOProfile;
import org.springframework.security.saml.websso.WebSSOProfileConsumer;
import org.springframework.security.saml.websso.WebSSOProfileConsumerHoKImpl;
import org.springframework.security.saml.websso.WebSSOProfileConsumerImpl;
import org.springframework.security.saml.websso.WebSSOProfileECPImpl;
import org.springframework.security.saml.websso.WebSSOProfileHoKImpl;
import org.springframework.security.saml.websso.WebSSOProfileImpl;
import org.springframework.security.saml.websso.WebSSOProfileOptions;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
/**
 * @author
 */
@AutoConfigureBefore({LoginSecurityConfig.class})
@Configuration
@Slf4j
public class SAMLConfig {

    @Autowired
    private SSOUserDetailsService SSOUserDetailsService;
    @Bean
    public WebSSOProfileConsumer webSSOprofileConsumer() {
        return new WebSSOProfileConsumerImpl();
    }
    @Bean
    public WebSSOProfileConsumerHoKImpl hokWebSSOprofileConsumer() {
        return new WebSSOProfileConsumerHoKImpl();
    }
    @Bean
    public SAMLAuthenticationProvider samlAuthenticationProvider() {
        SAMLAuthenticationProvider provider = new SAMLAuthenticationProvider();
        provider.setUserDetails(SSOUserDetailsService);
        provider.setConsumer(webSSOprofileConsumer());
        provider.setHokConsumer(hokWebSSOprofileConsumer());
        provider.setForcePrincipalAsString(false);
        return provider;
    }
    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(Collections.singletonList(samlAuthenticationProvider()));
    }
    @Bean(initMethod = "initialize")
    public StaticBasicParserPool parserPool() {
        return new StaticBasicParserPool();
    }
    @Bean
    public SAMLProcessorImpl processor() {
        HttpClient httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
        ArtifactResolutionProfileImpl artifactResolutionProfile = new ArtifactResolutionProfileImpl(httpClient);
        HTTPSOAP11Binding soapBinding = new HTTPSOAP11Binding(parserPool());
        artifactResolutionProfile.setProcessor(new SAMLProcessorImpl(soapBinding));

        VelocityEngine velocityEngine = VelocityFactory.getEngine();
        Collection<SAMLBinding> bindings = new ArrayList<>();
        bindings.add(new HTTPRedirectDeflateBinding(parserPool()));
        bindings.add(new HTTPPostBinding(parserPool(), velocityEngine));
        bindings.add(new HTTPArtifactBinding(parserPool(), velocityEngine, artifactResolutionProfile));
        bindings.add(new HTTPSOAP11Binding(parserPool()));
        bindings.add(new HTTPPAOS11Binding(parserPool()));
        return new SAMLProcessorImpl(bindings);
    }
    @Bean
    public SecurityContextLogoutHandler logoutHandler() {
        SecurityContextLogoutHandler handler = new SecurityContextLogoutHandler();
        handler.setInvalidateHttpSession(true);
        handler.setClearAuthentication(true);
        return handler;
    }
    @Bean
    public SimpleUrlLogoutSuccessHandler successLogoutHandler() {
        SimpleUrlLogoutSuccessHandler handler = new SimpleUrlLogoutSuccessHandler();
        handler.setDefaultTargetUrl("/");
        return handler;
    }
    @Bean
    public SAMLLogoutFilter samlLogoutFilter() {
        SAMLLogoutFilter filter = new SAMLLogoutFilter(successLogoutHandler(), new LogoutHandler[]{logoutHandler()}, new LogoutHandler[]{logoutHandler()});
        filter.setFilterProcessesUrl("/sso-logout");
        return filter;
    }
    @Bean
    public SAMLLogoutProcessingFilter samlLogoutProcessingFilter() {
        SAMLLogoutProcessingFilter filter = new SAMLLogoutProcessingFilter(successLogoutHandler(), logoutHandler());
        filter.setFilterProcessesUrl("/SingleLogout");
        return filter;
    }
    @Bean
    public MetadataGeneratorFilter metadataGeneratorFilter(MetadataGenerator metadataGenerator) {
        MetadataGeneratorFilter filter = new MetadataGeneratorFilter(metadataGenerator);
        //TODO MetadataManager normalizeBaseUrl
        filter.setDisplayFilter(metadataDisplayFilter());
        return filter;
    }
    @Bean
    public MetadataDisplayFilter metadataDisplayFilter(){
        MetadataDisplayFilter filter = new MetadataDisplayFilter();
        filter.setFilterProcessesUrl("/saml/metadata");
        //TODO MetadataManager KeyManager SAMLContextProvider
        return filter;
    }
    @Bean
    BeanFactoryPostProcessor idpMetadataLoader() {
        return beanFactory -> {
            PathMatchingResourcePatternResolver metadataFilesResolver = new PathMatchingResourcePatternResolver();
            try {
                Resource[] idpMetadataFiles = metadataFilesResolver.getResources("classpath:/idp/idp-*.xml");
                Stream.of(idpMetadataFiles).forEach(idpMetadataFile -> {
                    try {
                        Timer refreshTimer = new Timer(true);
                        ResourceBackedMetadataProvider delegate = null;
                        delegate = new ResourceBackedMetadataProvider(refreshTimer, new SAMLResource(idpMetadataFile));
                        delegate.setParserPool(parserPool());
                        ExtendedMetadata extendedMetadata = extendedMetadata().clone();
                        ExtendedMetadataDelegate provider = new ExtendedMetadataDelegate(delegate, extendedMetadata);
                        provider.setMetadataTrustCheck(true);
                        provider.setMetadataRequireSignature(false);
                        //TODO
                        String idpFileName = idpMetadataFile.getFilename();
                        String idpName = idpFileName.substring(idpFileName.lastIndexOf("idp-") + 4, idpFileName.lastIndexOf(".xml"));
                        extendedMetadata.setAlias(idpName);
                        beanFactory.registerSingleton(idpName, provider);
                        log.info("Loaded Idp Metadata bean {}: {}", idpName, idpMetadataFile);

                    } catch (Exception e) {
                        throw new IllegalStateException("Unable to initialize IDP Metadata", e);
                    }
                });
            } catch (Exception e) {
                throw new IllegalStateException("Unable to initialize IDP Metadata", e);
            }
        };
    }
    @Bean
    public ExtendedMetadata extendedMetadata() {
        ExtendedMetadata metadata = new ExtendedMetadata();
        //TODO Signature algorithm encryption, etc.
        //set flag to true to present user with IDP Selection screen
        metadata.setIdpDiscoveryEnabled(true);
        metadata.setRequireLogoutRequestSigned(true);
        //metadata.setRequireLogoutResponseSigned(true);
        //metadata.setAlias("");
        metadata.setSignMetadata(false);
        return metadata;
    }
    @Bean
    public MetadataGenerator metadataGenerator(KeyManager keyManager) {
        MetadataGenerator generator = new MetadataGenerator();
        generator.setEntityId("https://localhost:5000/sso-metadata/sso-metadata.html");
        generator.setExtendedMetadata(extendedMetadata());
        generator.setIncludeDiscoveryExtension(false);
        generator.setKeyManager(keyManager);
        generator.setSamlEntryPoint(samlEntryPoint());
        generator.setSamlLogoutProcessingFilter(samlLogoutProcessingFilter());
        generator.setSamlWebSSOFilter(samlWebSSOProcessingFilter());
        generator.setSamlWebSSOHoKFilter(samlWebSSOHoKProcessingFilter());
        generator.setEntityBaseURL("https://localhost:5000");
        generator.setId("A1B2C3D4");
        //TODO id entityBaseURL , etc.
        return generator;
    }
    @Bean(value = "samlWebSSOProcessingFilter")
    public SAMLProcessingFilter samlWebSSOProcessingFilter(){
        SAMLProcessingFilter filter = new SAMLProcessingFilter();
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(successRedirectHandler());
        filter.setAuthenticationFailureHandler(authenticationFailureHandler());
        filter.setFilterProcessesUrl("/ssologin");
        return filter;
    }
    @Bean(value ="samlWebSSOHoKProcessingFilter")
    public SAMLWebSSOHoKProcessingFilter samlWebSSOHoKProcessingFilter(){
        SAMLWebSSOHoKProcessingFilter filter = new SAMLWebSSOHoKProcessingFilter();
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(successRedirectHandler());
        filter.setAuthenticationFailureHandler(authenticationFailureHandler());
        return filter;
    }
//    @Bean
//    public SavedRequestAwareAuthenticationSuccessHandler successRedirectHandler() {
//        SavedRequestAwareAuthenticationSuccessHandler handler = new SavedRequestAwareAuthenticationSuccessHandler();
//
//        handler.setDefaultTargetUrl("/home");
//        return handler;
//    }
    @Bean
    public AuthenticationSuccessHandler successRedirectHandler() {
        return (request, response, authentication) -> {
           response.sendRedirect("/home");
        };
    }
    @Bean
    public SimpleUrlAuthenticationFailureHandler authenticationFailureHandler() {
        SimpleUrlAuthenticationFailureHandler handler = new SimpleUrlAuthenticationFailureHandler();
        handler.setUseForward(false);
        //handler.setDefaultFailureUrl("/error");
        return handler;
    }
    @Bean
    public SAMLDiscovery samlIDPDiscovery() {
        SAMLDiscovery filter = new SAMLDiscovery();
        filter.setFilterProcessesUrl("/saml/discovery");
        filter.setIdpSelectionPath("/idpselection");
        return filter;
    }
    @Bean
    public SAMLEntryPoint samlEntryPoint() {
        WebSSOProfileOptions options = new WebSSOProfileOptions();
        options.setIncludeScoping(false);
        //TODO Name ID policy
        SAMLEntryPoint entryPoint = new SAMLEntryPoint();
        //TODO initializes SAML WebSSO Profile, IDP Discovery or ECP Profile from the SP side
        entryPoint.setFilterProcessesUrl("/saml/login");
//        entryPoint.setSamlDiscovery(samlIDPDiscovery());
        entryPoint.setDefaultProfileOptions(options);
        entryPoint.setWebSSOprofile(webSSOprofile());
        entryPoint.setWebSSOprofileECP(ecpProfile());
        entryPoint.setWebSSOprofileHoK(hokWebSSOProfile());
        entryPoint.setSamlLogger(samlLogger());
        return entryPoint;
    }
    @Bean
    public WebSSOProfile webSSOprofile() {
        return new WebSSOProfileImpl();
    }

    @Bean
    public WebSSOProfileECPImpl ecpProfile() {
        return new WebSSOProfileECPImpl();
    }

    @Bean
    public WebSSOProfileHoKImpl hokWebSSOProfile() {
        return new WebSSOProfileHoKImpl();
    }

    @Bean
    public SingleLogoutProfile logoutProfile() {
        return new SingleLogoutProfileImpl();
    }
    @Bean
    public KeystoreFactory keystoreFactory(ResourceLoader resourceLoader) {
        return new KeystoreFactory(resourceLoader);
    }
    @Bean
    public KeyManager keyManager(KeystoreFactory keystoreFactory) throws Exception{
        KeyStore keystore = keystoreFactory.loadKeystore("classpath:/localhost.cert", "classpath:/localhost.key.der", "localhost", "");
        return new JKSKeyManager(keystore, ImmutableMap.of("localhost", ""), "localhost");
    }

    @Bean
    public TLSProtocolConfigurer tlsProtocolConfigurer(KeyManager keyManager) {
        TLSProtocolConfigurer configurer = new TLSProtocolConfigurer();
        configurer.setKeyManager(keyManager);
        return configurer;
    }
    @Bean
    public static SAMLBootstrap sAMLBootstrap() {
        return new SAMLBootstrap();
    }

    @Bean
    public ParserPoolHolder parserPoolHolder() {
        return new ParserPoolHolder();
    }

    @Bean
    public SAMLContextProviderImpl contextProvider() {
        return new SAMLContextProviderImpl();
    }

    @Bean
    public SAMLDefaultLogger samlLogger() {
        return new SAMLDefaultLogger();
    }

    @Bean
    public MetadataManager metadataManager(List<MetadataProvider> metadataProviders) throws MetadataProviderException {
        CachingMetadataManager cachingMetadataManager = new CachingMetadataManager(metadataProviders);
        cachingMetadataManager.setRefreshCheckInterval(-1);
        return cachingMetadataManager;
    }

}

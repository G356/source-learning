package com.vdenotaris.boot.saml.config;

public interface samlConst {
  /********* key information ***********/
  String storeFile="classpath:/saml/samlKeystore.jks";
  String storePass="nalle123";
  String key01="apollo";
  String value01="nalle123";
  String defaultKey=key01;
  String signingAlgorithm="http://www.w3.org/2001/04/xmldsig-more#rsa-sha256";

  /********* idp information ***********/
  String idpSelectionPath="/saml/discovery";
  String idpMetadataURL="https://app.onelogin.com/saml/metadata/c4491824-3fcc-426c-916f-c19bb2b8322e";

  /********* login information ***********/
  String entityId="https://localhost:5000/sso-metadata/sso-metadata.html";
  String successRedirectTargetUrl="/landing";
  String failureUrl="/error";
  String successLogoutTargetUrl="/";
  String formLoginUrl="/login";

  /********* filter matcherUrl ***********/
  String samlEntryPoint="/saml/login";
  String samlLogoutFilter="/saml/logout";
  String metadataDisplayFilter="/saml/metadata";
  String samlWebSSOProcessingFilter="/ssologin"; //default:/saml/SSO
  String samlWebSSOHoKProcessingFilter="/saml/SSOHoK";
  String samlLogoutProcessingFilter="/sso-logout";//default:/saml/SingleLogout
  String samlIDPDiscovery="/saml/discovery";

}

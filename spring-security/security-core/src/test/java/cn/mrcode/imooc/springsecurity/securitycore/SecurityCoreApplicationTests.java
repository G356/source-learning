package cn.mrcode.imooc.springsecurity.securitycore;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@SpringBootConfiguration
public class SecurityCoreApplicationTests {
/** 1.Security中的Filters
 * ChannelProcessingFilter
 * SecurityContextPersistenceFilter
 * ConcurrentSessionFilter
 * Authentication Filter (核心鉴权中心)
 * SecurityContextHolderAwareRequestFilter
 * RememberMeAuthenticationFilter
 * AnonymousAuthenticationFilter
 * ExceptionTranslationFilter
 * FilterSecurityInterceptor
 */
/** 2.web基础过滤器AbstractAuthenticationProcessingFilter
 *  处理Session/Cookie/SecurityContext等领域的Request后形成Authentication交给AuthenticationManager，后者处理完后再返回给前者
 *  依赖组件(自身属性)如下：
 *  AuthenticationManager(身份验证)
 *  AuthenticationSuccessHandler(验证成功后续处理)
 *  AuthenticationFailureHandler(验证失败后续处理)
 *  SessionAuthenticationStrategy(回话验证处理)
 *  RememberMeServices("记住我"管理行为)
 *  ApplicationEventPublisher(事件发布管理器)
 */
/**
 * 3.Spring Security在Filter中的几个重要组件
 * SecurityContext(存取Authentication的上下文,被SecurityContextHolderStrategy,SecurityContextHolder获取)
 * AuthenticationToken(一般用于保存客户端提交身份信息,基类为AbstractAuthenticationToken)
 * Authentication(一般用于服务端保存身份信息)
 */
/**4.基于用户名和密码的认证流程
 * UsernamePasswordAuthenticationToken(继承于AbstractAuthenticationToken)
 * UsernamePasswordAuthenticationFilter(继承于AbstractAuthenticationProcessingFilter)
 * 逻辑:Filter负责从Request处获取参数封装成Token认证类，然后调用AuthenticationManager的authenticate()方法对Token进行认证并返回给WEB
 * ProviderManager(AuthenticationManager的实现,负责调度不同Provider然后对不同Provider进行认证)
 * AbstractUserDetailsAuthenticationProvider(AuthenticationProvider的实现,先判断Token类是否支持，继而进行认证)
*/
	@Test
	public void contextLoads() {
	}

	@Test
	public void authentication() {
		/*存储用户信息的来源*/
		UserDetailsService userDetailsService = new InMemoryUserDetailsManager();
		((InMemoryUserDetailsManager)userDetailsService).createUser(User.withUsername("user").password("{noop}password").roles("USER").build());
		UserDetails userDetails = userDetailsService.loadUserByUsername("user");
		System.out.println("应用的用户信息："+userDetails.toString());
		/*用户信息验证的逻辑*/
		AuthenticationProvider provider=new DaoAuthenticationProvider();
		((DaoAuthenticationProvider)provider).setUserDetailsService(userDetailsService);
		List<AuthenticationProvider> providers=new ArrayList<>();
		providers.add(provider);
		AuthenticationManager manager=new ProviderManager(providers);
		/*客户端输入的用户信息*/
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("user", "password");
		Authentication result = manager.authenticate(authenticationToken);

		System.out.println("外部待验证信息; "+authenticationToken.toString());
		System.out.println("验证结果信息  ; "+result.toString());

	}
	@Test
	public void daoAuthenticationProvider() {
		PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();

		UserDetailsService userDetailsService = new InMemoryUserDetailsManager();
		((InMemoryUserDetailsManager)userDetailsService).createUser(User.withUsername("user").password(passwordEncoder.encode("password")).roles("USER").build());

		AuthenticationProvider provider=new DaoAuthenticationProvider();
		((DaoAuthenticationProvider)provider).setUserDetailsService(userDetailsService);
		((DaoAuthenticationProvider)provider).setPasswordEncoder(passwordEncoder);
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("user", "password");
		Class<? extends Authentication> tokenClass = authenticationToken.getClass();
		System.out.println(provider.supports(tokenClass));

		Authentication result = provider.authenticate(authenticationToken);

		System.out.println(result);
	}

}

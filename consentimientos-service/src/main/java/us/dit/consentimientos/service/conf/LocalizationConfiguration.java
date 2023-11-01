package us.dit.consentimientos.service.conf;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

/**
 * Configuración de la localización de la aplicación
 */
@Configuration
public class LocalizationConfiguration implements WebMvcConfigurer {
	@Bean
	public LocaleResolver localeResolver() {
		// The locale will be a session attribute, defaulting to Spanish-Spain
		SessionLocaleResolver lr = new SessionLocaleResolver();
		lr.setDefaultLocale(new Locale("es", "ES"));
		return lr;
	}

	@Bean
	public MessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		// The localization resources will be under the "lang" directory, and start with
		// "messages". The default file will be "resources/lang/messages.properties",
		// and messages for other locales will be:
		// "resources/lang/messages_LN.properties", where LN is the language. E.g.
		// "messages_en.properties" for ENglish
		messageSource.setBasenames("lang/messages");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}

	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
		// This will configure a query parameter that can be used to set the locale.
		// E.g. some/url?lang=en
		lci.setParamName("lang");
		return lci;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
	}
}

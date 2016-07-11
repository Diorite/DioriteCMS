package org.diorite.web.cms.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.security.Principal;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import org.diorite.web.cms.models.Account;

@Configuration
public class VelocityConnector implements HandlerInterceptor
{
    @Override
    public boolean preHandle(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse, final Object o) throws Exception
    {
        return true;
    }

    @Override
    public void postHandle(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse, final Object o, ModelAndView modelAndView) throws Exception
    {
        if (modelAndView == null)
        {
            modelAndView = new ModelAndView();
        }
        else if (modelAndView.getViewName().startsWith("redirect"))
        {
            return; // Don't affect redirects
        }

        final Principal auth = httpServletRequest.getUserPrincipal();
        final boolean isLoggedIn = auth != null && (auth instanceof UsernamePasswordAuthenticationToken);

        modelAndView.addObject("isLoggedIn", isLoggedIn);
        if (isLoggedIn)
        {
            final Account account = (Account) ((UsernamePasswordAuthenticationToken) auth).getPrincipal();

            modelAndView.addObject("userId", account.getId());
            modelAndView.addObject("userName", account.getUserName());
            modelAndView.addObject("isAdmin", account.hasPermission("acp.access"));
        }
    }

    @Override
    public void afterCompletion(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse, final Object o, final Exception e) throws Exception
    {
    }

    @Configuration
    public static class AnnotationSecurityConfiguration extends WebMvcConfigurerAdapter
    {
        @Override
        public void addInterceptors(final InterceptorRegistry registry)
        {
            registry.addInterceptor(new VelocityConnector());
        }
    }
}

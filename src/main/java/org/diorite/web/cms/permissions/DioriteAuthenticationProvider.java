package org.diorite.web.cms.permissions;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import org.diorite.web.cms.core.DioriteCms;
import org.diorite.web.cms.dao.AccountRepository;
import org.diorite.web.cms.models.Account;

@Component
public class DioriteAuthenticationProvider implements AuthenticationProvider
{
    @Autowired
    private DioriteCms cms;
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException
    {
        final String username = authentication.getName();
        final String password = (String) authentication.getCredentials();

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password))
        {
            throw new BadCredentialsException("Fill username and password fields!");
        }

        final Account account = this.accountRepository.findByUserName(username);
        if (account == null)
        {
            throw new UsernameNotFoundException(username);
        }

        if (account.getPassword().equals(password))
        {
            return new UsernamePasswordAuthenticationToken(account, password, account.getAuthorities());
        }
        else
        {
            throw new BadCredentialsException("Bad password");
        }
    }

    @Override
    public boolean supports(final Class<?> aClass)
    {
        if (UsernamePasswordAuthenticationToken.class.equals(aClass))
        {
            return true;
        }
        this.cms.getLogger().warning("Unsupported authentication method: " + aClass);
        return false;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).appendSuper(super.toString()).append("accountRepository", this.accountRepository).toString();
    }
}

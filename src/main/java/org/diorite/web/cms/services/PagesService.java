package org.diorite.web.cms.services;

import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.diorite.web.cms.pages.DefaultProvidersRegister;
import org.diorite.web.cms.pages.IPagesProvider;
import org.diorite.web.cms.pages.Page;

@Service
public class PagesService
{
    private final HashMap<String, IPagesProvider> providers;

    @Autowired
    public PagesService(final DefaultProvidersRegister defaultProvidersRegister)
    {
        this.providers = new HashMap<>();
        defaultProvidersRegister.register(this);
    }

    public void registerProvider(final IPagesProvider provider)
    {
        this.providers.put(provider.getName(), provider);
    }

    public Set<Page> getAllPages()
    {
        return this.providers.values().stream().flatMap(set -> set.getPages().stream()).collect(Collectors.toSet());
    }

    public Page getPage(final String provider, final String arg)
    {
        return this.providers.get(provider).getPage(arg);
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).appendSuper(super.toString()).append("providers", this.providers).toString();
    }
}

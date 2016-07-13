package org.diorite.web.cms.controllers;

import java.security.Principal;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.diorite.web.cms.dao.StaticPageRepository;
import org.diorite.web.cms.models.Account;
import org.diorite.web.cms.models.StaticPage;

@Controller
/*
  This controller handles all requests to static pages
 */
public class PageController
{
    @Autowired
    private StaticPageRepository staticPageRepository;

    @RequestMapping(value = "/page/{id}", method = RequestMethod.GET)
    public String page(final @PathVariable Integer id, final Model model, final Principal principal)
    {
        if (! this.staticPageRepository.exists(id))
        {
            model.addAttribute("status", HttpStatus.NOT_FOUND);
            model.addAttribute("error", "Page with specified ID wasn't found!");
            return "error";
        }

        final StaticPage page = this.staticPageRepository.getOne(id);

        boolean isAdmin = false;
        if (principal != null)
        {
            final Authentication auth = (Authentication) principal;
            final Account account = (Account) auth.getPrincipal();
            isAdmin = account.hasPermission("access.acp");
        }

        if (! page.isPublished() && ! isAdmin)
        {
            throw new AccessDeniedException("You don't have permissions to access this page");
        }

        model.addAttribute("page", page);
        return "static_page";
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).appendSuper(super.toString()).toString();
    }
}

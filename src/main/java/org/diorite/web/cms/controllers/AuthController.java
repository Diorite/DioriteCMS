package org.diorite.web.cms.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.security.Principal;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import org.diorite.web.cms.dao.AccountRepository;
import org.diorite.web.cms.dao.GroupRepository;
import org.diorite.web.cms.models.Account;

@Controller
@RequestMapping(value = "/auth")
public class AuthController
{
    @Autowired
    private GroupRepository   groupRepository;
    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping(value = "/access_denied")
    public String accessDenied(final Model model)
    {
        model.addAttribute("status", "403");
        model.addAttribute("error", "You don't have permissions to access this page!");
        return "error";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(final Model model, final Principal principal, final @RequestParam(required = false, defaultValue = "false") Boolean failed)
    {
        if (principal != null)
        {
            return "redirect:/";
        }
        model.addAttribute("failed", failed);
        return "forms/login";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register(final Model model, final Principal principal)
    {
        if (principal != null)
        {
            return "redirect:/";
        }
        return "forms/register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String doRegister(final Model model, final Principal principal, final @RequestParam String user, final @RequestParam String email, final @RequestParam String password)
    {
        if (principal != null)
        {
            return "redirect:/";
        }

        if (StringUtils.isEmpty(user) || StringUtils.isEmpty(email) || StringUtils.isEmpty(password))
        {
            model.addAttribute("fill_all_fields", true);
            return "forms/register";
        }

        if (this.accountRepository.existsByUserName(user))
        {
            model.addAttribute("username_exists", true);
            return "forms/register";
        }

        final Account account = new Account(user, null, email, password, this.groupRepository.findOne(1));
        this.accountRepository.saveAndFlush(account);

        return "redirect:/";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(final HttpServletRequest request, final HttpServletResponse response)
    {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null)
        {
            new SecurityContextLogoutHandler().logout(request, response, auth);
            return "redirect:/auth/login";
        }
        else
        {
            return "redirect:/";
        }
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).appendSuper(super.toString()).toString();
    }
}

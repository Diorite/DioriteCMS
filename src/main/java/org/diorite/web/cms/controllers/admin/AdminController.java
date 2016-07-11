package org.diorite.web.cms.controllers.admin;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@PreAuthorize("isAuthenticated() and principal.hasPermission('acp.access')")
public class AdminController
{
    @RequestMapping(path = {"", "/"})
    public String root()
    {
        return "redirect:/admin/home";
    }

    @RequestMapping("/home")
    public String home()
    {
        return "admin/home";
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).appendSuper(super.toString()).toString();
    }
}

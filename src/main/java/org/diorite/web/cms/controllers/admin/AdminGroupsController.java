package org.diorite.web.cms.controllers.admin;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.diorite.web.cms.dao.GroupRepository;
import org.diorite.web.cms.models.Group;
import org.diorite.web.cms.services.PermissionsService;

@Controller
@RequestMapping("/admin/groups")
@PreAuthorize("isAuthenticated() and principal.hasPermission('acp.access')")
public class AdminGroupsController
{
    @Autowired
    private GroupRepository    groupRepository;
    @Autowired
    private PermissionsService permissionsService;

    @RequestMapping({"", "/"})
    public String list(final Model model)
    {
        model.addAttribute("groups", this.groupRepository.findAll());
        return "admin/groups_list";
    }

    @RequestMapping("/new")
    public String newGroup()
    {
        final Group group = this.groupRepository.saveAndFlush(new Group());
        return "redirect:/admin/groups/" + group.getId();
    }

    

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).appendSuper(super.toString()).toString();
    }
}

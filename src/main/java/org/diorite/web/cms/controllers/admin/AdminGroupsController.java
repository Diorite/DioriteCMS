package org.diorite.web.cms.controllers.admin;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import org.diorite.web.cms.dao.GroupRepository;
import org.diorite.web.cms.models.Group;
import org.diorite.web.cms.models.Permission;
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
        return "admin/group_list";
    }

    @RequestMapping("/new")
    public String newGroup()
    {
        final Group group = this.groupRepository.saveAndFlush(new Group());
        return "redirect:/admin/groups/" + group.getId();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String editGroup(final Model model, final @PathVariable Integer id)
    {
        final Group group = this.groupRepository.findOne(id);
        model.addAttribute("group", group);
        model.addAttribute("permissions", this.permissionsService.getAllPermissions());
        return "admin/group_edit";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public String postEditGroup(final @PathVariable Integer id, final @RequestParam(required = false) String name, final @RequestParam(required = false) String permission, final @RequestParam(required = false) Boolean value)
    {
        final Group group = this.groupRepository.findOne(id);
        if (name != null)
        {
            group.setDisplayName(name);
        }

        if (permission != null && value != null)
        {
            final Permission perm = this.permissionsService.get(permission);
            final boolean has = group.hasPermission(perm);

            if (value && !has)
            {
                group.grantPermission(perm);
            }
            else if (!value && has)
            {
                group.removePermission(perm);
            }
        }

        this.groupRepository.save(group);
        return "redirect:/admin/groups/" + id;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteGroup(final @PathVariable Integer id)
    {
        final Group group = this.groupRepository.getOne(id);
        if (group.isSpecial())
        {
            return;
        }
        this.groupRepository.delete(group);
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).appendSuper(super.toString()).toString();
    }
}

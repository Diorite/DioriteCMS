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

import org.diorite.web.cms.dao.StaticPageRepository;
import org.diorite.web.cms.models.StaticPage;

@Controller
@RequestMapping("/admin/pages")
@PreAuthorize("isAuthenticated() and principal.hasPermission('acp.access')")
public class AdminPagesController
{
    @Autowired
    private StaticPageRepository staticPageRepository;

    @RequestMapping(value = {"/", ""})
    public String staticPages(final Model model)
    {
        model.addAttribute("pages", this.staticPageRepository.findAll());
        return "admin/static_pages_list";
    }

    @RequestMapping("/new")
    public String newStaticPage()
    {
        final StaticPage newStaticPage = this.staticPageRepository.saveAndFlush(new StaticPage("Statyczna strona", "", false, true));
        return "redirect:/admin/pages/" + newStaticPage.getId();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String editStaticPage(final Model model, final @PathVariable Integer id)
    {
        model.addAttribute("page", this.staticPageRepository.getOne(id));
        return "admin/static_pages_edit";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public String postEditStaticPage(final @PathVariable Integer id, final @RequestParam(required = false) String name, final @RequestParam(required = false) String content, final @RequestParam(required = false) Boolean published, final @RequestParam(required = false) Boolean frame)
    {
        final StaticPage page = this.staticPageRepository.getOne(id);
        if (page == null)
        {
            return "redirect:/admin/pages"; // TODO
        }

        if (name != null)
        {
            page.setName(name);
        }
        if (content != null)
        {
            page.setContent(content);
        }
        if (published != null)
        {
            page.setPublished(published);
        }
        if (frame != null)
        {
            page.setDisplayFrame(frame);
        }

        this.staticPageRepository.save(page);
        return "redirect:/admin/pages/" + page.getId();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteStaticPage(final @PathVariable Integer id)
    {
        this.staticPageRepository.delete(id);
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).appendSuper(super.toString()).toString();
    }
}

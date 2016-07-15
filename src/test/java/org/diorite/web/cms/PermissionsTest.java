package org.diorite.web.cms;

import static junit.framework.TestCase.assertEquals;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.diorite.web.cms.core.DioriteCms;
import org.diorite.web.cms.dao.GroupRepository;
import org.diorite.web.cms.models.Group;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = DioriteCms.class)
public class PermissionsTest
{
    @Autowired
    private GroupRepository groupRepository;

    @Test
    public void hasPermissionTest()
    {
        final Group group1 = this.groupRepository.findOne(1);
        assertEquals(true, group1.hasPermission("perm1"));
        assertEquals(true, group1.hasPermission("perm2"));
        assertEquals(true, group1.hasPermission("perm3"));

        final Group group2 = this.groupRepository.findOne(2);
        assertEquals(true, group2.hasPermission("perm1"));
        assertEquals(false, group2.hasPermission("perm2"));
        assertEquals(true, group2.hasPermission("perm3"));
    }

    @Test
    public void degrantingTest()
    {
        final Group group3 = this.groupRepository.findOne(3);
        group3.removePermission("perm2");

        assertEquals(false, group3.hasPermission("perm1"));
        assertEquals(false, group3.hasPermission("perm2"));
        assertEquals(false, group3.hasPermission("perm3"));
    }

    @Test
    public void grantingTest()
    {
        final Group group4 = this.groupRepository.findOne(4);

        assertEquals(false, group4.hasPermission("perm1"));
        assertEquals(false, group4.hasPermission("perm2"));
        assertEquals(false, group4.hasPermission("perm3"));

        group4.grantPermission("perm1");

        assertEquals(true, group4.hasPermission("perm1"));
        assertEquals(false, group4.hasPermission("perm2"));
        assertEquals(false, group4.hasPermission("perm3"));

        group4.grantPermissions("perm2", "perm3");

        assertEquals(true, group4.hasPermission("perm1"));
        assertEquals(true, group4.hasPermission("perm2"));
        assertEquals(true, group4.hasPermission("perm3"));
    }
}

package org.diorite.web.cms.models;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.Sets;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.security.core.GrantedAuthority;

import org.diorite.web.cms.core.DioriteCms;
import org.diorite.web.cms.permissions.PermissionsGrantable;
import org.diorite.web.cms.permissions.PermissionsHolder;

@Entity(name = "groups")
public class Group implements GrantedAuthority, PermissionsHolder, PermissionsGrantable
{
    // Entity structure
    @Id
    @GeneratedValue
    private int             id;
    private String          displayName;
    private boolean         isSpecial;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Permission> permissions;

    public Group()
    {
    }

    @Override
    public String getAuthority()
    {
        return String.valueOf(this.id);
    }

    public String getDisplayName()
    {
        return this.displayName;
    }

    public void setDisplayName(final String displayName)
    {
        this.displayName = displayName;
    }

    public int getId()
    {
        return this.id;
    }

    public boolean isSpecial()
    {
        return this.isSpecial;
    }

    @Override
    public Set<Permission> getPermissions()
    {
        return Sets.newCopyOnWriteArraySet(this.permissions);
    }

    @Override
    public boolean hasPermission(final Permission permission)
    {
        return this.permissions.contains(permission);
    }

    @Override
    public void grantPermission(final Permission permission)
    {
        if (this.permissions == null)
        {
            this.permissions = new HashSet<>();
        }
        this.permissions.add(DioriteCms.getInstance().getPermissionsService().get(permission));
    }

    @Override
    public void removePermission(final Permission permission)
    {
        this.permissions.remove(permission);
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || this.getClass() != o.getClass())
        {
            return false;
        }

        final Group group = (Group) o;

        return this.id == group.id && (this.displayName != null ? this.displayName.equals(group.displayName) : group.displayName == null && this.permissions.equals(group.permissions));

    }

    @Override
    public int hashCode()
    {
        int result = this.id;
        result = 31 * result + (this.displayName != null ? this.displayName.hashCode() : 0);
        result = 31 * result + this.permissions.hashCode();
        return result;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).appendSuper(super.toString()).append("id", this.id).append("displayName", this.displayName).append("permissions", this.permissions).toString();
    }
}

package org.diorite.web.cms.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity(name = "permissions")
public class Permission
{
    private static final int MAX_PERMISSION_LENGTH = 64;

    @Id
    @Column(nullable = false, unique = true, length = MAX_PERMISSION_LENGTH)
    private String name;
    @Column(nullable = false)
    private String displayName;

    public Permission()
    {
    }

    public Permission(final String name)
    {
        this.name = name;
        if (this.name.length() > MAX_PERMISSION_LENGTH)
        {
            throw new IllegalArgumentException();
        }
    }

    public Permission(final String name, final String displayName)
    {
        this(name);
        this.displayName = displayName;
    }

    public String getName()
    {
        return this.name;
    }

    public String getDisplayName()
    {
        return this.displayName;
    }

    public void setDisplayName(final String displayName)
    {
        this.displayName = displayName;
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

        final Permission that = (Permission) o;

        return this.name.equals(that.name);

    }

    @Override
    public int hashCode()
    {
        return this.name.hashCode();
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).appendSuper(super.toString()).append("name", this.name).append("displayName", this.displayName).toString();
    }
}

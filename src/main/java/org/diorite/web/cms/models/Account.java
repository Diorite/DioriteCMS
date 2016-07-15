package org.diorite.web.cms.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import org.diorite.web.cms.permissions.PermissionsHolder;

@Entity(name = "accounts")
public class Account implements PermissionsHolder, UserDetails
{
    @Id
    @GeneratedValue
    private int         id;
    @Column(nullable = false, unique = true)
    private String      userName;
    @Column(unique = true)
    private String      displayName;
    @Column(nullable = false)
    private String      email;
    @Column(nullable = false)
    private String      password;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Group>  groups;

    public Account()
    {
    }

    public Account(final String userName, final String displayName, final String email, final String password, final Set<Group> groups)
    {
        this.userName = userName;
        this.displayName = displayName;
        this.email = email;
        this.password = password;
        this.groups = groups;
    }

    public int getId()
    {
        return this.id;
    }

    @Override
    public String getUsername()
    {
        return this.userName;
    }

    public void setUsername(final String username)
    {
        this.userName = username;
    }

    public String getDisplayName()
    {
        return this.displayName;
    }

    public void setDisplayName(final String displayName)
    {
        this.displayName = displayName;
    }

    public String getEmail()
    {
        return this.email;
    }

    public void setEmail(final String email)
    {
        this.email = email;
    }

    @Override
    public String getPassword()
    {
        return this.password;
    }

    public void setPassword(final String password)
    {
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return this.groups;
    }

    public boolean isInGroup(final Group group)
    {
        return this.groups.contains(group);
    }

    public boolean addGroup(final Group group)
    {
        return this.groups.add(group);
    }

    public boolean removeGroup(final Group group)
    {
        return this.groups.remove(group);
    }

    @Override
    public Set<Permission> getPermissions()
    {
        return this.groups.stream().map(Group::getPermissions).flatMap(Collection::stream).collect(Collectors.toSet());
    }

    @Override
    public boolean hasPermission(final Permission permission)
    {
        for (final Group group : this.groups)
        {
            if (group.hasPermission(permission))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isAccountNonExpired()
    {
        return true;
    }

    @Override
    public boolean isAccountNonLocked()
    {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired()
    {
        return true;
    }

    @Override
    public boolean isEnabled()
    {
        return true;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).appendSuper(super.toString()).append("id", this.id).append("userName", this.userName).append("displayName", this.displayName).append("email", this.email).append("password", this.password).append("groups", this.groups).toString();
    }
}

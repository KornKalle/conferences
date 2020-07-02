package de.basisit.conferences

import groovy.transform.CompileStatic
import org.springframework.ldap.core.DirContextAdapter
import org.springframework.ldap.core.DirContextOperations
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper

/**
 * This Class should map additional properties from LDAP to Users, it is not functional right now
 * I followed this guide:
 * https://grails-plugins.github.io/grails-spring-security-ldap/v4/index.html#custom-userdetailscontextmapper
 */
@CompileStatic
class LDAPUserDetailsContextMapper implements UserDetailsContextMapper{

    @Override
    UserDetails mapUserFromContext(DirContextOperations ctx, String username, Collection<? extends GrantedAuthority> authorities) {
        String commonName = ctx.attributes.get('cn')?.get() as String

        new LDAPUserDetails(username,
                '',
                true,
                false,
                false,
                false,
                authorities,
                commonName) as UserDetails
    }

    @Override
    void mapUserToContext(UserDetails user, DirContextAdapter ctx) {
        throw new IllegalStateException("Only retrieving data from AD is currently supported")
    }
}

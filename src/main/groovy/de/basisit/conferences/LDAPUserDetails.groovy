package de.basisit.conferences

import groovy.transform.CompileStatic
import org.springframework.security.core.GrantedAuthority

/**
 * This Class should map additional properties from LDAP to Users, it is not functional right now
 * I followed this guide:
 * https://grails-plugins.github.io/grails-spring-security-ldap/v4/index.html#custom-userdetailscontextmapper
 */
@CompileStatic
class LDAPUserDetails extends User{

    // extra instance variables
    final String commonName

    LDAPUserDetails(String username, String password, boolean enabled, boolean accountExpired,
                  boolean accountLocked, boolean passwordExpired,
                  Collection<GrantedAuthority> authorities, String commonName) {

    //    super(username, password, enabled, accountExpired, accountLocked,
    //            passwordExpired, authorities)

        this.commonName = commonName
    }
}

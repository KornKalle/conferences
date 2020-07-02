
// Uncomment this value for LDAP debugging in Development Environment
//logging.level.org.springframework.security='DEBUG'

// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.userLookup.userDomainClassName = 'de.basisit.conferences.User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'de.basisit.conferences.UserRole'
grails.plugin.springsecurity.authority.className = 'de.basisit.conferences.Role'
grails.plugin.springsecurity.logout.postOnly = false

// REPLACE THIS BEFORE USING THE APPLICATION IN ANY ENVIRONMENT
grails.plugin.springsecurity.rest.token.storage.jwt.secret = 'thisisanunsecuresecretpleasechangeit'
grails.plugin.springsecurity.rest.login.endpointUrl = '/api/login'
grails.plugin.springsecurity.rest.login.failureStatusCode=403
grails.plugin.springsecurity.rest.token.validation.enableAnonymousAccess = true
grails.plugin.springsecurity.roleHierarchy = '''
   ROLE_ADMIN > ROLE_USER > ROLE_ANONYMOUS
'''

grails.plugin.springsecurity.controllerAnnotations.staticRules = [
	[pattern: '/',               access: ['permitAll']],
	[pattern: '/error',          access: ['permitAll']],
	[pattern: '/index',          access: ['permitAll']],
	[pattern: '/index.gsp',      access: ['permitAll']],
	[pattern: '/shutdown',       access: ['permitAll']],
	[pattern: '/assets/**',      access: ['permitAll']],
	[pattern: '/**/js/**',       access: ['permitAll']],
	[pattern: '/**/css/**',      access: ['permitAll']],
	[pattern: '/**/images/**',   access: ['permitAll']],
	[pattern: '/**/favicon.ico', access: ['permitAll']]
]

grails.plugin.springsecurity.filterChain.chainMap = [
		[pattern: '/assets/**', filters: 'none'],
		[pattern: '/**/js/**', filters: 'none'],
		[pattern: '/**/css/**', filters: 'none'],
		[pattern: '/**/images/**', filters: 'none'],
		[pattern: '/**/favicon.ico', filters: 'none'],
		//Stateless chain
		[
				pattern: '/api/**',
				filters: 'JOINED_FILTERS,-exceptionTranslationFilter,-authenticationProcessingFilter,-securityContextPersistenceFilter,-rememberMeAuthenticationFilter'
		],

		//Traditional chain
		[
				pattern: '/**',
				filters: 'JOINED_FILTERS,-restTokenValidationFilter,-restExceptionTranslationFilter'
		]
]

// locations to search for config files that get merged into the main config;
// config files can be ConfigSlurper scripts, Java properties files, or classes
// in the classpath in ConfigSlurper format

// grails.config.locations = [ "classpath:${appName}-config.properties",
//                             "classpath:${appName}-config.groovy",
//                             "file:${userHome}/.grails/${appName}-config.properties",
//                             "file:${userHome}/.grails/${appName}-config.groovy"]

// if (System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }

def httpPort = System.getProperty("grails.server.port.http", "8080")
def httpsPort = System.getProperty("grails.server.port.https", "8443")

grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination
grails.mime.file.extensions = true // enables the parsing of file extensions from URLs into the request format
grails.mime.use.accept.header = false
grails.mime.types = [
    all:           '*/*',
    atom:          'application/atom+xml',
    css:           'text/css',
    csv:           'text/csv',
    form:          'application/x-www-form-urlencoded',
    html:          ['text/html','application/xhtml+xml'],
    js:            'text/javascript',
    json:          ['application/json', 'text/json'],
    multipartForm: 'multipart/form-data',
    rss:           'application/rss+xml',
    text:          'text/plain',
    xml:           ['text/xml', 'application/xml']
]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// What URL patterns should be processed by the resources plugin
grails.resources.adhoc.patterns = ['/images/*', '/css/*', '/js/*', '/plugins/*']

// The default codec used to encode data with ${}
grails.views.default.codec = "none" // none, html, base64
grails.views.gsp.encoding = "UTF-8"
grails.converters.encoding = "UTF-8"
// enable Sitemesh preprocessing of GSP pages
grails.views.gsp.sitemesh.preprocess = true
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []
// whether to disable processing of multi part requests
grails.web.disable.multipart=false

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']

// configure auto-caching of queries by default (if false you can cache individual queries with 'cache: true')
grails.hibernate.cache.queries = false

grails.app.context = "/"

// PORTAL-GRAILS TODO update these tags to be appropriate for your team
grails.plugins.bvcommon.aws.bv.nexus.team='reporting@bazaarvoice.com'
grails.plugins.bvcommon.aws.bv.nexus.role='reporting'
grails.plugins.bvcommon.aws.bv.nexus.ec2.key.name='team-ec2-key'
grails.plugins.bvcommon.aws.instance.type='m1.medium'

// Added by the Spring Security Core plugin:
grails.plugins.springsecurity.userLookup.userDomainClassName = 'com.bazaarvoice.portal.User'
grails.plugins.springsecurity.userLookup.authorityJoinClassName = 'com.bazaarvoice.portal.UserRole'
grails.plugins.springsecurity.authority.className = 'com.bazaarvoice.portal.Role'
grails.plugins.springsecurity.rejectIfNoRule = true
grails.plugins.springsecurity.portMapper.httpPort = httpPort
grails.plugins.springsecurity.portMapper.httpsPort = httpsPort
// default to more secure rejectIfNoRule strategy
grails.plugins.springsecurity.rejectIfNoRule = true
// require https for all calls; also redirects http calls to https (http://grails-plugins.github.com/grails-spring-security-core/docs/manual/guide/17%20Channel%20Security.html)
grails.plugins.springsecurity.secureChannel.definition = [
        '/**':         'REQUIRES_SECURE_CHANNEL'
]

// PORTAL-GRAILS TODO you almost certainly want to change this approach to request mapping.  See you options here: http://grails-plugins.github.io/grails-spring-security-core/docs/manual/guide/5%20Configuring%20Request%20Mappings%20to%20Secure%20URLs.html
grails.plugins.springsecurity.securityConfigType = "InterceptUrlMap"
grails.plugins.springsecurity.interceptUrlMap = [
        '/login/denied':  ['IS_AUTHENTICATED_ANONYMOUSLY'],
        '/**':            ['ROLE_ADMIN']
]

// Tell spring security to check the "X-Forwarded-Proto" set by our load balancer for https rather than
// the url scheme
grails.plugins.springsecurity.secureChannel.useHeaderCheckChannelSecurity = true
// load balancer ports to use in deployed environments
grails.plugins.springsecurity.portMapper.httpPort = 80
grails.plugins.springsecurity.portMapper.httpsPort = 443

environments {
    development {
        grails.logging.jul.usebridge = true
        grails.serverURL = "https://timetraveler.portal.bazaarvoice.com:" + httpsPort

        // disable the "X-Forwarded-Proto" in development since there's no load balancer
        grails.plugins.springsecurity.secureChannel.useHeaderCheckChannelSecurity = false
        // ensure spring security knows what ports we're serving from so it can do its redirects properly
        grails.plugins.springsecurity.portMapper.httpPort = httpPort
        grails.plugins.springsecurity.portMapper.httpsPort = httpsPort
    }
    test {
        // use "test" for BV "dev" environment, since "dev" is used in Grails for local development
        grails.plugins.bvcommon.aws.deployment.env='dev'

        // PORTAL-GRAILS TODO product url goes here: grails.serverURL = "https://my-app-dev.bazaarvoice.com"
        grails.serverURL = "https://reporting-dev.elasticbeanstalk.com"
    }
    qa {
        grails.plugins.bvcommon.aws.deployment.env='qa'

        // PORTAL-GRAILS TODO product url goes here: grails.serverURL = "https://my-app-qa.bazaarvoice.com"
    }
    production {
        grails.logging.jul.usebridge = false

        grails.plugins.bvcommon.aws.deployment.env='prod'
        // PORTAL-GRAILS TODO product url goes here: grails.serverURL = "https://my-app.bazaarvoice.com"
    }
}

// log4j configuration
log4j = {
    // Example of changing the log pattern for the default console appender:
    //
    //appenders {
    //    console name:'stdout', layout:pattern(conversionPattern: '%c{2} %m%n')
    //}

    // enables dumping app properties at startup
    info   'bvcommon.log.PropertiesLogger'

    error  'org.codehaus.groovy.grails.web.servlet',        // controllers
           'org.codehaus.groovy.grails.web.pages',          // GSP
           'org.codehaus.groovy.grails.web.sitemesh',       // layouts
           'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
           'org.codehaus.groovy.grails.web.mapping',        // URL mapping
           'org.codehaus.groovy.grails.commons',            // core / classloading
           'org.codehaus.groovy.grails.plugins',            // plugins
           'org.codehaus.groovy.grails.orm.hibernate',      // hibernate integration
           'org.springframework',
           'org.hibernate',
           'net.sf.ehcache.hibernate'
}

// Portal-app (at least as of 1.1.4; maybe earlier) requires this
grails.plugins.portalapp.usePortalNav = true
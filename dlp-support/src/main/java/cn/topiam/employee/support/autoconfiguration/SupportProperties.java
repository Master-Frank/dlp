package cn.topiam.employee.support.autoconfiguration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * Configuration properties class for the support module
 * Used to configure various properties of the TopIAM support module
 */
@ConfigurationProperties(
   prefix = "topiam"
)
public class SupportProperties {
    
    /**
     * Security configuration properties
     */
    @NestedConfigurationProperty
    private Security security = new Security();
    
    /**
     * Demo environment configuration properties
     */
    @NestedConfigurationProperty
    private final Demo demo = new Demo();
    
    /**
     * Default prefix
     */
    public static final String DEFAULT_PREFIX = "topiam";
    
    /**
     * Server configuration properties
     */
    @NestedConfigurationProperty
    private final Server server = new Server();

    /**
     * Get server configuration properties
     * 
     * @return Server configuration properties
     */
    public Server getServer() {
        return this.server;
    }

    /**
     * Get demo environment configuration properties
     * 
     * @return Demo environment configuration properties
     */
    public Demo getDemo() {
        return this.demo;
    }

    /**
     * Get security configuration properties
     * 
     * @return Security configuration properties
     */
    public Security getSecurity() {
        return this.security;
    }

    /**
     * Set security configuration properties
     * 
     * @param security Security configuration properties
     */
    public void setSecurity(Security security) {
        this.security = security;
    }

    /**
     * Demo environment configuration class
     */
    public static class Demo {
        
        /**
         * Whether demo environment is enabled
         */
        private Boolean open;

        /**
         * Constructor, initializes demo environment to disabled
         */
        public Demo() {
            this.open = Boolean.FALSE;
        }

        /**
         * Get demo environment enabled status
         * 
         * @return Demo environment enabled status
         */
        public Boolean getOpen() {
            return this.open;
        }

        /**
         * Set demo environment enabled status
         * 
         * @param open Demo environment enabled status
         */
        public void setOpen(Boolean open) {
            this.open = open;
        }
    }

    /**
     * Security configuration class
     */
    public static class Security {
        
        /**
         * Jump configuration properties
         */
        @NestedConfigurationProperty
        private Jump jump = new Jump();

        /**
         * Set jump configuration properties
         * 
         * @param jump Jump configuration properties
         */
        public void setJump(Jump jump) {
            this.jump = jump;
        }

        /**
         * Get jump configuration properties
         * 
         * @return Jump configuration properties
         */
        public Jump getJump() {
            return this.jump;
        }

        /**
         * Jump configuration class
         */
        public static class Jump {
            
            /**
             * Default redirect URL
             */
            private String defaultRedirectUrl;

            /**
             * Get default redirect URL
             * 
             * @return Default redirect URL
             */
            public String getDefaultRedirectUrl() {
                return this.defaultRedirectUrl;
            }

            /**
             * Set default redirect URL
             * 
             * @param defaultRedirectUrl Default redirect URL
             */
            public void setDefaultRedirectUrl(String defaultRedirectUrl) {
                this.defaultRedirectUrl = defaultRedirectUrl;
            }
        }
    }

    /**
     * Server configuration class
     */
    public static class Server {
        
        /**
         * OpenAPI public base URL
         */
        private String openApiPublicBaseUrl;
        
        /**
         * Portal public base URL
         */
        private String portalPublicBaseUrl;
        
        /**
         * Console public base URL
         */
        private String consolePublicBaseUrl;

        /**
         * Set portal public base URL
         * 
         * @param portalPublicBaseUrl Portal public base URL
         */
        public void setPortalPublicBaseUrl(String portalPublicBaseUrl) {
            this.portalPublicBaseUrl = portalPublicBaseUrl;
        }

        /**
         * Get portal public base URL
         * 
         * @return Portal public base URL
         */
        public String getPortalPublicBaseUrl() {
            return this.portalPublicBaseUrl;
        }

        /**
         * Set console public base URL
         * 
         * @param consolePublicBaseUrl Console public base URL
         */
        public void setConsolePublicBaseUrl(String consolePublicBaseUrl) {
            this.consolePublicBaseUrl = consolePublicBaseUrl;
        }

        /**
         * Set OpenAPI public base URL
         * 
         * @param openApiPublicBaseUrl OpenAPI public base URL
         */
        public void setOpenApiPublicBaseUrl(String openApiPublicBaseUrl) {
            this.openApiPublicBaseUrl = openApiPublicBaseUrl;
        }

        /**
         * Get console public base URL
         * 
         * @return Console public base URL
         */
        public String getConsolePublicBaseUrl() {
            return this.consolePublicBaseUrl;
        }

        /**
         * Get OpenAPI public base URL
         * 
         * @return OpenAPI public base URL
         */
        public String getOpenApiPublicBaseUrl() {
            return this.openApiPublicBaseUrl;
        }
    }
}
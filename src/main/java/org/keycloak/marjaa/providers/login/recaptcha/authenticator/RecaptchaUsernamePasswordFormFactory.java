package org.keycloak.marjaa.providers.login.recaptcha.authenticator;


import org.keycloak.Config;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.models.credential.PasswordCredentialModel;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;

import java.util.ArrayList;
import java.util.List;

public class RecaptchaUsernamePasswordFormFactory implements AuthenticatorFactory {

    public static final String PROVIDER_ID = "recaptcha-u-p-form";

    // Artık UsernamePasswordForm olmadığı için kendi authenticator’ınızı kullanacaksınız
    public static final RecaptchaUsernamePasswordForm SINGLETON = new RecaptchaUsernamePasswordForm();

    @Override
    public Authenticator create(KeycloakSession session) {
        // Quarkus Keycloak’ta thread-safe olduğu sürece singleton kullanmak hâlâ doğrudur.
        return SINGLETON;
    }

    @Override
    public void init(Config.Scope config) {
        // Quarkus modelinde burası hâlâ kullanılabiliyor
    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {
        // no-op
    }

    @Override
    public void close() {
        // no-op
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    @Override
    public String getReferenceCategory() {
        return PasswordCredentialModel.TYPE;
    }

    @Override
    public boolean isConfigurable() {
        // recaptcha için config gerekiyor
        return true;
    }

    private static final AuthenticationExecutionModel.Requirement[] REQUIREMENT_CHOICES = {
            AuthenticationExecutionModel.Requirement.REQUIRED
    };

    @Override
    public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
        return REQUIREMENT_CHOICES;
    }

    @Override
    public String getDisplayType() {
        return "Recaptcha Username + Password Form";
    }

    @Override
    public String getHelpText() {
        return "Validates username and password with Google Recaptcha verification.";
    }

    private static final List<ProviderConfigProperty> CONFIG_PROPERTIES = new ArrayList<>();

    static {
        List<ProviderConfigProperty> properties = ProviderConfigurationBuilder.create()
                .property()
                .name(RecaptchaUsernamePasswordForm.SITE_KEY)
                .label("Recaptcha Site Key")
                .type(ProviderConfigProperty.STRING_TYPE)
                .helpText("Google Recaptcha site key.")
                .add()
                .property()
                .name(RecaptchaUsernamePasswordForm.SITE_SECRET)
                .label("Recaptcha Secret")
                .type(ProviderConfigProperty.STRING_TYPE)
                .helpText("Google Recaptcha secret.")
                .add()
                .property()
                .name(RecaptchaUsernamePasswordForm.USE_RECAPTCHA_NET)
                .label("Use recaptcha.net")
                .type(ProviderConfigProperty.BOOLEAN_TYPE)
                .helpText("Use recaptcha.net instead of google.com (for blocked regions).")
                .add()
                .build();
        CONFIG_PROPERTIES.addAll(properties);
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return CONFIG_PROPERTIES;
    }

    @Override
    public boolean isUserSetupAllowed() {
        return false;
    }
}

package com.dslplatform.client;

import java.io.IOException;

public class ProjectAuthorization extends CredentialsAuthorization {
    public ProjectAuthorization(final ProjectSettings projectSettings)
            throws IOException {
        super(projectSettings.get("username"), projectSettings
                .get("project-id"));
    }
}

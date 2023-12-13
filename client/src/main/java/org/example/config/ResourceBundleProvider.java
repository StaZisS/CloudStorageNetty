package org.example.config;

import com.google.inject.Provider;

import java.util.ResourceBundle;

public class ResourceBundleProvider implements Provider<ResourceBundle> {
    @Override
    public ResourceBundle get() {
        return ResourceBundle.getBundle("Bundle");
    }
}
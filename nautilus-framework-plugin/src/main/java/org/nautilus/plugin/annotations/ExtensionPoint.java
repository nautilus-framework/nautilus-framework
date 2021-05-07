package org.nautilus.plugin.annotations;

@PluginExtension
public interface ExtensionPoint {

    public String getName();

    public String getId();

    public String toString();
}

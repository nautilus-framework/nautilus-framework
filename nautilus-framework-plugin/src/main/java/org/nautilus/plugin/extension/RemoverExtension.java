package org.nautilus.plugin.extension;

import org.nautilus.core.remover.AbstractRemover;
import org.nautilus.plugin.annotations.ExtensionPoint;

public interface RemoverExtension extends ExtensionPoint {

    public AbstractRemover getRemover(ProblemExtension problemExtension);
}

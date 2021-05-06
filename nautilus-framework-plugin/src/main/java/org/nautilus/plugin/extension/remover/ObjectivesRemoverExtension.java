package org.nautilus.plugin.extension.remover;

import org.nautilus.core.remover.AbstractRemover;
import org.nautilus.core.remover.ObjectivesRemover;
import org.nautilus.plugin.extension.ProblemExtension;

public class ObjectivesRemoverExtension extends AbstractRemoverExtension {

    @Override
    public String getName() {
        return "By Objectives";
    }

    @Override
    public AbstractRemover getRemover(ProblemExtension problemExtension) {
        return new ObjectivesRemover();
    }
}

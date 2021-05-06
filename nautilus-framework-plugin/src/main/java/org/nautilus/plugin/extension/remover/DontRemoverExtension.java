package org.nautilus.plugin.extension.remover;

import org.nautilus.core.remover.AbstractRemover;
import org.nautilus.core.remover.DontRemover;
import org.nautilus.plugin.extension.ProblemExtension;

public class DontRemoverExtension extends AbstractRemoverExtension {

    @Override
    public String getName() {
        return "Don't Remove";
    }

    @Override
    public AbstractRemover getRemover(ProblemExtension problemExtension) {
        return new DontRemover();
    }
}

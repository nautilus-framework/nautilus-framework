package org.nautilus.plugin.extension.remover;

import org.nautilus.core.remover.AbstractRemover;
import org.nautilus.plugin.extension.ProblemExtension;
import org.pf4j.Extension;

@Extension
public class VariablesRemoverExtension extends AbstractRemoverExtension {

    @Override
    public String getName() {
        return "By Variables";
    }
    @Override
    public AbstractRemover getRemover(ProblemExtension problemExtension) {
        return problemExtension.getRemover();
    }
}

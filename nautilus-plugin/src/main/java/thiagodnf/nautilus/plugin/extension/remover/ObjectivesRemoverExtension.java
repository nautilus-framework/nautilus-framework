package thiagodnf.nautilus.plugin.extension.remover;

import org.pf4j.Extension;

import thiagodnf.nautilus.core.remover.AbstractRemover;
import thiagodnf.nautilus.core.remover.ObjectivesRemover;
import thiagodnf.nautilus.plugin.extension.ProblemExtension;

@Extension
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

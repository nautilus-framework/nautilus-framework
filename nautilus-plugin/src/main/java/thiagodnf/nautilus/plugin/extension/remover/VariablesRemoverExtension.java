package thiagodnf.nautilus.plugin.extension.remover;

import org.pf4j.Extension;

import thiagodnf.nautilus.core.remover.AbstractRemover;
import thiagodnf.nautilus.plugin.extension.ProblemExtension;

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

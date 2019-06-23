package thiagodnf.nautilus.plugin.extension.remover;

import org.pf4j.Extension;

import thiagodnf.nautilus.core.remover.AbstractRemover;
import thiagodnf.nautilus.core.remover.DontRemover;
import thiagodnf.nautilus.plugin.extension.ProblemExtension;

@Extension
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

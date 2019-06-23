package thiagodnf.nautilus.plugin.extension.normalizer;

import org.pf4j.Extension;

import thiagodnf.nautilus.core.normalize.AbstractNormalize;
import thiagodnf.nautilus.core.normalize.ByParetoFrontValuesNormalize;

@Extension
public class ByParetoFrontValuesNormalizerExtension extends AbstractNormalizerExtension {

    @Override
    public AbstractNormalize getNormalizer() {
         return new ByParetoFrontValuesNormalize();
    }

    @Override
    public String getName() {
        return "By Pareto-front Values";
    }
}

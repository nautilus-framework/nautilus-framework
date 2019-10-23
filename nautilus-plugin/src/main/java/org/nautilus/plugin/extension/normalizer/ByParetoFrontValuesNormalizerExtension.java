package org.nautilus.plugin.extension.normalizer;

import org.nautilus.core.normalize.AbstractNormalize;
import org.nautilus.core.normalize.ByParetoFrontValuesNormalize;
import org.pf4j.Extension;

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

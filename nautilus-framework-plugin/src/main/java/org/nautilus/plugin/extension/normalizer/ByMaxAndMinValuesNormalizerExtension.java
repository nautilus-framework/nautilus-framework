package org.nautilus.plugin.extension.normalizer;

import org.nautilus.core.normalize.AbstractNormalize;
import org.nautilus.core.normalize.ByMaxAndMinValuesNormalize;

public class ByMaxAndMinValuesNormalizerExtension extends AbstractNormalizerExtension {

    @Override
    public AbstractNormalize getNormalizer() {
         return new ByMaxAndMinValuesNormalize();
    }

    @Override
    public String getName() {
        return "By Max and Min Values";
    }
}

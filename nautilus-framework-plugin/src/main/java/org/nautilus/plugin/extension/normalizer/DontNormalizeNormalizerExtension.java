package org.nautilus.plugin.extension.normalizer;

import org.nautilus.core.normalize.AbstractNormalize;
import org.nautilus.core.normalize.DontNormalize;

public class DontNormalizeNormalizerExtension extends AbstractNormalizerExtension {

    @Override
    public AbstractNormalize getNormalizer() {
         return new DontNormalize();
    }

    @Override
    public String getName() {
        return "Don't Normalize";
    }
}

package thiagodnf.nautilus.plugin.extension.normalizer;

import org.pf4j.Extension;

import thiagodnf.nautilus.core.normalize.AbstractNormalize;
import thiagodnf.nautilus.core.normalize.DontNormalize;

@Extension
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

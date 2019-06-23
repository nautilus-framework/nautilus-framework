package thiagodnf.nautilus.plugin.extension.normalizer;

import org.pf4j.Extension;

import thiagodnf.nautilus.core.normalize.AbstractNormalize;
import thiagodnf.nautilus.core.normalize.ByMaxAndMinValuesNormalize;

@Extension
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

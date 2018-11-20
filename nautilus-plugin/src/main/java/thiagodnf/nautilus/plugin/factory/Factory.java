package thiagodnf.nautilus.plugin.factory;

import java.util.HashMap;
import java.util.Map;

import thiagodnf.nautilus.core.util.Converter;
import thiagodnf.nautilus.plugin.extension.Extension;

public abstract class Factory {

	private Map<String, Extension> extensions = new HashMap<>();

	public void addExtension(Extension extension) {
		this.extensions.put(Converter.toKey(extension.getName()), extension);
	}

	public Map<String, Extension> getExtensions() {
		return extensions;
	}

	public Extension getExtension(String key) {

		if (!getExtensions().containsKey(key)) {
			throw new RuntimeException("The '" + key + "' extension was not found");
		}

		return getExtensions().get(key);
	}
}

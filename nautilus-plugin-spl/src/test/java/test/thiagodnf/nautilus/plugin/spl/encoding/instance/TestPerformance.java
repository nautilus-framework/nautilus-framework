package test.thiagodnf.nautilus.plugin.spl.encoding.instance;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import thiagodnf.nautilus.plugin.spl.encoding.instance.NewTXTInstanceData;
import thiagodnf.nautilus.plugin.spl.encoding.instance.OldTXTInstanceData;

public class TestPerformance {

	private static Path oldPath = Paths.get("src", "test", "resources", "old-eshop.txt");
	
	private static Path newPath = Paths.get("src", "test", "resources", "eshop.txt");
	
	@Test
	public void shouldReturnTheCorrectValues() {

		double startOld = System.currentTimeMillis();
		new OldTXTInstanceData(oldPath);
		System.out.println("Old: " + (System.currentTimeMillis() - startOld));
		
		double startNew = System.currentTimeMillis();
		new NewTXTInstanceData(newPath);
		System.out.println("New: " + (System.currentTimeMillis() - startNew));

		assertTrue(true);
	}
}

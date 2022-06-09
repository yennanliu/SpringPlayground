package lab.mockito.more;


import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
public class SpyAnnotationTest {

	@Spy
	List<String> spyInterface;
	
	@Test
	public void show_spy_as_interface() {
		// When
		spyInterface.add("one");
		// Then
		verify(spyInterface, times(1)).add("one");
		assertNull(spyInterface.get(0));
		assertEquals(0, spyInterface.size());
	}
	
	@Spy
	ArrayList<String> spyImplement;

	@Test
	public void show_spy_as_implement() {
		// When
		spyImplement.add("one");
		// Then
		verify(spyImplement, times(1)).add("one");
		assertNotNull(spyImplement.get(0));
		assertEquals(1, spyImplement.size());
	}

}

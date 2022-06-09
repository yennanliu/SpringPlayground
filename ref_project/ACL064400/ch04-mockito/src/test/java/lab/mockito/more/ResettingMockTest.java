package lab.mockito.more;


import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ResettingMockTest {
	
	@Spy
	ArrayList<String> spyList;
	
	@Test
	public void show_without_reset_1() throws Exception {
		spyList.add("one");
		assertEquals(1, spyList.size());
	}
	
	@Test
	public void show_without_reset_2() throws Exception {
		spyList.add("two");
		assertEquals(1, spyList.size());
	}
		

}

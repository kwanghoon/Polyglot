package tool.compiler.java.test;

//import static org.junit.Assert.*;

import org.junit.Test;

public class TestTypeArgInference {

	@Test
	public void test() {
//		fail("Not yet implemented");
		String[] args = new String[5];
		args[0] = "-d";
		args[1] = "output";
		args[2] = "tests\\paper.java";
		args[3] = "-report";
		args[4] = "verbose=0";
		
		tool.compiler.java.Main.main(args);
	}
}
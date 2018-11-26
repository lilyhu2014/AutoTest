package api.autotest.framework;

import org.robotframework.javalib.library.AnnotationLibrary;

public class BasicLibrary extends AnnotationLibrary {
	public static final String ROBOT_LIBRARY_SCOPE = "GLOBAL";
	
	private static final String KEYWORD_PATTERN = "api/autotest/framework/keywords/**/*.class";
	
	public BasicLibrary() {
		addKeywordPattern(KEYWORD_PATTERN);
	}

}

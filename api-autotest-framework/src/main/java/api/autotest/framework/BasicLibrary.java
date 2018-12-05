package api.autotest.framework;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.robotframework.javalib.annotation.RobotKeywords;
import org.robotframework.javalib.library.AnnotationLibrary;

@RobotKeywords
public class BasicLibrary extends AnnotationLibrary {
	public static final String ROBOT_LIBRARY_SCOPE = "GLOBAL";
	private final static Logger LOGGER = Logger.getLogger("BasicLibrary");
	
	static List<String> keywordPatterns = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;
		{
			add("api/autotest/framework/keywords/**/*.class");
		}
	};
	
//	private static final String KEYWORD_PATTERN = "api/autotest/framework/keywords/*.class";
	
	public BasicLibrary() {
//		addKeywordPattern(KEYWORD_PATTERN);
		super(keywordPatterns);
		LOGGER.info("User defined keywords loaded.");
	}

}

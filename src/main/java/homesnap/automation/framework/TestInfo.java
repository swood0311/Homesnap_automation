package homesnap.automation.framework;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD })
	public @interface TestInfo {
		public enum APPS {
			PRO_PLUS, SMOKE,NOTSET
		}

		public enum AUTHORS {
		   STANLEY,CHRIS,NOTSET,
		}

		String ID() default "null";
		AUTHORS AUTHOR() default AUTHORS.NOTSET;
		APPS APP() default APPS.NOTSET;
}

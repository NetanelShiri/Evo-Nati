package ett.utils;


import UI.UIAdapter;
import engine.ett.FileManager;
import engine.users.UserManager;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import static ett.constants.Constants.INT_PARAMETER_ERROR;

public class ServletUtils {

	private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";
	private static final String FILE_MANAGER_ATTRIBUTE_NAME = "fileManager";
	private static final String ENGINE_MANAGER_ATTRIBUTE_NAME = "engineManager";
	/*
	Note how the synchronization is done only on the question and\or creation of the relevant managers and once they exists -
	the actual fetch of them is remained un-synchronized for performance POV
	 */
	private static final Object userManagerLock = new Object();
	private static final Object chatManagerLock = new Object();
	private static final Object engineManagerLock = new Object();

	public static UserManager getUserManager(ServletContext servletContext) {

		synchronized (userManagerLock) {
			if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
				servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, new UserManager());
			}
		}
		return (UserManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
	}

	public static UIAdapter getEngineManager(ServletContext servletContext) {

		synchronized (engineManagerLock) {
			if (servletContext.getAttribute(ENGINE_MANAGER_ATTRIBUTE_NAME) == null) {
				servletContext.setAttribute(ENGINE_MANAGER_ATTRIBUTE_NAME, new UIAdapter());
			}
		}
		return (UIAdapter) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
	}

	public static FileManager getFileManager(ServletContext servletContext) {
		synchronized (chatManagerLock) {
			if (servletContext.getAttribute(FILE_MANAGER_ATTRIBUTE_NAME) == null) {
				servletContext.setAttribute(FILE_MANAGER_ATTRIBUTE_NAME, new FileManager());
			}
		}
		return (FileManager) servletContext.getAttribute(FILE_MANAGER_ATTRIBUTE_NAME);
	}
/*
	public static DataManager getDataManager(ServletContext servletContext) {
		synchronized (chatManagerLock) {
			if (servletContext.getAttribute(CHAT_MANAGER_ATTRIBUTE_NAME) == null) {
				servletContext.setAttribute(CHAT_MANAGER_ATTRIBUTE_NAME, new DataManager());
			}
		}
		return (DataManager) servletContext.getAttribute(CHAT_MANAGER_ATTRIBUTE_NAME);
	}
*/
	public static int getIntParameter(HttpServletRequest request, String name) {
		String value = request.getParameter(name);
		if (value != null) {
			try {
				return Integer.parseInt(value);
			} catch (NumberFormatException numberFormatException) {
			}
		}
		return INT_PARAMETER_ERROR;
	}
}

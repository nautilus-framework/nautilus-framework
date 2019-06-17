package thiagodnf.nautilus.web.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Privileges {

	public final static String SHOW_ADMIN_PAGE = "SHOW_ADMIN_PAGE";
	
	public final static String SHOW_ROLES_PAGE = "SHOW_ROLES_PAGE";
	
	public final static String SHOW_USERS_PAGE = "SHOW_USERS_PAGE";
	
	public final static String UPLOAD_PLUGIN = "UPLOAD_PLUGIN";

	public final static String DOWNLOAD_PLUGIN = "DOWNLOAD_PLUGIN";

	public final static String DELETE_PLUGIN = "DELETE_PLUGIN";
	
	public final static String CREATE_ROLE = "CREATE_ROLE";
	
	public final static String EDIT_ROLE = "EDIT_ROLE";
	
	public final static String SAVE_ROLE = "SAVE_ROLE";
	
	public final static String DELETE_ROLE = "DELETE_ROLE";

	public static final String SAVE_USER = "SAVE_USER";

	public static final String DELETE_USER = "DELETE_USER";

	public static final String EDIT_USER = "EDIT_USER";
	
	public static List<String> getPrivilegies() {

		List<String> privilegies = new ArrayList<>();

		for (Field f : Privileges.class.getDeclaredFields()) {
			try {
				privilegies.add((String) f.get(null));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		return privilegies;
	}
}

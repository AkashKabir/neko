package neko;

import org.robolectric.util.ActivityController;

public class Helpers {
    public static Object getActivity(ActivityController controller) {
        return controller.get();
    }
}

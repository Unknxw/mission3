package fr.cned.emdsgil.fr;

import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;

public abstract class Global {

    static Map<Integer, CostsMonth> listFraisMois = new HashMap<>();
    static Integer userId;
    static final String filename = "save.fic";

    /**
     * Update date
     * @param datePicker
     * @param showDay
     */
    static void updateDate(DatePicker datePicker, boolean showDay) {
        try {
            Field[] fields = datePicker.getClass().getDeclaredFields();
            for (Field field : fields) {
                int daySpinnerId = Resources.getSystem().getIdentifier("day", "id", "android");
                datePicker.init(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), null);
                if (daySpinnerId != 0) {
                    View daySpinner = datePicker.findViewById(daySpinnerId);
                    if (!showDay)
                        daySpinner.setVisibility(View.GONE);
                }
            }
        } catch (SecurityException | IllegalArgumentException e) {
            Log.d("ERROR", Objects.requireNonNull(e.getMessage()));
        }
    }

}

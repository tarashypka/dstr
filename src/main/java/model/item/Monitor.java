package model.item;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by deoxys on 27.05.16.
 */
public class Monitor extends Item {
    private int screenSize;
    private Map<String, String> extendedFields;

    @Override
    public Map<String, String> getExtendedFields() {
        if (this.extendedFields == null) {
            this.extendedFields = new HashMap<>();
        }

        this.extendedFields.put("screenSize", Integer.toString(this.screenSize));

        return this.extendedFields;
    }

    public int getScreenSize() {
        return screenSize;
    }

    public void setScreenSize(int screenSize) {
        this.screenSize = screenSize;
    }
}

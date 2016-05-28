package model.item;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by deoxys on 27.05.16.
 */
public class Music extends Item {
    private String format;
    private String artist;
    private String album;
    private String UPC;
    private Map<String, String> extendedFields;

    @Override
    public Map<String, String> getExtendedFields() {
        if (this.extendedFields == null) {
            this.extendedFields = new HashMap<>();
        }

        this.extendedFields.put("format", this.format);
        this.extendedFields.put("artist", this.artist);
        this.extendedFields.put("album", this.album);
        this.extendedFields.put("UPC", this.UPC);

        return this.extendedFields;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getUPC() {
        return UPC;
    }

    public void setUPC(String UPC) {
        this.UPC = UPC;
    }
}

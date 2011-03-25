package net.jhorstmann.propertychangesupport;

import net.jhorstmann.propertychangesupport.api.PropertyChangeSupport;

@PropertyChangeSupport
public class ImageBean {

    private String href;
    private String alt;
    private int width;
    private int height;

    public ImageBean() {
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getHref() {
        return href;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getAlt() {
        return alt;
    }

    public void setWidth(int width) {
        String before = "before : " + this.width;
        System.out.println(before);
        this.width = width;
        String after  = "after  : " + this.width;
        System.out.println(after);
    }

    public int getWidth() {
        return width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHeight() {
        return height;
    }
}

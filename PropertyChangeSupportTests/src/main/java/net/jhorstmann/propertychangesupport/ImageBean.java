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
        String tmp = this.alt;
        this.alt = alt;
        if (Math.random()==0.0) {
             return;
        } else {
            System.out.println( tmp);
        }

    }

    public String getAlt() {
        return alt;
    }

    public void setWidth(int width) {
        this.width = width;
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

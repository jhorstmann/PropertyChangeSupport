package net.jhorstmann.propertychangesupport.api;

import java.beans.PropertyChangeListener;

public interface PropertyChangeEventSource {

    public void addPropertyChangeListener(PropertyChangeListener listener);
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener);
    public void removePropertyChangeListener(PropertyChangeListener listener);
    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener);
}

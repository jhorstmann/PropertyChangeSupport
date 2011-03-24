package net.jhorstmann.propertychangesupport;

import net.jhorstmann.propertychangesupport.api.PropertyChangeEventSource;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyChangeSupportTest {
    static class MyPropertyChangeListener implements PropertyChangeListener {
        private String property;
        private Object oldValue;
        private Object newValue;


        public void propertyChange(PropertyChangeEvent evt) {
            property = evt.getPropertyName();
            oldValue = evt.getOldValue();
            newValue = evt.getNewValue();
        }

        public Object getNewValue() {
            return newValue;
        }

        public Object getOldValue() {
            return oldValue;
        }

        public String getProperty() {
            return property;
        }
    }

    @Test
    public void testStringProperty() {
         ImageBean image = new ImageBean();
         MyPropertyChangeListener pcl = new MyPropertyChangeListener();
         ((PropertyChangeEventSource)image).addPropertyChangeListener(pcl);

         image.setAlt("abc");
         assertEquals("alt", pcl.getProperty());
         assertNull(pcl.getOldValue());
         assertEquals("abc", pcl.getNewValue());

         image.setAlt("def");
         assertEquals("alt", pcl.getProperty());
         assertEquals("abc", pcl.getOldValue());
         assertEquals("def", pcl.getNewValue());
    }

    @Test
    public void testIntProperty() {
         ImageBean image = new ImageBean();
         MyPropertyChangeListener pcl = new MyPropertyChangeListener();
         ((PropertyChangeEventSource)image).addPropertyChangeListener(pcl);

         image.setWidth(100);
         assertEquals("width", pcl.getProperty());
         assertEquals(0, pcl.getOldValue());
         assertEquals(100, pcl.getNewValue());

         image.setWidth(200);
         assertEquals("width", pcl.getProperty());
         assertEquals(100, pcl.getOldValue());
         assertEquals(200, pcl.getNewValue());
    }
}

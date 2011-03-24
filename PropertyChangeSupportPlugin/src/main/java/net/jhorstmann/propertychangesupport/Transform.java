package net.jhorstmann.propertychangesupport;

import net.jhorstmann.propertychangesupport.api.PropertyChangeSupport;
import net.jhorstmann.propertychangesupport.api.PropertyChangeEventSource;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.util.CheckClassAdapter;

class Transform {

    private Transform() {
    }

    private static boolean isAnnotationPresent(List annotations) {
        String desc = "L" + PropertyChangeSupport.class.getName().replace('.', '/') + ";";
        for (Iterator it = annotations.iterator(); it.hasNext();) {
            AnnotationNode annotation = (AnnotationNode) it.next();
            System.out.println(annotation.desc);
            if (annotation.desc.equals(desc)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isAnnotationPresent(ClassNode cn) {
        if (cn.visibleAnnotations != null) {
            boolean res = isAnnotationPresent(cn.visibleAnnotations);
            if (res) {
                return true;
            }
        }
        if (cn.invisibleAnnotations != null) {
            boolean res = isAnnotationPresent(cn.invisibleAnnotations);
            if (res) {
                return true;
            }
        }
        return false;
    }

    private static boolean isAlreadyTransformed(ClassNode cn) {
        String desc = PropertyChangeEventSource.class.getName().replace('.', '/');
        System.out.println(cn.interfaces);
        return (cn.interfaces != null && cn.interfaces.contains(desc));
    }

    private static void transformIfNeccessary(File file) throws IOException {
        InputStream is = new FileInputStream(file);
        ClassNode cn;
        try {
            ClassReader cr = new ClassReader(is);
            cn = new ClassNode();
            cr.accept(cn, 0);
        } finally {
            is.close();
        }

        if (!isAlreadyTransformed(cn) && isAnnotationPresent(cn)) {
            System.out.println("Transforming file " + file);
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            ClassVisitor ca = new CheckClassAdapter(cw);
            ClassVisitor cv = new PropertyChangeSupportAdapter(ca);
            cn.accept(cv);
            FileOutputStream os = new FileOutputStream(file);
            try {
                os.write(cw.toByteArray());
            } finally {
                os.close();
            }
        }
    }

    static class ClassesFilter implements FileFilter {

        public boolean accept(File dir, String name) {
            return name.endsWith(".class");
        }

        public boolean accept(File file) {
            return file.isDirectory() || file.isFile() && file.getName().endsWith(".class");
        }
    }
    private static final FileFilter CLASSES_FILTER = new ClassesFilter();

    static void transformRecursive(File file) throws IOException {
        if (file.isDirectory()) {
            System.out.println("Transforming directory " + file);
            File[] list = file.listFiles(CLASSES_FILTER);
            for (File child : list) {
                transformRecursive(child);
            }
        } else if (file.isFile()) {
            transformIfNeccessary(file);
        }
    }
}

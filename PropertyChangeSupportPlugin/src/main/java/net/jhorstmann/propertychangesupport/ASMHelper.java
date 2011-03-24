package net.jhorstmann.propertychangesupport;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

class ASMHelper {
    private ASMHelper() {
    }

    private static boolean matchType(final String desc, final int argsLength, final Type resType) {
        Type[] args = Type.getArgumentTypes(desc);
        Type res = Type.getReturnType(desc);
        
        return args.length == argsLength && (resType == null ? (res != Type.VOID_TYPE) : (res == resType));
    }

    private static boolean matchType(final String desc, final int argsLength, final Type firstArgType, final Type resType) {
        Type[] args = Type.getArgumentTypes(desc);
        Type res = Type.getReturnType(desc);
        
        return args.length == argsLength && args[0] == firstArgType && (resType == null ? (res != Type.VOID_TYPE) : (res == resType));
    }

    public static boolean isPublicSetter(final int access, final String name, final String desc) {
        if (name.length() > 3 && name.startsWith("set") && (access & Opcodes.ACC_PUBLIC) != 0 && (access & Opcodes.ACC_STATIC) == 0) {
            return matchType(desc, 1, Type.VOID_TYPE);
        }
        else {
            return false;
        }
    }
    
    public static boolean isPublicIndexedSetter(final int access, final String name, final String desc) {
        if (name.length() > 3 && name.startsWith("set") && (access & Opcodes.ACC_PUBLIC) != 0 && (access & Opcodes.ACC_STATIC) == 0) {
            return matchType(desc, 2, Type.INT_TYPE, Type.VOID_TYPE);
        }
        else {
            return false;
        }
    }
    
    public static boolean isPublicGetter(final int access, final String name, final String desc) {
        if (name.length() > 3 && name.startsWith("get") && (access & Opcodes.ACC_PUBLIC) != 0 && (access & Opcodes.ACC_STATIC) == 0) {
            return matchType(desc, 0, null);
        }
        else {
            return false;
        }
    }

    public static boolean isPublicIndexedGetter(final int access, final String name, final String desc) {
        if (name.length() > 3 && name.startsWith("get") && (access & Opcodes.ACC_PUBLIC) != 0 && (access & Opcodes.ACC_STATIC) == 0) {
            return matchType(desc, 1, Type.INT_TYPE, null);
        }
        else {
            return false;
        }
    }

    public static void generateToString(MethodVisitor mv, String desc) {
        String sig = "(" + (desc.charAt(0) == 'L' ? 
                            "Ljava/lang/Object;" : 
                            (desc.charAt(0) == 'S' ? 
                             "I" : 
                             desc)) + ")Ljava/lang/String;";
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/String", "valueOf", sig);
    }

    private static String getWrapperType(char ch) {
        switch (ch) {
            case 'I':
                return "Integer";
            case 'B':
                return "Byte";
            case 'C':
                return "Character";
            case 'S':
                return "Short";
            case 'J':
                return "Long";
            case 'Z':
                return "Boolean";
            case 'D':
                return "Double";
            case 'F':
                return "Float";
            case 'V':
                throw new IllegalArgumentException("Can not box Void");
            default:
                throw new IllegalArgumentException("Unknown type " + ch);
        }
    }

    public static void generateAutoBoxIfNeccessary(MethodVisitor mv, String desc) {
        if (desc.startsWith("L")) {
            // Is already an Object
        }
        else if (desc.length() == 1) {
            String type = "java/lang/" + ASMHelper.getWrapperType(desc.charAt(0));
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, type, "valueOf", "(" + desc + ")L" + type + ";");
        }
        else {
            throw new IllegalArgumentException("Unkown type " + desc);
        }
    }
}

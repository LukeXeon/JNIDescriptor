package org.keixe.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@SuppressWarnings("WeakerAccess")
public final class JNIDescriptor
{
    private JNIDescriptor()
    {
        throw new AssertionError();
    }

    public static String makeMethodDescriptor(Class<?> returnType, Class<?>[] pramTypes)
    {
        StringBuilder builder = new StringBuilder("(");
        for (Class pramType : pramTypes)
        {
            if (pramType.isPrimitive())
            {
                makePrimitiveDescriptor(builder, pramType);
            } else
            {
                makeDescriptor(builder, pramType);
            }
        }
        builder.append(')');
        makeDescriptor(builder, returnType);
        return builder.toString();
    }

    public static String fromField(Field field)
    {
        return makeTypeDescriptor(field.getType());
    }

    public static String makeTypeDescriptor(Class<?> type)
    {
        StringBuilder builder = new StringBuilder(type.getName().length());
        makeDescriptor(builder, type);
        return builder.toString();
    }

    public static String fromMethod(Method method)
    {
        return makeMethodDescriptor(method.getReturnType(), method.getParameterTypes());
    }

    private static void makePrimitiveDescriptor(StringBuilder builder, Class pramType)
    {
        char c;
        if (pramType == Integer.TYPE)
        {
            c = 'I';
        } else if (pramType == Void.TYPE)
        {
            c = 'V';
        } else if (pramType == Boolean.TYPE)
        {
            c = 'Z';
        } else if (pramType == Byte.TYPE)
        {
            c = 'B';
        } else if (pramType == Character.TYPE)
        {
            c = 'C';
        } else if (pramType == Short.TYPE)
        {
            c = 'S';
        } else if (pramType == Double.TYPE)
        {
            c = 'D';
        } else if (pramType == Float.TYPE)
        {
            c = 'F';
        } else /* if (d == Long.TYPE) */
        {
            c = 'J';
        }
        builder.append(c);
    }

    private static void makeDescriptor(StringBuilder builder, Class pramType)
    {
        while (pramType.isArray())
        {
            builder.append('[');
            Class next = pramType.getComponentType();
            if (next == null)
            {
                break;
            }
            pramType = next;
        }
        if (pramType.isPrimitive())
        {
            makePrimitiveDescriptor(builder, pramType);
        } else
        {
            builder.append("L").append(pramType.getName().replace('.', '/'))
                    .append(';');
        }
    }
}

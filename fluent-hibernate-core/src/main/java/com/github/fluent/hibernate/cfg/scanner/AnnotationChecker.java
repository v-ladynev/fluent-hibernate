package com.github.fluent.hibernate.cfg.scanner;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;

/**
 * Based on <a href="https://github.com/rmuller/infomas-asl/tree/master/annotation-detector">
 * annotation-detector</a> author <a href="mailto:rmuller@xiam.nl">Ronald K. Muller</a>
 *
 * @author V.Ladynev
 */
public class AnnotationChecker {

    private static final int HEAD = 0xCAFEBABE;

    // AnnotationElementValue

    private static final int BYTE = 'B';

    private static final int CHAR = 'C';

    private static final int DOUBLE = 'D';

    private static final int FLOAT = 'F';

    private static final int INT = 'I';

    private static final int LONG = 'J';

    private static final int SHORT = 'S';

    private static final int BOOLEAN = 'Z';

    // used for AnnotationElement only

    private static final int STRING = 's';

    private static final int ENUM = 'e';

    private static final int CLASS = 'c';

    private static final int ANNOTATION = '@';

    private static final int ARRAY = '[';

    private final ClassFileBuffer buffer = new ClassFileBuffer();

    private final ConstantPool constantPool = new ConstantPool();

    private final String annotationDescriptor;

    public AnnotationChecker(Class<? extends Annotation> annotation) {
        annotationDescriptor = ResourceUtils.toDescriptor(annotation);
    }

    /**
     *
     * @param classStream
     *            a class file stream, it is closed in this method
     */
    public boolean hasAnnotation(InputStream classStream) throws IOException {
        try {
            buffer.readFrom(classStream);
            return hasCafebabe() ? detect() : false;
        } finally {
            classStream.close();
        }
    }

    private boolean hasCafebabe() throws IOException {
        return buffer.size() > 4 && buffer.readInt() == HEAD;
    }

    /**
     * Inspect the given (Java) class file in streaming mode.
     */
    private boolean detect() throws IOException {
        readVersion();
        readConstantPool();
        readAccessFlags();
        readThisClass();
        readSuperClass();
        readInterfaces();
        readFields();
        readMethods();
        return checkTypeAnnotations();
    }

    private void readVersion() throws IOException {
        // sequence: minor version, major version (argument_index is 1-based)
        buffer.skipBytes(4);
    }

    private void readConstantPool() throws IOException {
        constantPool.readEntries(buffer);
    }

    private void readAccessFlags() throws IOException {
        buffer.skipUnsignedShort();
    }

    private void readThisClass() throws IOException {
        buffer.skipUnsignedShort();
    }

    private void readSuperClass() throws IOException {
        buffer.skipUnsignedShort();
    }

    private void readInterfaces() throws IOException {
        int count = buffer.readUnsignedShort();
        buffer.skipBytes(count * 2); // count * u2
    }

    private void readFields() throws IOException {
        readInfoStruct();
    }

    private void readMethods() throws IOException {
        readInfoStruct();
    }

    private void readInfoStruct() throws IOException {
        int count = buffer.readUnsignedShort();

        for (int i = 0; i < count; ++i) {
            readAccessFlags();
            readNameIndex();
            readDescriptorIndex();
            readAttributes();
        }
    }

    private void readAttributes() throws IOException {
        int count = buffer.readUnsignedShort();

        for (int i = 0; i < count; ++i) {
            readNameIndex();
            // in bytes, use this to skip the attribute info block
            int length = buffer.readInt();
            buffer.skipBytes(length);
        }
    }

    private void readNameIndex() throws IOException {
        buffer.skipUnsignedShort();
    }

    private void readDescriptorIndex() throws IOException {
        buffer.skipUnsignedShort();
    }

    private boolean checkTypeAnnotations() throws IOException {
        int count = buffer.readUnsignedShort();

        for (int i = 0; i < count; ++i) {
            String name = resolveUtf8();
            // in bytes, use this to skip the attribute info block
            int length = buffer.readInt();
            if ("RuntimeVisibleAnnotations".equals(name)
                    || "RuntimeInvisibleAnnotations".equals(name)) {
                if (checkTypeAnnotationsHelper()) {
                    return true;
                }
            } else {
                buffer.skipBytes(length);
            }
        }

        return false;
    }

    private boolean checkTypeAnnotationsHelper() throws IOException {
        // the number of Runtime(In)VisibleAnnotations
        int count = buffer.readUnsignedShort();

        for (int i = 0; i < count; ++i) {
            String descriptor = readAnnotation();
            if (annotationDescriptor.equals(descriptor)) {
                return true;
            }
        }

        return false;
    }

    private String readAnnotation() throws IOException {
        String rawTypeName = resolveUtf8();
        // num_element_value_pairs
        int count = buffer.readUnsignedShort();

        for (int i = 0; i < count; ++i) {
            buffer.skipUnsignedShort();
            readAnnotationElementValue();
        }
        return rawTypeName;
    }

    private void readAnnotationElementValue() throws IOException {
        int tag = buffer.readUnsignedByte();

        switch (tag) {
        case BYTE:
        case CHAR:
        case DOUBLE:
        case FLOAT:
        case INT:
        case LONG:
        case SHORT:
        case BOOLEAN:
        case STRING:
            buffer.skipUnsignedShort();
            break;
        case ENUM:
            buffer.skipBytes(4); // 2 * u2
            break;
        case CLASS:
            buffer.skipUnsignedShort();
            break;
        case ANNOTATION:
            readAnnotation();
            break;
        case ARRAY:
            int count = buffer.readUnsignedShort();
            for (int i = 0; i < count; ++i) {
                readAnnotationElementValue();
            }
            break;
        default:
            throw new ClassFormatError("Not a valid annotation element type tag: 0x"
                    + Integer.toHexString(tag));
        }
    }

    /**
     * Look up the String value, identified by the u2 index value from constant pool (direct or
     * indirect).
     */
    private String resolveUtf8() throws IOException {
        int index = buffer.readUnsignedShort();
        return constantPool.getConstant(index);
    }

}

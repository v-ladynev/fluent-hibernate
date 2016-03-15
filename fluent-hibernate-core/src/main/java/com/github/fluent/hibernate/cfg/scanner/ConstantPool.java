package com.github.fluent.hibernate.cfg.scanner;

import java.io.DataInput;
import java.io.IOException;

/**
 *
 * @author V.Ladynev
 */
class ConstantPool {

    private static final int CP_UTF8 = 1;

    private static final int CP_INTEGER = 3;

    private static final int CP_FLOAT = 4;

    private static final int CP_LONG = 5;

    private static final int CP_DOUBLE = 6;

    private static final int CP_CLASS = 7;

    private static final int CP_STRING = 8;

    private static final int CP_REF_FIELD = 9;

    private static final int CP_REF_METHOD = 10;

    private static final int CP_REF_INTERFACE = 11;

    private static final int CP_NAME_AND_TYPE = 12;

    private static final int CP_METHOD_HANDLE = 15;

    private static final int CP_METHOD_TYPE = 16;

    private static final int CP_INVOKE_DYNAMIC = 18;

    private static final int BUFFER_SIZE = 8 * 1024;

    private Object[] buffer = new Object[BUFFER_SIZE];

    private DataInput di;

    public void readEntries(DataInput di) throws IOException {
        this.di = di;
        int count = di.readUnsignedShort();
        allocateBuffer(count);
        for (int i = 1; i < count; ++i) {
            if (readConstantPoolEntry(i)) {
                // double slot
                ++i;
            }
        }
    }

    private void allocateBuffer(int size) {
        if (buffer.length < size) {
            buffer = new Object[size];
        }
    }

    /**
     * Return {@code true} if a double slot is read (in case of Double or Long constant).
     */
    private boolean readConstantPoolEntry(int index) throws IOException {
        int tag = di.readUnsignedByte();
        switch (tag) {
        case CP_METHOD_TYPE:
            di.skipBytes(2); // readUnsignedShort()
            return false;
        case CP_METHOD_HANDLE:
            di.skipBytes(3);
            return false;
        case CP_INTEGER:
        case CP_FLOAT:
        case CP_REF_FIELD:
        case CP_REF_METHOD:
        case CP_REF_INTERFACE:
        case CP_NAME_AND_TYPE:
        case CP_INVOKE_DYNAMIC:
            di.skipBytes(4); // readInt() / readFloat() / readUnsignedShort() * 2
            return false;
        case CP_LONG:
        case CP_DOUBLE:
            di.skipBytes(8); // readLong() / readDouble()
            return true;
        case CP_UTF8:
            buffer[index] = di.readUTF();
            return false;
        case CP_CLASS:
        case CP_STRING:
            // reference to CP_UTF8 entry. The referenced index can have a higher number!
            buffer[index] = di.readUnsignedShort();
            return false;
        default:
            throw new ClassFormatError("Unkown tag value for constant pool entry: " + tag);
        }
    }

    /**
     * Look up the String value, identified by the u2 index value from constant pool (direct or
     * indirect).
     */
    public String getConstant(int index) {
        Object value = buffer[index];
        return (String) (value instanceof Integer ? buffer[(Integer) value] : value);
    }

}

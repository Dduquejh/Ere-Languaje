enum ObjectType {
    BOOLEAN,
    INTEGER,
    NULL,
    FLOAT,
    STRING
}

abstract class CustomObjects {
    public abstract ObjectType type();

    public abstract String inspect();
}

class IntegerObject extends CustomObjects {
    int value;

    public IntegerObject(int value) {
        this.value = value;
    }

    public ObjectType type() {
        return ObjectType.INTEGER;
    }

    public String inspect() {
        return String.valueOf(value);
    }

    public int getValue() {
        return value;
    }
}

class BooleanObject extends CustomObjects {
    boolean value;

    public BooleanObject(boolean value) {
        this.value = value;
    }

    public ObjectType type() {
        return ObjectType.BOOLEAN;
    }

    public String inspect() {
        return String.valueOf(value);
    }
}

class Null extends CustomObjects {
    public ObjectType type() {
        return ObjectType.NULL;
    }

    public String inspect() {
        return "nulo";
    }
}

class StringObject extends CustomObjects {
    String value;

    public StringObject(String value) {
        this.value = value;
    }

    public ObjectType type() {
        return ObjectType.STRING;
    }

    public String inspect() {
        return "\"" + value + "\"";
    }
    public String getValue() {
        return value;
    }
}


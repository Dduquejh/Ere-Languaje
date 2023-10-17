import java.util.HashMap;
enum ObjectType {
    BOOLEAN,
    INTEGER,
    NULL,
    FLOAT,
    ERROR,
    RETURN
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

class ReturnObject extends CustomObjects{
    Object value;

    public ReturnObject(Object value) {
        this.value = value;
    }

    @Override
    public String inspect() {
        return null;
    }

    @Override
    public ObjectType type() {
        return null;
    }

    public Object getValue() {
        return value;
    }    
}

class Error extends CustomObjects{
    String message;
    
    public Error(String message) {
        this.message = message;
    }

    @Override
    public String inspect() {
        return "Error: "+this.message;
    }

    @Override
    public ObjectType type() {
        return ObjectType.ERROR;
    }
    
}
class Environment{
    private HashMap<String, Object> store = new HashMap<>();

    public void set(String key, Object value){

    }

    public void delete(String key){
        store.remove(key);
    }

    public Object get(String key) {
        return store.get(key);
    }

}

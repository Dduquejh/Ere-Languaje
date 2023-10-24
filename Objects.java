import java.util.HashMap;
import java.util.List;
enum ObjectType {
    BOOLEAN,
    INTEGER,
    NULL,
    FLOAT,
    ERROR,
    RETURN,
    FUNCTION
}

abstract class CustomObjects {
    public abstract ObjectType type();

    public abstract String inspect();
}

class DefaultCustomObject extends CustomObjects{

    @Override
    public String inspect() {
        return null;
    }

    @Override
    public ObjectType type() {
        return null;
    }
    
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

    public CustomObjects get(String key) {
        Object value = store.get(key);
        if (value instanceof CustomObjects)
            return (CustomObjects) value;
        else 
            return null; 
    }
}


class FunctionObject extends CustomObjects{
    private List <Identifier> parameters;
    private Block body;
    private Environment env;
    
    public FunctionObject(List<Identifier> parameters, Block body, Environment env) {
        this.parameters = parameters;
        this.body = body;
        this.env = env;
    }

    @Override
    public String inspect() {
        String params = String.join(", ", parameters.stream().map(Object::toString).toArray(String[]::new));

        return "procedimiento(" + params + ") {\n" + body.toString() + "\n}";
    }

    @Override
    public ObjectType type() {
        return ObjectType.FUNCTION;
    }

    
}
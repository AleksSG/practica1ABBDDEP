package serviceLocator;

import java.util.HashMap;
import java.util.Map;

public class SimpleServiceLocator implements ServiceLocator {

    private enum ObjectType {SERVICE, CONSTANT};

    private Map<String, <Object, >> services = new HashMap<>();

    @Override
    public void setService(String name, Factory factory) throws LocatorError {

    }

    @Override
    public void setConstant(String name, Object value) throws LocatorError {

    }

    @Override
    public Object getObject(String name) throws LocatorError {
        return null;
    }
}

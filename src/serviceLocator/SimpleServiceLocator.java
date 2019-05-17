package serviceLocator;

import utils.Pair;

import java.util.HashMap;
import java.util.Map;

public class SimpleServiceLocator implements ServiceLocator {

    private enum ObjectType {SERVICE, CONSTANT};

    private final Map<String, Pair<ObjectType, Object>> services;

    public SimpleServiceLocator(){
        services = new HashMap<>();
    }

    @Override
    public void setService(String name, Factory factory) throws LocatorError {
        if(services.containsKey(name))
            throw new LocatorError(new IllegalArgumentException("This key already exists in the map."));
    }

    @Override
    public void setConstant(String name, Object value) throws LocatorError {

    }

    @Override
    public Object getObject(String name) throws LocatorError {
        return null;
    }
}

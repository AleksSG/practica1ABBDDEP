package serviceLocator;

import utils.Pair;

import java.util.HashMap;
import java.util.Map;

public class CachedServiceLocator implements ServiceLocator {

    private enum ObjectType {SERVICE, CONSTANT}

    private final Map<String, Pair<ObjectType, Object>> services;

    public CachedServiceLocator(){
        services = new HashMap<>();
    }

    @Override
    public synchronized void setService(String name, Factory factory) throws LocatorError {
        if(services.containsKey(name))
            throw new LocatorError(new IllegalArgumentException("The key already exists in the map."));
        services.put(name, new Pair<>(ObjectType.SERVICE, factory));
    }

    @Override
    public synchronized void setConstant(String name, Object value) throws LocatorError {
        if(services.containsKey(name))
            throw new LocatorError(new IllegalArgumentException("The key already exists in the map."));
        services.put(name, new Pair<>(ObjectType.CONSTANT, value));
    }

    @Override
    public synchronized Object getObject(String name) throws LocatorError {
        Pair<ObjectType, Object> value = services.get(name);
        if(value == null)
            throw new LocatorError(new IllegalArgumentException("The key was not found in the map."));

        switch(value.getFirst()) {
            case SERVICE:
                Object newObject = ((Factory)value.getSecond()).create(this);
                services.put(name, new Pair<>(ObjectType.CONSTANT, newObject));
                return newObject;
            case CONSTANT:
                return value.getSecond();
        }

        throw new LocatorError(new IllegalArgumentException("The ObjectType was neither SERVICE nor CONSTANT."));
    }
}

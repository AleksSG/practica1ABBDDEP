package servicelocator;

import utils.Pair;

import java.util.HashMap;
import java.util.Map;

public class SimpleServiceLocator implements ServiceLocator {

    private enum ObjectType {SERVICE, CONSTANT}

    private final Map<String, Pair<ObjectType, Object>> services;

    SimpleServiceLocator(){
        services = new HashMap<>();
    }

    @Override
    public void setService(String name, Factory factory) throws LocatorError {
        if(services.containsKey(name))
            throw new LocatorError(new IllegalArgumentException("The key already exists in the map."));
        services.put(name, new Pair<>(ObjectType.SERVICE, factory));
    }

    @Override
    public void setConstant(String name, Object value) throws LocatorError {
        if(services.containsKey(name))
            throw new LocatorError(new IllegalArgumentException("The key already exists in the map."));
        services.put(name, new Pair<>(ObjectType.CONSTANT, value));
    }

    @Override
    public Object getObject(String name) throws LocatorError {
        Pair<ObjectType, Object> value = services.get(name);
        if(value == null)
            throw new LocatorError(new IllegalArgumentException("The key was not found in the map."));

        switch(value.getFirst()) {
            case SERVICE:
                return ((Factory)value.getSecond()).create(this);
            case CONSTANT:
                return value.getSecond();
        }

        throw new LocatorError(new IllegalArgumentException("The ObjectType was neither SERVICE nor CONSTANT."));
    }

    public int getServicesLength() {
        return this.services.size();
    }
}

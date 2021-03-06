package servicelocator2;

import utils.Pair;

import java.util.HashMap;
import java.util.Map;

public class CachedServiceLocator implements ServiceLocator {

    private enum ObjectType {SERVICE, CONSTANT}

    private final Map<Class, Pair<ObjectType, Object>> services;

    CachedServiceLocator(){
        services = new HashMap<>();
    }

    @Override
    public <T> void setService(Class<T> klass, Factory<T> factory) throws LocatorError {
        if(services.containsKey(klass))
            throw new LocatorError(new IllegalArgumentException("The key already exists in the map."));
        services.put(klass, new Pair<>(ObjectType.SERVICE, factory));
    }

    @Override
    public <T> void setConstant(Class<T> klass, T value) throws LocatorError {
        if(services.containsKey(klass))
            throw new LocatorError(new IllegalArgumentException("The key already exists in the map."));
        services.put(klass, new Pair<>(ObjectType.CONSTANT, value));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getObject(Class<T> klass) throws LocatorError {
        Pair<ObjectType, Object> value = services.get(klass);
        if(value == null)
            throw new LocatorError(new IllegalArgumentException("The key was not found in the map."));

        switch(value.getFirst()) {
            case SERVICE:
                T newObject = ((Factory<T>)value.getSecond()).create(this);
                services.put(klass, new Pair<>(ObjectType.CONSTANT, newObject));
                return newObject;
            case CONSTANT:
                return (T) value.getSecond();
        }

        throw new LocatorError(new IllegalArgumentException("The ObjectType was neither SERVICE nor CONSTANT."));
    }

    public int getServicesLength() {
        return this.services.size();
    }
}

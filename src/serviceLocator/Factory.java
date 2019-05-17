package serviceLocator;

public interface Factory {
    Object create(ServiceLocator sl) throws LocatorError;
}

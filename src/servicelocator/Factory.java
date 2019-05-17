package servicelocator;

public interface Factory {
    Object create(ServiceLocator sl) throws LocatorError;
}

package serviceLocator;

public interface ServiceLocator {
    void setService(String name, Factory factory) throws LocatorError;
    void setConstant(String name, Object value) throws LocatorError;
    Object getObject(String name) throws LocatorError;
}

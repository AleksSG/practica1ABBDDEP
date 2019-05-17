package servicelocator2;

import servicelocator2.LocatorError;
import servicelocator2.ServiceLocator;

public interface Factory<T> {
    T create(ServiceLocator sl) throws LocatorError;
}

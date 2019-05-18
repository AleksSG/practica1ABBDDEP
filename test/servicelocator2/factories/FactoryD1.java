package servicelocator2.factories;

import implementations.ImplementationD1;
import interfaces.InterfaceD;
import servicelocator2.Factory;
import servicelocator2.LocatorError;
import servicelocator2.ServiceLocator;

public class FactoryD1 implements Factory<InterfaceD> {
    @Override
    public InterfaceD create(ServiceLocator sl) throws LocatorError {
        try {
            Integer d = sl.getObject(Integer.class);
            return new ImplementationD1(d);
        } catch (ClassCastException ex) {
            throw new LocatorError(ex);
        }
    }
}

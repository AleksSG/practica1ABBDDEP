package servicelocator.factories;

import implementations.ImplementationD1;
import interfaces.InterfaceD;
import servicelocator.Factory;
import servicelocator.LocatorError;
import servicelocator.ServiceLocator;

public class FactoryD1 implements Factory {

    @Override
    public InterfaceD create(ServiceLocator sl) throws LocatorError {
        try {
            Integer d = (Integer) sl.getObject("Constant Integer");
            return new ImplementationD1(d);
        } catch (ClassCastException ex) {
            throw new LocatorError(ex);
        }
    }
}

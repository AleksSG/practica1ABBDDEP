package servicelocator2.factories;

import implementations.ImplementationC1;
import interfaces.InterfaceC;
import servicelocator2.Factory;
import servicelocator2.LocatorError;
import servicelocator2.ServiceLocator;

public class FactoryC1 implements Factory<InterfaceC> {
    @Override
    public InterfaceC create(ServiceLocator sl) throws LocatorError {
        try {
            String c = sl.getObject(String.class);
            return new ImplementationC1(c);
        } catch (ClassCastException ex) {
            throw new LocatorError(ex);
        }
    }
}

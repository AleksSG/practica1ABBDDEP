package servicelocator.factories;

import implementations.ImplementationC1;
import interfaces.InterfaceC;
import servicelocator.Factory;
import servicelocator.LocatorError;
import servicelocator.ServiceLocator;

public class FactoryC1 implements Factory {


    @Override
    public InterfaceC create(ServiceLocator sl) throws LocatorError {
        try {
            String c = (String) sl.getObject("Constant String");
            return new ImplementationC1(c);
        } catch (ClassCastException ex) {
            throw new LocatorError(ex);
        }
    }
}

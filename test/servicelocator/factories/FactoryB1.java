package servicelocator.factories;

import implementations.ImplementationB1;
import interfaces.InterfaceB;
import interfaces.InterfaceD;
import servicelocator.Factory;
import servicelocator.LocatorError;
import servicelocator.ServiceLocator;

public class FactoryB1 implements Factory {

    @Override
    public InterfaceB create(ServiceLocator sl) throws LocatorError {
        try {
            InterfaceD d = (InterfaceD) sl.getObject("D");
            return new ImplementationB1(d);
        } catch (ClassCastException ex) {
            throw new LocatorError(ex);
        }
    }
}

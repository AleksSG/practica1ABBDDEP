package servicelocator2.factories;

import implementations.ImplementationB1;
import interfaces.InterfaceB;
import interfaces.InterfaceD;
import servicelocator2.Factory;
import servicelocator2.LocatorError;
import servicelocator2.ServiceLocator;

public class FactoryB1 implements Factory<InterfaceB> {
    @Override
    public InterfaceB create(ServiceLocator sl) throws LocatorError {
        InterfaceD d = sl.getObject(InterfaceD.class);
        return new ImplementationB1(d);
    }
}

package servicelocator2.factories;

import interfaces.InterfaceA;
import interfaces.InterfaceB;
import interfaces.InterfaceC;
import servicelocator2.Factory;
import servicelocator2.LocatorError;
import servicelocator2.ServiceLocator;
import implementations.ImplementationA1;

public class FactoryA1 implements Factory<InterfaceA> {
    @Override
    public InterfaceA create(ServiceLocator sl) throws LocatorError {
        try {
            InterfaceB b = sl.getObject(InterfaceB.class);
            InterfaceC c = sl.getObject(InterfaceC.class);
            return new ImplementationA1(b,c);
        } catch (ClassCastException ex) {
            throw new LocatorError(ex);
        }
    }
}

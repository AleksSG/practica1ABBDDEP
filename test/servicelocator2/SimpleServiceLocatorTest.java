package servicelocator2;

import interfaces.InterfaceA;
import interfaces.InterfaceB;
import interfaces.InterfaceC;
import interfaces.InterfaceD;

import static org.junit.jupiter.api.Assertions.assertNotSame;

class SimpleServiceLocatorTest extends ServiceLocatorTest {

    @Override
    ServiceLocator createServiceLocator() {
        return new SimpleServiceLocator();
    }

    @Override
    void checkObjectReturnsReferences() throws LocatorError {
        assertNotSame(sl.getObject(InterfaceA.class), sl.getObject(InterfaceA.class));
        assertNotSame(sl.getObject(InterfaceB.class), sl.getObject(InterfaceB.class));
        assertNotSame(sl.getObject(InterfaceC.class), sl.getObject(InterfaceC.class));
        assertNotSame(sl.getObject(InterfaceD.class), sl.getObject(InterfaceD.class));
    }
}
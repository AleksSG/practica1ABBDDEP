package servicelocator2;

import interfaces.InterfaceA;
import interfaces.InterfaceB;
import interfaces.InterfaceC;
import interfaces.InterfaceD;

import static org.junit.jupiter.api.Assertions.assertSame;

class CachedServiceLocatorTest extends ServiceLocatorTest {

    @Override
    ServiceLocator createServiceLocator() {
        return new CachedServiceLocator();
    }

    @Override
    void checkObjectReturnsReferences() throws LocatorError {
        assertSame(sl.getObject(InterfaceA.class), sl.getObject(InterfaceA.class));
        assertSame(sl.getObject(InterfaceB.class), sl.getObject(InterfaceB.class));
        assertSame(sl.getObject(InterfaceC.class), sl.getObject(InterfaceC.class));
        assertSame(sl.getObject(InterfaceD.class), sl.getObject(InterfaceD.class));
    }
}
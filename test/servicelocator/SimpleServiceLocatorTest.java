package servicelocator;

import static org.junit.jupiter.api.Assertions.assertNotSame;

class SimpleServiceLocatorTest extends ServiceLocatorTest {

    @Override
    ServiceLocator createServiceLocator() {
        return new SimpleServiceLocator();
    }

    @Override
    void checkObjectReturnsReferences() throws LocatorError {
        assertNotSame(sl.getObject("A"), sl.getObject("A"));
        assertNotSame(sl.getObject("B"), sl.getObject("B"));
        assertNotSame(sl.getObject("C"), sl.getObject("C"));
        assertNotSame(sl.getObject("D"), sl.getObject("D"));
    }
}
package servicelocator;

import static org.junit.jupiter.api.Assertions.assertSame;

class CachedServiceLocatorTest extends ServiceLocatorTest {

    @Override
    ServiceLocator createServiceLocator() {
        return new CachedServiceLocator();
    }

    @Override
    void checkObjectReturnsReferences() throws LocatorError {
        assertSame(sl.getObject("A"), sl.getObject("A"));
        assertSame(sl.getObject("B"), sl.getObject("B"));
        assertSame(sl.getObject("C"), sl.getObject("C"));
        assertSame(sl.getObject("D"), sl.getObject("D"));
    }
}
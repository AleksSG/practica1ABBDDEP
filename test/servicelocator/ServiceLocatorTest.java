package servicelocator;

import implementations.ImplementationA1;
import implementations.ImplementationB1;
import implementations.ImplementationC1;
import implementations.ImplementationD1;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import servicelocator.factories.FactoryA1;
import servicelocator.factories.FactoryB1;
import servicelocator.factories.FactoryC1;
import servicelocator.factories.FactoryD1;

import static org.junit.jupiter.api.Assertions.*;

abstract class ServiceLocatorTest {

    protected ServiceLocator sl;

    private Integer constantInteger;
    private String constantString;

    private Factory factoryA;
    private Factory factoryB;
    private Factory factoryC;
    private Factory factoryD;


    @BeforeEach
    void setUp(){
        sl = createServiceLocator();

        constantInteger = 10;
        constantString = "String";

        factoryA = new FactoryA1();
        factoryB = new FactoryB1();
        factoryC = new FactoryC1();
        factoryD = new FactoryD1();
    }

    abstract ServiceLocator createServiceLocator();

    @Test
    void setServicesCorrectly() {
        assertEquals(0, sl.getServicesLength());
        addServices();
        assertEquals(4, sl.getServicesLength());
    }

    @Test
    void setServicesRepeatedKey() {
        addServices();
        assertThrows(LocatorError.class, () -> sl.setService("A", factoryA));
        assertThrows(LocatorError.class, () -> sl.setService("B", factoryB));
        assertThrows(LocatorError.class, () -> sl.setService("C", factoryC));
        assertThrows(LocatorError.class, () -> sl.setService("D", factoryD));
    }

    @Test
    void setConstantsCorrectly() {
        assertEquals(0, sl.getServicesLength());
        addConstant();
        assertEquals(2, sl.getServicesLength());
        addFactoriesAsConstant();
        assertEquals(6, sl.getServicesLength());
    }

    @Test
    void setConstantsRepeatedKey() {
        addConstant();
        assertThrows(LocatorError.class, () -> sl.setConstant("Constant Integer", 100));
        assertThrows(LocatorError.class, () -> sl.setConstant("Constant String", "Diferent Value"));
        assertDoesNotThrow(() -> sl.setConstant("Diferent Key", "Diferent Value"));
    }

    @Test
    void checkDoesNotThrowWhenAllDependenciesAreThere() {
        addServices();
        addConstant();
        assertDoesNotThrow(() -> sl.getObject("A"));
    }

    @Test
    void checkThrowsWhenNotAllDependenciesAreThere() {
        try {
            sl.setService("A", factoryA);
            sl.setService("B", factoryB);
            sl.setService("C", factoryC);
        }
        catch(Exception e) {
            fail(e.toString());
        }
        assertThrows(LocatorError.class, () -> sl.getObject("A"));
    }

    @Test
    void getNoExistingKeyError() {
        assertThrows(LocatorError.class, () -> sl.getObject("Not Existing Key"));
    }

    @Test
    void getObjectsFromConstantsCheck() {
        addConstant();
        addFactoriesAsConstant();

        try {
            //Check if they are Objects (constants).
            assertSame(sl.getObject("Constant Integer"), constantInteger);
            assertSame(sl.getObject("Constant String"), constantString);

            assertSame(sl.getObject("Constant factA"), factoryA);
            assertSame(sl.getObject("Constant factB"), factoryB);
            assertSame(sl.getObject("Constant factC"), factoryC);
            assertSame(sl.getObject("Constant factD"), factoryD);
        } catch (LocatorError e) {
            fail(e.toString());
        }
    }

    @Test
    void getObjectsFromFactoriesCheck() {
        addConstant();
        addServices();

        try {
            assertTrue(sl.getObject("A") instanceof ImplementationA1);
            assertTrue(sl.getObject("B") instanceof ImplementationB1);
            assertTrue(sl.getObject("C") instanceof ImplementationC1);
            assertTrue(sl.getObject("D") instanceof ImplementationD1);
        } catch (LocatorError e) {
            fail(e.toString());
        }
    }

    @Test
    void getObjectReturnsReferencesOnFactories() {
        addConstant();
        addServices();

        try {
            checkObjectReturnsReferences();
        } catch (LocatorError e) {
            fail(e.toString());
        }
    }

    abstract void checkObjectReturnsReferences() throws LocatorError;

    private void addServices() {
        try {
            sl.setService("A", factoryA);
            sl.setService("B", factoryB);
            sl.setService("C", factoryC);
            sl.setService("D", factoryD);
        } catch (LocatorError e) {
            fail(e.toString());
        }
    }

    private void addConstant() {
        try {
            sl.setConstant("Constant Integer", constantInteger);
            sl.setConstant("Constant String", constantString);
        } catch (LocatorError e) {
            fail(e.toString());
        }
    }

    private void addFactoriesAsConstant() {
        try {
            sl.setConstant("Constant factA", factoryA);
            sl.setConstant("Constant factB", factoryB);
            sl.setConstant("Constant factC", factoryC);
            sl.setConstant("Constant factD", factoryD);
        } catch (LocatorError e) {
            fail(e.toString());
        }
    }
}
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

class CachedServiceLocatorTest {

    private CachedServiceLocator csl;

    private Integer constantInteger;
    private String constantString;

    private Factory factoryA;
    private Factory factoryB;
    private Factory factoryC;
    private Factory factoryD;


    @BeforeEach
    void setUp(){
        csl = new CachedServiceLocator();

        constantInteger = 10;
        constantString = "String";

        factoryA = new FactoryA1();
        factoryB = new FactoryB1();
        factoryC = new FactoryC1();
        factoryD = new FactoryD1();
    }

    @Test
    void setServicesCorrectly() {
        assertEquals(0, csl.getServicesLenght());
        addServices();
        assertEquals(4, csl.getServicesLenght());
    }

    @Test
    void setServicesRepeatedKey() {
        addServices();
        assertThrows(LocatorError.class, () -> csl.setService("A", factoryA));
        assertThrows(LocatorError.class, () -> csl.setService("B", factoryB));
        assertThrows(LocatorError.class, () -> csl.setService("C", factoryC));
        assertThrows(LocatorError.class, () -> csl.setService("D", factoryD));
    }

    @Test
    void setConstantsCorrectly() {
        assertEquals(0, csl.getServicesLenght());
        addConstant();
        assertEquals(2, csl.getServicesLenght());
        addFactoriesAsConstant();
        assertEquals(6, csl.getServicesLenght());
    }

    @Test
    void setConstantsRepeatedKey() {
        addConstant();
        assertThrows(LocatorError.class, () -> csl.setConstant("Constant Integer", 100));
        assertThrows(LocatorError.class, () -> csl.setConstant("Constant String", "Diferent Value"));
        assertDoesNotThrow(() -> csl.setConstant("Diferent Key", "Diferent Value"));
    }

    @Test
    void checkDoesNotThrowWhenAllDependenciesAreThere() {
        addServices();
        addConstant();
        assertDoesNotThrow(() -> csl.getObject("A"));
    }

    @Test
    void checkThrowsWhenNotAllDependenciesAreThere() {
        try {
            csl.setService("A", factoryA);
            csl.setService("B", factoryB);
            csl.setService("C", factoryC);
        }
        catch(Exception e) {
            fail(e.toString());
        }
        assertThrows(LocatorError.class, () -> csl.getObject("A"));
    }

    @Test
    void getNoExistingKeyError() {
        assertThrows(LocatorError.class, () -> csl.getObject("Not Existing Key"));
    }

    @Test
    void getObjectsFromConstantsCheck() {
        addConstant();
        addFactoriesAsConstant();

        try {
            //Check if they are Objects (constants).
            assertSame(csl.getObject("Constant Integer"), constantInteger);
            assertSame(csl.getObject("Constant String"), constantString);

            assertSame(csl.getObject("Constant factA"), factoryA);
            assertSame(csl.getObject("Constant factB"), factoryB);
            assertSame(csl.getObject("Constant factC"), factoryC);
            assertSame(csl.getObject("Constant factD"), factoryD);
        } catch (LocatorError e) {
            fail(e.toString());
        }
    }

    @Test
    void getObjectsFromFactoriesCheck() {
        addConstant();
        addServices();

        try {
            assertTrue(csl.getObject("A") instanceof ImplementationA1);
            assertTrue(csl.getObject("B") instanceof ImplementationB1);
            assertTrue(csl.getObject("C") instanceof ImplementationC1);
            assertTrue(csl.getObject("D") instanceof ImplementationD1);
        } catch (LocatorError e) {
            fail(e.toString());
        }
    }

    @Test
    void getObjectReturnsDifferentImplementationOnFactories() {
        addConstant();
        addServices();

        try {
            assertSame(csl.getObject("A"), csl.getObject("A"));
            assertSame(csl.getObject("B"), csl.getObject("B"));
            assertSame(csl.getObject("C"), csl.getObject("C"));
            assertSame(csl.getObject("D"), csl.getObject("D"));
        } catch (LocatorError e) {
            fail(e.toString());
        }
    }

    private void addServices() {
        try {
            csl.setService("A", factoryA);
            csl.setService("B", factoryB);
            csl.setService("C", factoryC);
            csl.setService("D", factoryD);
        } catch (LocatorError e) {
            fail(e.toString());
        }
    }

    private void addConstant() {
        try {
            csl.setConstant("Constant Integer", constantInteger);
            csl.setConstant("Constant String", constantString);
        } catch (LocatorError e) {
            fail(e.toString());
        }
    }

    private void addFactoriesAsConstant() {
        try {
            csl.setConstant("Constant factA", factoryA);
            csl.setConstant("Constant factB", factoryB);
            csl.setConstant("Constant factC", factoryC);
            csl.setConstant("Constant factD", factoryD);
        } catch (LocatorError e) {
            fail(e.toString());
        }
    }
}
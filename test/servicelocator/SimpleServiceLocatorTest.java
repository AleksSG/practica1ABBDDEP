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

class SimpleServiceLocatorTest {

    private SimpleServiceLocator ssl;

    private Integer constantInteger;
    private String constantString;

    private Factory factoryA;
    private Factory factoryB;
    private Factory factoryC;
    private Factory factoryD;


    @BeforeEach
    void setUp(){
        ssl = new SimpleServiceLocator();

        constantInteger = 10;
        constantString = "String";

        factoryA = new FactoryA1();
        factoryB = new FactoryB1();
        factoryC = new FactoryC1();
        factoryD = new FactoryD1();
    }

    @Test
    void setServicesCorrectly() {
        assertEquals(0, ssl.getServicesLenght());
        addServices();
        assertEquals(4, ssl.getServicesLenght());
    }

    @Test
    void setServicesRepeatedKey() {
        addServices();
        assertThrows(LocatorError.class, () -> ssl.setService("A", factoryA));
        assertThrows(LocatorError.class, () -> ssl.setService("B", factoryB));
        assertThrows(LocatorError.class, () -> ssl.setService("C", factoryC));
        assertThrows(LocatorError.class, () -> ssl.setService("D", factoryD));
    }

    @Test
    void setConstantsCorrectly() {
        assertEquals(0, ssl.getServicesLenght());
        addConstant();
        assertEquals(2, ssl.getServicesLenght());
        addFactoriesAsConstant();
        assertEquals(6, ssl.getServicesLenght());
    }

    @Test
    void setConstantsRepeatedKey() {
        addConstant();
        assertThrows(LocatorError.class, () -> ssl.setConstant("Constant Integer", 100));
        assertThrows(LocatorError.class, () -> ssl.setConstant("Constant String", "Diferent Value"));
        assertDoesNotThrow(() -> ssl.setConstant("Diferent Key", "Diferent Value"));
    }

    @Test
    void checkDoesNotThrowWhenAllDependenciesAreThere() {
        addServices();
        addConstant();
        assertDoesNotThrow(() -> ssl.getObject("A"));
    }

    @Test
    void checkThrowsWhenNotAllDependenciesAreThere() {
        try {
            ssl.setService("A", factoryA);
            ssl.setService("B", factoryB);
            ssl.setService("C", factoryC);
        }
        catch(Exception e) {
            fail(e.toString());
        }
        assertThrows(LocatorError.class, () -> ssl.getObject("A"));
    }

    @Test
    void getNoExistingKeyError() {
        assertThrows(LocatorError.class, () -> ssl.getObject("Not Existing Key"));
    }

    @Test
    void getObjectsFromConstantsCheck() {
        addConstant();
        addFactoriesAsConstant();

        try {
            //Check if they are Objects (constants).
            assertSame(ssl.getObject("Constant Integer"), constantInteger);
            assertSame(ssl.getObject("Constant String"), constantString);

            assertSame(ssl.getObject("Constant factA"), factoryA);
            assertSame(ssl.getObject("Constant factB"), factoryB);
            assertSame(ssl.getObject("Constant factC"), factoryC);
            assertSame(ssl.getObject("Constant factD"), factoryD);
        } catch (LocatorError e) {
            fail(e.toString());
        }
    }

    @Test
    void getObjectsFromFactoriesCheck() {
        addConstant();
        addServices();

        try {
            assertTrue(ssl.getObject("A") instanceof ImplementationA1);
            assertTrue(ssl.getObject("B") instanceof ImplementationB1);
            assertTrue(ssl.getObject("C") instanceof ImplementationC1);
            assertTrue(ssl.getObject("D") instanceof ImplementationD1);
        } catch (LocatorError e) {
            fail(e.toString());
        }
    }

    @Test
    void getObjectReturnsDifferentImplementationOnFactories() {
        addConstant();
        addServices();

        try {
            assertNotSame(ssl.getObject("A"), ssl.getObject("A"));
            assertNotSame(ssl.getObject("B"), ssl.getObject("B"));
            assertNotSame(ssl.getObject("C"), ssl.getObject("C"));
            assertNotSame(ssl.getObject("D"), ssl.getObject("D"));
        } catch (LocatorError e) {
            fail(e.toString());
        }
    }

    private void addServices() {
        try {
            ssl.setService("A", factoryA);
            ssl.setService("B", factoryB);
            ssl.setService("C", factoryC);
            ssl.setService("D", factoryD);
        } catch (LocatorError e) {
            fail(e.toString());
        }
    }

    private void addConstant() {
        try {
            ssl.setConstant("Constant Integer", constantInteger);
            ssl.setConstant("Constant String", constantString);
        } catch (LocatorError e) {
            fail(e.toString());
        }
    }

    private void addFactoriesAsConstant() {
        try {
            ssl.setConstant("Constant factA", factoryA);
            ssl.setConstant("Constant factB", factoryB);
            ssl.setConstant("Constant factC", factoryC);
            ssl.setConstant("Constant factD", factoryD);
        } catch (LocatorError e) {
            fail(e.toString());
        }
    }
}
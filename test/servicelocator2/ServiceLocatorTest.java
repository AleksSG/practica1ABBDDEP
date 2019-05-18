package servicelocator2;

import implementations.ImplementationA1;
import implementations.ImplementationB1;
import implementations.ImplementationC1;
import implementations.ImplementationD1;
import interfaces.InterfaceA;
import interfaces.InterfaceB;
import interfaces.InterfaceC;
import interfaces.InterfaceD;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import servicelocator2.factories.FactoryA1;
import servicelocator2.factories.FactoryB1;
import servicelocator2.factories.FactoryC1;
import servicelocator2.factories.FactoryD1;

import static org.junit.jupiter.api.Assertions.*;

abstract class ServiceLocatorTest {

    protected ServiceLocator sl;

    private Integer constantInteger;
    private String constantString;

    private FactoryA1 factoryA;
    private FactoryB1 factoryB;
    private FactoryC1 factoryC;
    private FactoryD1 factoryD;

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

    @Test
    void setServicesCorrectly() {
        assertEquals(0, sl.getServicesLength());
        addServices();
        assertEquals(4, sl.getServicesLength());
    }

    abstract ServiceLocator createServiceLocator();

    @Test
    void setServicesRepeatedKey() {
        addServices();
        assertThrows(LocatorError.class, () -> sl.setService(InterfaceA.class, factoryA));
        assertThrows(LocatorError.class, () -> sl.setService(InterfaceB.class, factoryB));
        assertThrows(LocatorError.class, () -> sl.setService(InterfaceC.class, factoryC));
        assertThrows(LocatorError.class, () -> sl.setService(InterfaceD.class, factoryD));

    }

    @Test
    void setConstantsCorrectly() {
        assertEquals(0, sl.getServicesLength());
        addConstant();
        assertEquals(2, sl.getServicesLength());
        addAllFactoriesAsConstants();
        assertEquals(6, sl.getServicesLength());
    }

    @Test
    void checkDoesNotThrowWhenAllDependenciesAreThere() {
        addServices();
        addConstant();
        assertDoesNotThrow(() -> sl.getObject(InterfaceA.class));
    }

    @Test
    void checkThrowsWhenNotAllDependenciesAreThere() {
        try {
            sl.setService(InterfaceA.class, factoryA);
            sl.setService(InterfaceB.class, factoryB);
            sl.setService(InterfaceC.class, factoryC);
        }
        catch(Exception e) {
            fail(e.toString());
        }
        assertThrows(LocatorError.class, () -> sl.getObject(InterfaceA.class));
    }

    @Test
    void getNoExistingKeyError() {
        assertThrows(LocatorError.class, () -> sl.getObject(String.class));
    }

    @Test
    void getObjectsFromConstantsCheck() {
        addConstant();
        addAllFactoriesAsConstants();

        try {
            //Check if they are Objects (constants).
            assertSame(sl.getObject(Integer.class), constantInteger);
            assertSame(sl.getObject(String.class), constantString);

            assertSame(sl.getObject(FactoryA1.class), factoryA);
            assertSame(sl.getObject(FactoryB1.class), factoryB);
            assertSame(sl.getObject(FactoryC1.class), factoryC);
            assertSame(sl.getObject(FactoryD1.class), factoryD);
        } catch (LocatorError e) {
            fail(e.toString());
        }
    }

    @Test
    void getObjectsFromFactoriesCheck() {
        addConstant();
        addServices();

        try {
            assertTrue(sl.getObject(InterfaceA.class) instanceof ImplementationA1);
            assertTrue(sl.getObject(InterfaceB.class) instanceof ImplementationB1);
            assertTrue(sl.getObject(InterfaceC.class) instanceof ImplementationC1);
            assertTrue(sl.getObject(InterfaceD.class) instanceof ImplementationD1);
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
            sl.setService(InterfaceA.class, factoryA);
            sl.setService(InterfaceB.class, factoryB);
            sl.setService(InterfaceC.class, factoryC);
            sl.setService(InterfaceD.class, factoryD);
        } catch (LocatorError e) {
            fail(e.toString());
        }
    }

    private void addConstant() {
        try {
            sl.setConstant(String.class, constantString);
            sl.setConstant(Integer.class, constantInteger);
        } catch (LocatorError e) {
            fail(e.toString());
        }
    }

    private void addAllFactoriesAsConstants() {
        try {
            sl.setConstant(FactoryA1.class, factoryA);
            sl.setConstant(FactoryB1.class, factoryB);
            sl.setConstant(FactoryC1.class, factoryC);
            sl.setConstant(FactoryD1.class, factoryD);
        } catch (LocatorError e) {
            fail(e.toString());
        }
    }
}

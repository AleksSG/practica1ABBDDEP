package servicelocator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CachedServiceLocatorTest {

    class ImplementationA1 implements InterfaceA {
        private InterfaceB b;
        private InterfaceC c;
        ImplementationA1(InterfaceB b, InterfaceC c) {
            this.b = b; this.c = c;
        }
    }

    class FactoryA1 implements Factory {

        @Override
        public InterfaceA create(ServiceLocator sl) throws LocatorError {
            try {
                InterfaceB b = (InterfaceB) sl.getObject("B");
                InterfaceC c = (InterfaceC) sl.getObject("C");
                return new ImplementationA1(b,c);
            } catch (ClassCastException ex) {
                throw new LocatorError(ex);
            }
        }
    }

    class ImplementationB1 implements InterfaceB {
        private InterfaceD d;
        ImplementationB1(InterfaceD d) {
            this.d = d;
        }
    }

    class FactoryB1 implements Factory {

        @Override
        public InterfaceB create(ServiceLocator sl) throws LocatorError {
            try {
                InterfaceD d = (InterfaceD) sl.getObject("D");
                return new ImplementationB1(d);
            } catch (ClassCastException ex) {
                throw new LocatorError(ex);
            }
        }
    }

    class ImplementationC1 implements InterfaceC {
        private String s;
        ImplementationC1(String s) {
            this.s = s;
        }
    }

    class FactoryC1 implements Factory {


        @Override
        public InterfaceC create(ServiceLocator sl) throws LocatorError {
            try {
                String c = (String) sl.getObject("Constant String");
                return new ImplementationC1(c);
            } catch (ClassCastException ex) {
                throw new LocatorError(ex);
            }
        }
    }

    class ImplementationD1 implements InterfaceD {
        private int i;
        ImplementationD1(int i) {
            this.i = i;
        }
    }

    class FactoryD1 implements Factory {

        @Override
        public InterfaceD create(ServiceLocator sl) throws LocatorError {
            try {
                Integer d = (Integer) sl.getObject("Constant Integer");
                return new ImplementationD1(d);
            } catch (ClassCastException ex) {
                throw new LocatorError(ex);
            }
        }
    }


    private CachedServiceLocator ssl;

    private Integer constantInteger;
    private String constantString;

    private Factory factoryA;
    private Factory factoryB;
    private Factory factoryC;
    private Factory factoryD;


    @BeforeEach
    void setUp(){
        ssl = new CachedServiceLocator();

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
            assertSame(ssl.getObject("A"), ssl.getObject("A"));
            assertSame(ssl.getObject("B"), ssl.getObject("B"));
            assertSame(ssl.getObject("C"), ssl.getObject("C"));
            assertSame(ssl.getObject("D"), ssl.getObject("D"));
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
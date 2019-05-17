package servicelocator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimpleServiceLocatorTest {

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


    private SimpleServiceLocator ssl;

    private Integer constantInteger;
    private String constantString;

    private InterfaceA constantA;
    private InterfaceB constantB;
    private InterfaceC constantC;
    private InterfaceD constantD;

    private Factory factoryA;
    private Factory factoryB;
    private Factory factoryC;
    private Factory factoryD;


    @BeforeEach
    void setUp(){
        ssl = new SimpleServiceLocator();

        constantInteger = 10;
        constantString = "String";
//        constantC = new ImplementationC1("Interface C1");
//        constantD = new ImplementationD1(20);
//        constantB = new ImplementationB1(constantD);
//        constantA = new ImplementationA1(constantB, constantC);

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
        assertThrows(LocatorError.class, () -> ssl.setService("factA", factoryA));
        assertThrows(LocatorError.class, () -> ssl.setService("factB", factoryB));
        assertThrows(LocatorError.class, () -> ssl.setService("factC", factoryC));
        assertThrows(LocatorError.class, () -> ssl.setService("factD", factoryD));
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
    void getObjectTypeCorrectly() {
        addServices();
        addConstant();
        addFactoriesAsConstant();

        try {
            //Check if they are Objects (constants).
            assertTrue(ssl.getObject("Constant Integer") instanceof Integer);
            assertTrue(ssl.getObject("Constant String") instanceof String);

            assertTrue(ssl.getObject("Constant factA") instanceof FactoryA1);
            assertTrue(ssl.getObject("Constant factB") instanceof FactoryB1);
            assertTrue(ssl.getObject("Constant factC") instanceof FactoryC1);
            assertTrue(ssl.getObject("Constant factD") instanceof FactoryD1);
        } catch (LocatorError e) {
            fail(e.toString());
        }

        //Check if they are Factories (services). "create" method cannot return new Instance due to no existing key "A" and throws an error. If it is thrown, "factA" is Service
        assertThrows(LocatorError.class, () -> ssl.getObject("factA"));
        assertThrows(LocatorError.class, () -> ssl.getObject("factB"));
        assertThrows(LocatorError.class, () -> ssl.getObject("factC"));
        assertThrows(LocatorError.class, () -> ssl.getObject("factD"));
    }

    @Test
    void getObjectCreateImplementations() {
        addServices();
        addConstant();
        addInterfacesAsConstant();

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
    void getObjectReturnsDifferentImplementation() {
        addServices();
        addConstant();
        addInterfacesAsConstant();

        try {
            //Both are created using "factX". The difference is that the X1 is stored in ssl to build the following implementations.
            //This way we do not repeat code, we just get from saved ssl and compare them.
            InterfaceD interD1 = (InterfaceD) ssl.getObject("D");
            InterfaceD interD2 = (InterfaceD) ssl.getObject("factD");
            assertFalse(interD1 == interD2);

            InterfaceC interC1 = (InterfaceC) ssl.getObject("C");
            InterfaceC interC2 = (InterfaceC) ssl.getObject("factC");
            assertFalse(interC1 == interC2);

            InterfaceB interB1 = (InterfaceB) ssl.getObject("B");
            InterfaceB interB2 = (InterfaceB) ssl.getObject("factB");
            assertFalse(interB1 == interB2);

            InterfaceA interA1 = (InterfaceA) ssl.getObject("A");
            InterfaceA interA2 = (InterfaceA) ssl.getObject("factA");
            assertFalse(interA1 == interA2);

        } catch (LocatorError e) {
            fail(e.toString());
        }
    }

    private void addServices() {
        try {
            ssl.setService("factA", factoryA);
            ssl.setService("factB", factoryB);
            ssl.setService("factC", factoryC);
            ssl.setService("factD", factoryD);
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

    private void addInterfacesAsConstant() {
        try {
            InterfaceD interD = (InterfaceD) ssl.getObject( "factD");
            ssl.setConstant("D", interD);
            InterfaceC interC = (InterfaceC) ssl.getObject( "factC");
            ssl.setConstant("C", interC);
            InterfaceB interB = (InterfaceB) ssl.getObject( "factB");
            ssl.setConstant("B", interB);
            InterfaceA interA = (InterfaceA) ssl.getObject( "factA");
            ssl.setConstant("A", interA);
        } catch (LocatorError e) {
            fail(e.toString());
        }

    }
}
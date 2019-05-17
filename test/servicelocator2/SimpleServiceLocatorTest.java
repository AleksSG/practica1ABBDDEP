package servicelocator2;

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

    class FactoryA1 implements Factory<InterfaceA> {

        @Override
        public InterfaceA create(ServiceLocator sl) throws LocatorError {
            try {
                InterfaceB b = sl.getObject(InterfaceB.class);
                InterfaceC c = sl.getObject(InterfaceC.class);
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

    class FactoryB1 implements Factory<InterfaceB> {

        @Override
        public InterfaceB create(ServiceLocator sl) throws LocatorError {
            try {
                InterfaceD d = sl.getObject(InterfaceD.class);
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

    class FactoryC1 implements Factory<InterfaceC> {


        @Override
        public InterfaceC create(ServiceLocator sl) throws LocatorError {
            try {
                String c = sl.getObject(String.class);
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

    class FactoryD1 implements Factory<InterfaceD> {

        @Override
        public InterfaceD create(ServiceLocator sl) throws LocatorError {
            try {
                Integer d = sl.getObject(Integer.class);
                return new ImplementationD1(d);
            } catch (ClassCastException ex) {
                throw new LocatorError(ex);
            }
        }
    }


    private SimpleServiceLocator ssl;

    private Integer constantInteger;
    private String constantString;

    private FactoryA1 factoryA;
    private FactoryB1 factoryB;
    private FactoryC1 factoryC;
    private FactoryD1 factoryD;

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
        assertThrows(LocatorError.class, () -> ssl.setService(InterfaceA.class, factoryA));
        assertThrows(LocatorError.class, () -> ssl.setService(InterfaceB.class, factoryB));
        assertThrows(LocatorError.class, () -> ssl.setService(InterfaceC.class, factoryC));
        assertThrows(LocatorError.class, () -> ssl.setService(InterfaceD.class, factoryD));

    }

    @Test
    void setConstantsCorrectly() {
        assertEquals(0, ssl.getServicesLenght());
        addConstant();
        assertEquals(2, ssl.getServicesLenght());
        addAllFactoriesAsConstants();
        assertEquals(6, ssl.getServicesLenght());
    }

    @Test
    void checkDoesNotThrowWhenAllDependenciesAreThere() {
        addServices();
        addConstant();
        assertDoesNotThrow(() -> ssl.getObject(InterfaceA.class));
    }

    @Test
    void checkThrowsWhenNotAllDependenciesAreThere() {
        try {
            ssl.setService(InterfaceA.class, factoryA);
            ssl.setService(InterfaceB.class, factoryB);
            ssl.setService(InterfaceC.class, factoryC);
        }
        catch(Exception e) {
            fail(e.toString());
        }
        assertThrows(LocatorError.class, () -> ssl.getObject(InterfaceA.class));
    }

    @Test
    void getNoExistingKeyError() {
        assertThrows(LocatorError.class, () -> ssl.getObject(String.class));
    }

    @Test
    void getObjectsFromConstantsCheck() {
        addConstant();
        addAllFactoriesAsConstants();

        try {
            //Check if they are Objects (constants).
            assertSame(ssl.getObject(Integer.class), constantInteger);
            assertSame(ssl.getObject(String.class), constantString);

            assertSame(ssl.getObject(FactoryA1.class), factoryA);
            assertSame(ssl.getObject(FactoryB1.class), factoryB);
            assertSame(ssl.getObject(FactoryC1.class), factoryC);
            assertSame(ssl.getObject(FactoryD1.class), factoryD);
        } catch (LocatorError e) {
            fail(e.toString());
        }
    }

    @Test
    void getObjectsFromFactoriesCheck() {
        addConstant();
        addServices();

        try {
            assertTrue(ssl.getObject(InterfaceA.class) instanceof ImplementationA1);
            assertTrue(ssl.getObject(InterfaceB.class) instanceof ImplementationB1);
            assertTrue(ssl.getObject(InterfaceC.class) instanceof ImplementationC1);
            assertTrue(ssl.getObject(InterfaceD.class) instanceof ImplementationD1);
        } catch (LocatorError e) {
            fail(e.toString());
        }
    }

    @Test
    void getObjectReturnsDifferentImplementationOnFactories() {
        addConstant();
        addServices();

        try {
            assertNotSame(ssl.getObject(InterfaceA.class), ssl.getObject(InterfaceA.class));
            assertNotSame(ssl.getObject(InterfaceB.class), ssl.getObject(InterfaceB.class));
            assertNotSame(ssl.getObject(InterfaceC.class), ssl.getObject(InterfaceC.class));
            assertNotSame(ssl.getObject(InterfaceD.class), ssl.getObject(InterfaceD.class));
        } catch (LocatorError e) {
            fail(e.toString());
        }
    }

    private void addServices() {
        try {
            ssl.setService(InterfaceA.class, factoryA);
            ssl.setService(InterfaceB.class, factoryB);
            ssl.setService(InterfaceC.class, factoryC);
            ssl.setService(InterfaceD.class, factoryD);
        } catch (LocatorError e) {
            fail(e.toString());
        }
    }

    private void addConstant() {
        try {
            ssl.setConstant(String.class, constantString);
            ssl.setConstant(Integer.class, constantInteger);
        } catch (LocatorError e) {
            fail(e.toString());
        }
    }

    private void addAllFactoriesAsConstants() {
        try {
            ssl.setConstant(FactoryA1.class, factoryA);
            ssl.setConstant(FactoryB1.class, factoryB);
            ssl.setConstant(FactoryC1.class, factoryC);
            ssl.setConstant(FactoryD1.class, factoryD);
        } catch (LocatorError e) {
            fail(e.toString());
        }
    }

}

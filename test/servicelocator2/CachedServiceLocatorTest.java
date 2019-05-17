package servicelocator2;

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


    private CachedServiceLocator csl;

    private Integer constantInteger;
    private String constantString;

    private FactoryA1 factoryA;
    private FactoryB1 factoryB;
    private FactoryC1 factoryC;
    private FactoryD1 factoryD;

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
        assertThrows(LocatorError.class, () -> csl.setService(InterfaceA.class, factoryA));
        assertThrows(LocatorError.class, () -> csl.setService(InterfaceB.class, factoryB));
        assertThrows(LocatorError.class, () -> csl.setService(InterfaceC.class, factoryC));
        assertThrows(LocatorError.class, () -> csl.setService(InterfaceD.class, factoryD));

    }

    @Test
    void setConstantsCorrectly() {
        assertEquals(0, csl.getServicesLenght());
        addConstant();
        assertEquals(2, csl.getServicesLenght());
        addAllFactoriesAsConstants();
        assertEquals(6, csl.getServicesLenght());
    }

    @Test
    void checkDoesNotThrowWhenAllDependenciesAreThere() {
        addServices();
        addConstant();
        assertDoesNotThrow(() -> csl.getObject(InterfaceA.class));
    }

    @Test
    void checkThrowsWhenNotAllDependenciesAreThere() {
        try {
            csl.setService(InterfaceA.class, factoryA);
            csl.setService(InterfaceB.class, factoryB);
            csl.setService(InterfaceC.class, factoryC);
        }
        catch(Exception e) {
            fail(e.toString());
        }
        assertThrows(LocatorError.class, () -> csl.getObject(InterfaceA.class));
    }

    @Test
    void getNoExistingKeyError() {
        assertThrows(LocatorError.class, () -> csl.getObject(String.class));
    }

    @Test
    void getObjectsFromConstantsCheck() {
        addConstant();
        addAllFactoriesAsConstants();

        try {
            //Check if they are Objects (constants).
            assertSame(csl.getObject(Integer.class), constantInteger);
            assertSame(csl.getObject(String.class), constantString);

            assertSame(csl.getObject(FactoryA1.class), factoryA);
            assertSame(csl.getObject(FactoryB1.class), factoryB);
            assertSame(csl.getObject(FactoryC1.class), factoryC);
            assertSame(csl.getObject(FactoryD1.class), factoryD);
        } catch (LocatorError e) {
            fail(e.toString());
        }
    }

    @Test
    void getObjectsFromFactoriesCheck() {
        addConstant();
        addServices();

        try {
            assertTrue(csl.getObject(InterfaceA.class) instanceof ImplementationA1);
            assertTrue(csl.getObject(InterfaceB.class) instanceof ImplementationB1);
            assertTrue(csl.getObject(InterfaceC.class) instanceof ImplementationC1);
            assertTrue(csl.getObject(InterfaceD.class) instanceof ImplementationD1);
        } catch (LocatorError e) {
            fail(e.toString());
        }
    }

    @Test
    void getObjectReturnsDifferentImplementationOnFactories() {
        addConstant();
        addServices();

        try {
            assertSame(csl.getObject(InterfaceA.class), csl.getObject(InterfaceA.class));
            assertSame(csl.getObject(InterfaceB.class), csl.getObject(InterfaceB.class));
            assertSame(csl.getObject(InterfaceC.class), csl.getObject(InterfaceC.class));
            assertSame(csl.getObject(InterfaceD.class), csl.getObject(InterfaceD.class));
        } catch (LocatorError e) {
            fail(e.toString());
        }
    }

    private void addServices() {
        try {
            csl.setService(InterfaceA.class, factoryA);
            csl.setService(InterfaceB.class, factoryB);
            csl.setService(InterfaceC.class, factoryC);
            csl.setService(InterfaceD.class, factoryD);
        } catch (LocatorError e) {
            fail(e.toString());
        }
    }

    private void addConstant() {
        try {
            csl.setConstant(String.class, constantString);
            csl.setConstant(Integer.class, constantInteger);
        } catch (LocatorError e) {
            fail(e.toString());
        }
    }

    private void addAllFactoriesAsConstants() {
        try {
            csl.setConstant(FactoryA1.class, factoryA);
            csl.setConstant(FactoryB1.class, factoryB);
            csl.setConstant(FactoryC1.class, factoryC);
            csl.setConstant(FactoryD1.class, factoryD);
        } catch (LocatorError e) {
            fail(e.toString());
        }
    }

}

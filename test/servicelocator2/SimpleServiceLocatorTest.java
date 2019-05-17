package servicelocator2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SimpleServiceLocatorTest {

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

    private void addSomeFactoriesAsConstantAndOthersAsService() {
        try {
            ssl.setService(InterfaceA.class, factoryA);
            ssl.setConstant(FactoryB1.class, factoryB);
            ssl.setConstant(FactoryC1.class, factoryC);
            ssl.setService(InterfaceD.class, factoryD);
        } catch (LocatorError e) {
            fail(e.toString());
        }
    }

    private void addConstant() {
        try {
            ssl.setConstant(String.class, "String");
            ssl.setConstant(Integer.class, 10);
        } catch (LocatorError e) {
            fail(e.toString());
        }
    }

}

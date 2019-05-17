package serviceLocator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SimpleServiceLocatorTest {

    class FactoryA1 implements Factory {

        class ImplementationA1 implements InterfaceA {
            private InterfaceB b;
            private InterfaceC c;
            ImplementationA1(InterfaceB b, InterfaceC c) {
                this.b = b; this.c = c;
            }
        }

        @Override
        public InterfaceA create(ServiceLocator sl) throws LocatorError {
            try {
                return null;
            } catch (ClassCastException ex) {
                throw new LocatorError(ex);
            }
        }
    }

    class FactoryB1 implements Factory {

        class ImplementationB1 implements InterfaceB {
            private InterfaceD d;
            ImplementationB1(InterfaceD d) {
                this.d = d;
            }
        }

        @Override
        public InterfaceB create(ServiceLocator sl) throws LocatorError {
            try {
                return null;
            } catch (ClassCastException ex) {
                throw new LocatorError(ex);
            }
        }
    }

    class FactoryC1 implements Factory {

        static class ImplementationC1 implements InterfaceB {
            private String s;
            ImplementationC1(String s) {
                this.s = s;
            }
        }

        @Override
        public InterfaceC create(ServiceLocator sl) throws LocatorError {
            try {
                return null;
            } catch (ClassCastException ex) {
                throw new LocatorError(ex);
            }
        }
    }

    class FactoryD1 implements Factory {

        class ImplementationD1 implements InterfaceB {
            private int i;
            ImplementationD1(int i) {
                this.i = i;
            }
        }

        @Override
        public InterfaceD create(ServiceLocator sl) throws LocatorError {
            try {
                return null;
            } catch (ClassCastException ex) {
                throw new LocatorError(ex);
            }
        }
    }


    @BeforeEach
    void setUp(){
        Integer constantInteger = 10;
        InterfaceC constantInterfaceC = new ImplementationC1("Interface C1");
    }

    @Test
    void setService() {
    }

    @Test
    void setConstant() {
    }

    @Test
    void getObject() {
    }
}
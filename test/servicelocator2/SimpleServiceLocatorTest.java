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
        public InterfaceD create(ServiceLocator2 sl) throws LocatorError {
            try {
                Integer d = (Integer) sl.getObject("Constant Integer");
                return new ImplementationD1(d);
            } catch (ClassCastException ex) {
                throw new LocatorError(ex);
            }
        }
    }


}

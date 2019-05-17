package serviceLocator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimpleServiceLocatorTest {

    private static class ImplementationA1 implements InterfaceA {
        private InterfaceB b;
        private InterfaceC c;
        public ImplementationA1(InterfaceB b, InterfaceC c) {
            this.b = b; this.c = c;
        }
    }





    @BeforeEach
    void setUp(){

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
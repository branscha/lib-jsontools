package com.sdicons.json.helper;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class HelperTest {

    // A <-- B <-- C <-- D1 <-- E11
    //                      <-- E12
    //               <-- D2 <-- E21
    //                      <-- E22
    static class A {};
    class B extends A {};
    class C extends B {};
    class D1 extends C {};
    class D2 extends C {};
    class E11 extends D1 {};
    class E12 extends D1 {};
    class E21 extends D2 {};
    class E22 extends D2 {};

    class Uno {};
    class Duo extends Uno {};
    class Tres  extends Duo {};

    private Helper a, b, c, d1, d2, e11, e12, e21, e22;
    private Helper uno, duo, tres;

    @Before
    public void createHelpers() {
        a = mock(Helper.class);
        b = mock(Helper.class);
        c = mock(Helper.class);
        d1 = mock(Helper.class);
        d2 = mock(Helper.class);
        e11 = mock(Helper.class);
        e12 = mock(Helper.class);
        e21 = mock(Helper.class);
        e22 = mock(Helper.class);
        uno = mock(Helper.class);
        duo = mock(Helper.class);
        tres = mock(Helper.class);

        when(a.getHelpedClass()).thenAnswer(new Answer<Class<?>>() {
            @Override
            public Class<?> answer(InvocationOnMock invocation) throws Throwable {
                return A.class;
            }
        });

        when(b.getHelpedClass()).thenAnswer(new Answer<Class<?>>() {
            @Override
            public Class<?> answer(InvocationOnMock invocation) throws Throwable {
                return B.class;
            }
        });

        when(c.getHelpedClass()).thenAnswer(new Answer<Class<?>>() {
            @Override
            public Class<?> answer(InvocationOnMock invocation) throws Throwable {
                return C.class;
            }
        });

        when(d1.getHelpedClass()).thenAnswer(new Answer<Class<?>>() {
            @Override
            public Class<?> answer(InvocationOnMock invocation) throws Throwable {
                return D1.class;
            }
        });

        when(d2.getHelpedClass()).thenAnswer(new Answer<Class<?>>() {
            @Override
            public Class<?> answer(InvocationOnMock invocation) throws Throwable {
                return D2.class;
            }
        });

        when(e11.getHelpedClass()).thenAnswer(new Answer<Class<?>>() {
            @Override
            public Class<?> answer(InvocationOnMock invocation) throws Throwable {
                return E11.class;
            }
        });

        when(e12.getHelpedClass()).thenAnswer(new Answer<Class<?>>() {
            @Override
            public Class<?> answer(InvocationOnMock invocation) throws Throwable {
                return E12.class;
            }
        });

        when(e21.getHelpedClass()).thenAnswer(new Answer<Class<?>>() {
            @Override
            public Class<?> answer(InvocationOnMock invocation) throws Throwable {
                return E21.class;
            }
        });

        when(e22.getHelpedClass()).thenAnswer(new Answer<Class<?>>() {
            @Override
            public Class<?> answer(InvocationOnMock invocation) throws Throwable {
                return E22.class;
            }
        });

        when(uno.getHelpedClass()).thenAnswer(new Answer<Class<?>>() {
            @Override
            public Class<?> answer(InvocationOnMock invocation) throws Throwable {
                return Uno.class;
            }
        });

        when(duo.getHelpedClass()).thenAnswer(new Answer<Class<?>>() {
            @Override
            public Class<?> answer(InvocationOnMock invocation) throws Throwable {
                return Duo.class;
            }
        });

        when(tres.getHelpedClass()).thenAnswer(new Answer<Class<?>>() {
            @Override
            public Class<?> answer(InvocationOnMock invocation) throws Throwable {
                return Tres.class;
            }
        });

    }

    @SuppressWarnings("unchecked")
    @Test
    public void oele() {
        // Simplest case.
        HelperRepository repo  = new HelperRepository<Helper>();
        repo.addHelper(a);

        // Ask for the only registered class.
        Assert.assertSame(a, repo.findHelper(A.class));
        Assert.assertSame(a, repo.findHelper(B.class));
        Assert.assertSame(a, repo.findHelper(C.class));

        // Ask for an unregistered class.
        Assert.assertNull(repo.findHelper(Uno.class));
    }

}

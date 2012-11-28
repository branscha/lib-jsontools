package com.sdicons.json.helper;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class RepositoryTest {

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

    private ClassHelper a, abis, b, c, d1, d2, e11, e12, e21, e22;
    private ClassHelper uno, duo, tres;

    @Before
    public void createHelpers() {
        a = mock(ClassHelper.class);
        abis = mock(ClassHelper.class);
        b = mock(ClassHelper.class);
        c = mock(ClassHelper.class);
        d1 = mock(ClassHelper.class);
        d2 = mock(ClassHelper.class);
        e11 = mock(ClassHelper.class);
        e12 = mock(ClassHelper.class);
        e21 = mock(ClassHelper.class);
        e22 = mock(ClassHelper.class);
        uno = mock(ClassHelper.class);
        duo = mock(ClassHelper.class);
        tres = mock(ClassHelper.class);

        when(a.getHelpedClass()).thenAnswer(new Answer<Class<?>>() {
            @Override
            public Class<?> answer(InvocationOnMock invocation) throws Throwable {
                return A.class;
            }
        });
        
        when(abis.getHelpedClass()).thenAnswer(new Answer<Class<?>>() {
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

    @Test
    public void basicLookup() {
        // Simplest case.
        ClassHelperRepository<ClassHelper> repo  = new ClassHelperRepository<ClassHelper>();
        repo.addHelper(a);

        // Ask for the only registered class.
        Assert.assertSame(a, repo.findHelper(A.class));
        Assert.assertSame(a, repo.findHelper(B.class));
        Assert.assertSame(a, repo.findHelper(C.class));

        // Ask for an unregistered class.
        Assert.assertNull(repo.findHelper(Uno.class));
        
        // Exercise the pretty print.
        Assert.assertNotNull(repo.prettyPrint());
    }
    
    @Test 
    public void fillHelperGap() {
        // Create a helper with a gap, we serve A and C directly but not B.
        ClassHelperRepository<ClassHelper> repo  = new ClassHelperRepository<ClassHelper>();
        repo.addHelper(a);
        repo.addHelper(c);
        //
        Assert.assertSame(a, repo.findHelper(A.class));
        Assert.assertSame(a, repo.findHelper(B.class)); // Should be handled by a.
        Assert.assertSame(c, repo.findHelper(C.class));
        
        // Now add the more specific helper b.
        repo.addHelper(b);
        //
        Assert.assertSame(a, repo.findHelper(A.class));
        Assert.assertSame(b, repo.findHelper(B.class)); // Should now be handled by b.
        Assert.assertSame(c, repo.findHelper(C.class));
        
        // Exercise the pretty print.
        Assert.assertNotNull(repo.prettyPrint());
    }
    
    @Test
    public void replaceHelper() {
        // Single helper.
        ClassHelperRepository<ClassHelper> repo  = new ClassHelperRepository<ClassHelper>();
        repo.addHelper(a);
        // Ask for the only registered class.
        Assert.assertSame(a, repo.findHelper(A.class));
        
        // Now we replace the helper for A.
        repo.addHelper(abis);
        Assert.assertSame(abis, repo.findHelper(A.class));
        
        // Change back to original.
        repo.addHelper(a);
        Assert.assertSame(a, repo.findHelper(A.class));
    }
    
    @Test
    public void rebalancing() {
        ClassHelperRepository<ClassHelper> repo  = new ClassHelperRepository<ClassHelper>();
        
        // A <-- B <-- C <-- D1 <-- E11
        repo.addHelper(a);
        repo.addHelper(b);
        repo.addHelper(c);
        repo.addHelper(d1);
        repo.addHelper(e11);
        //
        Assert.assertSame(a, repo.findHelper(A.class));
        Assert.assertSame(b, repo.findHelper(B.class));
        Assert.assertSame(c, repo.findHelper(C.class));
        Assert.assertSame(d1, repo.findHelper(D1.class));
        Assert.assertSame(e11, repo.findHelper(E11.class));
        Assert.assertSame(d1, repo.findHelper(E12.class));
        Assert.assertSame(c, repo.findHelper(D2.class));
        Assert.assertSame(c, repo.findHelper(E21.class));
        Assert.assertSame(c, repo.findHelper(E22.class));

        // A <-- B <-- C <-- D1 <-- E11
        //               <--------- E22
        repo.addHelper(e22);
        //
        Assert.assertSame(a, repo.findHelper(A.class));
        Assert.assertSame(b, repo.findHelper(B.class));
        Assert.assertSame(c, repo.findHelper(C.class));
        Assert.assertSame(d1, repo.findHelper(D1.class));
        Assert.assertSame(e11, repo.findHelper(E11.class));
        Assert.assertSame(d1, repo.findHelper(E12.class));
        Assert.assertSame(c, repo.findHelper(D2.class));
        Assert.assertSame(c, repo.findHelper(E21.class));
        Assert.assertSame(e22, repo.findHelper(E22.class));

        // A <-- B <-- C <-- D1 <-- E11
        //                      <-- E12
        //               <--------- E21
        //               <--------- E22
        repo.addHelper(e12);
        repo.addHelper(e21);
        //
        Assert.assertSame(a, repo.findHelper(A.class));
        Assert.assertSame(b, repo.findHelper(B.class));
        Assert.assertSame(c, repo.findHelper(C.class));
        Assert.assertSame(d1, repo.findHelper(D1.class));
        Assert.assertSame(e11, repo.findHelper(E11.class));
        Assert.assertSame(e12, repo.findHelper(E12.class));
        Assert.assertSame(c, repo.findHelper(D2.class));
        Assert.assertSame(e21, repo.findHelper(E21.class));
        Assert.assertSame(e22, repo.findHelper(E22.class));

        // A <-- B <-- C <-- D1 <-- E11
        //                      <-- E12
        //               <-- D2 <-- E21
        //                      <-- E22
        repo.addHelper(d2);
        //
        Assert.assertSame(a, repo.findHelper(A.class));
        Assert.assertSame(b, repo.findHelper(B.class));
        Assert.assertSame(c, repo.findHelper(C.class));
        Assert.assertSame(d1, repo.findHelper(D1.class));
        Assert.assertSame(e11, repo.findHelper(E11.class));
        Assert.assertSame(e12, repo.findHelper(E12.class));
        Assert.assertSame(d2, repo.findHelper(D2.class));
        Assert.assertSame(e21, repo.findHelper(E21.class));
        Assert.assertSame(e22, repo.findHelper(E22.class));
    }
}

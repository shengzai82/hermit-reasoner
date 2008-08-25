package org.semanticweb.HermiT.owlapi;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AllTests extends TestCase {

    public static Test suite() {
        TestSuite suite = new TestSuite(
                "Test for the normalization and clasification code used with the OWLAPI. ");
        // $JUnit-BEGIN$
        suite.addTestSuite(ClausificationTest.class);
        suite.addTestSuite(NormalizationTest.class);
        // $JUnit-END$
        return suite;
    }

}

package cse41321.containers;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.testng.Assert.*;

public class PolynomialsTest {

    // Store the original standard out before changing it.
    private final PrintStream originalStdOut = System.out;
    private ByteArrayOutputStream consoleContent = new ByteArrayOutputStream();

    @org.testng.annotations.BeforeMethod
    public void setUp() {
        // Redirect all System.out to consoleContent.
        System.setOut(new PrintStream(this.consoleContent));
    }

    @org.testng.annotations.AfterMethod
    public void tearDown() {
        System.setOut(this.originalStdOut);     // Restore original standard out
        // Clear the consoleContent.
        this.consoleContent = new ByteArrayOutputStream();
    }

    @org.testng.annotations.Test
    public void testAppendTerm() {
        SinglyLinkedList<Double> singlyLinkedList = new SinglyLinkedList<>();
        Polynomials.appendTerm(singlyLinkedList, 6.0);
        Polynomials.appendTerm(singlyLinkedList, 0.0);
        Polynomials.appendTerm(singlyLinkedList, -5.3);
        Polynomials.appendTerm(singlyLinkedList, 3.1);
        assertEquals(singlyLinkedList.getSize(), 4);
        SinglyLinkedList.Element element = singlyLinkedList.getHead();
        assertEquals(element.getData(), 6.0);
        element = element.getNext();
        assertEquals(element.getData(), 0.0);
        element = element.getNext();
        assertEquals(element.getData(), -5.3);
        element = element.getNext();
        assertEquals(element.getData(), 3.1);
    }

    @org.testng.annotations.Test
    public void testDisplay() {
        SinglyLinkedList singlyLinkedList = new SinglyLinkedList();
        Polynomials.appendTerm(singlyLinkedList, 6.0);
        Polynomials.appendTerm(singlyLinkedList, 0.0);
        Polynomials.appendTerm(singlyLinkedList, -5.3);
        Polynomials.appendTerm(singlyLinkedList, 3.1);
        Polynomials.display(singlyLinkedList);
        assertTrue(this.consoleContent.toString().contains("6.00x^3 - 5.30x + 3.10"));
    }

    @org.testng.annotations.Test
    public void testEvaluate() {
        double result = 0.0;
        SinglyLinkedList singlyLinkedList = new SinglyLinkedList();
        Polynomials.appendTerm(singlyLinkedList, 6.0);
        Polynomials.appendTerm(singlyLinkedList, 0.0);
        Polynomials.appendTerm(singlyLinkedList, -5.3);
        Polynomials.appendTerm(singlyLinkedList, 3.1);
        result = Polynomials.evaluate(singlyLinkedList, 7.0);
        assertEquals(result, 2024.0);
        result = Polynomials.evaluate(singlyLinkedList, 6.0);
        assertNotEquals(result, 2024.0);
    }
}
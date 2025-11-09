import itsc2214.*;
import org.junit.*;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link Project3} class.
 * 
 * This test suite validates:
 * 
 *   Parenthesis balancing behavior
 *   Infix to postfix conversion correctness
 *   Postfix and infix evaluation accuracy
 *   Exception handling for invalid or malformed expressions
 *
 *
 * @author Cleo
 * 
 */
public class Project3Test {

    private ExpressionEvaluator runner;

    /**
     * Initializes a fresh {@code Project3} instance before each test.
     */
    @Before
    public void setup() {
        runner = new Project3();
    }

    /**
     * Tests the {@link Project3#balancedParenthesis(String)} method
     * with various valid and invalid expressions.
     */
    @Test
    public void testBalancedParenthesis() {
        assertTrue("() should be valid", runner.balancedParenthesis("()"));
        assertFalse("()) not balanced", runner.balancedParenthesis("())"));
        assertFalse("( not balanced", runner.balancedParenthesis("("));
        assertTrue("(()) should be valid", runner.balancedParenthesis("(())"));
        assertTrue("()() should be valid", runner.balancedParenthesis("()()"));
        assertFalse(")() not balanced", runner.balancedParenthesis(")()"));
        assertTrue("(3+4)*(5-2) valid", runner.balancedParenthesis("(3+4)*(5-2)"));
        assertTrue("((10)) valid", runner.balancedParenthesis("((10))"));
        assertFalse("((10) invalid", runner.balancedParenthesis("((10)"));
    }

    /**
     * Tests the {@link Project3#infix2Postfix(String)} method for
     * consistent token ordering in postfix output.
     */
    @Test
    public void testInfix2Postfix() {
        QueueADT<String> q = runner.infix2Postfix("10 + 20");
        assertEquals("10", q.dequeue());
        assertEquals("20", q.dequeue());
        assertEquals("+", q.dequeue());
        assertTrue(q.isEmpty());

        q = runner.infix2Postfix("10 + 20 * 5");
        assertEquals("10", q.dequeue());
        assertEquals("20", q.dequeue());
        assertEquals("+", q.dequeue());
        assertEquals("5", q.dequeue());
        assertEquals("*", q.dequeue());
        assertTrue(q.isEmpty());

        q = runner.infix2Postfix("10 + (20 * 5)");
        assertEquals("10", q.dequeue());
        assertEquals("20", q.dequeue());
        assertEquals("5", q.dequeue());
        assertEquals("*", q.dequeue());
        assertEquals("+", q.dequeue());
        assertTrue(q.isEmpty());
    }

    /**
     * Tests {@link Project3#evaluatePostfix(QueueADT)} for basic arithmetic correctness.
     */
    @Test
    public void testEvaluatePostfix() {
        QueueADT<String> q = Factory.makeQueueArrayList();
        q.enqueue("10");
        q.enqueue("20");
        q.enqueue("+");
        assertEquals(30, runner.evaluatePostfix(q));
    }

    /**
     * Tests the full {@link Project3#evaluateInfix(String)} method
     * which combines parenthesis checking, infix-to-postfix conversion,
     * and postfix evaluation.
     */
    @Test
    public void testEvaluateInfix() {
        assertEquals(30, runner.evaluateInfix("10 + 20"));
        assertEquals(70, runner.evaluateInfix("10 + (20 * 3)"));
        assertEquals(9, runner.evaluateInfix("(3+6)"));
    }

    /**
     * Verifies that the evaluator correctly throws exceptions
     * for invalid inputs and division by zero.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidExpression() {
        runner.evaluateInfix("10 + (5");
    }

    /**
     * Tests handling of division by zero.
     */
    @Test(expected = ArithmeticException.class)
    public void testDivisionByZero() {
        runner.evaluateInfix("10 / (5 - 5)");
    }
}

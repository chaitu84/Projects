import java.util.StringTokenizer;
import itsc2214.*;

/**
 * Project3 implements the {@link ExpressionEvaluator} interface for ITSC 2214 Fall 2025.
 * 
 * This class provides functionality to:
 *
 *   Check for balanced parentheses in infix expressions
 *   Convert infix expressions to postfix notation
 *   Evaluate postfix expressions
 *   Evaluate infix expressions by combining both processes
 * 
 *
 *
 *
 * @author Cleo
 * 
 */
public class Project3 implements ExpressionEvaluator {

    /**
     * Checks whether the parentheses in a given infix expression are balanced.
     *
     * @param expr The infix expression as a String
     * @return {@code true} if all parentheses are balanced, {@code false} otherwise
     */
    @Override
    public boolean balancedParenthesis(String expr) {
        StackADT<String> stack = Factory.makeStackArrayList();
        StringTokenizer st = new StringTokenizer(expr, " +-*/()", true);

        while (st.hasMoreTokens()) {
            String token = st.nextToken().trim();
            if (token.isEmpty()) {
                continue;
            }

            if (token.equals("(")) {
                stack.push(token);
            } else if (token.equals(")")) {
                if (stack.isEmpty()) {
                    return false;
                } else {
                    stack.pop();
                }
            }
        }

        return stack.isEmpty();
    }

    /**
     * Converts an infix expression into postfix (Reverse Polish) notation.
     * 
     * The conversion uses a stack for operators and a queue for output. Parentheses are used
     * to control operator order, and they are removed in the resulting postfix expression.
     * 
     *
     * @param expr The infix expression as a String
     * @return A {@code QueueADT<String>} containing tokens of the postfix expression
     */
    @Override
    public QueueADT<String> infix2Postfix(String expr) {
        QueueADT<String> queue = Factory.makeQueueArrayList();
        StackADT<String> stack = Factory.makeStackArrayList();
        StringTokenizer st = new StringTokenizer(expr, " +-*/()", true);

        while (st.hasMoreTokens()) {
            String token = st.nextToken().trim();
            if (token.isEmpty()) {
                continue;
            }

            if (token.matches("[0-9]+")) {
                queue.enqueue(token);
            } else if (token.equals("(")) {
                stack.push(token);
            } else if (token.equals(")")) {
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    queue.enqueue(stack.pop());
                }
                if (!stack.isEmpty() && stack.peek().equals("(")) {
                    stack.pop();
                }
            } else if (token.equals("+") || token.equals("-")
                    || token.equals("*") || token.equals("/")) {
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    queue.enqueue(stack.pop());
                }
                stack.push(token);
            }
        }

        while (!stack.isEmpty()) {
            queue.enqueue(stack.pop());
        }

        return queue;
    }

    /**
     * Evaluates a postfix (Reverse Polish) expression and returns the integer result.
     *
     * @param expr A {@code QueueADT<String>} representing the postfix expression
     * @return The result of evaluating the postfix expression
     * @throws ArithmeticException If division by zero occurs
     * @throws IllegalArgumentException If the expression is malformed
     */
    @Override
    public int evaluatePostfix(QueueADT<String> expr)
            throws ArithmeticException, IllegalArgumentException {

        if (expr == null) {
            throw new IllegalArgumentException("Null queue passed to evaluatePostfix");
        }

        StackADT<String> stack = Factory.makeStackArrayList();

        while (!expr.isEmpty()) {
            String token = expr.dequeue();
            if (token == null) {
                continue;
            }

            token = token.trim();
            if (token.isEmpty()) {
                continue;
            }

            if (token.matches("[0-9]+")) {
                stack.push(token);
            } else if (token.equals("+") || token.equals("-")
                    || token.equals("*") || token.equals("/")) {

                if (stack.isEmpty()) {
                    throw new IllegalArgumentException(
                            "Missing operand for operator " + token);
                }

                int right = Integer.parseInt(stack.pop());
                if (stack.isEmpty()) {
                    throw new IllegalArgumentException(
                            "Missing operand for operator " + token);
                }

                int left = Integer.parseInt(stack.pop());
                int result;

                switch (token) {
                    case "+":
                        result = left + right;
                        break;
                    case "-":
                        result = left - right;
                        break;
                    case "*":
                        result = left * right;
                        break;
                    case "/":
                        if (right == 0) {
                            throw new ArithmeticException("Division by zero");
                        }
                        result = left / right;
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown operator: " + token);
                }

                stack.push(Integer.toString(result));
            } else {
                throw new IllegalArgumentException(
                        "Invalid token in postfix expression: " + token);
            }
        }

        if (stack.isEmpty()) {
            throw new IllegalArgumentException("No result after evaluation");
        }

        int finalResult = Integer.parseInt(stack.pop());

        if (!stack.isEmpty()) {
            throw new IllegalArgumentException("Extra operands left after evaluation");
        }

        return finalResult;
    }

    /**
     * Evaluates an infix expression by checking for balanced parentheses, converting to
     * postfix notation, and evaluating the resulting postfix expression.
     *
     * @param expr The infix expression as a String
     * @return The integer result of evaluation
     * @throws ArithmeticException If division by zero occurs
     * @throws IllegalArgumentException If parentheses are unbalanced or the expression is invalid
     */
    @Override
    public int evaluateInfix(String expr)
            throws ArithmeticException, IllegalArgumentException {

        if (expr == null) {
            throw new IllegalArgumentException("Null expression passed to evaluateInfix");
        }

        if (!balancedParenthesis(expr)) {
            throw new IllegalArgumentException("Unbalanced parentheses");
        }

        QueueADT<String> postfix = infix2Postfix(expr);
        return evaluatePostfix(postfix);
    }
}

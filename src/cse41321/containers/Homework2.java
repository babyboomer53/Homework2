package cse41321.containers;

import java.util.NoSuchElementException;

// SinglyLinkedList.java

class SinglyLinkedList<E> {
    // An element in a linked list
    public class Element {
        private E data;
        private Element next;

        // Only allow SinglyLinkedList to construct Elements
        private Element(E data) {
            this.data = data;
            this.next = null;
        }

        public E getData() {
            return data;
        }

        public Element getNext() {
            return next;
        }

        private SinglyLinkedList getOwner() {
            return SinglyLinkedList.this;
        }
    }

    private Element head;
    private Element tail;
    private int size;

    public Element getHead() {
        return head;
    }

    public Element getTail() {
        return tail;
    }

    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public Element insertHead(E data) {
        Element newElement = new Element(data);

        if (isEmpty()) {
            // Insert into empty list
            head = newElement;
            tail = newElement;
        } else {
            // Insert into non-empty list
            newElement.next = head;
            head = newElement;
        }

        ++size;

        return newElement;
    }

    public Element insertTail(E data) {
        Element newElement = new Element(data);

        if (isEmpty()) {
            // Insert into empty list
            head = newElement;
            tail = newElement;
        } else {
            // Insert into non-empty list
            tail.next = newElement;
            tail = newElement;
        }

        ++size;

        return newElement;
    }

    public Element insertAfter(Element element, E data)
            throws IllegalArgumentException {
        // Check pre-conditions
        if (element == null) {
            throw new IllegalArgumentException(
                    "Argument 'element' must not be null");
        }
        if (element.getOwner() != this) {
            throw new IllegalArgumentException(
                    "Argument 'element' does not belong to this list");
        }

        // Insert new element
        Element newElement = new Element(data);
        if (tail == element) {
            // Insert new tail
            element.next = newElement;
            tail = newElement;
        } else {
            // Insert into middle of list
            newElement.next = element.next;
            element.next = newElement;
        }

        ++size;

        return newElement;
    }

    public E removeHead() throws NoSuchElementException {
        // Check pre-conditions
        if (isEmpty()) {
            throw new NoSuchElementException("Cannot remove from empty list");
        }

        // Remove the head
        Element oldHead = head;
        if (size == 1) {
            // Handle removal of the last element
            head = null;
            tail = null;
        } else {
            head = head.next;
        }

        --size;

        return oldHead.data;
    }

    // Note that there is no removeTail.  This cannot be implemented
    // efficiently because it would require O(n) to scan from head until
    // reaching the item _before_ tail.

    public E removeAfter(Element element)
            throws IllegalArgumentException, NoSuchElementException {
        // Check pre-conditions
        if (element == null) {
            throw new IllegalArgumentException(
                    "Argument 'element' must not be null");
        }
        if (element.getOwner() != this) {
            throw new IllegalArgumentException(
                    "Argument 'element' does not belong to this list");
        }
        if (element == tail) {
            throw new IllegalArgumentException(
                    "Argument 'element' must have a non-null next element");
        }

        // Remove element
        Element elementToRemove = element.next;
        if (elementToRemove == tail) {
            // Remove the tail
            element.next = null;
            tail = element;
        } else {
            // Remove from middle of list
            element.next = elementToRemove.next;
        }

        --size;

        return elementToRemove.data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SinglyLinkedList<?> that = (SinglyLinkedList<?>) o;

        if (this.size != that.size) return false;

        // Return whether all elements are the same
        SinglyLinkedList<?>.Element thisElem = this.getHead();
        SinglyLinkedList<?>.Element thatElem = that.getHead();
        while (thisElem != null && thatElem != null) {
            if (!thisElem.getData().equals(thatElem.getData())) {
                return false;
            }
            thisElem = thisElem.getNext();
            thatElem = thatElem.getNext();
        }

        return true;
    }
}

class Polynomials extends SinglyLinkedList<Double> {

    /**
     * This method adds the terms of a polynomial expression to a list.
     *
     * @param polynomial a SinglyLinkedList object
     * @param constant an object of type Double containing the coefficient of the term
     */
    static void appendTerm(SinglyLinkedList<Double> polynomial, Double constant) {
        polynomial.insertTail(constant);
    }

    /**
     * This method retrieves the terms of a polynomial expression from a list, and displays that expression in standard format.
     *
     * @param polynomial a SinglyLinkedList containing the terms of a polynomial expression in descending order of magnitude.
     */
    static void display(SinglyLinkedList<Double> polynomial) {
        Element element = polynomial.getHead();
        // The exponent of the high order term is one less than the number of elements in the list.
        int order = polynomial.getSize() - 1;
        String operator;
        while (element != null) {
            if (element.getData() != 0.0) {
                if (polynomial.getSize() == order + 1) {    // Is this the constant for the first term?
                    operator = element.getData() >= 0.0 ? "" : "-";
                } else if (element.getData() >= 0.0) {
                    operator = " + ";
                } else {
                    operator = " - ";
                }
                String term = String.format("%.2fx^%d", Math.abs(element.getData()), order)
                        .replaceAll("[\\^][1]$|x[\\^][0]$", "");
                System.out.print(operator + term);
            }
            element = element.getNext();
            --order;
        }
    }

    /**
     * This method traverses a list to evaluate the terms of a polynomial expression, and returns the result as a double
     * precision floating point number.
     * @param polynomial a SinglyLinkedList containing the terms of a polynomial in descending order of magnitude.
     *
     * @param variable the value of the unknown quantity (i.e., the variable).
     * @return the computed value of the polynomial expression
     */
    static Double evaluate(SinglyLinkedList<Double> polynomial, Double variable) {
        Element element = polynomial.getHead();
        int order = polynomial.getSize() - 1;
        Double solution = 0.0;
        while (element != null) {
            solution += element.getData() * (Math.pow(variable, order));
            element = element.getNext();
            --order;
        }
        return solution;
    }

    /**
     * This method accepts two arguments. The first argument is an object of type Double, containing the value of the
     * unknown quantity (or variable). The second argument consists of a sequence of one or more objects of type Double.
     * When invoked, this method will populate a SinglyLinkedList with the values passed from the coefficients list.
     * Next, it will render the polynomial represented by its contents, and display it on the console standard notation.
     * Finally, it will evaluate the expression and display the results on the console.
     *
     * @param variable the value of the single unknown that will be used to evaluate the polynomial expression.
     * @param constants a sequence of floating-point numbers representing the coefficients of the terms in the
     *                     polynomial expression. The list should represent a complete order, meaning that missing terms
     *                     should be represented by zero (0.0).
     */
    static void soupToNuts(Double variable, Double... constants) {
        SinglyLinkedList<Double> singlyLinkedList = new SinglyLinkedList<>();
        System.out.println("Creating list of coefficients...");
        for (Double constant : constants) {
            Polynomials.appendTerm(singlyLinkedList, constant);
        }
        Polynomials.display(singlyLinkedList);
        System.out.printf("%n%.2f%n", Polynomials.evaluate(singlyLinkedList, variable));
    }
}

public class Homework2 {
    public static void main(String[] arguments) {
        Polynomials.soupToNuts(1.0, 1.0, 1.0);
        System.out.println();
        Polynomials.soupToNuts(2.03, 1.0, 0.0, -1.0);
        System.out.println();
        Polynomials.soupToNuts(5.0, -3.0, 0.5, -2.0);
        System.out.println();
        Polynomials.soupToNuts(123.45, -0.3125, 0.0, -9.915, -7.75, 40.0);
    }
}

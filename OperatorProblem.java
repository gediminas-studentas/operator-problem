import java.util.LinkedList;
import java.util.Optional;

/**
 * Problem: Write a program that outputs all possibilities to put + or - or nothing between the numbers
 * 1, 2, ..., 9 (in this order) such that the result is always 100. For example: 1 + 2 + 34 â€“ 5 + 67 â€“ 8 + 9 = 100.
 *
 * @Author Gediminas Kikilas
 */
public class OperatorProblem {

    public static void main(String[] args) {
        LinkedList<OperationDecisionTree> parents = buildOperatorDecisionTree();
        parents.stream().filter(p -> p.getSumSoFar() == 100).forEach(p -> System.out.println(p.traverseToString()));
    }

    private static LinkedList<OperationDecisionTree> buildOperatorDecisionTree() {
        LinkedList<OperationDecisionTree> parents = new LinkedList<>();
        parents.add(new OperationDecisionTree(1, Operator.NONE, 1));
        for (int nextNumber = 2; nextNumber <= 9; nextNumber++) {
            LinkedList<OperationDecisionTree> parentsSnapshot = new LinkedList<>(parents);
            parents = new LinkedList<>();
            for (OperationDecisionTree parent : parentsSnapshot) {
                parents.add(new OperationDecisionTree(parent, nextNumber, Operator.PLUS, parent.getSumSoFar() + nextNumber));
                parents.add(new OperationDecisionTree(parent, nextNumber, Operator.MINUS, parent.getSumSoFar() - nextNumber));

                int nextNoneNumber = parent.getNumber() * 10 + nextNumber;
                if (parent.getOperator().equals(Operator.MINUS)) {
                    parents.add(parent.copyWithNumber(nextNoneNumber, parent.getSumSoFar() + parent.getNumber() - nextNoneNumber));
                } else {
                    parents.add(parent.copyWithNumber(nextNoneNumber, parent.getSumSoFar() - parent.getNumber() + nextNoneNumber));
                }
            }
        }

        return parents;
    }

    private enum Operator {
        PLUS("+"),
        MINUS("-"),
        NONE("");

        private final String symbol;

        Operator(String symbol) {
            this.symbol = symbol;
        }

        public String getSymbol() {
            return symbol;
        }
    }

    private static class OperationDecisionTree {
        private final int number;
        private final Operator operator;
        private final int sumSoFar;
        private final Optional<OperationDecisionTree> parent;

        public OperationDecisionTree(OperationDecisionTree parent, int number, Operator operator, int sumSoFar) {
            this.number = number;
            this.operator = operator;
            this.sumSoFar = sumSoFar;
            this.parent = Optional.ofNullable(parent);
        }

        public OperationDecisionTree(int number, Operator operator, int sumSoFar) {
            this(null, number, operator, sumSoFar);
        }

        public OperationDecisionTree copyWithNumber(int number, int sumSoFar) {
            return new OperationDecisionTree(parent.orElse(null), number, operator, sumSoFar);
        }

        public int getNumber() {
            return number;
        }

        public Operator getOperator() {
            return operator;
        }

        public Optional<OperationDecisionTree> getParent() {
            return parent;
        }

        public int getSumSoFar() {
            return sumSoFar;
        }

        public String traverseToString() {
            OperationDecisionTree head = this;
            String equation = "";
            while (head != null) {
                equation = head.operator.symbol + head.number + equation;
                head = head.parent.orElse(null);
            }

            return equation;
        }
    }

}

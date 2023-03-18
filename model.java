import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

abstract class Expression
{
    abstract Object get(Scope scope);

    Object set(Scope scope, Object value) {
        throw new UnsupportedOperationException();
    }

    static Expression True() {
        return new Expression() {
            Object get(Scope scope) {
                return Boolean.TRUE;
            }
            public String toString() {
                return "[True]";
            }
        };
    }

    static Expression False() {
        return new Expression() {
            Object get(Scope scope) {
                return Boolean.FALSE;
            }
            public String toString() {
                return "[False]";
            }
        };
    }

    static Expression Null() {
        return new Expression() {
            Object get(Scope scope) {
                return null;
            }
            public String toString() {
                return "[Null]";
            }
        };
    }

    static Expression Number(final Float value) {
        return new Expression() {
            Object get(Scope scope) {
                return value;
            }
            public String toString() {
                return "[Number " + value + "]";
            }
        };
    }

    static Expression String(final String value) {
        return new Expression() {
            Object get(Scope scope) {
                return value;
            }
            public String toString() {
                return "[String " + value + "]";
            }
        };
    }

    static Expression List(final Expression[] elements) {
        return new Expression() {
            Object get(Scope scope) {
                List<Object> list = new ArrayList<>(elements.length);
                for (int i = 0; i < elements.length; i++) {
                    list.add(elements[i].get(scope));
                }
                return list;
            }
            public String toString() {
                return "[List " + java.util.Arrays.asList(elements) + "]";
            }
        };
    }

    static Expression Map(final Entry[] entries) {
        return new Expression() {
            Object get(Scope scope) {
                Map<String, Object> map = new HashMap<>(entries.length);
                for (int i = 0; i < entries.length; i++) {
                    Entry entry = entries[i];
                    map.put(entry.key, entry.value.get(scope));
                }
                return map;
            }
            public String toString() {
                return "[Map " + java.util.Arrays.asList(entries) + "]";
            }
        };
    }

    static Expression Function(final String[] parameters, final Statement body) {
        return new Expression() {
            Object get(Scope scope) {
                return Function.create(scope, parameters, body);
            }
            public String toString() {
                return "[Function " + java.util.Arrays.asList(parameters) + " " + body + "]";
            }
        };
    }

    static Expression Call(final Expression function, final Expression[] arguments) {
        return new Expression() {
            Object get(Scope scope) {
                Object[] args = new Object[arguments.length];
                for (int i = 0; i < arguments.length; i++) {
                    args[i] = arguments[i].get(scope);
                }
                return ((Function) function.get(scope)).call(args);
            }
            public String toString() {
                return "[Call " + function + " " + java.util.Arrays.asList(arguments) + "]";
            }
        };
    }

    static Expression Symbol(final String name) {
        return new Expression() {
            Object get(Scope scope) {
                return scope.get(name);
            }
            Object set(Scope scope, Object value) {
                return scope.set(name, value);
            }
            public String toString() {
                return "[Symbol " + name + "]";
            }
        };
    }

    static Expression Element(final Expression list, final Expression index) {
        return new Expression() {
            Object get(Scope scope) {
                return ((List<Object>) list.get(scope)).get((int) (float) index.get(scope));
            }
            Object set(Scope scope, Object value) {
                ((List<Object>) list.get(scope)).set((int) (float) index.get(scope), value);
                return value;
            }
            public String toString() {
                return "[Element " + list + " " + index + "]";
            }
        };
    }

    static Expression Value(final Expression map, final String key) {
        return new Expression() {
            Object get(Scope scope) {
                return ((Map<String, Object>) map.get(scope)).get(key);
            }
            Object set(Scope scope, Object value) {
                ((Map<String, Object>) map.get(scope)).put(key, value);
                return value;
            }
            public String toString() {
                return "[Value " + map + " " + key + "]";
            }
        };
    }

    static Expression Negate(final Expression expression) {
        return new Expression() {
            Object get(Scope scope) {
                return -(float) expression.get(scope);
            }
            public String toString() {
                return "[Negate " + expression + "]";
            }
        };
    }

    static Expression Not(final Expression expression) {
        return new Expression() {
            Object get(Scope scope) {
                return !(boolean) expression.get(scope);
            }
            public String toString() {
                return "[Not " + expression + "]";
            }
        };
    }

    static Expression Preincrement(final Expression expression) {
        return new Expression() {
            Object get(Scope scope) {
                float value = (float) expression.get(scope);
                return expression.set(scope, value + 1f);
            }
            public String toString() {
                return "[Preincrement " + expression + "]";
            }
        };
    }

    static Expression Predecrement(final Expression expression) {
        return new Expression() {
            Object get(Scope scope) {
                float value = (float) expression.get(scope);
                return expression.set(scope, value - 1f);
            }
            public String toString() {
                return "[Predecrement " + expression + "]";
            }
        };
    }

    static Expression Postincrement(final Expression expression) {
        return new Expression() {
            Object get(Scope scope) {
                float value = (float) expression.get(scope);
                expression.set(scope, value + 1f);
                return value;
            }
            public String toString() {
                return "[Postincrement " + expression + "]";
            }
        };
    }

    static Expression Postdecrement(final Expression expression) {
        return new Expression() {
            Object get(Scope scope) {
                float value = (float) expression.get(scope);
                expression.set(scope, value - 1f);
                return value;
            }
            public String toString() {
                return "[Postdecrement " + expression + "]";
            }
        };
    }

    static Expression Add(final Expression left, final Expression right) {
        return new Expression() {
            Object get(Scope scope) {
                return (float) left.get(scope) + (float) right.get(scope);
            }
            public String toString() {
                return "[Add " + left + " " + right + "]";
            }
        };
    }

    static Expression Subtract(final Expression left, final Expression right) {
        return new Expression() {
            Object get(Scope scope) {
                return (float) left.get(scope) - (float) right.get(scope);
            }
            public String toString() {
                return "[Subtract " + left + " " + right + "]";
            }
        };
    }

    static Expression Multiply(final Expression left, final Expression right) {
        return new Expression() {
            Object get(Scope scope) {
                return (float) left.get(scope) * (float) right.get(scope);
            }
            public String toString() {
                return "[Multiply " + left + " " + right + "]";
            }
        };
    }

    static Expression Divide(final Expression left, final Expression right) {
        return new Expression() {
            Object get(Scope scope) {
                return (float) left.get(scope) / (float) right.get(scope);
            }
            public String toString() {
                return "[Divide " + left + " " + right + "]";
            }
        };
    }

    static Expression Remainder(final Expression left, final Expression right) {
        return new Expression() {
            Object get(Scope scope) {
                return (float) left.get(scope) % (float) right.get(scope);
            }
            public String toString() {
                return "[Remainder " + left + " " + right + "]";
            }
        };
    }

    static Expression Less(final Expression left, final Expression right) {
        return new Expression() {
            Object get(Scope scope) {
                return (float) left.get(scope) < (float) right.get(scope);
            }
            public String toString() {
                return "[Less " + left + " " + right + "]";
            }
        };
    }

    static Expression LessEqual(final Expression left, final Expression right) {
        return new Expression() {
            Object get(Scope scope) {
                return (float) left.get(scope) <= (float) right.get(scope);
            }
            public String toString() {
                return "[LessEqual " + left + " " + right + "]";
            }
        };
    }

    static Expression Greater(final Expression left, final Expression right) {
        return new Expression() {
            Object get(Scope scope) {
                return (float) left.get(scope) > (float) right.get(scope);
            }
            public String toString() {
                return "[Greater " + left + " " + right + "]";
            }
        };
    }

    static Expression GreaterEqual(final Expression left, final Expression right) {
        return new Expression() {
            Object get(Scope scope) {
                return (float) left.get(scope) >= (float) right.get(scope);
            }
            public String toString() {
                return "[GreaterEqual " + left + " " + right + "]";
            }
        };
    }

    static Expression Equal(final Expression left, final Expression right) {
        return new Expression() {
            Object get(Scope scope) {
                return Objects.equals(left.get(scope), right.get(scope));
            }
            public String toString() {
                return "[Equal " + left + " " + right + "]";
            }
        };
    }

    static Expression NotEqual(final Expression left, final Expression right) {
        return new Expression() {
            Object get(Scope scope) {
                return Objects.equals(left.get(scope), right.get(scope));
            }
            public String toString() {
                return "[NotEqual " + left + " " + right + "]";
            }
        };
    }

    static Expression Set(final Expression left, final Expression right) {
        return new Expression() {
            Object get(Scope scope) {
                return left.set(scope, right.get(scope));
            }
            public String toString() {
                return "[Set " + left + " " + right + "]";
            }
        };
    }

    static Expression SetAdd(final Expression left, final Expression right) {
        return new Expression() {
            Object get(Scope scope) {
                return left.set(scope, (float) left.get(scope) + (float) right.get(scope));
            }
            public String toString() {
                return "[SetAdd " + left + " " + right + "]";
            }
        };
    }

    static Expression SetSubtract(final Expression left, final Expression right) {
        return new Expression() {
            Object get(Scope scope) {
                return left.set(scope, (float) left.get(scope) - (float) right.get(scope));
            }
            public String toString() {
                return "[SetSubtract " + left + " " + right + "]";
            }
        };
    }

    static Expression SetMultiply(final Expression left, final Expression right) {
        return new Expression() {
            Object get(Scope scope) {
                return left.set(scope, (float) left.get(scope) * (float) right.get(scope));
            }
            public String toString() {
                return "[SetMultiply " + left + " " + right + "]";
            }
        };
    }

    static Expression SetDivide(final Expression left, final Expression right) {
        return new Expression() {
            Object get(Scope scope) {
                return left.set(scope, (float) left.get(scope) / (float) right.get(scope));
            }
            public String toString() {
                return "[SetDivide " + left + " " + right + "]";
            }
        };
    }

    static Expression SetRemainder(final Expression left, final Expression right) {
        return new Expression() {
            Object get(Scope scope) {
                return left.set(scope, (float) left.get(scope) % (float) right.get(scope));
            }
            public String toString() {
                return "[SetRemainder " + left + " " + right + "]";
            }
        };
    }

    static Expression And(final Expression left, final Expression right) {
        return new Expression() {
            Object get(Scope scope) {
                return (boolean) left.get(scope) && (boolean) right.get(scope);
            }
            public String toString() {
                return "[And " + left + " " + right + "]";
            }
        };
    }

    static Expression Or(final Expression left, final Expression right) {
        return new Expression() {
            Object get(Scope scope) {
                return (boolean) left.get(scope) || (boolean) right.get(scope);
            }
            public String toString() {
                return "[Or " + left + " " + right + "]";
            }
        };
    }

    static Expression Conditional(final Expression condition, final Expression ifTrue, final Expression ifFalse) {
        return new Expression() {
            Object get(Scope scope) {
                return (boolean) condition.get(scope) ? ifTrue.get(scope) : ifFalse.get(scope);
            }
            public String toString() {
                return "[Conditional " + condition + " " + ifTrue + " " + ifFalse + "]";
            }
        };
    }
}

abstract class Statement
{
    abstract void execute(Scope scope);

    static Statement Empty() {
        return new Statement() {
            void execute(Scope scope) {}
            public String toString() {
                return "[Empty]";
            }
        };
    }

    static Statement Function(final String name, final String[] parameters, final Statement body) {
        return new Statement() {
            void execute(Scope scope) {
                scope.define(name, Function.create(scope, parameters, body));
            }
            public String toString() {
                return "[Function " + name + " " + java.util.Arrays.asList(parameters) + " " + body + "]";
            }
        };
    }

    static Statement Break(final String label) {
        return new Statement() {
            void execute(Scope scope) {
                throw new BreakException(label);
            }
            public String toString() {
                return "[Break " + label + "]";
            }
        };
    }

    static Statement Continue(final String label) {
        return new Statement() {
            void execute(Scope scope) {
                throw new ContinueException(label);
            }
            public String toString() {
                return "[Continue " + label + "]";
            }
        };
    }

    static Statement Return(final Expression value) {
        return new Statement() {
            void execute(Scope scope) {
                throw new ReturnException(value.get(scope));
            }
            public String toString() {
                return "[Return " + value + "]";
            }
        };
    }

    static Statement Program(final Statement[] statements) {
        return new Statement() {
            void execute(Scope scope) {
                for (int i = 0; i < statements.length; i++) {
                    statements[i].execute(scope);
                }
            }
            public String toString() {
                return "[Program " + java.util.Arrays.asList(statements) + "]";
            }
        };
    }

    static Statement Block(final Statement[] statements) {
        return new Statement() {
            void execute(Scope scope) {
                Scope blockScope = new Scope(scope);
                for (int i = 0; i < statements.length; i++) {
                    statements[i].execute(blockScope);
                }
            }
            public String toString() {
                return "[Block " + java.util.Arrays.asList(statements) + "]";
            }
        };
    }

    static Statement Expression(final Expression expression) {
        return new Statement() {
            void execute(Scope scope) {
                expression.get(scope);
            }
            public String toString() {
                return "[Expression " + expression + "]";
            }
        };
    }

    static Statement Var(final String name, final Expression value) {
        return new Statement() {
            void execute(Scope scope) {
                scope.define(name, value.get(scope));
            }
            public String toString() {
                return "[Var " + name + " " + value + "]";
            }
        };
    }

    static Statement If(final Expression condition, final Statement ifTrue, final Statement ifFalse) {
        return new Statement() {
            void execute(Scope scope) {
                if ((boolean) condition.get(scope)) {
                    ifTrue.execute(scope);
                } else {
                    ifFalse.execute(scope);
                }
            }
            public String toString() {
                return "[If " + condition + " " + ifTrue + " " + ifFalse + "]";
            }
        };
    }

    static Statement Switch(final String label, final Expression expression, final Case[] cases) {
        return new Statement() {
            void execute(Scope scope) {
                try {
                    Object value = expression.get(scope);
                    boolean executing = false;
                    for (int i = 0; i < cases.length; i++) {
                        Case case_ = cases[i];
                        if (case_.value == null || Objects.equals(case_.value.get(scope), value)) {
                            executing = true;
                        }
                        if (executing) {
                            case_.body.execute(scope);
                        }
                    }
                } catch (BreakException e) {
                    if (e.label != null && !e.label.equals(label)) {
                        throw e;
                    }
                }
            }
            public String toString() {
                return "[Switch " + expression + " " + java.util.Arrays.asList(cases) + "]";
            }
        };
    }

    static Statement While(final String label, final Expression condition, final Statement body) {
        return new Statement() {
            void execute(Scope scope) {
                try {
                    while ((boolean) condition.get(scope)) {
                        try {
                            body.execute(scope);
                        } catch (ContinueException e) {
                            if (e.label != null && !e.label.equals(label)) {
                                throw e;
                            }
                        }
                    }
                } catch (BreakException e) {
                    if (e.label != null && !e.label.equals(label)) {
                        throw e;
                    }
                }
            }
            public String toString() {
                return "[While " + label + " " + condition + " " + body + "]";
            }
        };
    }

    static Statement For(
        final String label,
        final Statement init,
        final Expression condition,
        final Statement step,
        final Statement body
    ) {
        return new Statement() {
            void execute(Scope scope) {
                Scope forScope = new Scope(scope);
                init.execute(forScope);
                try {
                    while ((boolean) condition.get(forScope)) {
                        try {
                            body.execute(forScope);
                            step.execute(forScope);
                        } catch (ContinueException e) {
                            if (e.label != null) {
                                if (e.label.equals(label)) {
                                    ;
                                } else {
                                    throw e;
                                }
                            } else {
                                ;
                            }
                        }
                    }
                } catch (BreakException e) {
                    if (e.label != null && !e.label.equals(label)) {
                        throw e;
                    }
                }
            }
            public String toString() {
                return "[For " + label + " " + init + " " + condition + " " + step + " " + body + "]";
            }
        };
    }
}

class Case
{
    Expression value;
    Statement body;

    Case(Expression v, Statement b) {
        value = v;
        body = b;
    }

    public String toString() {
        return "[Case " + value + " " + body + "]";
    }
}

class ReturnException extends RuntimeException
{
    Object value;

    ReturnException(Object v) {
        value = v;
    }
}

class BreakException extends RuntimeException
{
    String label;

    BreakException(String l) {
        label = l;
    }
}

class ContinueException extends RuntimeException
{
    String label;

    ContinueException(String l) {
        label = l;
    }
}

class Entry
{
    String key;
    Expression value;

    Entry(String k, Expression v) {
        key = k;
        value = v;
    }

    public String toString() {
        return "[Entry " + key + " " + value + "]";
    }
}

class Scope
{
    Scope parent;
    Map<String, Object> symbols;

    Scope(Scope p) {
        parent = p;
        symbols = new HashMap<>();
    }

    void reset() {
        symbols.clear();
    }

    void define(String name, Object value) {
        if (symbols.containsKey(name)) {
            throw new IllegalStateException("Duplicate `" + name + "`");
        }
        symbols.put(name, value);
    }

    Object get(String name) {
        if (symbols.containsKey(name)) {
            return symbols.get(name);
        }
        if (parent != null) {
            return parent.get(name);
        }
        throw new IllegalStateException("Undefined `" + name + "`");
    }

    Object set(String name, Object value) {
        if (symbols.containsKey(name)) {
            symbols.put(name, value);
            return value;
        }
        if (parent != null) {
            return parent.set(name, value);
        }
        throw new IllegalStateException("Undefined `" + name + "`");
    }
}

abstract class Function
{
    abstract Object call(Object[] arguments);

    static Function NULL = new Function() {
        Object call(Object[] arguments) {
            return null;
        }
    };

    static Function create(final Scope scope, final String[] parameters, final Statement body) {
        return new Function() {
            Object call(Object[] arguments) {
                Scope callScope = new Scope(scope);
                callScope.define("arguments", Arrays.asList(arguments));
                for (int i = 0; i < parameters.length; i++) {
                    if (i < arguments.length) {
                        callScope.define(parameters[i], arguments[i]);
                    } else {
                        callScope.define(parameters[i], null);
                    }
                }
                try {
                    body.execute(callScope);
                    return null;
                } catch (ReturnException e) {
                    return e.value;
                }
            }
            public String toString() {
                return "[Function " + java.util.Arrays.asList(parameters) + " " + body + "]";
            }
        };
    }
}

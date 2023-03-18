import java.util.ArrayList;
import java.util.LinkedList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

class Token
{
    byte type;
    String value;

    Token(byte t, String v) {
        type = t;
        value = v;
    }

    public String toString() {
        return "[Token " + type + " " + value + "]";
    }
}

class Parsing
{
    static final byte ADD          = 0;
    static final byte AND          = 1;
    static final byte BREAK        = 2;
    static final byte CASE         = 3;
    static final byte COLON        = 4;
    static final byte COMMA        = 5;
    static final byte CONTINUE     = 6;
    static final byte CURLY_CLOSE  = 7;
    static final byte CURLY_OPEN   = 8;
    static final byte DECREMENT    = 9;
    static final byte DEFAULT      = 10;
    static final byte DIVIDE       = 11;
    static final byte DOT          = 12;
    static final byte ELSE         = 13;
    static final byte EQUAL        = 14;
    static final byte FALSE        = 15;
    static final byte FOR          = 16;
    static final byte FUNCTION     = 17;
    static final byte GREATER      = 18;
    static final byte GREATER_EQUAL= 19;
    static final byte IF           = 20;
    static final byte INCREMENT    = 21;
    static final byte LESS         = 22;
    static final byte LESS_EQUAL   = 23;
    static final byte MULTIPLY     = 24;
    static final byte NOT          = 25;
    static final byte NOT_EQUAL    = 26;
    static final byte NULL         = 27;
    static final byte NUMBER       = 28;
    static final byte OPERATOR     = 29;
    static final byte OR           = 30;
    static final byte QUESTION     = 31;
    static final byte REMAINDER    = 32;
    static final byte RETURN       = 33;
    static final byte ROUND_CLOSE  = 34;
    static final byte ROUND_OPEN   = 35;
    static final byte SEMICOLON    = 36;
    static final byte SET          = 37;
    static final byte SET_ADD      = 38;
    static final byte SET_DIVIDE   = 39;
    static final byte SET_MULTIPLY = 40;
    static final byte SET_REMAINDER= 41;
    static final byte SET_SUBTRACT = 42;
    static final byte SQUARE_CLOSE = 43;
    static final byte SQUARE_OPEN  = 44;
    static final byte STRING       = 45;
    static final byte SUBTRACT     = 46;
    static final byte SWITCH       = 47;
    static final byte SYMBOL       = 48;
    static final byte TRUE         = 49;
    static final byte VAR          = 50;
    static final byte WHILE        = 51;

    static Pattern symbol;
    static Pattern number;
    static Pattern hex;
    static Pattern source;

    static {
        symbol = Pattern.compile("[A-Za-z_$][A-Za-z0-9_$]*");
        number = Pattern.compile("\\d+(\\.\\d+)?");
        hex = Pattern.compile("0x[0-9a-f]+");
        source = Pattern.compile("\\/\\*[\\s\\S]*?\\*\\/|\\/\\/[^\r\n]*|[\\[\\]\\(\\)\\{\\}]|\\+\\+|\\-\\-|\\+\\=|\\-\\=|\\*\\=|\\/\\=|\\%\\=|\\!\\=|\\=\\=|\\&\\&|\\|\\||[\\<\\>\\=\\?\\:\\;\\.\\,\\-\\!\\+\\-\\*\\/\\%]|\"(\\\\.|[^\"])*?\"|'(\\\\.|[^'])*?'|0x[0-9a-f]+|\\d+(\\.\\d+)?|[A-Za-z_$][A-Za-z0-9_$]*");
    }

    static LinkedList<Token> tokenize(String string) {
        LinkedList<Token> tokens = new LinkedList<>();
        Matcher matcher = source.matcher(string);
        while (matcher.find()) {
            String match = matcher.group();
            switch (match) {
                case "!"       : tokens.add(new Token(NOT           , match)); break;
                case "!="      : tokens.add(new Token(NOT_EQUAL     , match)); break;
                case "%"       : tokens.add(new Token(REMAINDER     , match)); break;
                case "%="      : tokens.add(new Token(SET_REMAINDER , match)); break;
                case "&&"      : tokens.add(new Token(AND           , match)); break;
                case "("       : tokens.add(new Token(ROUND_OPEN    , match)); break;
                case ")"       : tokens.add(new Token(ROUND_CLOSE   , match)); break;
                case "*"       : tokens.add(new Token(MULTIPLY      , match)); break;
                case "*="      : tokens.add(new Token(SET_MULTIPLY  , match)); break;
                case "+"       : tokens.add(new Token(ADD           , match)); break;
                case "++"      : tokens.add(new Token(INCREMENT     , match)); break;
                case "+="      : tokens.add(new Token(SET_ADD       , match)); break;
                case ","       : tokens.add(new Token(COMMA         , match)); break;
                case "-"       : tokens.add(new Token(SUBTRACT      , match)); break;
                case "--"      : tokens.add(new Token(DECREMENT     , match)); break;
                case "-="      : tokens.add(new Token(SET_SUBTRACT  , match)); break;
                case "."       : tokens.add(new Token(DOT           , match)); break;
                case "/"       : tokens.add(new Token(DIVIDE        , match)); break;
                case "/="      : tokens.add(new Token(SET_DIVIDE    , match)); break;
                case ":"       : tokens.add(new Token(COLON         , match)); break;
                case ";"       : tokens.add(new Token(SEMICOLON     , match)); break;
                case "<"       : tokens.add(new Token(LESS          , match)); break;
                case "<="      : tokens.add(new Token(LESS_EQUAL    , match)); break;
                case "="       : tokens.add(new Token(SET           , match)); break;
                case "=="      : tokens.add(new Token(EQUAL         , match)); break;
                case ">"       : tokens.add(new Token(GREATER       , match)); break;
                case ">="      : tokens.add(new Token(GREATER_EQUAL , match)); break;
                case "?"       : tokens.add(new Token(QUESTION      , match)); break;
                case "["       : tokens.add(new Token(SQUARE_OPEN   , match)); break;
                case "]"       : tokens.add(new Token(SQUARE_CLOSE  , match)); break;
                case "break"   : tokens.add(new Token(BREAK         , match)); break;
                case "case"    : tokens.add(new Token(CASE          , match)); break;
                case "continue": tokens.add(new Token(CONTINUE      , match)); break;
                case "default" : tokens.add(new Token(DEFAULT       , match)); break;
                case "else"    : tokens.add(new Token(ELSE          , match)); break;
                case "false"   : tokens.add(new Token(FALSE         , match)); break;
                case "for"     : tokens.add(new Token(FOR           , match)); break;
                case "function": tokens.add(new Token(FUNCTION      , match)); break;
                case "if"      : tokens.add(new Token(IF            , match)); break;
                case "null"    : tokens.add(new Token(NULL          , match)); break;
                case "return"  : tokens.add(new Token(RETURN        , match)); break;
                case "switch"  : tokens.add(new Token(SWITCH        , match)); break;
                case "true"    : tokens.add(new Token(TRUE          , match)); break;
                case "var"     : tokens.add(new Token(VAR           , match)); break;
                case "while"   : tokens.add(new Token(WHILE         , match)); break;
                case "{"       : tokens.add(new Token(CURLY_OPEN    , match)); break;
                case "||"      : tokens.add(new Token(OR            , match)); break;
                case "}"       : tokens.add(new Token(CURLY_CLOSE   , match)); break;
                default:
                    if (match.startsWith("\"") || match.startsWith("'")) {
                        tokens.add(new Token(STRING, unquote(match)));
                    } else if (hex.matcher(match).matches()) {
                        tokens.add(new Token(NUMBER, String.valueOf(Integer.parseInt(match.substring(2), 16))));
                    } else if (number.matcher(match).matches()) {
                        tokens.add(new Token(NUMBER, match));
                    } else if (symbol.matcher(match).matches()) {
                        tokens.add(new Token(SYMBOL, match));
                    }
            }
        }
        return tokens;
    }

    static String unquote(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < s.length() - 1; i++) {
            char c = s.charAt(i);
            if (c == '\\') {
                c = s.charAt(++i);
                switch (c) {
                    case '\\':
                    case '\'':
                    case '"':
                        sb.append(c);
                        break;
                    case 't':
                        sb.append('\t');
                        break;
                    case 'r':
                        sb.append('\r');
                        break;
                    case 'n':
                        sb.append('\n');
                        break;
                    default:
                        throw new IllegalStateException("Invalid escape character");
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    static Statement parse(String string) {
        return parse(tokenize(string));
    }

    static Statement parse(LinkedList<Token> tokens) {
        ArrayList<Statement> statements = new ArrayList<>();
        while (true) {
            Token token = tokens.peekFirst();
            if (token == null) {
                break;
            }
            statements.add(parseStatement(tokens));
        }
        return Statement.Program(statements.toArray(new Statement[0]));
    }

    static Statement parseStatement(LinkedList<Token> tokens) {
        Token token = tokens.peekFirst();
        String label = null;
        if (token.type == SYMBOL) {
            Token next = tokens.get(1);
            if (next != null && next.type == COLON) {
                label = token.value;
                tokens.removeFirst();
                tokens.removeFirst();
                token = tokens.peekFirst();
            }
        }
        switch (token.type) {
            case VAR:
                return parseVar(tokens);
            case IF:
                return parseIf(tokens);
            case SWITCH:
                return parseSwitch(label, tokens);
            case WHILE:
                return parseWhile(label, tokens);
            case FOR:
                return parseFor(label, tokens);
            case CURLY_OPEN:
                return parseBlock(tokens);
            case BREAK:
                return parseBreak(tokens);
            case CONTINUE:
                return parseContinue(tokens);
            case FUNCTION:
                return parseFunction(tokens);
            case RETURN:
                return parseReturn(tokens);
            default:
                return Statement.Expression(parseExpression(tokens));
        }
    }

    static Statement parseFunction(LinkedList<Token> tokens) {
        tokens.removeFirst();
        Token name = tokens.removeFirst();
        if (name.type != SYMBOL) {
            throw new IllegalStateException("Expected symbol");
        }
        Token token = tokens.removeFirst();
        if (token.type != ROUND_OPEN) {
            throw new IllegalStateException("Expected `(`");
        }
        ArrayList<String> parameters = new ArrayList<>();
        while (true) {
            Token next = tokens.peekFirst();
            if (next == null) {
                throw new IllegalStateException("Expected `)`");
            } else if (next.type == ROUND_CLOSE) {
                tokens.removeFirst();
                break;
            } else if (next.type == COMMA) {
                tokens.removeFirst();
            } else {
                if (next.type != SYMBOL) {
                    throw new IllegalStateException("Expected symbol");
                }
                tokens.removeFirst();
                parameters.add(next.value);
            }
        }
        return Statement.Function(name.value, parameters.toArray(new String[0]), parseStatement(tokens));
    }

    static Statement parseSwitch(String label, LinkedList<Token> tokens) {
        tokens.removeFirst();
        Token token = tokens.removeFirst();
        if (token.type != ROUND_OPEN) {
            throw new IllegalStateException("Expected `(`");
        }
        Expression expression = parseExpression(tokens);
        token = tokens.removeFirst();
        if (token.type != ROUND_CLOSE) {
            throw new IllegalStateException("Expected `)`");
        }
        token = tokens.removeFirst();
        if (token.type != CURLY_OPEN) {
            throw new IllegalStateException("Expected `{`");
        }
        ArrayList<Case> cases = new ArrayList<>();
        while (true) {
            token = tokens.peekFirst();
            if (token == null) {
                throw new IllegalStateException("Expected `}`");
            }
            if (token.type == CURLY_CLOSE) {
                tokens.removeFirst();
                break;
            }
            switch (token.type) {
                case CASE: {
                    tokens.removeFirst();
                    Expression value = parseExpression(tokens);
                    token = tokens.removeFirst();
                    if (token.type != COLON) {
                        throw new IllegalStateException("Expected `:`");
                    }
                    ArrayList<Statement> statements = new ArrayList<>();
                    while (true) {
                        token = tokens.peekFirst();
                        if (token == null) {
                            throw new IllegalStateException("Expected `}`");
                        }
                        if (token.type == CURLY_CLOSE || token.type == CASE || token.type == DEFAULT) {
                            break;
                        }
                        statements.add(parseStatement(tokens));
                    }
                    cases.add(new Case(value, Statement.Block(statements.toArray(new Statement[0]))));
                    break;
                }
                case DEFAULT: {
                    tokens.removeFirst();
                    token = tokens.removeFirst();
                    if (token.type != COLON) {
                        throw new IllegalStateException("Expected `:`");
                    }
                    ArrayList<Statement> statements = new ArrayList<>();
                    while (true) {
                        token = tokens.peekFirst();
                        if (token == null) {
                            throw new IllegalStateException("Expected `}`");
                        }
                        if (token.type == CURLY_CLOSE || token.type == CASE || token.type == DEFAULT) {
                            break;
                        }
                        statements.add(parseStatement(tokens));
                    }
                    cases.add(new Case(null, Statement.Block(statements.toArray(new Statement[0]))));
                    break;
                }
                default:
                    throw new IllegalStateException("Expected `case` or `default`");
            }
        }
        return Statement.Switch(label, expression, cases.toArray(new Case[0]));
    }

    static Statement parseReturn(LinkedList<Token> tokens) {
        tokens.removeFirst();
        Token token = tokens.peekFirst();
        switch (token.type) {
            case INCREMENT:
            case DECREMENT:
            case SUBTRACT:
            case NOT:
            case TRUE:
            case FALSE:
            case NULL:
            case SYMBOL:
            case NUMBER:
            case STRING:
            case FUNCTION:
            case SQUARE_OPEN:
            case ROUND_OPEN:
            case CURLY_OPEN:
                return Statement.Return(parseExpression(tokens));
            default:
                return Statement.Return(Expression.Null());
        }
    }

    static Statement parseFor(String label, LinkedList<Token> tokens) {
        tokens.removeFirst();
        Token token = tokens.removeFirst();
        if (token.type != ROUND_OPEN) {
            throw new IllegalStateException("Expected `(`");
        }
        Statement init;
        token = tokens.peekFirst();
        if (token.type == SEMICOLON) {
            init = Statement.Empty();
            tokens.removeFirst();
        } else {
            init = parseStatement(tokens);
            token = tokens.removeFirst();
            if (token.type != SEMICOLON) {
                throw new IllegalStateException("Expected `;`");
            }
        }
        Expression condition;
        token = tokens.peekFirst();
        if (token.type == SEMICOLON) {
            condition = Expression.True();
            tokens.removeFirst();
        } else {
            condition = parseExpression(tokens);
            token = tokens.removeFirst();
            if (token.type != SEMICOLON) {
                throw new IllegalStateException("Expected `;`");
            }
        }
        Statement step;
        token = tokens.peekFirst();
        if (token.type == ROUND_CLOSE) {
            step = Statement.Empty();
            tokens.removeFirst();
        } else {
            step = parseStatement(tokens);
            token = tokens.removeFirst();
            if (token.type != ROUND_CLOSE) {
                throw new IllegalStateException("Expected `)`");
            }
        }
        Statement body = parseStatement(tokens);
        return Statement.For(label, init, condition, step, body);
    }

    static Statement parseBreak(LinkedList<Token> tokens) {
        tokens.removeFirst();
        Token token = tokens.peekFirst();
        String label;
        if (token.type == SYMBOL) {
            label = token.value;
            tokens.removeFirst();
        } else {
            label = null;
        }
        return Statement.Break(label);
    }

    static Statement parseContinue(LinkedList<Token> tokens) {
        tokens.removeFirst();
        Token token = tokens.peekFirst();
        String label;
        if (token.type == SYMBOL) {
            label = token.value;
            tokens.removeFirst();
        } else {
            label = null;
        }
        return Statement.Continue(label);
    }

    static Statement parseVar(LinkedList<Token> tokens) {
        tokens.removeFirst();
        Token name = tokens.removeFirst();
        if (name.type != SYMBOL) {
            throw new IllegalStateException("Expected symbol");
        }
        Token token = tokens.removeFirst();
        if (token.type != SET) {
            throw new IllegalStateException("Expected `=`");
        }
        return Statement.Var(name.value, parseExpression(tokens));
    }

    static Statement parseBlock(LinkedList<Token> tokens) {
        tokens.removeFirst();
        ArrayList<Statement> statements = new ArrayList<>();
        while (true) {
            Token token = tokens.peekFirst();
            if (token == null) {
                throw new IllegalStateException("Expected `}`");
            }
            if (token.type == CURLY_CLOSE) {
                tokens.removeFirst();
                break;
            }
            statements.add(parseStatement(tokens));
        }
        return Statement.Block(statements.toArray(new Statement[0]));
    }

    static Statement parseIf(LinkedList<Token> tokens) {
        tokens.removeFirst();
        Token token = tokens.removeFirst();
        if (token.type != ROUND_OPEN) {
            throw new IllegalStateException("Expected `(`");
        }
        Expression condition = parseExpression(tokens);
        token = tokens.removeFirst();
        if (token.type != ROUND_CLOSE) {
            throw new IllegalStateException("Expected `)`");
        }
        Statement ifTrue = parseStatement(tokens);
        token = tokens.peekFirst();
        Statement ifFalse;
        if (token != null && token.type == ELSE) {
            tokens.removeFirst();
            ifFalse = parseStatement(tokens);
        } else {
            ifFalse = Statement.Empty();
        }
        return Statement.If(condition, ifTrue, ifFalse);
    }

    static Statement parseWhile(String label, LinkedList<Token> tokens) {
        tokens.removeFirst();
        Token token = tokens.removeFirst();
        if (token.type != ROUND_OPEN) {
            throw new IllegalStateException("Expected `(`");
        }
        Expression condition = parseExpression(tokens);
        token = tokens.removeFirst();
        if (token.type != ROUND_CLOSE) {
            throw new IllegalStateException("Expected `)`");
        }
        return Statement.While(label, condition, parseStatement(tokens));
    }

    static Expression parseExpression(LinkedList<Token> tokens) {
        return parseSet(tokens);
    }

    static Expression parseSet(LinkedList<Token> tokens) {
        Expression expression = parseConditional(tokens);
        loop:
        while (true) {
            Token token = tokens.peekFirst();
            if (token == null) {
                break;
            } else {
                switch (token.type) {
                    case SET:
                        tokens.removeFirst();
                        expression = Expression.Set(expression, parseExpression(tokens));
                        break;
                    case SET_ADD:
                        tokens.removeFirst();
                        expression = Expression.SetAdd(expression, parseExpression(tokens));
                        break;
                    case SET_MULTIPLY:
                        tokens.removeFirst();
                        expression = Expression.SetMultiply(expression, parseExpression(tokens));
                        break;
                    case SET_DIVIDE:
                        tokens.removeFirst();
                        expression = Expression.SetDivide(expression, parseExpression(tokens));
                        break;
                    case SET_REMAINDER:
                        tokens.removeFirst();
                        expression = Expression.SetRemainder(expression, parseExpression(tokens));
                        break;
                    default:
                        break loop;
                }
            }
        }
        return expression;
    }

    static Expression parseConditional(LinkedList<Token> tokens) {
        Expression condition = parseBoolean(tokens);
        while (true) {
            Token token = tokens.peekFirst();
            if (token != null && token.type == QUESTION) {
                tokens.removeFirst();
                Expression ifTrue = parseExpression(tokens);
                token = tokens.removeFirst();
                if (token == null || token.type != COLON) {
                    throw new IllegalStateException("Expected `:`");
                }
                condition = Expression.Conditional(condition, ifTrue, parseExpression(tokens));
            } else {
                break;
            }
        }
        return condition;
    }

    static Expression parseBoolean(LinkedList<Token> tokens) {
        Expression expression = parseCompare(tokens);
        loop:
        while (true) {
            Token token = tokens.peekFirst();
            if (token == null) {
                break;
            } else {
                switch (token.type) {
                    case AND:
                        tokens.removeFirst();
                        expression = Expression.And(expression, parseCompare(tokens));
                        break;
                    case OR:
                        tokens.removeFirst();
                        expression = Expression.Or(expression, parseCompare(tokens));
                        break;
                    default:
                        break loop;
                }
            }
        }
        return expression;
    }

    static Expression parseCompare(LinkedList<Token> tokens) {
        Expression expression = parseAdd(tokens);
        loop:
        while (true) {
            Token token = tokens.peekFirst();
            if (token == null) {
                break;
            } else {
                switch (token.type) {
                    case LESS:
                        tokens.removeFirst();
                        expression = Expression.Less(expression, parseAdd(tokens));
                        break;
                    case LESS_EQUAL:
                        tokens.removeFirst();
                        expression = Expression.LessEqual(expression, parseAdd(tokens));
                        break;
                    case GREATER:
                        tokens.removeFirst();
                        expression = Expression.Greater(expression, parseAdd(tokens));
                        break;
                    case GREATER_EQUAL:
                        tokens.removeFirst();
                        expression = Expression.GreaterEqual(expression, parseAdd(tokens));
                        break;
                    case EQUAL:
                        tokens.removeFirst();
                        expression = Expression.Equal(expression, parseAdd(tokens));
                        break;
                    case NOT_EQUAL:
                        tokens.removeFirst();
                        expression = Expression.NotEqual(expression, parseAdd(tokens));
                        break;
                    default:
                        break loop;
                }
            }
        }
        return expression;
    }

    static Expression parseAdd(LinkedList<Token> tokens) {
        Expression expression = parseMultiply(tokens);
        loop:
        while (true) {
            Token token = tokens.peekFirst();
            if (token == null) {
                break;
            } else {
                switch (token.type) {
                    case ADD:
                        tokens.removeFirst();
                        expression = Expression.Add(expression, parseMultiply(tokens));
                        break;
                    case SUBTRACT:
                        tokens.removeFirst();
                        expression = Expression.Subtract(expression, parseMultiply(tokens));
                        break;
                    default:
                        break loop;
                }
            }
        }
        return expression;
    }

    static Expression parseMultiply(LinkedList<Token> tokens) {
        Expression expression = parsePrefix(tokens);
        loop:
        while (true) {
            Token token = tokens.peekFirst();
            if (token == null) {
                break;
            } else {
                switch (token.type) {
                    case MULTIPLY:
                        tokens.removeFirst();
                        expression = Expression.Multiply(expression, parsePrefix(tokens));
                        break;
                    case DIVIDE:
                        tokens.removeFirst();
                        expression = Expression.Divide(expression, parsePrefix(tokens));
                        break;
                    case REMAINDER:
                        expression = Expression.Remainder(expression, parsePrefix(tokens));
                        tokens.removeFirst();
                        break;
                    default:
                        break loop;
                }
            }
        }
        return expression;
    }

    static Expression parsePrefix(LinkedList<Token> tokens) {
        Token token = tokens.peekFirst();
        switch (token.type) {
            case INCREMENT:
            case DECREMENT:
            case SUBTRACT:
            case NOT:
                tokens.removeFirst();
                break;
        }
        Expression expression = parsePostfix(tokens);
        switch (token.type) {
            case INCREMENT:
                expression = Expression.Preincrement(expression);
                break;
            case DECREMENT:
                expression = Expression.Predecrement(expression);
                break;
            case SUBTRACT:
                expression = Expression.Negate(expression);
                break;
            case NOT:
                expression = Expression.Not(expression);
                break;
        }
        return expression;
    }

    static Expression parsePostfix(LinkedList<Token> tokens) {
        Expression expression = parseTerm(tokens);
        loop:
        while (true) {
            Token token = tokens.peekFirst();
            if (token == null) {
                break;
            } else {
                switch (token.type) {
                    case INCREMENT:
                        tokens.removeFirst();
                        expression = Expression.Postincrement(expression);
                        break;
                    case DECREMENT:
                        tokens.removeFirst();
                        expression = Expression.Postdecrement(expression);
                        break;
                    case SQUARE_OPEN:
                        tokens.removeFirst();
                        Expression index = parseExpression(tokens);
                        token = tokens.removeFirst();
                        if (token.type != SQUARE_CLOSE) {
                            throw new IllegalStateException("Expected `]`");
                        }
                        expression = Expression.Element(expression, index);
                        break;
                    case DOT:
                        tokens.removeFirst();
                        token = tokens.removeFirst();
                        if (token.type != SYMBOL) {
                            throw new IllegalStateException("Expected symbol");
                        }
                        expression = Expression.Value(expression, token.value);
                        break;
                    case ROUND_OPEN:
                        tokens.removeFirst();
                        ArrayList<Expression> arguments = new ArrayList<>();
                        while (true) {
                            Token next = tokens.peekFirst();
                            if (next == null) {
                                throw new IllegalStateException("Expected `)`");
                            } else if (next.type == ROUND_CLOSE) {
                                tokens.removeFirst();
                                break;
                            } else if (next.type == COMMA) {
                                tokens.removeFirst();
                            } else {
                                arguments.add(parseExpression(tokens));
                            }
                        }
                        expression = Expression.Call(expression, arguments.toArray(new Expression[0]));
                        break;
                    default:
                        break loop;
                }
            }
        }
        return expression;
    }

    static Expression parseTerm(LinkedList<Token> tokens) {
        Token token = tokens.removeFirst();
        switch (token.type) {
            case TRUE:
                return Expression.True();
            case FALSE:
                return Expression.False();
            case NULL:
                return Expression.Null();
            case SYMBOL:
                return Expression.Symbol(token.value);
            case NUMBER:
                return Expression.Number(Float.parseFloat(token.value));
            case STRING:
                return Expression.String(token.value);
            case FUNCTION: {
                token = tokens.removeFirst();
                if (token.type != ROUND_OPEN) {
                    throw new IllegalStateException("Expected `(`");
                }
                ArrayList<String> parameters = new ArrayList<>();
                while (true) {
                    Token next = tokens.peekFirst();
                    if (next == null) {
                        throw new IllegalStateException("Expected `)`");
                    } else if (next.type == ROUND_CLOSE) {
                        tokens.removeFirst();
                        break;
                    } else if (next.type == COMMA) {
                        tokens.removeFirst();
                    } else {
                        if (next.type != SYMBOL) {
                            throw new IllegalStateException("Expected symbol");
                        }
                        tokens.removeFirst();
                        parameters.add(next.value);
                    }
                }
                return Expression.Function(parameters.toArray(new String[0]), parseStatement(tokens));
            }
            case SQUARE_OPEN: {
                ArrayList<Expression> elements = new ArrayList<>();
                while (true) {
                    Token next = tokens.peekFirst();
                    if (next == null) {
                        throw new IllegalStateException("Expected `]`");
                    } else if (next.type == SQUARE_CLOSE) {
                        tokens.removeFirst();
                        break;
                    } else if (next.type == COMMA) {
                        tokens.removeFirst();
                    } else {
                        elements.add(parseExpression(tokens));
                    }
                }
                return Expression.List(elements.toArray(new Expression[0]));
            }
            case CURLY_OPEN: {
                ArrayList<Entry> entries = new ArrayList<>();
                while (true) {
                    Token next = tokens.peekFirst();
                    if (next == null) {
                        throw new IllegalStateException("Expected `}`");
                    } else if (next.type == CURLY_CLOSE) {
                        tokens.removeFirst();
                        break;
                    } else if (next.type == COMMA) {
                        tokens.removeFirst();
                    } else {
                        if (next.type != SYMBOL) {
                            throw new IllegalStateException("Expected symbol");
                        }
                        String key = tokens.removeFirst().value;
                        next = tokens.removeFirst();
                        if (next.type != COLON) {
                            throw new IllegalStateException("Expected `:`");
                        }
                        entries.add(new Entry(key, parseExpression(tokens)));
                    }
                }
                return Expression.Map(entries.toArray(new Entry[0]));
            }
            case ROUND_OPEN: {
                Expression expression = parseExpression(tokens);
                token = tokens.removeFirst();
                if (token.type != ROUND_CLOSE) {
                    throw new IllegalStateException("Expected `)`");
                }
                return expression;
            }
            default:
                throw new IllegalStateException("Expected expression");
        }
    }
}

package elte.electronicStore.query;

import com.querydsl.core.types.JavaTemplates;
import com.querydsl.core.types.Operator;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.PathType;

/**
 * {@code CollQueryTemplates} extends {@link JavaTemplates} to add module specific operation
 * templates.
 *
 * @author Xavier
 */
public class JMSTemplates extends JavaTemplates {

    @SuppressWarnings("FieldNameHidesFieldInSuperclass") //intentional
    public static final JMSTemplates DEFAULT = new JMSTemplates();

    protected JMSTemplates() {
        
        // boolean
        add(Ops.AND, "{0} AND  {1}", Precedence.AND);
        add(Ops.NOT, "NOT {0}", Precedence.NOT_HIGH);
        add(Ops.OR, "{0} OR {1}", Precedence.OR);
        
        add(Ops.IS_NULL, "{0s} IS NULL");
        add(Ops.IS_NOT_NULL, "{0s} IS NOT NULL");

        // collection
        add(Ops.IN, " {0} IN ({1s})");
        add(Ops.NOT_IN, "{0} NOT IN ({1s})");

        //like
        add(Ops.LIKE, "{0} LIKE {1s}", Precedence.COMPARISON);
        add(Ops.LIKE_IC, "{0l} LIKE {1l}", Precedence.COMPARISON);
        add(Ops.LIKE_ESCAPE, "{0} LIKE {1} escape '{2s}'", Precedence.COMPARISON);
        add(Ops.LIKE_ESCAPE_IC, "{0l} LIKE {1l} escape '{2s}'", Precedence.COMPARISON);
        add(Ops.STARTS_WITH, "{0} LIKE '{1s}%'");
        add(Ops.ENDS_WITH, "{0} LIKE '%{1s}'");
        
        
        // Comparable
        add(Ops.BETWEEN, "({0}  BETWEEN  {1s} AND {2s})", Precedence.COMPARISON);
        add(Ops.GOE, "{0} >= {1s}", Precedence.COMPARISON);
        add(Ops.GT, "{0} > {1s}", Precedence.COMPARISON);
        add(Ops.LOE, "{0} <= {1s}", Precedence.COMPARISON);
        add(Ops.LT, "{0} < {1s}", Precedence.COMPARISON);
        add(Ops.EQ, "{0} = '{1s}'");
        add(Ops.NE," {0} <> '{1s}'");
        
//        // path types
        add(PathType.PROPERTY, "{1s}");
        add(PathType.VARIABLE, "{0s}");
        add(PathType.DELEGATE, "{0}");
        add(Ops.ORDINAL, "ordinal({0})");
        
        
         // numeric
        add(Ops.NEGATE, "-{0}", Precedence.NEGATE);
        add(Ops.ADD, "{0s} + {1s}", Precedence.ARITH_LOW);
        add(Ops.DIV, "{0s} / {1s}", Precedence.ARITH_HIGH);
        add(Ops.MOD, "{0s} % {1s}", Precedence.ARITH_HIGH);
        add(Ops.MULT, "{0s} * {1s}", Precedence.ARITH_HIGH);
        add(Ops.SUB, "{0s} - {1s}", Precedence.ARITH_LOW);
        
        // Math
        for (Operator op : Ops.MathOps.values()) {
            add(op, "Math." + getTemplate(op));
        }
    }
}

package elte.electronicStore.query;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.primitives.Primitives;
import com.querydsl.codegen.Serializer;
import com.querydsl.core.QueryException;
import com.querydsl.core.support.SerializerBase;
import com.querydsl.core.types.*;

/**
 * {@code CollQuerySerializer} is a {@link Serializer} implementation for the Java language
 *
 * @author Xavier
 */
public class JMSSelectorSerializer extends SerializerBase<JMSSelectorSerializer> {


    private static final Set<Class<?>> WRAPPER_TYPES = ImmutableSet.copyOf(Primitives.allWrapperTypes());

    private static final Map<Operator, String> OPERATOR_SYMBOLS = Maps.newIdentityHashMap();

    private static final Map<Class<?>, String> CAST_SUFFIXES = Maps.newHashMap();
    
    private final Templates templates;

    private boolean strict = true;
     private static final Set<Operator> SAME_PRECEDENCE = ImmutableSet.<Operator>of(Ops.CASE,
        Ops.CASE_WHEN, Ops.CASE_ELSE, Ops.CASE_EQ, Ops.CASE_EQ_WHEN, Ops.CASE_EQ_ELSE);

    static {
        OPERATOR_SYMBOLS.put(Ops.EQ, " == ");
        OPERATOR_SYMBOLS.put(Ops.NE, " != ");
        OPERATOR_SYMBOLS.put(Ops.GT, " > ");
        OPERATOR_SYMBOLS.put(Ops.LT, " < ");
        OPERATOR_SYMBOLS.put(Ops.GOE, " >= ");
        OPERATOR_SYMBOLS.put(Ops.LOE, " <= ");

        OPERATOR_SYMBOLS.put(Ops.ADD, " + ");
        OPERATOR_SYMBOLS.put(Ops.SUB, " - ");
        OPERATOR_SYMBOLS.put(Ops.MULT, " * ");
        OPERATOR_SYMBOLS.put(Ops.DIV, " / ");

        CAST_SUFFIXES.put(Boolean.class, ".booleanValue()");
        CAST_SUFFIXES.put(Byte.class, ".byteValue()");
        CAST_SUFFIXES.put(Character.class, ".charValue()");
        CAST_SUFFIXES.put(Double.class, ".doubleValue()");
        CAST_SUFFIXES.put(Float.class, ".floatValue()");
        CAST_SUFFIXES.put(Integer.class, ".intValue()");
        CAST_SUFFIXES.put(Long.class, ".longValue()");
        CAST_SUFFIXES.put(Short.class, ".shortValue()");
        CAST_SUFFIXES.put(String.class, ".toString()");
    }
    public JMSSelectorSerializer(Templates templates) {
        super(templates);
        this.templates =templates;
    }

    @Override
    public Void visit(Path<?> path, Void context) {
        final PathType pathType = path.getMetadata().getPathType();
         if (pathType == PathType.PROPERTY) {
            final Path<?> parent = path.getMetadata().getParent();
            final String property = path.getMetadata().getName();
            final Class<?> parentType = parent.getType();
            try {
                // getter
                Method m = getAccessor(parentType, property);
                if (m != null && Modifier.isPublic(m.getModifiers())) {
                    handle(parent);
                    append(property);
                } 

            } catch (Exception e) {
                throw new QueryException(e);
            }
            
             } else if (pathType == PathType.DELEGATE) {
            append("(");
            append("(").append(path.getType().getName()).append(")");
            path.getMetadata().getParent().accept(this, context);
            append(")");

        } 
         return null;
        
    }

    @Override
    protected void visitOperation(Class<?> type, Operator operator, List<? extends Expression<?>> args) {

        final Template template = templates.getTemplate(operator);
        if (template != null) {
            final int precedence = templates.getPrecedence(operator);
            boolean first = true;
            for (final Template.Element element : template.getElements()) {
                final Object rv = element.convert(args);
                if (rv instanceof Expression) {
                    final Expression<?> expr = (Expression<?>) rv;
                    if (precedence > -1 && expr instanceof Operation) {
                        Operator op = ((Operation<?>) expr).getOperator();
                        int opPrecedence = templates.getPrecedence(op);
                        if (precedence < opPrecedence) {
                            append("(").handle(expr).append(")");
                        } else if (!first && precedence == opPrecedence && !SAME_PRECEDENCE.contains(op)) {
                            append("(").handle(expr).append(")");
                        } else {
                            handle(expr);
                        }
                    } else {
                        handle(expr);
                    }
                    first = false;
                } else if (element.isString()) {
//                    append(rv.toString());
                    append(rv.toString().replace("[", "").replace("]", ""));
                } else {
                    visitConstant(rv);
                }
            }
        } else if (strict) {
            throw new IllegalArgumentException("No pattern found for " + operator);
        } else {
            append(operator.toString());
            append("(");
            handle(", ", args);
            append(")");
        }
        }
    
    @Override
    public void visitConstant(Object constant) {
        super.visitConstant(constant); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public Void visit(ParamExpression<?> expr, Void context) {
        return null;
    }

    @Override
    public Void visit(FactoryExpression<?> expr, Void context) {
         visitConstant(expr);
        append(".newInstance(");
        handle(", ", expr.getArgs());
        append(")");
        return null;
    }


    @Override
    public Void visit(SubQueryExpression<?> query, Void context) {
       throw new IllegalArgumentException("Not supported");
    }
    
    private Method getAccessor(Class<?> owner, String property) {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(owner);
            PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor pd : descriptors) {
                if (pd.getName().equals(property)) {
                    return pd.getReadMethod();
                }
            }
            return null;
        } catch (IntrospectionException e) {
            return null;
        }
    }
}
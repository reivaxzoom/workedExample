package edu.elte.jmsSelExpr;

import org.junit.Test;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import static org.junit.Assert.assertEquals;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.Assert;

/**
 * Provides boolean expression constructor examples with operations such as:
 * or, and, it meet JMS selector compliance
 * @author Xavier
 */
public class BooleanExpressionsTest extends AbstractExpressionTest{

    @Test
    public void or1() {
        Expression addidas = rd.category.eq("sport").or(rd.category.eq("shoes")).or(rd.category.eq("test"));
        assertEquals("category = 'sport' OR category = 'shoes' OR category = 'test'", super.serialize(addidas));
    }

    @Test
    public void or2() {
        BooleanBuilder addidas1 = new BooleanBuilder();
        Expression wallmart1 = addidas1.or(sp.or(sh).or(ts));
        assertEquals("category = 'sport' OR category = 'shoes' OR category = 'test'", super.serialize(wallmart1));
    }

    @Test
    public void or3() {
//        Expression wallmart1 = Expressions.anyOf(sp, sh, ts);
        BooleanBuilder wallmart1  = new BooleanBuilder();
        wallmart1.andAnyOf(sp, sh, ts);
        assertEquals("category = 'sport' OR category = 'shoes' OR category = 'test'", super.serialize(wallmart1));
    }
     @Test
    public void or4() {
         BooleanBuilder builder = new BooleanBuilder();
        builder.andAnyOf(sp, sh, ts);
        assertEquals("category = 'sport' OR category = 'shoes' OR category = 'test'", super.serialize(builder));
    }
    

    @Test
    public void and1() {
        BooleanBuilder bb = new BooleanBuilder();
        bb.and(rd.category.eq("food")).and(rd.category.eq("pharmacy")).and(rd.category.eq("library"));
        assertEquals("category = 'food' AND  category = 'pharmacy' AND  category = 'library'", serialize(bb));
    }
    
    
    @Test
    public void and2() {
        BooleanBuilder addidas1 = new BooleanBuilder();
        Expression andLine = addidas1.and(sp.and(sh).and(ts));
        assertEquals("category = 'sport' AND  category = 'shoes' AND  category = 'test'", serialize(andLine));
    }

    @Test
    public void and3() {
        BooleanBuilder andAll= new BooleanBuilder();
        andAll.andAnyOf(sp,sh,ts);
//        Expression andAll = Expressions.allOf(sp, sh, ts);
        assertEquals("category = 'sport' AND  category = 'shoes' AND  category = 'test'", serialize(andAll));
    }
    
    @Test
    public void and4() {
         BooleanBuilder builder = new BooleanBuilder();
        builder.orAllOf(sp, sh, ts);
        assertEquals("category = 'sport' AND  category = 'shoes' AND  category = 'test'", super.serialize(builder));
    }
    
    @Test
    public void and123(){
         BooleanExpression andAll = Expressions.allOf(sp, sh, ts);
         BooleanBuilder bb = new BooleanBuilder();
         bb.and(sp.and(sh).and(ts));
         BooleanExpression andInline = rd.category.eq("sport").and(rd.category.eq("shoes")).and(rd.category.eq("test"));
         BooleanBuilder bb1 = new BooleanBuilder();
         bb1.and(rd.category.eq("sport")).and(rd.category.eq("shoes")).and(rd.category.eq("test"));
         assertThat(serialize(andAll),is(allOf( equalTo(serialize(bb1)), equalTo(serialize(andInline)),equalTo(serialize(bb)),notNullValue())));
    }
    
    @Test
    public void or123(){
         BooleanExpression andAll = Expressions.anyOf(sp, sh, ts);
         BooleanBuilder bb = new BooleanBuilder();
         bb.or(sp.or(sh).or(ts));
         BooleanExpression andInline = rd.category.eq("sport").or(rd.category.eq("shoes")).or(rd.category.eq("test"));
         BooleanBuilder bb1 = new BooleanBuilder();

         bb1.not();
         bb1.or(rd.category.eq("sport")).or(rd.category.eq("shoes")).or(rd.category.eq("test"));
         
         assertThat(serialize(andAll),is(allOf( equalTo(serialize(bb1)), equalTo(serialize(andInline)),equalTo(serialize(bb)),notNullValue())));
    }
    
    @Test
    public void andOr(){
         BooleanExpression bdg = rd.budget.between(100,500);
         BooleanExpression itn = rd.budget.gt(100);
         BooleanBuilder builder = new BooleanBuilder(sp);
         builder.and(bdg.or(itn)).andNot(ts);
         assertEquals("category = 'sport' AND  ((budget  BETWEEN  100 AND 500) OR budget > 100) AND  NOT (category = 'test')", serialize(builder));
    }
    
    
    @Test
    public void ar1(){
        BooleanBuilder b1 = new BooleanBuilder(sp);
        b1.not().not().not().not().not().not();
        System.out.println(serialize(b1));
        Assert.assertEquals("NOT NOT NOT NOT NOT NOT (category = 'sport')", serialize(b1));
    }
    @Test
    public void orNot(){
        BooleanBuilder b1 = new BooleanBuilder(sp);
        b1.orNot(ts);
        System.out.println(serialize(b1));
        Assert.assertEquals("category = 'sport' OR NOT (category = 'test')", serialize(b1));
    }
    
    
}

package edu.elte.jmsSelExpr;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.BooleanExpression;
import elte.sportStore.model.QRequestData;
import elte.sportStore.query.JMSSelectorSerializer;
import elte.sportStore.query.JMSTemplates;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides the basic skeleton for queries 
 * @author Xavier
 */
class AbstractExpressionTest {
    
    protected static final QRequestData rd = QRequestData.requestData;
    
     protected BooleanExpression sp = rd.category.eq("sport");
     protected BooleanExpression sh = rd.category.eq("shoes");
     protected BooleanExpression ts = rd.category.eq("test");
     
     List<String> countries = Arrays.asList("Belgium", "Bulgaria", "Croatia", "Cyprus", "Czech Republic", "Denmark", "Estonia", "Finland", "France", "Germany", "Greece", "Hungary", "Ireland", "Italy", "Latvia", "Lithuania", "Luxembourg", "Malta", "Netherlands", "Poland", "Portugal", "Romania", "Slovakia", "Slovenia", "Spain", "Sweden", "United Kingdom");
     List str1=countries.subList(0, 5).stream().map(x -> "'"+x+"'" ).collect(Collectors.toList());
    
     
      protected String serialize(Expression exp) {
        JMSSelectorSerializer serializer = new JMSSelectorSerializer(JMSTemplates.DEFAULT);
        serializer.handle(exp);
        return serializer.toString();
    }
     
}
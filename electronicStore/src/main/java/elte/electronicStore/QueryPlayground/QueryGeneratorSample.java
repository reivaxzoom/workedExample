package elte.electronicStore.QueryPlayground;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Expression;
import elte.electronicStore.model.QRequestData;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.StringExpression;
import elte.electronicStore.query.JMSSelectorSerializer;
import elte.electronicStore.query.JMSTemplates;

/**
 *
 * @author Xavier
 */
public class QueryGeneratorSample {
        private static final QRequestData rd = QRequestData.requestData;
        
    
    public static void main(String[] args) {
        QueryGeneratorSample qg= new QueryGeneratorSample();
        
         List<String> countries1 = Arrays.asList("Hungary", "Ireland", "Italy", "Latvia", "Lithuania", "Luxembourg", "Malta", "Netherlands", "Poland", "Portugal", "Romania", "Slovakia", "Slovenia", "Spain", "Sweden", "United Kingdom");
        List formatedCountries = countries1.stream().map(x -> "'" + x + "'").collect(Collectors.toList());
        BooleanBuilder bb = new BooleanBuilder(rd.country.in(formatedCountries));
        
        bb.and(rd.category.eq("sport").or(rd.category.eq("gym")).or(rd.category.eq("shoes")).or(rd.category.eq("beverages")).or(rd.category.eq("futbol")));
        bb.and(rd.budget.between(100,200));
        bb.and(rd.phone.startsWith("36"));
        bb.and(rd.frecuent.isFalse());
        
        

//         Jms like is not able to check wheter a word belogs to an argument or not, like only checks if it start or ends. 
//         .comments.contains("party")
//         bb.and(rd.comments.contains("party").or(rd.comments.contains("game")).or(rd.comments.contains("outdoor")));
//         bb.and(rd.date)
        System.out.println(qg.genQuery(bb));
        
        
        
        Expression addidas=rd.category.eq("sport").or(rd.category.eq("shoes")).or(rd.category.eq("test"));
        System.out.println(qg.genQuery(addidas));
        Expression bestBuy=rd.category.eq("sport").or(rd.category.eq("shoes")).or(rd.category.eq("test"));
        System.out.println(qg.genQuery(bestBuy));
        Expression supermarket=rd.category.eq("sport").or(rd.category.eq("shoes")).or(rd.category.eq("test"));
        System.out.println(qg.genQuery(supermarket));
        Expression wallmart=rd.category.eq("sport").or(rd.category.eq("shoes")).or(rd.category.eq("test"));
        System.out.println(qg.genQuery(wallmart));
        
        
         BooleanExpression sp = rd.category.eq("sport");
         BooleanExpression sh = rd.category.eq("shoes");
         BooleanExpression ts = rd.category.eq("test");
         BooleanBuilder addidas1 = new BooleanBuilder();
        Expression wallmart1= addidas1.or(sp.or(sh).or(ts));
        System.out.println(qg.genQuery(wallmart1));
        
        Expression wallmart2= Expressions.anyOf(sp,sh,ts);
        System.out.println(qg.genQuery(wallmart2));

        
        List<String> countries = Arrays.asList("Belgium", "Bulgaria", "Croatia", "Cyprus", "Czech Republic", "Denmark", "Estonia", "Finland", "France", "Germany", "Greece", "Hungary", "Ireland", "Italy", "Latvia", "Lithuania", "Luxembourg", "Malta", "Netherlands", "Poland", "Portugal", "Romania", "Slovakia", "Slovenia", "Spain", "Sweden", "United Kingdom");
        List str1=countries.subList(0, 5).stream().map(x -> "'"+x+"'" ).collect(Collectors.toList());
        //in
        Expression in=rd.country.in(str1);
        System.out.println(qg.genQuery(in));
        Expression nin=rd.country.notIn(str1);
        System.out.println(qg.genQuery(nin));
        //like
        Expression like=rd.country.like("Belgium");
        System.out.println(qg.genQuery(like));
        Expression nlike=rd.country.notLike("Croatia");
        System.out.println(qg.genQuery(nlike));
        //between
        Expression bt=rd.budget.between(100,200);
        System.out.println(qg.genQuery(bt));
        Expression nbt=rd.budget.notBetween(500,600);
        System.out.println(qg.genQuery(nbt));
        // is null
        Expression nul=rd.comments.isNull();
        System.out.println(qg.genQuery(nul));
        Expression nnul=rd.comments.isNotNull();
        System.out.println(qg.genQuery(nnul));
        //comparators lt gt
        Expression gt=rd.budget.gt(100);
        System.out.println(qg.genQuery(gt));
        Expression lt=rd.budget.lt(50);
        System.out.println(qg.genQuery(lt));
        //aritmetic operations
        Expression add=rd.budget.add(5);
        System.out.println(qg.genQuery(add));
        Expression sus=rd.budget.subtract(2);
        System.out.println(qg.genQuery(sus));
        Expression times=rd.budget.multiply(5);
        System.out.println(qg.genQuery(times));
        Expression div=rd.budget.divide(2);
        System.out.println(qg.genQuery(div));
        
//        mix sample releaseYear * 2 > 2000 - 20
         NumberExpression<Integer> n1 = Expressions.numberPath(Integer.class, "100");
         NumberExpression<Integer> n2 = Expressions.numberPath(Integer.class, "20");
         
         
        Expression mix=rd.budget.multiply(2).gt(n2.subtract(n1));
//        Expression mix=n2.subtract(n1);
        System.out.println(qg.genQuery(mix));
        System.out.println(mix.toString());
        
        StringExpression a = Expressions.stringPath("a");
        StringExpression b = Expressions.stringPath("b");
        Expression<?> expr = a.append(b);
        System.out.println(expr.toString());
        System.out.println(qg.genQuery(expr));
        
        //start end with
        Expression stw=rd.category.startsWith("sp");
        System.out.println(qg.genQuery(stw));
    }
    
    
    
      protected static String serialize(Expression exp) {
        JMSSelectorSerializer serializer = new JMSSelectorSerializer(JMSTemplates.DEFAULT);
        serializer.handle(exp);
        return serializer.toString();
    }
    
     public String genQuery(Expression exp){
        JMSSelectorSerializer serializer = new JMSSelectorSerializer(JMSTemplates.DEFAULT);
         serializer.handle(exp);
        return serializer.toString().replace("'true'", "TRUE").replace("'false'", "FALSE") ;
     }
     
}

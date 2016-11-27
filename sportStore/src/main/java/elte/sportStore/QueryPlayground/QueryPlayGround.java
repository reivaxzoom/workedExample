/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elte.sportStore.QueryPlayground;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import elte.sportStore.model.QRequestData;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Xavier
 */
public class QueryPlayGround {

    private static final QRequestData rd = QRequestData.requestData;

    public static void main(String[] args) {
        QueryGeneratorSample qg = new QueryGeneratorSample();

        List<String> countries1 = Arrays.asList("Hungary", "Ireland", "Italy", "Latvia", "Lithuania", "Luxembourg", "Malta", "Netherlands", "Poland", "Portugal", "Romania", "Slovakia", "Slovenia", "Spain", "Sweden", "United Kingdom");
        List formatedCountries = countries1.stream().map(x -> "'" + x + "'").collect(Collectors.toList());

        BooleanBuilder bb1 = new BooleanBuilder(rd.category.eq("app"));
        BooleanBuilder bb3 = new BooleanBuilder();
//bb1.not().not().not().not();
        BooleanExpression be = rd.category.eq("app").and(rd.phone.eq("3699998854"));
        BooleanExpression beTrue = Expressions.TRUE;

        Predicate p1 = rd.category.eq("app");
        Predicate p2 = rd.category.eq("oqq1");
        Predicate p3 = rd.address.eq("huba ut");
        bb1.and(p1.not()).and(p2).or(rd.address.eq("a1").or(p3).or(beTrue));
//        app1.or(app);
//        .and().and().and(bb1).or();

        BooleanBuilder bb2 = new BooleanBuilder(bb1);
        bb2.and(bb3).or(bb2).or(bb1.and(p3).and(p3));

//System.out.println(qg.genQuery(bb1));
        System.out.println(qg.genQuery(bb2));

//        BooleanBuilder bb = new BooleanBuilder(rd.country.in(formatedCountries));
//        
//        bb.and(rd.category.eq("sport").or(rd.category.eq("gym")).or(rd.category.eq("shoes")).or(rd.category.eq("beverages")).or(rd.category.eq("futbol")));
//        bb.and(rd.budget.between(100,200));
//        bb.and(rd.phone.startsWith("36")); 
//        bb.and(rd.frecuent.isFalse());
    }

}

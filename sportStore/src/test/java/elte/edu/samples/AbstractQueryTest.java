package elte.edu.samples;

import elte.sportStore.model.RequestData;
import com.querydsl.collections.*;
import com.querydsl.core.DefaultQueryMetadata;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;

import com.querydsl.core.Tuple;
import com.querydsl.core.alias.Alias;
import com.querydsl.core.types.Expression;
import elte.sportStore.model.QRequestData;

public abstract class AbstractQueryTest {

    protected List<Integer> ints = new ArrayList<>();
    protected List<Integer> myInts = new ArrayList<>();
    protected TestQuery<?> last;
    
    //=========================================
    protected final RequestData taz    = new RequestData("111", "xavier");
    protected final RequestData bugs   = new RequestData("222", "bugs");
    protected final RequestData daffy  = new RequestData("333", "daffy");
    protected final RequestData elmer  = new RequestData("444", "elmer");

//  Object notation    
//    protected final QRequestData rq = new QRequestData("reqData1");
//    protected final QRequestData rq2 = new QRequestData("reqData2");
//    protected final QRequestData reqData3 = new QRequestData("reqData3");
//    protected final QRequestData reqData4 = new QRequestData("reqData4");
    

//Static notation
    protected final QRequestData rq = QRequestData.requestData;
    protected final QRequestData rq2 = QRequestData.requestData;
    protected final QRequestData reqData3 = QRequestData.requestData;
    protected final QRequestData reqData4 = QRequestData.requestData;

    
    @Before
    public void setUp() {
        myInts.addAll(Arrays.asList(1, 2, 3, 4));
        Alias.resetAlias();
    }

    protected TestQuery<?> query() {
        last = new TestQuery<Void>();
        return last;
    }

    static class TestQuery<T> extends AbstractCollQuery<T, TestQuery<T>> {

        List<Object> res = new ArrayList<>();

        public TestQuery() {
            super(new DefaultQueryMetadata(), DefaultQueryEngine.getDefault());
        }

        @Override
        public List<T> fetch() {
            List<T> rv = super.fetch();
            for (T o : rv) {
                res.add(o);
            }
            return rv;
        }

        @Override
        public <U> TestQuery<U> select(Expression<U> expr) {
            queryMixin.setProjection(expr);
            @SuppressWarnings("unchecked") // This is the new projection's type
            TestQuery<U> newType = (TestQuery<U>) queryMixin.getSelf();
            return newType;
        }

        @Override
        public TestQuery<Tuple> select(Expression<?>... exprs) {
            queryMixin.setProjection(exprs);
            @SuppressWarnings("unchecked") // This is the new projection's type
            TestQuery<Tuple> newType = (TestQuery<Tuple>) queryMixin.getSelf();
            return newType;
        }
    }
}

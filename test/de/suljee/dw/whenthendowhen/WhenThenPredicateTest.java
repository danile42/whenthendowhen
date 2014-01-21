package de.suljee.dw.whenthendowhen;

import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiMethodCallExpression;
import com.intellij.psi.PsiReferenceExpression;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by daniel on 19.01.14.
 */
public class WhenThenPredicateTest {
    @Test
    public void isNotApplicableOnAnyCall() throws Exception {
        PsiMethodCallExpression whenCallMock = mockMethod("doSomething");

        WhenThenPredicate predicate = new WhenThenPredicate();
        assertThat(predicate.satisfiedBy(whenCallMock), is(false));
    }

    @Test
    public void isApplicableOnWhenCall() throws Exception {
        PsiMethodCallExpression whenCallMock = mockMethod("when");

        WhenThenPredicate predicate = new WhenThenPredicate();
        assertThat(predicate.satisfiedBy(whenCallMock), is(true));
    }

    private PsiMethodCallExpression mockMethod(String methodName) {
        PsiMethodCallExpression methodCallExpression = mock(PsiMethodCallExpression.class);
        PsiReferenceExpression referenceExpression = mock(PsiReferenceExpression.class);
        when(referenceExpression.getReferenceName()).thenReturn(methodName);
        when(methodCallExpression.getMethodExpression()).thenReturn(referenceExpression);
        return methodCallExpression;
    }
}

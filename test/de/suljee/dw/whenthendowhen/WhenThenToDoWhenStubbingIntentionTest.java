package de.suljee.dw.whenthendowhen;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethodCallExpression;
import com.intellij.psi.PsiReferenceExpression;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by daniel on 19.01.14.
 */
public class WhenThenToDoWhenStubbingIntentionTest {
    @Test
    public void testName() throws Exception {
        PsiMethodCallExpression psiElementMock = mock(PsiMethodCallExpression.class);
        PsiReferenceExpression psiMethodExpressionMock = mock(PsiReferenceExpression.class);
        when(psiElementMock.getMethodExpression()).thenReturn(psiMethodExpressionMock);

    }
}

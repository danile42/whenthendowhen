package de.suljee.dw.whenthendowhen;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiMethodCallExpression;
import com.intellij.psi.PsiReferenceExpression;
import com.siyeh.ipp.base.PsiElementPredicate;
import de.suljee.dw.AbstractStatefulIntentionPredicate;

/**
 * Holds stubbing elements determined by predicate for later use by intention.
 */
public abstract class AbstractStatefulStubbingIntentionPredicate extends AbstractStatefulIntentionPredicate {
    PsiMethodCallExpression thenCall = null;
    PsiMethodCallExpression whenCall = null;
    PsiReferenceExpression mockObject = null;
    PsiMethodCallExpression stubbedMethodCall = null;
    PsiExpression[] stubbedMethodArguments = null;

    @Override
    protected void resetState() {
        thenCall = null;
        whenCall = null;
        mockObject = null;
        stubbedMethodCall = null;
        stubbedMethodArguments = null;
    }

    @Override
    protected final boolean isSuccessful() {
        return thenCall != null
                && whenCall != null
                && mockObject != null
                && stubbedMethodCall != null
                && stubbedMethodArguments != null;
    }

}

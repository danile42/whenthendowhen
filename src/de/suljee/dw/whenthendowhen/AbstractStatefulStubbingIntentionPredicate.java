package de.suljee.dw.whenthendowhen;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiMethodCallExpression;
import com.intellij.psi.PsiReferenceExpression;
import com.siyeh.ipp.base.PsiElementPredicate;

/**
 * Holds stubbing elements determined by predicate for later use by intention.
 */
public abstract class AbstractStatefulStubbingIntentionPredicate implements PsiElementPredicate {
    PsiMethodCallExpression thenCall = null;
    PsiMethodCallExpression whenCall = null;
    PsiReferenceExpression mockObject = null;
    PsiMethodCallExpression stubbedMethodCall = null;
    PsiExpression[] stubbedMethodArguments = null;

    abstract void analyze(PsiElement element);

    @Override
    public boolean satisfiedBy(PsiElement psiElement) {
        resetState();
        analyze(psiElement);
        return isSuccessful();
    }

    void resetState() {
        thenCall = null;
        whenCall = null;
        mockObject = null;
        stubbedMethodCall = null;
        stubbedMethodArguments = null;
    }

    protected final boolean isSuccessful() {
        return thenCall != null
                && whenCall != null
                && mockObject != null
                && stubbedMethodCall != null
                && stubbedMethodArguments != null;
    }

    protected final boolean isMethodCall(PsiElement element) {
        return (element instanceof PsiMethodCallExpression);
    }

    protected final String getMethodName(PsiMethodCallExpression methodCall) {
        return methodCall.getMethodExpression().getReferenceName();
    }

    protected final PsiExpression[] getMethodArguments(PsiMethodCallExpression methodCall) {
        return methodCall.getArgumentList().getExpressions();
    }
}

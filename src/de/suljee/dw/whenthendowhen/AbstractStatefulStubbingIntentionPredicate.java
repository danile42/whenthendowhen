package de.suljee.dw.whenthendowhen;

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
}

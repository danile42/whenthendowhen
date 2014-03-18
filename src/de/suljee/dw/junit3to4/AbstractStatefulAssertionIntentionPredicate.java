package de.suljee.dw.junit3to4;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiMethodCallExpression;
import de.suljee.dw.AbstractStatefulIntentionPredicate;

/**
 * Created by daniel on 17.03.14.
 */
public abstract class AbstractStatefulAssertionIntentionPredicate extends AbstractStatefulIntentionPredicate {
    protected PsiMethodCallExpression assertCall;
    protected PsiExpression[] assertCallArguments;

    @Override
    protected void resetState() {
        assertCall = null;
    }

    @Override
    protected boolean isSuccessful() {
        return assertCall != null;
    }
}

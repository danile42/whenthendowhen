package de.suljee.dw;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiMethodCallExpression;
import com.siyeh.ipp.base.PsiElementPredicate;

/**
 * Created by daniel on 17.03.14.
 */
public abstract class AbstractStatefulIntentionPredicate implements PsiElementPredicate {
    protected abstract void analyze(PsiElement element);

    @Override
    public final boolean satisfiedBy(PsiElement psiElement) {
        resetState();
        analyze(psiElement);
        return isSuccessful();
    }

    protected abstract void resetState();

    protected abstract boolean isSuccessful();
}

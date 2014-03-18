package de.suljee.dw.junit3to4;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiMethodCallExpression;

import static de.suljee.dw.whenthendowhen.PsiUtils.getMethodArguments;
import static de.suljee.dw.whenthendowhen.PsiUtils.getMethodName;
import static de.suljee.dw.whenthendowhen.PsiUtils.isMethodCall;

/**
 * Detects JUnit 3 assertions.
 */
public class Junit3AssertionPredicate extends AbstractStatefulAssertionIntentionPredicate {
    @Override
    public void analyze(PsiElement element) {
        if (isMethodCall(element)) {
            analyzeMethodCall((PsiMethodCallExpression) element);
        }
    }

    private void analyzeMethodCall(PsiMethodCallExpression methodCall) {
        if (getMethodName(methodCall).startsWith("assert") && !getMethodName(methodCall).startsWith("assertThat")) {
            assertCall = methodCall;
            assertCallArguments = getMethodArguments(assertCall);
        }
    }
}

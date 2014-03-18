package de.suljee.dw.junit3to4;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiPrimitiveType;
import com.intellij.psi.PsiType;
import com.siyeh.ipp.base.Intention;
import com.siyeh.ipp.base.PsiElementPredicate;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

import static de.suljee.dw.whenthendowhen.PsiUtils.getMethodName;
import static de.suljee.dw.whenthendowhen.PsiUtils.joinExpressionTexts;

/**
 * Converts JUnit 3 assertions to JUnit 4 + Hamcrest matcher.
 */
public class Junit3AssertionToJunit4AndHamcrestIntention extends Intention {
    private Junit3AssertionPredicate PREDICATE;

    @Override
    protected void processIntention(@NotNull PsiElement psiElement) {
        replaceExpression("assertThat(" + matcherString() + ")",
                PREDICATE.assertCall);
    }

    private String matcherString() {
        String assertionType = StringUtils.removeStart(getMethodName(PREDICATE.assertCall), "assert");
        List<PsiExpression> assertArgs = Arrays.asList(PREDICATE.assertCallArguments);
        int lastArgIndex = assertArgs.size() - 1;

        String messageString = null;
        String actualString = getText(assertArgs, 1); // default value - may be replaced below
        String matcherMethod = "";
        String matchArguments = getText(assertArgs, 0);
        if (assertionType.equals("Equals")) {
            matcherMethod = "equalTo";
            if (assertArgs.size() == 2) {
                // default values
            } else if (assertArgs.size() == 3) {
                if ((typeEquals(assertArgs.get(0), PsiType.FLOAT)
                        || typeEquals(assertArgs.get(0), PsiType.DOUBLE))
                        && typesEqual(assertArgs)) {
                    // double/float comparison with error margin
                    matcherMethod = "closeTo";
                    matchArguments = getText(assertArgs, 0) + ", " + getText(assertArgs, 2);
                } else {
                    // with message
                    messageString = getText(assertArgs, 0);
                    actualString = getText(assertArgs, 2);
                    matchArguments = getText(assertArgs, 1);
                }
            } else if (assertArgs.size() == 4) {
                // double/float comparison with error margin and message
                messageString = getText(assertArgs, 0);
                actualString = getText(assertArgs, 2);
                matcherMethod = "closeTo";
                matchArguments = getText(assertArgs, 1) + ", " + getText(assertArgs, 3);
            }
        } else if (assertionType.equals("True")) {
            matcherMethod = "is";
            if (assertArgs.size() == 2) {
                messageString = getText(assertArgs, 0);
            }
            matchArguments = "true";
        } else if (assertionType.equals("False")) {
            matcherMethod = "is";
            if (assertArgs.size() == 2) {
                messageString = getText(assertArgs, 0);
            }
            matchArguments = "false";
        } else if (assertionType.equals("NotNull")) {
            matcherMethod = "is";
            if (assertArgs.size() == 2) {
                messageString = getText(assertArgs, 0);
                actualString = getText(assertArgs, 1);
            }
            matchArguments = "notNullValue()";
        } else if (assertionType.equals("Null")) {
            matcherMethod = "is";
            if (assertArgs.size() == 2) {
                messageString = getText(assertArgs, 0);
                actualString = getText(assertArgs, 1);
            }
            matchArguments = "nullValue()";
        } else if (assertionType.equals("NotSame")) {
            matcherMethod = "not(is";
            if (assertArgs.size() == 3) {
                messageString = getText(assertArgs, 0);
                actualString = getText(assertArgs, 1);
                matchArguments = getText(assertArgs, 2);
            } else {
                matchArguments = getText(assertArgs, 1);
            }
        } else if (assertionType.equals("Same")) {
            matcherMethod = "is";
            if (assertArgs.size() == 3) {
                messageString = getText(assertArgs, 0);
                actualString = getText(assertArgs, 1);
                matchArguments = getText(assertArgs, 2);
            } else {
                matchArguments = getText(assertArgs, 1);
            }
        }
        return (messageString != null ? messageString + ", " : "")
                + actualString + ", " + matcherMethod + "(" + matchArguments + ")";
    }

    private String getText(List<PsiExpression> expressions, int index) {
        return expressions.get(index).getText();
    }

    private boolean typeEquals(PsiExpression expression, PsiPrimitiveType type) {
        return expression.getType().equals(type);
    }

    private boolean typesEqual(List<PsiExpression> arguments) {
        PsiType lastType = null;
        for (PsiExpression expression : arguments) {
            if (lastType != null && !expression.getType().equals(lastType)) {
                return false;
            }
            lastType = expression.getType();
        }
        return true;
    }

    @NotNull
    @Override
    protected PsiElementPredicate getElementPredicate() {
        PREDICATE = new Junit3AssertionPredicate();
        return PREDICATE;
    }

    @NotNull
    @Override
    public String getText() {
        return "Replace JUnit3 assertion with JUnit4+Hamcrest";
    }
}

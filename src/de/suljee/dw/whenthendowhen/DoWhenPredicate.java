/*
 * Copyright 2014 Daniel Warmuth
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.suljee.dw.whenthendowhen;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiMethodCallExpression;
import com.intellij.psi.PsiReferenceExpression;

import static de.suljee.dw.whenthendowhen.PsiUtils.getMethodArguments;
import static de.suljee.dw.whenthendowhen.PsiUtils.getMethodName;
import static de.suljee.dw.whenthendowhen.PsiUtils.isMethodCall;

/**
 * Determines whether the current element is a "do-when" stubbing.
 */
public class DoWhenPredicate extends AbstractStatefulStubbingIntentionPredicate {
    @Override
    protected void analyze(PsiElement element) {
        if (isMethodCall(element)) {
            analyzeMethodCall((PsiMethodCallExpression) element);
        }
    }

    private void analyzeMethodCall(PsiMethodCallExpression methodCall) {
        String methodName = getMethodName(methodCall);
        if (!methodName.equals("when") && !methodName.startsWith("do")) {
            stubbedMethodCall = methodCall;
            stubbedMethodArguments = getMethodArguments(methodCall);

            if (isMethodCall(methodCall.getMethodExpression().getFirstChild())) {
                analyzeSubMethodCall((PsiMethodCallExpression) methodCall.getMethodExpression().getFirstChild());
            }
        }
    }

    private void analyzeSubMethodCall(PsiMethodCallExpression firstSubCall) {
        String firstSubMethodName = getMethodName(firstSubCall);
        if ((firstSubMethodName.equals("when"))) {
            whenCall = firstSubCall;
            mockObject = getMockObject(whenCall);

            if (isMethodCall(firstSubCall.getMethodExpression().getFirstChild())) {
                thenCall = getThenCall((PsiMethodCallExpression) firstSubCall.getMethodExpression().getFirstChild());
            }
        }
    }

    private PsiMethodCallExpression getThenCall(PsiMethodCallExpression subSubMethodCall) {
        String subSubMethodName = getMethodName(subSubMethodCall);
        if ((subSubMethodName.startsWith("do"))) {
            return subSubMethodCall;
        } else {
            return null;
        }
    }

    private PsiReferenceExpression getMockObject(PsiMethodCallExpression methodCall) {
        PsiExpression[] whenCallArguments = getMethodArguments(methodCall);
        if (containsReference(whenCallArguments)) {
            return getFirstReference(whenCallArguments);
        } else {
            return null;
        }
    }

    private PsiReferenceExpression getFirstReference(PsiExpression[] expressions) {
        return (PsiReferenceExpression) expressions[0];
    }

    private boolean containsReference(PsiExpression[] expressions) {
        return expressions.length > 0 && expressions[0] instanceof PsiReferenceExpression;
    }
}

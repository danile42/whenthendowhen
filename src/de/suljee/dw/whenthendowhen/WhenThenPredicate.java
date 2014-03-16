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

/**
 * Determines whether the current element is a "when-then" stubbing.
 */
public class WhenThenPredicate extends AbstractStatefulStubbingIntentionPredicate {
    @Override
    public void analyze(PsiElement element) {
        if (isMethodCall(element)) {
            analyzeMethodCall((PsiMethodCallExpression) element);
        }
    }

    private void analyzeMethodCall(PsiMethodCallExpression methodCall) {
        if ((getMethodName(methodCall).startsWith("then"))) {
            thenCall = methodCall;
            if (isMethodCall(methodCall.getMethodExpression().getFirstChild())) {
                analyzeSubMethodCall((PsiMethodCallExpression) methodCall.getMethodExpression().getFirstChild());
            }
        }
    }

    private void analyzeSubMethodCall(PsiMethodCallExpression subMethodCall) {
        if ((getMethodName(subMethodCall).equals("when"))) {
            whenCall = subMethodCall;
            PsiExpression[] whenCallArguments = getMethodArguments(whenCall);
            if (containsMethodCall(whenCallArguments)) {
                stubbedMethodCall = getFirstMethodCall(whenCallArguments);
                stubbedMethodArguments = getMethodArguments(stubbedMethodCall);
                mockObject = getMockObject(stubbedMethodCall);
            }
        }
    }

    private boolean containsMethodCall(PsiExpression[] methodCallArguments) {
        return methodCallArguments.length > 0 && isMethodCall(methodCallArguments[0]);
    }

    private PsiMethodCallExpression getFirstMethodCall(PsiExpression[] methodCallArguments) {
        return (PsiMethodCallExpression) methodCallArguments[0];
    }

    private PsiReferenceExpression getMockObject(PsiMethodCallExpression methodCall) {
        PsiReferenceExpression methodExpression = methodCall.getMethodExpression();
        if (((methodExpression.getFirstChild() instanceof PsiReferenceExpression))) {
            return (PsiReferenceExpression) methodExpression.getFirstChild();
        } else {
            return null;
        }
    }
}

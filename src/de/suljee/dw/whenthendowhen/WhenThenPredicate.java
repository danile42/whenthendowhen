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
    public boolean satisfiedBy(PsiElement element) {
        if (!(element instanceof PsiMethodCallExpression)) {
            return false;
        }

        PsiMethodCallExpression methodCall = (PsiMethodCallExpression) element;
        PsiReferenceExpression methodReference = methodCall.getMethodExpression();

        String methodName = methodReference.getReferenceName();
        if (!(methodName.startsWith("then"))) {
            return false;
        }
        if (!(methodReference.getFirstChild() instanceof PsiMethodCallExpression)) {
            return false;
        }
        PsiMethodCallExpression subMethodCall = (PsiMethodCallExpression) methodReference.getFirstChild();
        String subMethodName = subMethodCall.getMethodExpression().getReferenceName();
        if (!(subMethodName.equals("when"))) {
            return false;
        }

        PsiExpression[] whenCallArguments = subMethodCall.getArgumentList().getExpressions();
        if (whenCallArguments.length > 0) {
            PsiElement whenCallArgument = whenCallArguments[0];
            if (whenCallArgument instanceof PsiMethodCallExpression) {
                PsiReferenceExpression whenCallArgumentExpression = ((PsiMethodCallExpression) whenCallArgument).getMethodExpression();
                if (!(whenCallArgumentExpression.getFirstChild() instanceof PsiReferenceExpression)) {
                    return false;
                }
                mockObject = (PsiReferenceExpression) whenCallArgumentExpression.getFirstChild();
                stubbedMethodCall = (PsiMethodCallExpression) whenCallArgument;
                stubbedMethodArguments = ((PsiMethodCallExpression) whenCallArgument).getArgumentList().getExpressions();
            }
        }

        thenCall = methodCall;
        whenCall = subMethodCall;

        return true;
    }
}

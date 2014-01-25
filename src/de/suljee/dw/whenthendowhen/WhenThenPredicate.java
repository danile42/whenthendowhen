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
        if ((element instanceof PsiMethodCallExpression)) {
            PsiMethodCallExpression methodCall = (PsiMethodCallExpression) element;
            PsiReferenceExpression methodReference = methodCall.getMethodExpression();

            String methodName = methodReference.getReferenceName();
            if ((methodName.startsWith("then"))) {
                thenCall = methodCall;
                if ((methodReference.getFirstChild() instanceof PsiMethodCallExpression)) {
                    PsiMethodCallExpression subMethodCall = (PsiMethodCallExpression) methodReference.getFirstChild();
                    String subMethodName = subMethodCall.getMethodExpression().getReferenceName();
                    if ((subMethodName.equals("when"))) {
                        whenCall = subMethodCall;
                        PsiExpression[] whenCallArguments = whenCall.getArgumentList().getExpressions();
                        if (whenCallArguments.length > 0 && whenCallArguments[0] instanceof PsiMethodCallExpression) {
                            stubbedMethodCall = (PsiMethodCallExpression) whenCallArguments[0];
                            stubbedMethodArguments = stubbedMethodCall.getArgumentList().getExpressions();
                            PsiReferenceExpression stubbedMethodCallExpression = stubbedMethodCall.getMethodExpression();
                            if (((stubbedMethodCallExpression.getFirstChild() instanceof PsiReferenceExpression))) {
                                mockObject = (PsiReferenceExpression) stubbedMethodCallExpression.getFirstChild();
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}

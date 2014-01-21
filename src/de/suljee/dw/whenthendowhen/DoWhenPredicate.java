/*
 * Copyright 2000-2014 JetBrains s.r.o.
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
import com.siyeh.ipp.base.PsiElementPredicate;

/**
 * Determines whether the current element is a "do-when" stubbing.
 */
public class DoWhenPredicate implements PsiElementPredicate {
    PsiMethodCallExpression thenCall = null;
    PsiMethodCallExpression whenCall = null;
    PsiMethodCallExpression mockedMethodCall = null;
    PsiReferenceExpression mockObject = null;
    PsiExpression[] mockedMethodArguments = null;

    @Override
    public boolean satisfiedBy(PsiElement element) {
        if (!(element instanceof PsiMethodCallExpression)) {
            return false;
        }

        PsiMethodCallExpression methodCall = (PsiMethodCallExpression) element;
        PsiReferenceExpression methodReference = methodCall.getMethodExpression();

        if (methodReference.getReferenceName().equals("when")
                || methodReference.getReferenceName().startsWith("do")) {
            return false;
        }
        mockedMethodCall = methodCall;
        mockedMethodArguments = methodCall.getArgumentList().getExpressions();


        if (!(methodReference.getFirstChild() instanceof PsiMethodCallExpression)) {
            return false;
        }
        PsiMethodCallExpression firstSubCall = (PsiMethodCallExpression) methodReference.getFirstChild();
        PsiReferenceExpression subMethodReference = firstSubCall.getMethodExpression();
        String firstSubMethodName = subMethodReference.getReferenceName();
        if (!(firstSubMethodName.equals("when"))) {
            return false;
        }
        whenCall = firstSubCall;

        if (!(subMethodReference.getFirstChild() instanceof PsiMethodCallExpression)) {
            return false;
        }
        PsiMethodCallExpression subSubMethodCall = (PsiMethodCallExpression) subMethodReference.getFirstChild();
        String subSubMethodName = subSubMethodCall.getMethodExpression().getReferenceName();
        if (!(subSubMethodName.startsWith("do"))) {
            return false;
        }
        thenCall = subSubMethodCall;

        PsiExpression[] whenCallArguments = whenCall.getArgumentList().getExpressions();
        if (whenCallArguments.length > 0) {
            PsiElement whenCallArgument = whenCallArguments[0];
            if (whenCallArgument instanceof PsiReferenceExpression) {
                mockObject = (PsiReferenceExpression) whenCallArgument;
            }
        }

        return true;
    }
}

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
import com.siyeh.ipp.base.Intention;
import com.siyeh.ipp.base.PsiElementPredicate;
import org.jetbrains.annotations.NotNull;

import static com.siyeh.ig.PsiReplacementUtil.replaceExpression;
import static de.suljee.dw.whenthendowhen.PsiUtils.joinExpressionTexts;

/**
 * Converts "do-when" stubbing to "when-then".
 */
public class DoWhenToWhenThenStubbingIntention extends Intention {
    private AbstractStatefulStubbingIntentionPredicate PREDICATE;

    @Override
    protected void processIntention(@NotNull PsiElement element) {
        replaceExpression(PREDICATE.stubbedMethodCall, whenClause() + stubbedMethodCall() + thenClause());
    }

    private String whenClause() {
        return String.format("when(%s", PREDICATE.mockObject.getText());
    }

    private String stubbedMethodCall() {
        return String.format(".%s(%s))", PREDICATE.stubbedMethodCall.getMethodExpression().getReferenceName(),
                                        joinExpressionTexts(PREDICATE.stubbedMethodArguments, ","));
    }

    private String thenClause() {
        String thenMethodName = PREDICATE.thenCall.getMethodExpression().getReferenceName();
        String thenMethodSuffix = thenMethodName.substring(2);
        return String.format(".then%s(%s)", thenMethodSuffix,
                                            joinExpressionTexts(PREDICATE.thenCall.getArgumentList().getExpressions(), ","));
    }

    @NotNull
    @Override
    protected PsiElementPredicate getElementPredicate() {
        PREDICATE = new DoWhenPredicate();
        return PREDICATE;
    }

    @NotNull
    @Override
    public String getText() {
        return "Replace 'do-when' with 'when-then'";
    }
}

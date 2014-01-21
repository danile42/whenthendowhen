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
import com.siyeh.ipp.base.Intention;
import com.siyeh.ipp.base.PsiElementPredicate;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;

import static de.suljee.dw.whenthendowhen.PsiUtils.joinExpressionTexts;

/**
 * Converts "when-then" stubbing to "do-when".
 */
public class WhenThenToDoWhenStubbingIntention extends Intention {
    public static final WhenThenPredicate PREDICATE = new WhenThenPredicate();

    @Override
    protected void processIntention(@NotNull PsiElement element) {
        String thenMethodName = PREDICATE.thenCall.getMethodExpression().getReferenceName();
        String doMethodSuffix = thenMethodName.substring(4);

        StringBuilder expr = new StringBuilder("do");
        expr.append(doMethodSuffix).append("(");
        expr.append(joinExpressionTexts(PREDICATE.thenCall.getArgumentList().getExpressions(), ","));
        expr.append(").when(");
        expr.append(PREDICATE.mockObject.getText());
        expr.append(").");
        expr.append(PREDICATE.mockedMethod);
        expr.append("(");
        expr.append(joinExpressionTexts(PREDICATE.mockedMethodArguments, ","));
        expr.append(")");

        replaceExpression(expr.toString(), PREDICATE.thenCall);
    }

    @NotNull
    @Override
    protected PsiElementPredicate getElementPredicate() {
        return PREDICATE;
    }

    @NotNull
    @Override
    public String getText() {
        return "Replace 'when-then' with 'do-when'";
    }
}

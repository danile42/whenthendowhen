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
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.apache.commons.collections4.CollectionUtils.collect;
import static org.apache.commons.lang.StringUtils.*;

public class PsiUtils {
    public static String joinExpressionTexts(PsiExpression[] psiExpressions, String separator) {
        return joinExpressionTexts(Arrays.asList(psiExpressions), separator);
    }

    public static String joinExpressionTexts(List<PsiExpression> psiExpressions, String separator) {
        Collection<String> mockedMethodArgs = collect(psiExpressions, PsiExpression::getText);
        return join(mockedMethodArgs, separator);
    }

    public static boolean isMethodCall(PsiElement element) {
        return (element instanceof PsiMethodCallExpression);
    }

    public static String getMethodName(PsiMethodCallExpression methodCall) {
        return methodCall.getMethodExpression().getReferenceName();
    }

    public static PsiExpression[] getMethodArguments(PsiMethodCallExpression methodCall) {
        return methodCall.getArgumentList().getExpressions();
    }
}

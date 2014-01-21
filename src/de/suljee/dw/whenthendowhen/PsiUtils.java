package de.suljee.dw.whenthendowhen;

import com.intellij.psi.PsiExpression;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by daniel on 21.01.14.
 */
public class PsiUtils {
    public static final Transformer EXPRESSION_TO_TEXT_TRANSFORMER = new Transformer() {
        @Override
        public Object transform(Object o) {
            PsiExpression expression = (PsiExpression) o;
            return expression.getText();
        }
    };

    static String joinExpressionTexts(PsiExpression[] psiExpressions, String separator) {
        Collection<String> mockedMethodArgs = CollectionUtils.collect(Arrays.asList(psiExpressions),
                EXPRESSION_TO_TEXT_TRANSFORMER);
        return StringUtils.join(mockedMethodArgs, separator);
    }
}

package org.nautilus.web.dialet;

import java.util.Collections;
import java.util.Set;

import org.nautilus.core.util.Formatter;
import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;

public class FormatterDialect extends AbstractDialect implements IExpressionObjectDialect {

	public FormatterDialect() {
		super("Formatter Dialect");
	}

	@Override
	public IExpressionObjectFactory getExpressionObjectFactory() {

		return new IExpressionObjectFactory() {

			@Override
			public Set<String> getAllExpressionObjectNames() {
				return Collections.singleton("formatter");
			}

			@Override
			public Object buildObject(IExpressionContext context, String expressionObjectName) {
				return new Formatter();
			}

			@Override
			public boolean isCacheable(String expressionObjectName) {
				return true;
			}
		};
	}
}

package thiagodnf.nautilus.web.dialet;

import java.util.Collections;
import java.util.Set;

import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;

import thiagodnf.nautilus.core.util.Converter;

public class ConverterDialect extends AbstractDialect implements IExpressionObjectDialect {

	public ConverterDialect() {
		super("Converter Dialect");
	}

	@Override
	public IExpressionObjectFactory getExpressionObjectFactory() {

		return new IExpressionObjectFactory() {

			@Override
			public Set<String> getAllExpressionObjectNames() {
				return Collections.singleton("converter");
			}

			@Override
			public Object buildObject(IExpressionContext context, String expressionObjectName) {
				return new Converter();
			}

			@Override
			public boolean isCacheable(String expressionObjectName) {
				return true;
			}
		};
	}
}

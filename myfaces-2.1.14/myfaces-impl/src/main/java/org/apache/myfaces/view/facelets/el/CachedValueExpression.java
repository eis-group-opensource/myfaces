package org.apache.myfaces.view.facelets.el;

import java.util.Map;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.view.facelets.FaceletContext;

import org.apache.myfaces.view.facelets.AbstractFaceletContext;

public class CachedValueExpression extends ValueExpression {
	
	private static final long serialVersionUID = -3593308305603853527L;
	
	private static final Object NULL = new Object();

	private final ValueExpression delegate;
	
	private final String tagId;

	private final Map<String, Object> cacheMap;

	public CachedValueExpression(ValueExpression delegate, FaceletContext ctx, String tagId) {
		this.delegate = delegate;
		this.tagId = tagId;
		this.cacheMap = ((AbstractFaceletContext)ctx).getPageContext().getAttributeMap();
	}
	
	@Override
	public Object getValue(ELContext context) {
		Object cached = cacheMap.get(tagId);
		if(cached == null) {
			cached = delegate.getValue(context);
			cacheMap.put(tagId, cached == null ? NULL : cached);
		} else if(cached == NULL) {
			return null;
		}
		return cached;
	}

	@Override
	public void setValue(ELContext context, Object value) {
		cacheMap.put(tagId, value == null ? NULL : value);
		delegate.setValue(context, value);
	}

	@Override
	public boolean isReadOnly(ELContext context) {
		return delegate.isReadOnly(context);
	}

	@Override
	public Class<?> getType(ELContext context) {
		return delegate.getType(context);
	}

	@Override
	public Class<?> getExpectedType() {
		return delegate.getExpectedType();
	}

	@Override
	public String getExpressionString() {
		return delegate.getExpressionString();
	}

	@Override
	public boolean equals(Object obj) {
		return delegate.equals(obj);
	}

	@Override
	public int hashCode() {
		return delegate.hashCode();
	}

	@Override
	public boolean isLiteralText() {
		return delegate.isLiteralText();
	}

}
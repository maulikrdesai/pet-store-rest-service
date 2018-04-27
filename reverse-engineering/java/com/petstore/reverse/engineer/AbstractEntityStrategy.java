package com.petstore.reverse.engineer;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.cfg.reveng.ReverseEngineeringStrategy;
import org.hibernate.cfg.reveng.TableIdentifier;
import org.hibernate.mapping.MetaAttribute;

public class AbstractEntityStrategy extends RelationFilterStrategy {

	public AbstractEntityStrategy(ReverseEngineeringStrategy delegate) {
		super(delegate);
	}

	@Override
	public Map<String, MetaAttribute> tableToMetaAttributes(TableIdentifier tableIdentifier) {

		@SuppressWarnings("unchecked")
		Map<String, MetaAttribute> metaAttributes = super.tableToMetaAttributes(tableIdentifier);
		if (metaAttributes == null) {
			metaAttributes = new HashMap<String, MetaAttribute>();
		}

		String abstractClassName = tableToAbstractClassName(tableIdentifier);
		// Update modifier
		MetaAttribute metaAttribute = new MetaAttribute("scope-class");
		metaAttribute.addValue("public abstract");
		metaAttributes.put(metaAttribute.getName(), metaAttribute);

		// Update class name
		metaAttribute = new MetaAttribute("generated-class");
		metaAttribute.addValue(abstractClassName);
		metaAttributes.put(metaAttribute.getName(), metaAttribute);

		metaAttribute = new MetaAttribute("extends");
		metaAttribute.addValue("com.petstore.dao.AbstractBaseEntity");
		metaAttributes.put(metaAttribute.getName(), metaAttribute);

		return metaAttributes;
	}

	private String tableToAbstractClassName(TableIdentifier tableIdentifier) {
		String className = super.tableToClassName(tableIdentifier);
		int dotIndex = className.lastIndexOf('.');
		return className.substring(0, dotIndex + 1) + "Abstract" + className.substring(dotIndex + 1);
	}

}

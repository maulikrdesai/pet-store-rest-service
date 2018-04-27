package com.petstore.reverse.engineer;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.cfg.reveng.ReverseEngineeringStrategy;
import org.hibernate.cfg.reveng.TableIdentifier;
import org.hibernate.mapping.MetaAttribute;

public class ConcreteEntityStrategy extends RelationFilterStrategy {

	public ConcreteEntityStrategy(ReverseEngineeringStrategy delegate) {
		super(delegate);
	}

	@Override
	public Map<String, MetaAttribute> tableToMetaAttributes(TableIdentifier tableIdentifier) {

		@SuppressWarnings("unchecked")
		Map<String, MetaAttribute> metaAttributes = super.tableToMetaAttributes(tableIdentifier);
		if (metaAttributes == null)
			metaAttributes = new HashMap<String, MetaAttribute>();

		String abstractClassName = tableToAbstractClassName(tableIdentifier);
		String concreteClassName = tableToClassName(tableIdentifier);
		MetaAttribute metaAttribute = new MetaAttribute("generated-class");
		metaAttribute.addValue(concreteClassName);
		metaAttributes.put(metaAttribute.getName(), metaAttribute);

		// Update extends modifier
		metaAttribute = new MetaAttribute("extends");
		metaAttribute.addValue(abstractClassName);
		metaAttributes.put(metaAttribute.getName(), metaAttribute);
		return metaAttributes;
	}

	public boolean excludeTable(TableIdentifier tableIdentifier) {
		if (super.excludeTable(tableIdentifier))
			return true;
		File concreteJavaFile = RelationFilterStrategy.getJavaFile(super.tableToClassName(tableIdentifier));
		File abstractJavaFile = RelationFilterStrategy.getJavaFile(tableToAbstractClassName(tableIdentifier));
		if (!abstractJavaFile.exists()) {
			System.err.println("WARNING: Abstract-Entity not-present @ " + abstractJavaFile);
			return true;
		}
		if (concreteJavaFile.exists()) {
			System.out.println("Concrete-Entity already present @ " + concreteJavaFile);
			return true;
		}
		System.out.println("Reverse-engineering Concrete-Entity @ " + concreteJavaFile);
		return false;
	}

	private String tableToAbstractClassName(TableIdentifier tableIdentifier) {
		String className = super.tableToClassName(tableIdentifier);
		int dotIndex = className.lastIndexOf('.');
		return className.substring(0, dotIndex + 1) + "Abstract" + className.substring(dotIndex + 1);
	}

}
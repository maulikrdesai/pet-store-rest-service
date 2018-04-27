package com.petstore.reverse.engineer;

import java.io.File;
import java.text.MessageFormat;
import java.util.List;

import org.hibernate.cfg.reveng.DelegatingReverseEngineeringStrategy;
import org.hibernate.cfg.reveng.ReverseEngineeringStrategy;
import org.hibernate.cfg.reveng.TableIdentifier;

public class RelationFilterStrategy extends DelegatingReverseEngineeringStrategy {

	public static final String OUTPUTDIR_PROP = "outputdir";

	public RelationFilterStrategy(ReverseEngineeringStrategy delegate) {
		super(delegate);
	}

	public static File getJavaFile(String javaClass) {
		String outputDirPath = System.getProperty(OUTPUTDIR_PROP, System.getenv(OUTPUTDIR_PROP));
		if (outputDirPath == null || outputDirPath.trim().isEmpty()) {
			outputDirPath = "src/main/java";
			System.err.println(MessageFormat.format("WARNING: No system-property/environment-variable [{0}] provided using default to be {1}",
					OUTPUTDIR_PROP, outputDirPath));
		}
		File outputDir = new File(outputDirPath);
		return new File(outputDir, javaClass.replace(".", "/") + ".java");
	}
}
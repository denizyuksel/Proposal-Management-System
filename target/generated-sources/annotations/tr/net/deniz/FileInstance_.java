package tr.net.deniz;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(FileInstance.class)
public abstract class FileInstance_ {

	public static volatile SingularAttribute<FileInstance, String> fileName;
	public static volatile SingularAttribute<FileInstance, byte[]> fileContent;
	public static volatile SingularAttribute<FileInstance, Long> fileId;

}

